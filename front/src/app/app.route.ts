import {Routes} from '@angular/router';
import {LeaveRequestComponent} from './widget/leaveRequest/leave-request.component';
import {ListRequestsComponent} from './widget/listRequests/list-requests.component';
import {RequestsApprobationComponent} from './widget/requestsApprobation/requests-approbation.component';
import {HomeComponent} from './widget/home/home.component';
import {SigninComponent} from './widget/signin/signin.component';
import {ScheduleComponent} from './widget/schedule/schedule.component';
import {AuthGuard} from './app.authGuard';
import {SetupTableComponent} from './widget/setup-table/setup-table.component';
import {DashboardComponent} from './widget/dashboard/dashboard.component';

export const ROUTES: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
    canActivate: [AuthGuard]
  },
  {
    path: 'request',
    component: LeaveRequestComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'list',
    component: ListRequestsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'approval',
    component: RequestsApprobationComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'schedule',
    component: ScheduleComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'setuptable',
    component: SetupTableComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'signin',
    component: SigninComponent
  }
];
