package com.businessanddecisions.models;

import com.businessanddecisions.Enums.StatusPlanning;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="plannings")
public class PlanningModel {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="creator")
    private UserModel creator;


    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt ;

    @OneToMany(mappedBy = "planning")
    @JsonBackReference
    private Set<SynchronisationModel> synchronisations = new HashSet<SynchronisationModel>();

    @ManyToMany
    @JoinTable(name="contributions", joinColumns = @JoinColumn(name="planning_id",referencedColumnName = "id"),inverseJoinColumns =@JoinColumn(name="user_id",referencedColumnName = "id"))
    private Set<UserModel> contributors = new HashSet<UserModel>();



    public PlanningModel() {}



    public PlanningModel(UserModel creator) {
        this.creator = creator;
    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<SynchronisationModel> getSynchronisations() {
        return synchronisations;
    }

    public void setSynchronisations(Set<SynchronisationModel> synchronisations) {
        this.synchronisations = synchronisations;
    }

    public Set<UserModel> getContributors() {
        return contributors;
    }

    public void setContributors(Set<UserModel> contributors) {
        this.contributors = contributors;
    }

   /* public String toString(){
        return String.format("[id=%d%n, url=%s, date upadate=%tD%n]",this.id, this.fileUrl,this.updatedAt);
    }*/
}
