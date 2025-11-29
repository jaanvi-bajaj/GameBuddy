package com.gamebuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;
    private Integer age;
    private String gender;

    // Comma separated sports (e.g. "Cricket,Football")
    private String sportsInterested;

    private String idCardFilename;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters omitted for brevity — include them in IDE or generate
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){this.firstName=firstName;}
    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName=lastName;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    public Integer getAge(){return age;}
    public void setAge(Integer age){this.age=age;}
    public String getGender(){return gender;}
    public void setGender(String gender){this.gender=gender;}
    public String getSportsInterested(){return sportsInterested;}
    public void setSportsInterested(String sportsInterested){this.sportsInterested=sportsInterested;}
    public String getIdCardFilename(){return idCardFilename;}
    public void setIdCardFilename(String idCardFilename){this.idCardFilename=idCardFilename;}
    public boolean isActive(){return active;}
    public void setActive(boolean active){this.active=active;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}