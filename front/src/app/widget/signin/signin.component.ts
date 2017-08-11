import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../service/authentication.service';
import {SharedService} from '../../service/shared.service';

@Component({
  selector: 'app-ptl-login',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  username: string;
  password: string;
  returnUrl: string;
  errorAuth: boolean;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService,
              private sharedService: SharedService) {}

  ngOnInit() {
    localStorage.removeItem('token');
    this.sharedService.initializeUser();
    this.sharedService.pageSignIn = true;
    this.errorAuth = false;

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
  }

  // If the user exist, log him and return the page wanted, otherwise display an error message
  onSubmit() {
    this.authenticationService.login(this.username, this.password).subscribe(logged => {
      if (logged) {
        this.sharedService.getUserConnected();
        this.sharedService.pageSignIn = false;
        this.router.navigate([this.returnUrl]);
      } else {
        this.errorAuth = true;
      }
    }, error => {
      this.username = '';
      this.password = '';
      this.errorAuth = true;
    });
  }

}
