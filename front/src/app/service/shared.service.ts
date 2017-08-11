import {Injectable, OnInit} from '@angular/core';
import {RolesService} from './roles.service';
import {User} from '../model/user';
import {UserService} from './user.service';
import {Router} from '@angular/router';

@Injectable()
export class SharedService implements OnInit {

  pageSignIn: boolean = false;
  roles: string = 'USER';
  user: User;

  constructor(private rolesService: RolesService,
              private userService: UserService,
              private router: Router) {
    this.initializeUser();
  }


  ngOnInit() {
  }

  // Reset the user
  initializeUser() {
    this.user = {
      login: '',
      daysLeft: 0
    };
  }

  getUserConnected() {
    this.userService.getUserConnected().subscribe(user => {
      if (user) {
        this.user = user;
        this.getRoles();
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
  }

  // Get all the roles of the user connected
  getRoles() {
    this.rolesService.getRoles().subscribe(roles => {
      if (roles.role.length > 0) {
        this.roles = roles.role;
      }
    });
  }

  // Return true if the user has the roles passed, false otherwise
  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }

  // Return true if the user is a user, false if he is manager, hr, admin, ...
  isSimpleUser(): boolean {
    return this.roles === 'USER' && !this.isHR() && !this.isManager();
  }

  // Return true if the user has a role HR, false otherwise
  isHR(): boolean {
    return this.roles.includes('HR');
  }

  // Return true if the user has a role Manager, false otherwise
  isManager(): boolean {
    return this.roles.includes('MANAGER');
  }
}
