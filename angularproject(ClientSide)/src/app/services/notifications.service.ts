import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import "rxjs/Rx";
import {HttpClient} from "@angular/common/http";


@Injectable()
export class NotificationService {
  
    userId: string = localStorage.getItem("id");

    constructor(private __http: HttpClient){}


    public getUserNotifications():any{
        return this.__http.get("http://localhost:8080/getUserNotifications/"+this.userId)
        .catch((e: any) => Observable.throw(this.errorHandler(e)));
    }

    public getAllUserNotifications(): any{
      return this.__http.get("http://localhost:8080/getAllUserNotifications/"+this.userId)
      .catch((e: any) => Observable.throw(this.errorHandler(e)));
    }
  // for catching errors
  private errorHandler(error: any){
    console.log(error)
  }
}