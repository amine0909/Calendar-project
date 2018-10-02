package com.businessanddecisions.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class UserModel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    private String email;
    private String password;

    @OneToMany(mappedBy = "creator")
    @JsonBackReference
    private Set<PlanningModel> planningsCreatedByMe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "his_boss")
    private UserModel hisBoss;

    private String role;
    public UserModel() {}

    public UserModel(Long id,String firstName,String lastName,String email,String role) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email =email;
        this.role=role;
    }

    @ManyToMany(cascade =  CascadeType.ALL)
    @JoinTable(name="contributions",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="planning_id",referencedColumnName = "id"))
    private Set<PlanningModel> contributedPlannings = new HashSet<PlanningModel>();

    public Set<PlanningModel> getPlanningsCreatedByMe() {
        return planningsCreatedByMe;
    }

    public void setPlanningsCreatedByMe(Set<PlanningModel> planningsCreatedByMe) {
        this.planningsCreatedByMe = planningsCreatedByMe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserModel getHisBoss() {
        return hisBoss;
    }

    public void setHisBoss(UserModel hisBoss) {
        this.hisBoss = hisBoss;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<PlanningModel> getContributedPlannings() {
        return contributedPlannings;
    }

    public void setContributedPlannings(Set<PlanningModel> contributedPlannings) {
        this.contributedPlannings = contributedPlannings;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", planningsCreatedByMe=" + planningsCreatedByMe +
                ", hisBoss=" + hisBoss +
                ", role='" + role + '\'' +
                ", contributedPlannings=" + contributedPlannings +
                '}';
    }


}
