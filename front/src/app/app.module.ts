import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

import {
  CalendarModule,
  DataTableModule,
  DialogModule,
  DropdownModule,
  ScheduleModule,
  SelectButtonModule
} from 'primeng/primeng';

import {AppComponent} from './app.component';
import {HeaderComponent} from './widget/header/header.component';
import {LeaveRequestComponent} from './widget/leaveRequest/leave-request.component';
import {HomeComponent} from './widget/home/home.component';
import {AuthGuard} from './app.authGuard';

import {RouterModule} from '@angular/router';
import {ROUTES} from './app.route';
import {ConnectionBackend, Http, HttpModule, XHRBackend} from '@angular/http';
import {ListRequestsComponent} from './widget/listRequests/list-requests.component';
import {RequestsApprobationComponent} from './widget/requestsApprobation/requests-approbation.component';
import {SigninComponent} from './widget/signin/signin.component';
import {AuthenticationService} from './service/authentication.service';
import {CustomHttp} from './service/custom-http.service';
import {UserService} from './service/user.service';
import {SharedService} from './service/shared.service';
import {RolesService} from './service/roles.service';
import {LeaveRequestService} from './service/leave-request.service';
import {ScheduleComponent} from './widget/schedule/schedule.component';
import {SetupTableComponent} from './widget/setup-table/setup-table.component';
import {HolidayService} from './service/holiday-service';
import { DashboardComponent } from './widget/dashboard/dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LeaveRequestComponent,
    HomeComponent,
    ListRequestsComponent,
    RequestsApprobationComponent,
    SigninComponent,
    ScheduleComponent,
    SetupTableComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    NoopAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CalendarModule,
    ScheduleModule,
    SelectButtonModule,
    DataTableModule,
    DialogModule,
    DropdownModule,
    RouterModule.forRoot(ROUTES)
  ],
  providers: [
    {provide: Http, useClass: CustomHttp},
    {provide: ConnectionBackend, useClass: XHRBackend},
    SharedService,
    LeaveRequestService,
    UserService,
    RolesService,
    HolidayService,
    AuthGuard,
    AuthenticationService
  ],
  bootstrap: [AppComponent]
})


export class AppModule { }
