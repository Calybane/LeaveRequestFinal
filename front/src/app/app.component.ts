import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {SharedService} from './service/shared.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-ptl-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: []
})

export class AppComponent implements OnInit {

  title: string = 'PTL';

  constructor(private sharedService: SharedService, private router: Router) {}

  ngOnInit() {
    if (localStorage.getItem('token')) {
      this.sharedService.getUserConnected();
    } else {
      this.router.navigate(['/signin']);
      return;
    }
  }

}


