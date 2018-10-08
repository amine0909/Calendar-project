import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {


  constructor() { }

  ngOnInit() {
  }

  onUploadError(){
    //Upload Error
    console.log("Error");
  }

  onUploadSuccess(){
    //Upload OK
    console.log("OK");
  }

  onUploadProcess() {
    //Upload Loading
    console.log("Processing");
  }

}
