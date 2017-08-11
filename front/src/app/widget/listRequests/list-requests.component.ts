import {Component, OnInit} from '@angular/core';
import {LeaveRequest} from '../../model/leave-request';
import {LeaveRequestService} from '../../service/leave-request.service';
import {LazyLoadEvent} from 'primeng/primeng';
import {SharedService} from '../../service/shared.service';
import * as jsPDF from 'jspdf';
import * as moment from 'moment';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-ptl-list-requests',
  templateUrl: './list-requests.component.html',
  styleUrls: ['./list-requests.component.css'],
  providers: [LeaveRequestService]
})
export class ListRequestsComponent implements OnInit {

  listRequests: LeaveRequest[];
  totalRecords: number;
  rows: number;

  constructor(private router: Router,
              private leaveRequestService: LeaveRequestService,
              private userService: UserService,
              private sharedService: SharedService) { }

  ngOnInit() {
    this.rows = 10;
  }

  // Get the user connected and his leave requests list, or returned the signin page
  loadLeaveLazy(event: LazyLoadEvent) {
    if (!this.sharedService.user.login) {
      this.userService.getUserConnected().subscribe(user => {
        if (user) {
          this.sharedService.user = user;
          this.sharedService.getRoles();
          this.setListLeaveRequests(event);
        } else {
          console.log('User not connected');
          this.router.navigate(['/signin']);
          return;
        }
      }, error => {
        console.log('Error : user not connected');
        this.router.navigate(['/signin']);
        return;
      });
    } else {
      this.setListLeaveRequests(event);
    }
  }

  // Get the list of leave request for the user connected
  setListLeaveRequests(event: LazyLoadEvent) {
    const page = (event.first / event.rows);
    const paging = '?page=' + page + '&size=' + event.rows + '&sort=leaveFrom';
    this.leaveRequestService.getAllLeaveRequestsByLogin(this.sharedService.user.login, paging).subscribe(list => {
      this.listRequests = list.content.map(request => {
        return {
          id: request.id,
          login: request.login,
          typeAbsence: request.typeAbsence,
          leaveFrom: request.leaveFrom,
          leaveTo: request.leaveTo,
          daysTaken: request.daysTaken,
          requestDate: request.requestDate,
          approvalDate: request.approvalDate,
          approvalHRDate: request.approvalHRDate,
          status: request.status,
          description: request.description
        };
      });
      this.totalRecords = list.totalElements;
      this.rows = list.size;
    });
  }

  // Delete the request passed in parameter
  deleteLeaveRequest(request: LeaveRequest) {
    if (confirm('Are you sure you want to deleteLeaveRequest this request ?')) {
      this.leaveRequestService.deleteLeaveRequestById(request.id).subscribe(response => {
        if (response) {
          this.listRequests = this.listRequests.filter(e => e.id !== request.id).map(e => e);
          this.userService.updateUser({login: request.login, daysLeft: (this.sharedService.user.daysLeft + request.daysTaken)}).subscribe(user => {
            this.sharedService.user.daysLeft = user.daysLeft;
          });
        }
      });
    }
  }

  // Return a style class about the request's status
  getStyleClass(row: LeaveRequest): string {
    if (row.status === 'Rejected') {
      return 'bg-danger text-white';
    } else if (row.status === 'Approved') {
      return 'bg-success text-white';
    }
  }

  // Return a label to show if the request is rejected or not by the manager
  getApprovalLabel(request: LeaveRequest): string {
    if (request.status === 'Rejected') {
      return 'Date of reject : ';
    } else {
      return 'Date of approval : ';
    }
  }

  // Create a pdf from the leave request passed in parameter
  createPDF(request: LeaveRequest) {
    console.log('save the request into file');

    const doc = new jsPDF();
    doc.setFontType('bold');
    doc.setFontSize(20);
    doc.text(35, 30, 'LEAVE OF ABSENCE - REQUEST FORM');
    doc.setFontType('normal');
    doc.setFontSize(14);
    doc.text(20, 50,  'Employee name : ' + this.sharedService.user.login);
    doc.text(20, 60,  'Type of absence : ' + request.typeAbsence);
    doc.text(20, 70,  'Absence dates :');
    doc.text(20, 80,  '      From ' + moment(request.leaveFrom).format('DD-MM-YYYY')
      + '          To (incl.) ' + moment(request.leaveTo).format('DD-MM-YYYY'));
    doc.text(20, 90,  'Number of days taken : ' + request.daysTaken);
    doc.text(20, 100, 'Number of annual leave days left : ' + this.sharedService.user.daysLeft);
    doc.text(20, 110, 'Request date : ' + moment(request.requestDate).format('DD-MM-YYYY'));
    doc.text(20, 120, 'Date of approval : ' + moment(request.approvalDate).format('DD-MM-YYYY'));

    // Save the PDF
    doc.save('Leave-request-' + moment(request.requestDate).format('DD-MM-YYYY') + '.pdf');
  }

}
