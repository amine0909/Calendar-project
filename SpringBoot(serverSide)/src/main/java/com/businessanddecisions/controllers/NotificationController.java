package com.businessanddecisions.controllers;


import com.businessanddecisions.models.NotificationModel;
import com.businessanddecisions.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value="/getUserNotifications/{id}")
    public ResponseEntity<HashMap<String, List<HashMap<String, Object>>>> getUserNotifications(@PathVariable("id") String userId){
        List<NotificationModel> notifications = this.notificationRepository.findByReceiver(Long.parseLong(userId));

        HashMap<String, List<HashMap<String, Object>>> hash = new HashMap<>();
        if(notifications.isEmpty()){
            hash.put("data", Collections.emptyList());
            return new ResponseEntity<>(hash, HttpStatus.OK);
        }

        HashMap<String, Object> results;
        int i=0;
        while(i<notifications.size()){
            results = new HashMap<>();
            List<HashMap<String, Object>> list = new ArrayList<>();
            results.put("contentNotif", notifications.get(i).getContent());
            results.put("senderNotif", notifications.get(i).getSender().getFirstName()+" "+notifications.get(i).getSender().getLastName());
            results.put("contentEvent", ManagerController.LobToIcs(notifications.get(i).getEvent().getContent()));
            list.add(results);
            hash.put("data", list);

            //update the "seen" field
            NotificationModel notif = notifications.get(i);
            notif.setSeen("true");
            this.notificationRepository.save(notif);
            i++;
        }

        return new ResponseEntity<>(hash, HttpStatus.OK);


    }



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value="/getAllUserNotifications/{id}")
    public ResponseEntity<HashMap<String, List<HashMap<String, Object>>>> getAllUserNotifications(@PathVariable("id") String userId){
        List<NotificationModel>   notifications = this.notificationRepository.findAllByReceiver(Long.parseLong(userId));


        HashMap<String, List<HashMap<String, Object>>> hash = new HashMap<>();
        HashMap<String, Object> results;
        List<HashMap<String, Object>> list = new ArrayList<>();
        int i=0;
        while(i<notifications.size()){
            results = new HashMap<>();
            results.put("contentNotif", notifications.get(i).getContent());
            results.put("senderNotif", notifications.get(i).getSender().getFirstName()+" "+notifications.get(i).getSender().getLastName());
            results.put("contentEvent", ManagerController.LobToIcs(notifications.get(i).getEvent().getContent()));
            list.add(results);

            //update the "seen" field
            NotificationModel notif = notifications.get(i);
            notif.setSeen("true");
            this.notificationRepository.save(notif);
            i++;
        }

        hash.put("data", list);
        return new ResponseEntity<>(hash, HttpStatus.OK);
    }


 }
