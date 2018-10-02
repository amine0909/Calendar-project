package com.businessanddecisions.models;

import com.businessanddecisions.Enums.StatusPlanning;
import com.businessanddecisions.Enums.VersionEvent;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Events")
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] content;

    @Enumerated(EnumType.STRING)
    private StatusPlanning Status; // waiting, accepted, refused


   // private String version; //alpha , beta

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Calendar")
    private PlanningModel Calendar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="submittedBy")
    private UserModel submittedBy;


    @CreationTimestamp
    private Date created_At;

    @UpdateTimestamp
    private Date updated_At;


    public EventModel(){}

    public EventModel(byte[] content, PlanningModel planning, UserModel user){
        this.content = content;
        this.Status = StatusPlanning.waiting;
        this.Calendar = planning;
        this.submittedBy = user;
        //this.version = "Beta";
    }


    public Long getId(){return this.id;}
    public void setId(Long id){this.id = id;}

    public StatusPlanning getStatusPlanning(){return this.Status;}
    public void setStatusPlanning(StatusPlanning status){this.Status = status;}

    public byte[] getContent() {return this.content;}
    public void setContent(byte[] content){this.content = content;}

    public PlanningModel getPlanning() {return this.Calendar;}
    public void setPlanning(PlanningModel planning) {this.Calendar = planning;}


    public UserModel getSubmittedBy() {return this.submittedBy;}
    public void setSubmittedBy(UserModel user) {this.submittedBy = user;}

    //public String getVersion() {return this.version;}
    //public void setVersion(String versionEvent) {this.version = versionEvent;}

    public String toString(){
        return "ID : "+this.id+","
                + "planning: "+this.Calendar.toString();

    }
}
