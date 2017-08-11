import { Component, OnInit } from '@angular/core';
import {LeaveRequestService} from '../../service/leave-request.service';
import {SharedService} from '../../service/shared.service';
import {Router} from '@angular/router';
import {SelectItem} from 'primeng/primeng';
import {LeaveRequestView} from '../../view/leave-request-view';
import {LeaveRequest} from '../../model/leave-request';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  list: LeaveRequestView[];

  constructor(private leaveRequestService: LeaveRequestService,
              private sharedService: SharedService,
              private router: Router) {}

  ngOnInit() {
    if (this.sharedService.isSimpleUser()) {
      this.router.navigate(['/home']);
    }
    this.loadLeaveRequests();
  }

  // Get all the leave request about a status
  loadLeaveRequests() {
    this.leaveRequestService.getAllLeaveRequestsView().subscribe(list => {
      this.list = list.map(request => {
        return {
          id: request.id,
          login: request.login,
          daysLeft: request.daysLeft,
          leaveFrom: request.leaveFrom,
          leaveTo: request.leaveTo,
          daysTaken: request.daysTaken,
          requestDate: request.requestDate,
          approvalDate: request.approvalDate,
          status: request.status
        };
      });
    });
  }

  // Return a style class about the request's status
  getStyleClass(row: LeaveRequest): string {
    if (row.status === 'Rejected') {
      return 'bg-danger text-white';
    } else if (row.status === 'Approved') {
      return 'bg-success text-white';
    }
  }

}
