import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../environments/environment';
import {Router} from '@angular/router';

import 'rxjs/add/operator/map'

const API_URL = environment.apiUrl;

@Injectable()
export class AuthenticationService {

  constructor(private http: Http, private router: Router) { }

  // if the credentials are right, log the user and return true, or return false
  login(username: string, password: string): Observable<boolean> {
    return this.http.post(API_URL + '/login', JSON.stringify({ username: username, password: password }))
      .map((response: Response) => {
        // login successful if there's a jwt token in the response
        const token: string = response.headers.get('authorization');
        if (token) {
          // store the jwt token.
          localStorage.setItem('token', token);
          return true;
        } else {
          return false;
        }
      }, error => {
        return false;
      });
  }

  // logout the user and return the signin page
  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('token');
    this.router.navigate(['/signin']);
  }

}
