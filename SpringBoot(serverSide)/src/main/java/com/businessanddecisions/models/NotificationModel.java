package com.businessanddecisions.models;

import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "notifications")
public class NotificationModel {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(name = "contentNotification")
    private String content;

    @ManyToOne(targetEntity = UserModel.class,fetch=FetchType.EAGER)
    private UserModel Sender;


    @ManyToOne(targetEntity = UserModel.class,fetch=FetchType.EAGER)
    private UserModel Receiver;

    @ManyToOne(targetEntity = EventModel.class,fetch=FetchType.EAGER)
    private EventModel event;

    @CreationTimestamp
    @Column(name="created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_At")
    private Date updatedAt;

    @Column(name = "seen")
    private String Seen; //"true", "false"


    public NotificationModel(){}

    public NotificationModel(String content,UserModel receiver, UserModel sender, EventModel event){
        this.content = content;
        this.Receiver = receiver;
        this.Sender = sender;
        this.Seen = "false";
        this.event = event;
    }


    public Long getId(){return  this.Id;}
    public void setId(Long id){this.Id = id;}

    public String getContent() {return this.content;}
    public void setContent(String content){this.content = content;}

    public UserModel getSender(){return this.Sender;}
    public void setSender(UserModel sender){this.Sender = sender;}

    public UserModel getReceiver(){return this.Receiver;}
    public void setReceiver(UserModel receiver){this.Receiver = receiver;}

    public String getSeen(){return this.Seen;}
    public void setSeen(String seen) {this.Seen = seen;}

    public EventModel getEvent(){return this.event;}
    public void setEvent(EventModel event) {this.event = event;}
}
