import { Component, OnInit } from '@angular/core';
import { UserModel } from '../models/user';
import { UsersService } from '../services/users.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-one-user',
  templateUrl: './one-user.component.html',
  styleUrls: ['./one-user.component.css']
})
export class OneUserComponent implements OnInit {
   public user: UserModel;
   public userId: number;
  constructor(private userService: UsersService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.userId = this.activatedRoute.snapshot.params['id'];
    this.user = this.userService.findUserByIdFromList(+this.userId);
  }

}
