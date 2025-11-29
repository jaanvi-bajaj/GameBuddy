package com.gamebuddy.controller;

import com.gamebuddy.model.SportGroup;
import com.gamebuddy.model.User;
import com.gamebuddy.service.GroupService;
import com.gamebuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService){
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/find")
    public String findGroups(@RequestParam(value="sport", required=false) String sport,
                             @RequestParam(value="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model, HttpSession session){
        if (session.getAttribute("userId") == null) return "redirect:/login";
        List<SportGroup> groups;
        if (sport != null && !sport.isBlank()) groups = groupService.searchBySport(sport);
        else groups = groupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("sportsList", new String[]{"Cricket","Football","Basketball","Badminton","Tennis"});
        return "find";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model){
        if (session.getAttribute("userId") == null) return "redirect:/login";
        model.addAttribute("sportsList", new String[]{"Cricket","Football","Basketball","Badminton","Tennis"});
        model.addAttribute("group", new SportGroup());
        model.addAttribute("sportCapacityMap", getCapacityMap());
        return "create";
    }

    @PostMapping("/create")
    public String createGroup(@RequestParam String sport,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                              @RequestParam String location,
                              @RequestParam(required = false) Integer maxCapacity,
                              HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Optional<User> ou = userService.findById(userId);
        if (ou.isEmpty()) return "redirect:/login";

        SportGroup g = new SportGroup();
        g.setSport(sport);
        g.setDate(date);
        g.setTime(time);
        g.setLocation(location);
        g.setMaxCapacity(maxCapacity == null ? defaultCapacityForSport(sport) : maxCapacity);
        SportGroup saved = groupService.createGroup(g, ou.get());
        model.addAttribute("group", saved);
        return "create-success";
    }

    @PostMapping("/join-code")
    public String joinWithCode(@RequestParam String code, HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Optional<User> ou = userService.findById(userId);
        if (ou.isEmpty()) return "redirect:/login";
        Optional<SportGroup> og = groupService.findByCode(code.trim().toUpperCase());
        if (og.isEmpty()){
            model.addAttribute("error", "Group not found with code: " + code);
            return "dashboard";
        }
        SportGroup g = og.get();
        if (g.isFull()){
            model.addAttribute("error", "Group is already full");
            return "dashboard";
        }
        if (g.getParticipants().contains(ou.get())){
            model.addAttribute("error", "You already joined this group");
            return "dashboard";
        }
        g.getParticipants().add(ou.get());
        groupService.save(g);
        return "redirect:/groups/" + g.getId();
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model, HttpSession session){
        if (session.getAttribute("userId") == null) return "redirect:/login";
        Optional<SportGroup> og = groupService.findById(id);
        if (og.isEmpty()) return "redirect:/groups/find";
        model.addAttribute("group", og.get());
        return "group-details";
    }

    @PostMapping("/{id}/leave")
    public String leaveGroup(@PathVariable Long id, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Optional<SportGroup> og = groupService.findById(id);
        Optional<User> ou = userService.findById(userId);
        if (og.isPresent() && ou.isPresent()){
            SportGroup g = og.get();
            g.getParticipants().remove(ou.get());
            groupService.save(g);
        }
        return "redirect:/groups/" + id;
    }

    private Map<String,Integer> getCapacityMap(){
        Map<String,Integer> m = new LinkedHashMap<>();
        m.put("Cricket",22);
        m.put("Football",22);
        m.put("Basketball",10);
        m.put("Badminton",4);
        m.put("Tennis",4);
        return m;
    }

    private int defaultCapacityForSport(String sport){
        return getCapacityMap().getOrDefault(sport, 10);
    }
}