import { Component, OnInit, ViewChild, ElementRef, TemplateRef } from '@angular/core';
import { CalendarComponent as CalComp } from 'ng-fullcalendar';
import { Options } from 'fullcalendar';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import * as moment from 'moment';
import { isNullOrUndefined } from 'util';


import { Planning } from '../interfaces/planning.interface';
import {PlanningService } from '../services/planning.service';
import {ActivatedRoute} from "@angular/router";

declare var $: any;



  let DeleteMode : boolean = false;

  let emptyEvent = [];

  let data = [];



@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {


  public Alert1  = false;
  public Alert2  = false;
  public Alert3  = false;

  modalRef: BsModalRef;
  @ViewChild('DeleteAll')
	DeleteAll : ElementRef;

  calendarOptions: Options;
  @ViewChild(CalComp) ucCalendar: CalComp;

  @ViewChild('eventName') eventname:ElementRef;
  @ViewChild('eventDateRange') eventDateRange:ElementRef;
  @ViewChild('eventDescription') eventDescription:ElementRef;





  eventsFromDataBase = []

  hourDeb: Date = new Date();
  hourEnd: Date = new Date();
  PlanningDate: Date;
  loading: boolean = true;
  data = [];
  startDateString:string = "";
  EndDateString: string  = "";
  startHourString:string = "";
  endtHourString: string = "";
  eventName: string = "";
  eventDesc: string = "";

  ButtonDisabled: boolean = true;
  ButtonDeleteDisabled: boolean;
  buttonStatus: string = "add";
  idCalendar;

  constructor(private modalService: BsModalService,private planningService: PlanningService,private activatedRoute: ActivatedRoute) {

  }

  ngOnInit() {
      // // by amine
      this.idCalendar = this.activatedRoute.snapshot.paramMap.get('id');

      this.planningService.getPlanning(this.idCalendar).subscribe(result => {
        console.log(result);
        this.fetchData(result);
        this.loading = false;
      });



     this.calendarOptions = {
        locale: 'fr',
        buttonText: {
          today: 'Ajourdu\'hui',
          month: 'Mois',
          week: 'Semaine',
          day: 'Jour',
          list: 'List'
        },
        editable: false,
        eventStartEditable: false,
        eventLimit: false,
        aspectRatio: 1.8,
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'month,agendaWeek,agendaDay,listMonth'
        },
        droppable: false,
        timezone: "africa/tunisia",
        events:this.eventsFromDataBase
      };

      this.ButtonDisabled = true;


  }


  private fetchData(data) {
    let i;
   if(data.length != 0){
    for(i=0; i<data.length;i++) {
      let OEvent = {} as Planning;
      OEvent.title = data[i].eventContent.summary.value;
      OEvent.start = data[i].eventContent.startDate.date;
      OEvent.end = data[i].eventContent.endDate.date;
      OEvent.desciption = data[i].eventContent.description.value;
      OEvent.id = data[i].idEvent;
      OEvent.nomPropri = data[i].nomPropri;
      OEvent.prenomPropri = data[i].prenomPropri;
      OEvent.status = data[i].Status;
      if(data[i].Status == "waiting") {OEvent.color = "grey";OEvent.textColor="white"}
      else if(data[i].Status == "refused") {OEvent.color = "red";OEvent.textColor="white"} 
      else{OEvent.color = "green";OEvent.textColor="white"}

      this.eventsFromDataBase.push(OEvent)
    }
    this.ButtonDeleteDisabled = false;
   }else{
    console.log("user have not a calendar")
    this.ButtonDeleteDisabled = true;
   }

  }




  // code for the plugin

  ClearCalender() {
    console.log("DeleteAll Clicked")

    let listofEvents = this.ucCalendar.fullCalendar('clientEvents' );
    listofEvents.forEach(element => {
      console.log(element._id);
      this.ucCalendar.fullCalendar('removeEvents', element._id);
    });
    this.ucCalendar.fullCalendar('refetchEvents' );
    this.ucCalendar.fullCalendar('rerenderEvents');
  }

  ToogleDeleteMode() {
    if (DeleteMode == false)
    {
      DeleteMode = true;
      console.log("DeleteMode : " + DeleteMode);
    }
    else {
      DeleteMode = false;
      console.log("DeleteMode : " + DeleteMode);
    }
  }

  eventClick(event){
    let event_id = event.event._id;

    if (DeleteMode == true) {

      this.ucCalendar.fullCalendar('removeEvents', event_id);
      this.prepareCurrentData(event.event.source.eventDefs)

    }
  }













  DeleteBtnText() {
    if (DeleteMode == true)
      return "Quitter Le Mode Suppression";
    else {
      return "Activer Le Mode Suppression"
    }
  }



  cursorType() {
    if (DeleteMode == false)
    {
      return "";
    }
    else {
      return "DeleteCursor"
    }
  }

  DeleteBtnClassToggle() {
    if (DeleteMode == false)
    {
      return "btn-secondary";
    }
    else {
      return "btn-alert"
    }
  }




  openModal(template: TemplateRef<any>) {
    //for modal (Important)
    this.modalRef = this.modalService.show(template);
  }



  createEventFromModal(eventname,PlanningDate,eventdescrip,hourDeb,hourEnd) {
    //get date from input - create an event objet - add it to the calendar

     if (isNullOrUndefined(eventname))
       {
         this.Alert1 = true;
         this.Alert2 = false;
        console.log("eventname undefined");
        return;
       }

     if (isNullOrUndefined(PlanningDate))
      {
        this.Alert2 = true;
        this.Alert1 = false;
        console.log("daterange undefined");
        return;
      }

    // 1 prepare the variables
    let StartDate = moment(PlanningDate[0]).format("YYYY-MM-DD");
    let EndDate =   moment(PlanningDate[1]).format("YYYY-MM-DD");


    this.startDateString = StartDate.toString();
    this.EndDateString = EndDate.toString();

    console.log(this.startDateString);
    console.log(this.EndDateString);


    // for hours now
    // add "0" when "Am" is chosed
    if(hourDeb.getHours().toString().length == 1){
      this.startHourString = "0"+hourDeb.getHours()+":"+hourDeb.getMinutes();
    }else{
      this.startHourString = hourDeb.getHours()+":"+hourDeb.getMinutes();
    }


    if(hourEnd.getHours().toString().length == 1){
      this.endtHourString = "0"+hourEnd.getHours()+":"+hourEnd.getMinutes();
    }else{
      this.endtHourString = hourEnd.getHours()+":"+hourEnd.getMinutes();
    }


    this.eventName = eventname;
    this.eventDesc = eventdescrip;
    this.buttonStatus ="add";
    this.ButtonDisabled = false;


      let e1 = {
        title: eventname,
        start: StartDate,
        end: EndDate,
        description: eventdescrip
      }


      let object = {
        eventName: this.eventName,
        eventDescription: this.eventDesc,
        startDate: this.startDateString,
        endDate: this.EndDateString,
        startHour: this.startHourString,
        endHour: this.endtHourString
      }

      data.push(object);
      this.ucCalendar.fullCalendar('renderEvent',e1);

      this.Alert1 = false;
      this.Alert2 = false;
      this.Alert3 = false;

      //hide the modal
      this.modalRef.hide();
      this.PlanningDate = null
      this.ButtonDisabled = false;
  }


 public saveCalendarChanges(): void{

        // 3: prepare the service
        if(this.buttonStatus == "add"){
          this.loading = true
          console.log(this.idCalendar)
          this.planningService.addEvents(data)
            .subscribe(result => {
              console.log(result)
              this.ButtonDisabled = false
              this.loading = false;
              location.reload();
             });
        }else if(this.buttonStatus == "update"){
          console.log(data)
          this.loading = true;
          this.planningService.saveModifiedEvents(data,this.idCalendar)
              .subscribe(result => {
                console.log(result)
                location.reload();
              })

        }
  }

  private prepareCurrentData(CurrentEvents) {
    let i=0;
    data = [];
    let currentData = CurrentEvents;
     while(i<currentData.length){
      let dateStart = new Date(currentData[i].dateProfile.start._i);
      let dateEnd = new Date(currentData[i].dateProfile.end._i);

      let startDate = dateStart.getFullYear()+"-"+(dateStart.getMonth()+1)+"-"+dateStart.getDate();
      let endDate = dateEnd.getFullYear()+"-"+(dateEnd.getMonth()+1)+"-"+dateEnd.getDate();
      let startHour = dateStart.getHours()+":"+dateStart.getMinutes();
      let endHour = dateEnd.getHours()+":"+dateEnd.getMinutes();
  
      let object = {
        eventName: currentData[i].title,
        eventDescription: currentData[i].miscProps.description,
        startDate: startDate,
        endDate: endDate,
        startHour: startHour,
        endHour: endHour,
        id: currentData[i].id
      }

      data.push(object)
      i++
    }
    console.log(data)
    this.ButtonDisabled = false;
    this.buttonStatus = "update"
  }

}
