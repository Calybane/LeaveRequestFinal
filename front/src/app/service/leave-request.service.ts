import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {LeaveRequest} from '../model/leave-request';
import {ScheduleEvent} from '../view/schedule-event';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

import * as moment from 'moment';
import {LeaveRequestView} from '../view/leave-request-view';

const API_URL = environment.apiUrl;

@Injectable()
export class LeaveRequestService {

  constructor(private http: Http) { }


  public getAllTypesAbsence(): Observable<string[]> {
    return this.http.get(API_URL + '/api/leaverequest/typesabsence')
      .map(requests => requests.json().map(request => request))
      .catch(this.handleError);
  }

  public getAllLeaveRequests(): Observable<LeaveRequest[]> {
    return this.http.get(API_URL + '/api/leaverequest')
      .map(requests => requests.json())
      .catch(this.handleError);
  }

  public getAllLeaveRequestsView(): Observable<any> {
    return this.http.get(API_URL + '/api/leaverequest/views')
      .map(requests => requests.json())
      .catch(this.handleError);
  }

  // Get all the leave request and parse them into a schedule event class (for the schedule)
  public getAllScheduleEventLeaveRequest(): Observable<ScheduleEvent[]> {
    return this.http.get(API_URL + '/api/leaverequest/schedule')
      .map(requests => {
        return requests.json().map(request => {
          return {
            id: request.id,
            title: request.login + (request.description !== '' ? ' : ' + request.description : ''),
            start: moment(request.leaveFrom).format('YYYY-MM-DD'),
            end: moment(request.leaveTo).add(1, 'day').format('YYYY-MM-DD'),
            status: request.status,
            color: (request.status.startsWith('Waiting') ? '#9e9e9e' : '#339933'),
            author: request.login,
            description: request.description
          };
        });
      })
      .catch(this.handleError);
  }

  public getAllLeaveRequestsByStatus(status: string, paging: string): Observable<any> {
    return this.http.get(API_URL + '/api/leaverequest/status/' + status + paging)
      .map(response => response.json())
      .catch(this.handleError);
  }

  public getAllLeaveRequestsByLogin(login: string, paging: string): Observable<any> {
    return this.http.get(API_URL + '/api/leaverequest/person/' + login + paging)
      .map(response => response.json())
      .catch(this.handleError);
  }

  public getAllDisabledDatesByLogin(login: string): Observable <Date[]> {
    return this.http.get(API_URL + '/api/leaverequest/person/' + login + '/disableddates')
      .map(response => response.json())
      .catch(this.handleError);
  }

  public getLeaveRequestById(id: number): Observable<LeaveRequest> {
    return this.http.get(API_URL + '/api/leaverequest/' + id)
      .map(response => {
        const request = response.json();
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
      })
      .catch(this.handleError);
  }

  public createLeaveRequest(leave: LeaveRequest): Observable<boolean> {
    return this.http.post(API_URL + '/api/leaverequest', leave)
      .map(response => response)
      .catch(this.handleError);
  }

  public updateLeaveRequest(leave: LeaveRequest): Observable<LeaveRequest> {
    return this.http.put(API_URL + '/api/leaverequest/' + leave.id, leave)
      .map(response => {
        const request = response.json();
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
      })
      .catch(this.handleError);
  }

  public deleteLeaveRequestById(id: number): Observable<LeaveRequest> {
    return this.http.delete(API_URL + '/api/leaverequest/' + id)
      .map(response => {
        // If response is true, the request is deleted
        return response.json();
      })
      .catch(this.handleError);
  }

  public approvedLeaveRequest(leave: LeaveRequest): Observable<LeaveRequest> {
    return this.http.put(API_URL + '/api/leaverequest/' + leave.id + '/changestatus/approved', leave)
      .map(response => {
        const request = response.json();
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
      })
      .catch(this.handleError);
  }

  public rejectedLeaveRequest(leave: LeaveRequest): Observable<LeaveRequest> {
    return this.http.put(API_URL + '/api/leaverequest/' + leave.id + '/changestatus/rejected', leave)
      .map(response => {
        const request = response.json();
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
      })
      .catch(this.handleError);
  }

  private handleError (error: Response | any) {
    console.error('ApiLeaveRequestService::handleError', error);
    return Observable.throw(error);
  }

}
