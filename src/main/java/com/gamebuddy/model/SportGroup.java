package com.gamebuddy.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sport_groups")
public class SportGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sport;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private int maxCapacity;
    @Column(unique = true)
    private String inviteCode;

    @ManyToOne
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "group_participants",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    // getters and setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getSport(){return sport;}
    public void setSport(String sport){this.sport=sport;}
    public LocalDate getDate(){return date;}
    public void setDate(LocalDate date){this.date=date;}
    public LocalTime getTime(){return time;}
    public void setTime(LocalTime time){this.time=time;}
    public String getLocation(){return location;}
    public void setLocation(String location){this.location=location;}
    public int getMaxCapacity(){return maxCapacity;}
    public void setMaxCapacity(int maxCapacity){this.maxCapacity=maxCapacity;}
    public String getInviteCode(){return inviteCode;}
    public void setInviteCode(String inviteCode){this.inviteCode=inviteCode;}
    public User getCreator(){return creator;}
    public void setCreator(User creator){this.creator=creator;}
    public Set<User> getParticipants(){return participants;}
    public void setParticipants(Set<User> participants){this.participants=participants;}
    public int getCurrentSize(){ return participants == null ? 0 : participants.size(); }
    public boolean isFull(){ return getCurrentSize() >= getMaxCapacity(); }
}