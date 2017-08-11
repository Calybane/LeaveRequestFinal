import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../service/user.service';
import {LazyLoadEvent} from 'primeng/primeng';
import * as moment from 'moment';
import {HolidayService} from '../../service/holiday-service';
import {Holiday} from '../../model/holiday';

@Component({
  selector: 'app-ptl-setup-table',
  templateUrl: './setup-table.component.html',
  styleUrls: ['./setup-table.component.css']
})
export class SetupTableComponent implements OnInit {

  users: User[];
  totalRecordsLeave: number;
  rowsLeave: number;

  publicHoliday: Date;
  description: string;
  minDate: Date;
  disabledDates: Date[] = Array<Date>();
  holidayDates: Holiday[] = Array<Holiday>();
  totalRecordsDate: number;
  rowsDate: number;

  alreadyExist: boolean;

  constructor(private holidayService: HolidayService, private userService: UserService) { }

  ngOnInit() {
    this.minDate = moment.utc('2017-08-01').toDate();
    this.rowsLeave = 20;
    this.rowsDate = 20;
    this.publicHoliday = moment.utc().toDate();
    this.description = '';
    this.alreadyExist = false;
    this.getAllDisabledDates();
  }

  // Get all the user
  loadLeaveLazy(event: LazyLoadEvent) {
    const page = (event.first / event.rows);
    const paging = '?page=' + page + '&size=' + event.rows + '&sort=login';
    this.userService.getAllUsers(paging).subscribe(users => {
      this.users = users.content.map(res => {
        return {
          login: res.login,
          daysLeft: res.daysLeft
        };
      });
      this.totalRecordsLeave = users.totalElements;
      this.rowsLeave = users.size;
    });
  }

  getAllDisabledDates() {
    this.holidayService.getAllHolidayDates().subscribe(holidays => {
      this.disabledDates = holidays.map(holiday => moment.utc(holiday.date).toDate());
      this.minDate = moment.utc('2017-08-01').toDate();
    });
  }

  // Get all disabled dates
  loadHolidaysLazy(event: LazyLoadEvent) {
    const page = (event.first / event.rows);
    const paging = '?page=' + page + '&size=' + event.rows + '&sort=date';
    this.holidayService.getAllHolidayDatesPaging(paging).subscribe(dates => {
      this.holidayDates = dates.content;
      this.totalRecordsDate = dates.totalElements;
      this.rowsDate = dates.size;
    });
  }

  // Update the user with his new number of days left
  addDays(rowIndex: number): void {
    this.userService.updateUser(this.users[rowIndex]).subscribe(reponse => {
      this.users[rowIndex] = reponse;
      this.users = this.users.map(e => e);
    });
  }

  // Add an holiday
  addHoliday() {
    if (!this.holidayDates.find(d => moment(d.date).format('YYYY-MM-DD') === moment(this.publicHoliday).format('YYYY-MM-DD'))) {
      const holiday = {id: null, date: moment.utc(this.publicHoliday).toDate(), description: this.description};
      this.holidayService.createHoliday(holiday).subscribe(response => {
        if (response) {
          this.disabledDates.push(moment.utc(response.date).toDate());
          this.holidayDates.push(response);
          this.holidayDates = this.holidayDates.slice();
          this.minDate = moment.utc('2017-08-01').toDate();
          this.alreadyExist = false;
        }
      }, error => {
        console.log('The date already exist');
        this.alreadyExist = true;
      });
    } else {
      console.log('The date already exist');
      this.alreadyExist = true;
    }
  }

  // Delete an holiday
  deleteHoliday(holiday: Holiday) {
    this.holidayService.deleteHoliday(holiday.id).subscribe(response => {
      if (response) {
        this.disabledDates = this.disabledDates.filter(d => moment(d).format('YYYY-MM-DD') !== moment(holiday.date).format('YYYY-MM-DD')).map(d => d);
        this.holidayDates = this.holidayDates.filter(d => d.id !== holiday.id).map(d => d);
        this.minDate = moment.utc('2017-08-01').toDate();
      }
    });
  }

}
