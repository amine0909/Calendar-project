package com.businessanddecisions.controllers;

import com.businessanddecisions.models.EventModel;
import com.businessanddecisions.models.PlanningModel;
import com.businessanddecisions.models.PlanningResponse;
import com.businessanddecisions.models.UserModel;
import com.businessanddecisions.repositories.EventRepository;
import com.businessanddecisions.repositories.PlanningRepository;
import com.businessanddecisions.repositories.UserRepository;
import com.businessanddecisions.services.PlanningService;
import net.fortuna.ical4j.model.component.CalendarComponent;

import java.awt.*;
import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmployeeController {
    @Autowired
    private PlanningRepository planningRepository;
    @Autowired
    private PlanningService plannningService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.POST, value="/create/{id}")
    public ResponseEntity<HashMap<String, String>>  createCalendar(@RequestBody  ArrayList<PlanningResponse> events, @PathVariable("id") String userId) {

        //1: check if user has not a calendar first ==> create a new one
        UserModel user = this.userRepository.findById(Long.parseLong(userId)).get();
        PlanningModel Planning = this.planningRepository.findByCreator(user);
        if(Planning == null){
            PlanningModel newPlanning = new PlanningModel(user);
            this.planningRepository.save(newPlanning);
            List<CalendarComponent> components = this.plannningService.CalendarOperations(events);
            for(int i=0; i<components.size(); i++){
                byte[] bytes = IcsToLob(components.get(i));
                this.eventRepository.save(new EventModel(bytes,newPlanning,null));

            }

            HashMap<String, String> hash = new HashMap<>();
            hash.put("Status", "Creation success");
            return new ResponseEntity<>(hash, HttpStatus.OK);
        }else{

            EventModel ev;
            for(int i=0; i<events.size(); i++){
                byte[] bytes = IcsToLob(this.plannningService.createEvent(events.get(i)));
                ev = new EventModel(bytes,Planning,null);
               // System.out.println(bytes);
                System.out.println(this.eventRepository.save(ev));
            }

            HashMap<String, String> hash2 = new HashMap<>();
            hash2.put("Status", "Appending success");

            return new ResponseEntity<>(hash2, HttpStatus.OK);

        }
    }





    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/getPlanning/{id}")
    public ResponseEntity<List<HashMap<String, Object>>> getPlanning(@PathVariable("id")  String idCalendar) {


        // 1 get the planning for that user
        List<EventModel> events = this.eventRepository.findByCalendar(Long.parseLong(idCalendar));
        List<HashMap<String, Object>> components = new ArrayList<>();
        HashMap<String, Object> hash;
        int i=0;
        while(i<events.size()){
            hash =  new HashMap<>();
            hash.put("idEvent", events.get(i).getId());
            hash.put("eventContent",LobToIcs(events.get(i).getContent()));
            hash.put("Status", events.get(i).getStatusPlanning());
            components.add(hash);
            i++;
        }

        return new ResponseEntity<>(components, HttpStatus.OK);
    }




    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value="/getAllPlannings/{id}")
    public ResponseEntity<List<HashMap<String, Object>>> getAllUserPlannings(@PathVariable("id") String userId) {
        List<HashMap<String, Object>> components = new ArrayList<>();

        //1 get the user with that id
        UserModel user = this.userRepository.findById(Long.parseLong(userId)).get();

        // 2 get the planning for that user
        PlanningModel plannings = this.planningRepository.findByCreator(user);

        if(plannings == null){
            return new ResponseEntity(Collections.emptyList(), HttpStatus.OK);
        }


        List<EventModel> events = this.eventRepository.findByCalendar(plannings.getId());
        HashMap<String, Object> hash;
        int i=0;
        while(i<events.size()){
            hash =  new HashMap<>();
            hash.put("idEvent", events.get(i).getId());
            hash.put("eventContent",LobToIcs(events.get(i).getContent()));
            hash.put("Status", events.get(i).getStatusPlanning());
            components.add(hash);
            i++;
        }


        return new ResponseEntity<>(components, HttpStatus.OK);

    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.PUT, value="/update/{id}")
    public ResponseEntity<HashMap<String, String>>  updateCalendar(@RequestBody ArrayList<PlanningResponse> events, @PathVariable("id") String idCalendar){


        List<String> listIds = new ArrayList<>();
        for(PlanningResponse pr: events){
           listIds.add(pr.getId());
        }

        List<EventModel> listEvents = this.eventRepository.findByCalendar(Long.parseLong(idCalendar));
        for(EventModel em : listEvents){
            if(!listIds.contains(em.getId().toString())){
                this.eventRepository.deleteById(em.getId());
            }
        }

      HashMap<String,String> hash = new HashMap<>();
      hash.put("Status", "Updating with success");
      return new ResponseEntity<>(hash, HttpStatus.OK);

    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value = "/getCalendarId/{id}")
    public ResponseEntity<HashMap<String, String>> getCalendarId (@PathVariable("id") String userId){
        UserModel user = this.userRepository.findById(Long.parseLong(userId)).get();
        PlanningModel planning = this.planningRepository.findByCreator(user);
        HashMap<String ,String > hash = new HashMap<>();

        if(planning == null){
            hash.put("id", "NOT_FOUND");
            return new ResponseEntity<>(hash, HttpStatus.OK);
        }

        hash.put("id", planning.getId().toString());
        return new ResponseEntity<>(hash, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteCalendar/{id}")
    public ResponseEntity<HashMap<String , String>> deleteCalendar(@PathVariable("id") String calendarId){
        this.eventRepository.deleteByCalendar(Long.parseLong(calendarId));
        this.planningRepository.deleteById(Long.parseLong(calendarId));
        HashMap<String , String> hash = new HashMap<>();
        hash.put("Status", "Done");

        return new ResponseEntity<>(hash, HttpStatus.OK);
    }





    /* SOME PRIVATE METHOD */

    private  List<CalendarComponent> readIcsFile(String nameFile) {
        return this.plannningService.ReadIcs(nameFile);
    }


    private byte[] IcsToLob(CalendarComponent component) {
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(component);
            byte[] bytes = bos.toByteArray();
            return bytes;

        }catch(IOException e){e.printStackTrace();}
        return null;
    }


    private CalendarComponent LobToIcs(byte[] eventByte){
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


    /*private byte[] IcsToLob(List<CalendarComponent> components) {
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(components);
            byte[] bytes = bos.toByteArray();
            return bytes;

        }catch(IOException e){e.printStackTrace();}
        return null;
    }

    // it works
    private List<CalendarComponent> LobToIcs(byte[] listByte){
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(listByte);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (List<CalendarComponent>) ois.readObject();

        }catch(IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    */
}
