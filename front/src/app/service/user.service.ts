import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Http, Response} from '@angular/http';
import {User} from '../model/user';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

const API_URL = environment.apiUrl;

@Injectable()
export class UserService {

  constructor(private http: Http) {
  }


  public getUserConnected(): Observable<User> {
    return this.http.get(API_URL + '/api/user/connected')
      .map(response => {
        if (response) {
          return response.json();
        }
        return null;
      })
      .catch(this.handleError);
  }

  public getUserByLogin(login: string): Observable<User> {
    return this.http.get(API_URL + '/api/user/username/' + login)
      .map(response => {
        if (response) {
          const user = response.json();
          return {
            login: user.login,
            daysLeft: user.daysLeft
          };
        } else {
          return null;
        }
      })
      .catch(this.handleError);
  }

  public getAllUsers(paging: string): Observable<any> {
    return this.http.get(API_URL + '/api/user' + paging)
      .map(users => users.json())
      .catch(this.handleError);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put(API_URL + '/api/user/update', user)
      .map(response => {
        const res = response.json();
        return {
          login: res.login,
          daysLeft: res.daysLeft
        };
      })
      .catch(this.handleError);
  }

  private handleError (error: Response | any) {
    console.error('ApiUserService::handleError', error);
    return Observable.throw(error);
  }
}
