import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';
import {Holiday} from '../model/holiday';
import {ScheduleEvent} from '../view/schedule-event';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

import * as moment from 'moment';

const API_URL = environment.apiUrl;

@Injectable()
export class HolidayService {

  constructor(private http: Http) {
  }


  public getAllHolidayDates(): Observable<any> {
    return this.http.get(API_URL + '/api/holiday/dates')
      .map(response => response.json())
      .catch(this.handleError);
  }

  public getAllHolidayDatesPaging(paging: string): Observable<any> {
    return this.http.get(API_URL + '/api/holiday/after' + paging)
      .map(response => response.json())
      .catch(this.handleError);
  }

  public getAllScheduleEventHoliday(): Observable<ScheduleEvent[]> {
    return this.http.get(API_URL + '/api/holiday/dates')
      .map(holidays => {
        return holidays.json().map(holiday => {
          return {
            title: 'Public holiday',
            start: moment(holiday.date).format('YYYY-MM-DD'),
            end: moment(holiday.date).add(1, 'day').format('YYYY-MM-DD'),
            description: holiday.description === '' ? 'Public holiday' : holiday.description
          };
        });
      })
      .catch(this.handleError);
  }

  public createHoliday(holiday: Holiday): Observable<Holiday> {
    return this.http.post(API_URL + '/api/holiday', holiday)
      .map(response => response.json())
      .catch(this.handleError);
  }

  public deleteHoliday(id: number): Observable<boolean> {
    return this.http.delete(API_URL + '/api/holiday/' + id)
      .map(response => response.json())
      .catch(this.handleError);
  }

  private handleError (error: Response | any) {
    console.error('ApiLeaveRequestService::handleError', error);
    return Observable.throw(error);
  }

}
