import { Component, OnInit,ViewChild, ElementRef, TemplateRef } from '@angular/core';
import { Options } from 'fullcalendar';
import { CalendarComponent as CalComp } from 'ng-fullcalendar';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import {PlanningService} from '../services/planning.service';
import { Planning } from '../interfaces/planning.interface';
import { Router } from '@angular/router';
import { TokenStorage } from '../services/token.storage';
import { AppGlobals } from '../globals/app.globals';
import {NotificationService} from '../services/notifications.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {



  modalRef: BsModalRef;
  @ViewChild('DeleteAll')
	DeleteAll : ElementRef;

  calendarOptions1: Options;
  @ViewChild(CalComp) ucCalendar: CalComp;

  @ViewChild('eventName') eventname:ElementRef;
  @ViewChild('eventDateRange') eventDateRange:ElementRef;
  @ViewChild('eventDescription') eventDescription:ElementRef;

  hasPlannings :boolean = false
  eventsFromDataBase = [];
  CalendarId;
  userRole: string;
  SnackBarsNotifications = [];
  isShown:boolean = true;
  constructor(private planningService: PlanningService, private _router: Router,private tokenStorage: TokenStorage,private appGlobals: AppGlobals,private notificationService: NotificationService) { }


  ngOnInit() {
    //check the role of the user
    const role = this.appGlobals.getUserRole(this.tokenStorage.getToken());
    if(this.appGlobals.isEmployee(role)){
      this.userRole = "employee";
        //get id calendar for the user
        this.planningService.getCalendarId().subscribe(result => {
          this.CalendarId = result.id;
          console.log(this.CalendarId);
        })
        //load user plannings
        this.planningService.getAllPlannings().subscribe(result =>{
          console.log(result);
          //console.log(result.length)
          if(result.length > 0){
            this.fetchData(result);
            this.hasPlannings = true;
          }
        })

        this.getUserEvent();

        this.calendarOptions1 = {
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
    
    }else if(this.appGlobals.isManager(role)){
        this.planningService.getEmployeesEvents().subscribe(result => {
          console.log(result);
          if(result.length > 0){
            this.fetchData(result);
            }
        })

        console.log(this.eventsFromDataBase);
      this.calendarOptions1 = {
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
    }
    


    


  }









  private fetchData(data) {
    let i;
    let j = 0;

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

        this.eventsFromDataBase.push(OEvent);

      }
    }

   public generate(i){
     return this.calendarOptions1 = {
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
   }


   public deleteCalendar() {
      this.planningService.deleteCalendar(this.CalendarId).subscribe(result => {
        console.log(result)
      })
      location.reload();
     
   }

   public AutoriserEvent(idEvent){
     this.planningService.autoriserEvent(idEvent).subscribe(result => {
       if(result.Status == "Done"){
         this.eventsFromDataBase.splice(this.eventsFromDataBase.findIndex(e=>e.id == idEvent),1)
       }else{
        console.log(result)
       }
     })
   }

   public RefuserEvent(idEvent){
      this.planningService.refuserEvent(idEvent).subscribe(result => {
        if(result.Status == "Done"){
          this.eventsFromDataBase.splice(this.eventsFromDataBase.findIndex(e=>e.id == idEvent),1)
        }else{
        console.log(result)
        }
      })
   }


   public getUserEvent(){
     this.notificationService.getUserNotifications().subscribe(result => {
        this.fetchNotifications(result)

     },error => {
       console.log(error)
     })
   }


   fetchNotifications(data){
     let i=0
     while(i<data["data"].length){
       console.log(data["data"][i])
       let object = {
         "content": data["data"][i]["contentNotif"],
         "managerName": data["data"][i]["senderNotif"],
       }
      this.SnackBarsNotifications.push(object)
       i++
     }

     setTimeout(()=>{
        document.getElementById("snackbar").classList.remove("show")
     },3200)
   }

}
