package com.businessanddecisions.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="synchronisations")
public class SynchronisationModel {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name="planning_id")
    private PlanningModel planning;

    @Column(name="sync_time")
    private Timestamp syncTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PlanningModel getPlanning() {
        return planning;
    }

    public void setPlanning(PlanningModel planning) {
        this.planning = planning;
    }

    public Timestamp getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Timestamp syncTime) {
        this.syncTime = syncTime;
    }
}
