import {Component, OnInit} from '@angular/core';
import {LeaveRequest} from '../../model/leave-request';
import {SelectItem} from 'primeng/primeng';
import {LeaveRequestService} from '../../service/leave-request.service';
import {UserService} from '../../service/user.service';
import {SharedService} from '../../service/shared.service';
import {Router} from '@angular/router';
import * as moment from 'moment';

@Component({
  selector: 'app-ptl-leave-request',
  templateUrl: './leave-request.component.html',
  styleUrls: ['./leave-request.component.css'],
  providers: [LeaveRequestService, UserService]
})

export class LeaveRequestComponent implements OnInit {

  minDate: Date;
  maxDate: Date;
  validForm: boolean = false;
  showWellSpecial: boolean;
  types: SelectItem[];

  disabledDates: Date[] = Array<Date>();

  leaveRequest: LeaveRequest;
  requestSubmitted: RequestSubmit;

  constructor(private leaveRequestService: LeaveRequestService,
              private userService: UserService,
              private sharedService: SharedService,
              private router: Router) {}

  ngOnInit() {
    // Initialize the leave request instance
    this.initializeLeaveRequest();

    // Set the absence types
    this.types = [];
    this.leaveRequestService.getAllTypesAbsence().subscribe(response => {
      response.forEach(type => {
        this.types.push({label: type, value: type});
      });
      this.leaveRequest.typeAbsence = this.types[0].value;
      this.onChangeTypes();
    });

    // Initialize the user connected, his roles and his disabled dates
    this.initialize();
  }

  // Initialize the user, his roles and his disabled dates
  initialize() {
    if (!this.sharedService.user.login) {
      // Get the user connected
      this.userService.getUserConnected().subscribe(user => {
        if (user) {
          this.sharedService.user = user;

          // Get his roles
          this.sharedService.getRoles();
          this.leaveRequest.login = this.sharedService.user.login;

          // Set the disabled dates
          this.setDates();
        } else {
          // Return on the signin page
          console.log('User not connected');
          this.router.navigate(['/signin']);
          return;
        }
      }, error => {
        // Return on the signin page
        console.log('Error : user not connected');
        this.router.navigate(['/signin']);
        return;
      });
    } else {
      // Set the disabled dates
      this.setDates();
    }
  }

  // Reset the leave request
  initializeLeaveRequest() {
    this.leaveRequest = {
      id: null,
      login: this.sharedService.user.login,
      typeAbsence: '',
      leaveFrom: moment.utc().toDate(),
      leaveTo: moment.utc().toDate(),
      daysTaken: 1,
      requestDate: moment.utc().toDate(),
      approvalDate: null,
      status: '',
      description: ''
    };

    // Set the dates of the leave request to the next open day
    if (this.leaveRequest.leaveFrom.getDay() === 5) {
      this.leaveRequest.leaveFrom = moment(this.leaveRequest.leaveFrom).add(3, 'day').toDate();
      this.leaveRequest.leaveTo = moment(this.leaveRequest.leaveTo).add(3, 'day').toDate();
    } else if (this.leaveRequest.leaveFrom.getDay() === 6) {
      this.leaveRequest.leaveFrom = moment(this.leaveRequest.leaveFrom).add(2, 'day').toDate();
      this.leaveRequest.leaveTo = moment(this.leaveRequest.leaveTo).add(2, 'day').toDate();
    } else {
      this.leaveRequest.leaveFrom = moment(this.leaveRequest.leaveFrom).add(1, 'day').toDate();
      this.leaveRequest.leaveTo = moment(this.leaveRequest.leaveTo).add(1, 'day').toDate();
    }
  }

  // Called when the form is submitted
  // If the form is right, the leave request is created
  // else a error message is displayed
  onSubmit() {
    if (this.leaveRequestValid()) {
      this.createLeaveRequest();
    } else {
      this.requestSubmitted = {
        message: 'The request is not valid',
        style: 'alert alert-danger'
      };
    }
  }

  getUserDaysLeft(): number {
    return this.sharedService.user.daysLeft;
  }

  // Return true if the leave request is right, false otherwise
  leaveRequestValid(): boolean {
    return moment.utc(this.leaveRequest.leaveFrom) <= moment.utc(this.leaveRequest.leaveTo)
      && this.leaveRequest.daysTaken > 0
      && this.leaveRequest.daysTaken <= this.getUserDaysLeft()
      && !this.intersectDates(moment.utc(this.leaveRequest.leaveFrom).toDate(),
                              moment.utc(this.leaveRequest.leaveTo).toDate(),
                              this.disabledDates);
  }

