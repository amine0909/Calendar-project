import { Component, OnInit } from '@angular/core';
import {NotificationService} from '../services/notifications.service';


@Component({
  selector: 'myprefix-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  public notificationsInfo = [];
  constructor(private __notificationService: NotificationService) { }

  ngOnInit() {
    this.getAllUserNotifications();
  }


  private getAllUserNotifications(){
    this.__notificationService.getAllUserNotifications().subscribe(result => {
      console.log(result)
      this.fetchData(result)
    })
  }

  private fetchData(data) {
    let i;
    let j = 0;

      for(i=0; i<data['data'].length;i++) {
       let object = {
         "senderNotif": data['data'][i].senderNotif,
         "contentNotif": data['data'][i].contentNotif,
         "startDate": data['data'][i].contentEvent.startDate.date,
         "endDate": data['data'][i].contentEvent.endDate.date,
         "EventTitle": data["data"][i].contentEvent.summary.value
       }

       this.notificationsInfo.push(object)

      }
      console.log(this.notificationsInfo)
    }


   

}
