import {Component, OnInit} from '@angular/core';
import {LeaveRequest} from '../../model/leave-request';
import {LeaveRequestService} from '../../service/leave-request.service';
import {LazyLoadEvent, SelectItem} from 'primeng/primeng';
import {SharedService} from '../../service/shared.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-ptl-requests-approbation',
  templateUrl: './requests-approbation.component.html',
  styleUrls: ['./requests-approbation.component.css']
})
export class RequestsApprobationComponent implements OnInit {

  listRequests: LeaveRequest[];
  totalRecords: number;
  rows: number;
  first: number;
  filterStatus: string;
  status: SelectItem[];


  constructor(private leaveRequestService: LeaveRequestService,
              private sharedService: SharedService,
              private router: Router) {
    this.status = [];
    this.status.push({label: 'In waiting', value: 'waiting'});
    this.status.push({label: 'Approved', value: 'approved'});
    this.status.push({label: 'Rejected', value: 'rejected'});

  }

  ngOnInit() {
    if (this.sharedService.isSimpleUser()) {
      this.router.navigate(['/home']);
    }
    this.filterStatus = 'waiting';
    this.first = 0;
    this.rows = 10;
  }

  // Approve the leave request
  approveRequest(rowIndex: number): void {
    const id = this.listRequests[rowIndex].id;
    this.leaveRequestService.approvedLeaveRequest(this.listRequests[rowIndex]).subscribe(reponse => {
      this.listRequests[rowIndex] = reponse;
      this.listRequests = this.listRequests.filter(r => r.id !== id).map(e => e);
    });
  }

  // Reject a leave request
  rejectRequest(rowIndex: number): void {
    const id = this.listRequests[rowIndex].id;
    this.leaveRequestService.rejectedLeaveRequest(this.listRequests[rowIndex]).subscribe(reponse => {
      this.listRequests[rowIndex] = reponse;
      this.listRequests = this.listRequests.filter(r => r.id !== id).map(e => e);
    });
  }

  onChangeStatus() {
    this.loadLeaveLazy(null);
  }

  // Get all the leave request about a status
  loadLeaveLazy(event: LazyLoadEvent) {
    if (event !== null) {
      this.first = event.first;
      this.rows = event.rows;
    }
    const page = (this.first / this.rows);
    const paging = '?page=' + page + '&size=' + this.rows + '&sort=leaveFrom';
    this.leaveRequestService.getAllLeaveRequestsByStatus(this.filterStatus, paging).subscribe(list => {
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
          status: request.status,
          description: request.description
        };
      });
      this.totalRecords = list.totalElements;
      this.rows = list.size;
    });
  }

  // Return true if the user has the role passed
  hasRole(role: string): boolean {
    return this.sharedService.hasRole(role);
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
