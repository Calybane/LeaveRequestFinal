import {Component, OnInit} from '@angular/core';
import {LeaveRequestService} from '../../service/leave-request.service';
import {ScheduleEvent} from '../../view/schedule-event';
import * as moment from 'moment';
import {HolidayService} from '../../service/holiday-service';

@Component({
  selector: 'app-ptl-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

  events: any[] = Array<any>();
  headerConfig: any;
  event: ScheduleEvent;
  dialogVisible: boolean = false;

  constructor(private leaveRequestService: LeaveRequestService, private holidayService: HolidayService) {}

  ngOnInit() {
    this.headerConfig = {
      left: 'prev,next today',
      center: 'title',
      right: 'month,agendaWeek'
    };

    this.leaveRequestService.getAllScheduleEventLeaveRequest().subscribe(request => {
      this.events = request;
      this.holidayService.getAllScheduleEventHoliday().subscribe(holidays => {
        holidays.forEach(holiday => this.events.push(holiday));
      });
    });
  }

  // Create an object to display it in the dialog box
  handleEventClick(ev) {
    if (window.innerWidth > 767) {
      this.event = {
        id: ev.calEvent.id,
        title: ev.calEvent.title,
        start: moment(ev.calEvent.start).format('YYYY-MM-DD'),
        end: moment(ev.calEvent.end).add(-1, 'day').format('YYYY-MM-DD'),
        status: ev.calEvent.status,
        color: ev.calEvent.color,
        login: ev.calEvent.login,
        description: ev.calEvent.description
      };

      this.dialogVisible = true;
    }
  }

}
