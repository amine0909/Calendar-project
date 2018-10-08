import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import "rxjs/Rx";

@Injectable()
export class PlanningService {
  userId = localStorage.getItem("id")
  constructor(private _http: HttpClient) { }


  // for employee
  public getPlanning(idCalendar){
    return this._http.get("http://localhost:8080/getPlanning/"+idCalendar)
      .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }


  public addEvents(events) {
    return this._http.post("http://localhost:8080/create/"+this.userId,events)
      .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }


  public deleteCalendar(idCalendar): any {
    return this._http.delete("http://localhost:8080/deleteCalendar/"+idCalendar)
    .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }

  public saveModifiedEvents(events,idCalendar) {
    return this._http.put("http://localhost:8080/update/"+idCalendar,events)
    .catch((e: any) => Observable.throw(this.errorHandler(e)));

  }

  public getAllPlannings(): any {
    return this._http.get("http://localhost:8080/getAllPlannings/"+this.userId)
    .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }

  public getCalendarId(): any{
    return this._http.get("http://localhost:8080/getCalendarId/"+this.userId)
    .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }


  // for manager

  public getEmployeesEvents(): any{
    return this._http.get("http://localhost:8080/getEmployeesEvents/"+this.userId)
    .catch((e: any) => Observable.throw(this.errorHandler(e)));
  }

   public autoriserEvent(idEvent): any{
      return this._http.put("http://localhost:8080/autoriserEvent/"+idEvent, {
        "managerId": this.userId
      })
      .catch((e: any) => Observable.throw(this.errorHandler(e)));
   }

   public refuserEvent(idEvent): any{
      return this._http.put("http://localhost:8080/refuserEvent/"+idEvent, {
        "managerId": this.userId
      })
      .catch((e: any) => Observable.throw(this.errorHandler(e)));
   }



  // for catching errors
  private errorHandler(error: any){
    console.log(error)
  }
}
