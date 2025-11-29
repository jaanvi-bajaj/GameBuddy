package com.gamebuddy.service;

import com.gamebuddy.model.SportGroup;
import com.gamebuddy.model.User;
import com.gamebuddy.repository.SportGroupRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final SportGroupRepository repo;
    private final SecureRandom random = new SecureRandom();

    public GroupService(SportGroupRepository repo){
        this.repo = repo;
    }

    public SportGroup createGroup(SportGroup group, User creator){
        String code = generateCode(group.getSport());
        group.setInviteCode(code);
        group.setCreator(creator);
        // add creator as participant
        group.getParticipants().add(creator);
        return repo.save(group);
    }

    public Optional<SportGroup> findByCode(String code){
        return repo.findByInviteCode(code);
    }

    public Optional<SportGroup> findById(Long id){
        return repo.findById(id);
    }

    public List<SportGroup> findAll(){
        return repo.findAll();
    }

    public List<SportGroup> searchBySport(String sport){
        return repo.findBySportIgnoreCaseContaining(sport == null ? "" : sport);
    }

    public SportGroup save(SportGroup g){ return repo.save(g); }

    private String generateCode(String sport){
        String prefix = sport == null ? "GAME" : sport.trim().toUpperCase();
        if (prefix.length() > 4) prefix = prefix.substring(0,4);
        int num = 10000 + random.nextInt(90000);
        return prefix + "-" + num;
    }
}