import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Component } from '@angular/core';
import { FullCalendarModule } from 'ng-fullcalendar';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { DropzoneModule } from 'ngx-dropzone-wrapper';
import { DROPZONE_CONFIG } from 'ngx-dropzone-wrapper';
import { DropzoneConfigInterface } from 'ngx-dropzone-wrapper';
import { AppComponent } from './app.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { CalendarComponent} from './calendar/calendar.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import * as moment from 'moment';
import { AlertModule } from 'ngx-bootstrap/alert';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { UsersService } from './services/users.service';
import { UserComponent } from './user/user.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppGlobals } from './globals/app.globals';
import { AuthService } from './services/authentification.service';
import { Interceptor } from './services/app.interceptor';
import { TokenStorage } from './services/token.storage';
import { AuthMiddleware } from './middlewares/authMiddleware';
import { AdminMiddleware } from './middlewares/adminMiddleware';
import { ManagerMiddleware } from './middlewares/managerMiddleware';
import { AllUsersComponent } from './all-users/all-users.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ForAllMiddleware } from './middlewares/forAllMiddleware';
import { AdminAndManagerMiddleware } from './middlewares/adminAndManagerMiddleware';
import { MyEmployeesComponent } from './my-employees/my-employees.component';
import { OneUserComponent } from './one-user/one-user.component';

import { PlanningService } from './services/planning.service';
import { DashboardComponent } from './dashboardEmployee/dashboard.component';
import { NewCalendarComponent } from './new-calendar/new-calendar.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { NotificationService } from './services/notifications.service';

const appRoutes: Routes = [
  { path: '', canActivate: [AuthMiddleware] , component: HomeComponent },
  { path: 'Login', component: LoginComponent },
  { path: 'SignUp', canActivate : [AuthMiddleware, AdminMiddleware] , component: SignupComponent },
  //{ path: 'Calendar', canActivate: [AuthMiddleware], component: CalendarComponent },
  {path : 'users/all', canActivate : [AuthMiddleware, AdminMiddleware], component: AllUsersComponent},
  {path : 'users/:id/edit', canActivate: [AuthMiddleware, AdminMiddleware], component : EditUserComponent},
  {path : 'users/myEmployees', canActivate : [AuthMiddleware, AdminAndManagerMiddleware], component :  MyEmployeesComponent},
  {path : 'users/:id', canActivate: [AuthMiddleware, AdminAndManagerMiddleware], component : OneUserComponent},
  {path :  'dashboard', canActivate: [AuthMiddleware], component: DashboardComponent},
  {path :  'dashboard/calendar/:id', canActivate: [AuthMiddleware], component: CalendarComponent},
  {path :  'dashboard/newCalendar', canActivate: [AuthMiddleware], component: NewCalendarComponent},
  {path : 'dashboard/notifications', canActivate: [AuthMiddleware], component: NotificationsComponent}
];


const DEFAULT_DROPZONE_CONFIG: DropzoneConfigInterface = {
  // Change this to your upload POST address:
   url: 'https://httpbin.org/post',
   maxFilesize: 50,
   createImageThumbnails: false,
   previewsContainer: false,
 };

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    CalendarComponent,
    UserComponent,
    AllUsersComponent,
    EditUserComponent,
    MyEmployeesComponent,
    OneUserComponent,
    DashboardComponent,
    NewCalendarComponent,
    NotificationsComponent,

  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    FormsModule,
    FullCalendarModule,
    DropzoneModule,
    ModalModule.forRoot(),
    BsDatepickerModule.forRoot(),
    TimepickerModule.forRoot(),
    AlertModule.forRoot(),
    TooltipModule.forRoot(),
    ReactiveFormsModule,
    HttpClientModule
    ],
  providers: [
    {
      provide: DROPZONE_CONFIG,
      useValue: DEFAULT_DROPZONE_CONFIG
    },
    UsersService,
    AuthService,
    NotificationService,
    AppGlobals,
    TokenStorage,
    AuthMiddleware,
    AdminMiddleware,
    ManagerMiddleware,
    ForAllMiddleware,
    AdminAndManagerMiddleware,
    {
      provide: HTTP_INTERCEPTORS,
      useClass : Interceptor,
      multi : true
    },
    PlanningService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