  // Create the leave request
  createLeaveRequest() {
    // Add the leave request to the db
    this.leaveRequestService.createLeaveRequest(this.leaveRequest).subscribe(request => {
      if (request) {
        this.userService.getUserConnected().subscribe(response => {
          this.sharedService.user = response;
          this.setValidForm();
        });

        // Add the dates from the leave request created to the disabled dates
        this.addDisabledDates(this.leaveRequest.leaveFrom, this.leaveRequest.leaveTo);

        // Reset the leave request instance
        this.initializeLeaveRequest();
        this.leaveRequest.typeAbsence = this.types[0].value;

        // Set the message on the screen
        this.requestSubmitted = {
          message: 'Request submitted',
          style: 'alert alert-success'
        };
      } else {
        this.requestSubmitted = {
          message: 'The request is not valid',
          style: 'alert alert-danger'
        };
      }
    }, error => {
      this.requestSubmitted = {
        message: 'The request can not be submitted',
        style: 'alert alert-danger'
      };
    });
  }

  // Set the variable to enable or disable the submit button
  setValidForm() {
    this.validForm = moment.utc(this.leaveRequest.leaveFrom) <= moment.utc(this.leaveRequest.leaveTo)
      && this.getUserDaysLeft() > 0
      && this.leaveRequest.daysTaken <= this.getUserDaysLeft()
      && !this.intersectDates(moment.utc(this.leaveRequest.leaveFrom).toDate(),
                              moment.utc(this.leaveRequest.leaveTo).toDate(),
                              this.disabledDates);
  }

  // Set the variable to show or hide the message about the Special leave
  onChangeTypes() {
    this.showWellSpecial = (this.leaveRequest.typeAbsence === 'Special leave');
  }

  // Change the number of days taken
  changeDaysTaken() {
    let nb = 0;
    let currentDate = moment(this.leaveRequest.leaveFrom.setHours(0, 0, 0, 0)).toDate();
    const endDate = moment(this.leaveRequest.leaveTo.setHours(0, 0, 0, 0)).toDate();
    while (currentDate <= endDate) {
      // depend of first day of week. here, first day is Sunday == 0 and Saturday == 6
      if (currentDate.getDay() > 0 && currentDate.getDay() < 6) {
        ++nb;
      }
      currentDate = moment.utc(currentDate).add(1, 'day').toDate();
    }

    this.leaveRequest.daysTaken = nb;
    this.setValidForm();
  }

  // Change the maximum date possible from the 'leavefrom' date, and ignoring weekends
  changeMaxDate() {
    this.maxDate = moment.utc(this.leaveRequest.leaveFrom.setHours(0, 0, 0, 0)).toDate();
    for (let i = 1; i < this.getUserDaysLeft(); ++i) {
      this.maxDate = moment.utc(this.maxDate).add(1, 'day').toDate();
      // depend of first day of week. here, first day is Sunday == 0 and Saturday == 6
      if (this.maxDate.getDay() === 0 || this.maxDate.getDay() === 6) {
        --i;
      }
    }
  }

  // Set the dates which are disabled (already taken)
  setDates() {
    this.leaveRequestService.getAllDisabledDatesByLogin(this.sharedService.user.login).subscribe(requests => {
      if (requests.length > 0) {
        requests.forEach(date => {
          this.disabledDates.push(moment.utc(date).toDate());
        });
      }

      // Set the minimum date possible
      this.minDate = moment.utc(this.leaveRequest.leaveFrom).toDate();

      // Set the maximum date possible
      this.changeMaxDate();

      this.setValidForm();
    });
  }

  // Add the dates form the created leave request in the disabled dates array
  addDisabledDates(startDate: Date, endDate: Date) {
    while (startDate <= endDate) {
      this.disabledDates.push(moment.utc(startDate).toDate());
      startDate = moment.utc(startDate).add(1, 'day').toDate();
    }
    this.disabledDates.map(e => e);
  }

  // Return true if the startdate or enddate are in the array of dates
  intersectDates(startDate: Date, endDate: Date, dates: Array<Date>): boolean {
    return dates.find(date => date.getDate() === startDate.getDate() && date.getDate() === endDate.getDate()) != null
  }

}

export interface RequestSubmit {
  message: string;
  style: string;
}
