package com.businessanddecisions.controllers;


import com.businessanddecisions.Enums.StatusPlanning;
import com.businessanddecisions.models.EventModel;
import com.businessanddecisions.models.NotificationModel;
import com.businessanddecisions.models.UserModel;
import com.businessanddecisions.repositories.EventRepository;
import com.businessanddecisions.repositories.NotificationRepository;
import com.businessanddecisions.repositories.UserRepository;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class ManagerController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value="/getEmployeesEvents/{id}")
    public ResponseEntity<List<HashMap<String, Object>>> getEmployeesEvents(@PathVariable("id") String managerId){
        List<UserModel> managerEmployee = this.userRepository.getManagerEmployee(Long.parseLong(managerId));
        List<HashMap<String, Object>> components = new ArrayList<>();
        HashMap<String, Object> hash;

        for(UserModel user: managerEmployee){
            List<EventModel> events = this.eventRepository.findByCreatorForManager(user.getId());
            for(EventModel event: events){
                hash =  new HashMap<>();
                hash.put("idEvent", event.getId());
                hash.put("eventContent", LobToIcs(event.getContent()));
                hash.put("Status", event.getStatusPlanning());
                hash.put("nomPropri", user.getFirstName());
                hash.put("prenomPropri", user.getLastName());
                components.add(hash);
            }

        }


        return new ResponseEntity<>(components,HttpStatus.OK);
    }



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.PUT, value="/autoriserEvent/{id}")
    public ResponseEntity<HashMap<String,String>> autoriserEvent(@PathVariable("id") String idEvent, @RequestBody HashMap<String, String> managerId){
        Long EventPropriId = this.eventRepository.getEventPropriID(Long.parseLong(idEvent));
        UserModel user = this.userRepository.findById(EventPropriId).get();
        EventModel event = this.eventRepository.findById(Long.parseLong(idEvent)).get();
        event.setStatusPlanning(StatusPlanning.accepted);
        UserModel manager = this.userRepository.findById(Long.parseLong(managerId.get("managerId"))).get();;
        event.setSubmittedBy(manager);
        HashMap<String, String> hash = new HashMap<>();
        if(this.eventRepository.save(event) != null){
            this.notificationRepository.save(new NotificationModel(
                    "accepted your event",
                    user,manager,event
            ));

            hash.put("Status", "Done");
            return new ResponseEntity<>(hash, HttpStatus.OK);
            }
        hash.put("Status", "Failed");
        return new ResponseEntity<>(hash, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.PUT, value="/refuserEvent/{id}")
    public ResponseEntity<HashMap<String,String>> refuserEvent (@PathVariable("id") String idEvent, @RequestBody HashMap<String, String> managerId){
        Long EventPropriId = this.eventRepository.getEventPropriID(Long.parseLong(idEvent));
        UserModel user = this.userRepository.findById(EventPropriId).get();
        EventModel event = this.eventRepository.findById(Long.parseLong(idEvent)).get();
        event.setStatusPlanning(StatusPlanning.refused);
        UserModel manager = this.userRepository.findById(Long.parseLong(managerId.get("managerId"))).get();;
        event.setSubmittedBy(manager);
        HashMap<String, String> hash = new HashMap<>();
        if(this.eventRepository.save(event) != null){
            this.notificationRepository.save(new NotificationModel(
                    "refused your event",
                    user,manager,event
            ));



            hash.put("Status", "Done");
            return new ResponseEntity<>(hash, HttpStatus.OK);
        }
        hash.put("Status", "Failed");
        return new ResponseEntity<>(hash, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static CalendarComponent LobToIcs(byte[] eventByte){
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(eventByte);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (CalendarComponent) ois.readObject();

        }catch(IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
