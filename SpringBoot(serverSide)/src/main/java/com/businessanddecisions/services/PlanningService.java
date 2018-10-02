package com.businessanddecisions.services;

import java.io.*;
import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.businessanddecisions.models.PlanningResponse;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.UidGenerator;
@Service
public class PlanningService {

	
	public List<CalendarComponent> ReadIcs(String nameFile) {
		if(new File(nameFile).exists()){
			try {
				FileInputStream file = new FileInputStream(nameFile);
				CalendarBuilder builder = new CalendarBuilder();
				Calendar calendar = builder.build(file);

				return calendar.getComponents();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		return null;
	}




	public String deleteCalendar(String userId){

		// get the current year
		int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
		/*
		File fileCalendar = new File("Plannings//45//2018//calendar.ics");
		System.out.println(fileCalendar);
		if(fileCalendar.delete()){
			return fileCalendar.getPath();
		}

		return null;*/

		FileOutputStream fis;

		try {
			File fileCalendar = new File("Plannings/45/2018/calendar.ics");
			fis = new FileOutputStream(fileCalendar);
			fis.close();
			if(fileCalendar.delete()){
				return fileCalendar.getPath();
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}


	/*public String updateCalendar(ArrayList<PlanningResponse> events, String userId) {

		// get the current year
		int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

		try {
			FileInputStream file = new FileInputStream("Plannings/"+userId+"/"+year+"/calendar.ics");
			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(file);

			// clear the calendar
			calendar.getComponents().clear();

			// add the new events
			this.createEvent(calendar,events);

			this.saveCalendar(calendar, new File("Plannings/"+userId+"/"+year+"/calendar.ics"));

			return "done";
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;


	}
*/




	public List<CalendarComponent> AppendEventsToCalendar(List<CalendarComponent> components ,ArrayList<PlanningResponse> events){
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone  timezone;
		timezone = registry.getTimeZone("UTC");


		// split time now
		String year1,month1,day1,hour1,minute1,year2,month2,day2,hour2,minute2,eventName,eventDescription;


		int i;
		for(i=0; events.size() > i; i++){
			year1 = events.get(i).getStartDate().split("-")[0];
			month1 = events.get(i).getStartDate().split("-")[1];
			day1 = events.get(i).getStartDate().split("-")[2];
			hour1 = events.get(i).getStartHour().split(":")[0];
			minute1 = events.get(i).getStartHour().split(":")[1];




			year2 = events.get(i).getEndDate().split("-")[0];
			month2 = events.get(i).getEndDate().split("-")[1];
			day2 = events.get(i).getEndDate().split("-")[2];
			hour2 = events.get(i).getEndHour().split(":")[0];
			minute2 = events.get(i).getEndHour().split(":")[1];

			eventName =  events.get(i).getEventName();
			eventDescription =  events.get(i).getEventDescription();



			// time de create event
			// start date
			java.util.Calendar startDate = java.util.Calendar.getInstance(timezone);
			startDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month1)-1);
			startDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day1));
			startDate.set(java.util.Calendar.YEAR, Integer.parseInt(year1));
			startDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour1)-1);
			startDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute1));
			startDate.set(java.util.Calendar.SECOND, 0);

			// end date
			java.util.Calendar endDate = java.util.Calendar.getInstance(timezone);
			endDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month2)-1);
			endDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day2)+1);
			endDate.set(java.util.Calendar.YEAR, Integer.parseInt(year2));
			endDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour2)-1);
			endDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute2));
			endDate.set(java.util.Calendar.SECOND, 0);

			DateTime start = new DateTime(startDate.getTime());
			DateTime end = new DateTime(endDate.getTime());
			start.setTimeZone(timezone);
			end.setTimeZone(timezone);

            try{
				// Create the event
				VEvent event = new VEvent(start,end,eventName);
				event.getProperties().add(new Description(eventDescription));
				// generate unique identifier..
				UidGenerator uid = new UidGenerator("1");
				event.getProperties().add(uid.generateUid());
				components.add(event);

			}catch (SocketException e){e.printStackTrace();}



		}

		return components;
	}



	public  List<CalendarComponent> CalendarOperations(ArrayList<PlanningResponse> events) {
		Calendar calendar;
		try {
			// 1 : create a calendar
			calendar = this.createCalendar();
			
			//2 : create an event
			this.createEvent(calendar,events);
			
			//3 save the calendar
			return this.saveCalendar(calendar);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	
	
	
	
	
	
	
	
	

	private Calendar createCalendar() throws SocketException {
		// 1: Create a calendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("Amine bejaoui"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Australia/Melbourne");


		return calendar;
	}
	
	
	
	private  void createEvent(Calendar calendar,ArrayList<PlanningResponse> events) throws SocketException {

		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone  timezone = registry.getTimeZone("UTC");
		

		// split time now
		String year1,month1,day1,hour1,minute1,year2,month2,day2,hour2,minute2,eventName,eventDescription;


		int i;
		for(i=0;i<events.size();i++){
			year1 = events.get(i).getStartDate().split("-")[0];
			month1 = events.get(i).getStartDate().split("-")[1];
			day1 = events.get(i).getStartDate().split("-")[2];
			hour1 = events.get(i).getStartHour().split(":")[0];
			minute1 = events.get(i).getStartHour().split(":")[1];




			year2 = events.get(i).getEndDate().split("-")[0];
			month2 = events.get(i).getEndDate().split("-")[1];
			day2 = events.get(i).getEndDate().split("-")[2];
			hour2 = events.get(i).getEndHour().split(":")[0];
			minute2 = events.get(i).getEndHour().split(":")[1];

			eventName =  events.get(i).getEventName();
			eventDescription =  events.get(i).getEventDescription();



			// time de create event
			// start date
			java.util.Calendar startDate = java.util.Calendar.getInstance(timezone);
			startDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month1)-1);
			startDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day1)-1);
			startDate.set(java.util.Calendar.YEAR, Integer.parseInt(year1));
			startDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour1)-1);
			startDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute1));
			startDate.set(java.util.Calendar.SECOND, 0);

			// end date
			java.util.Calendar endDate = java.util.Calendar.getInstance(timezone);
			endDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month2)-1);
			endDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day2));
			endDate.set(java.util.Calendar.YEAR, Integer.parseInt(year2));
			endDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour2)-1);
			endDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute2));
			endDate.set(java.util.Calendar.SECOND, 0);

			DateTime start = new DateTime(startDate.getTime());
			DateTime end = new DateTime(endDate.getTime());
			start.setTimeZone(timezone);
			end.setTimeZone(timezone);


			// Create the event
			VEvent event = new VEvent(start,end,eventName);
			event.getProperties().add(new Description(eventDescription));
			Attendee attendee1 = new Attendee(URI.create("MAILTO"+"aminebejaoui@gmail.com"));
			event.getProperties().add(attendee1);
			// generate unique identifier..
			UidGenerator uid = new UidGenerator("1");
			event.getProperties().add(uid.generateUid());


			calendar.getComponents().add(event);
		}


		
	}


	public VEvent createEvent(PlanningResponse event){
		try{
			TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
			TimeZone  timezone = registry.getTimeZone("UTC");


			// split time now
			String year1,month1,day1,hour1,minute1,year2,month2,day2,hour2,minute2,eventName,eventDescription;


			year1 = event.getStartDate().split("-")[0];
			month1 = event.getStartDate().split("-")[1];
			day1 = event.getStartDate().split("-")[2];
			hour1 = event.getStartHour().split(":")[0];
			minute1 = event.getStartHour().split(":")[1];




			year2 = event.getEndDate().split("-")[0];
			month2 = event.getEndDate().split("-")[1];
			day2 = event.getEndDate().split("-")[2];
			hour2 = event.getEndHour().split(":")[0];
			minute2 = event.getEndHour().split(":")[1];

			eventName =  event.getEventName();
			eventDescription =  event.getEventDescription();



			// time de create event
			// start date
			java.util.Calendar startDate = java.util.Calendar.getInstance(timezone);
			startDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month1)-1);
			startDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day1)+1);
			startDate.set(java.util.Calendar.YEAR, Integer.parseInt(year1));
			startDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour1)-1);
			startDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute1));
			startDate.set(java.util.Calendar.SECOND, 0);

			// end date
			java.util.Calendar endDate = java.util.Calendar.getInstance(timezone);
			endDate.set(java.util.Calendar.MONTH,	 Integer.parseInt(month2)-1);
			endDate.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day2)+1);
			endDate.set(java.util.Calendar.YEAR, Integer.parseInt(year2));
			endDate.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour2)-1);
			endDate.set(java.util.Calendar.MINUTE, Integer.parseInt(minute2));
			endDate.set(java.util.Calendar.SECOND, 0);

			DateTime start = new DateTime(startDate.getTime());
			DateTime end = new DateTime(endDate.getTime());
			start.setTimeZone(timezone);
			end.setTimeZone(timezone);


			// Create the event
			VEvent Vevent = new VEvent(start,end,eventName);
			Vevent.getProperties().add(new Description(eventDescription));
			// generate unique identifier..
			UidGenerator uid = new UidGenerator("1");
			Vevent.getProperties().add(uid.generateUid());
			return Vevent;
		}catch (SocketException e){e.printStackTrace();}



		return null;

	}


	private List<CalendarComponent>  saveCalendar(Calendar calendar) {
		return calendar.getComponents();
	}





}
