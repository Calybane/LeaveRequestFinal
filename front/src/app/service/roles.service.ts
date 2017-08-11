import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {environment} from 'environments/environment';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

const API_URL = environment.apiUrl;

@Injectable()
export class RolesService {

  constructor(private http: Http) { }


  // Get all the roles of the user connected
  public getRoles(): Observable<any> {
    return this.http.get(API_URL + '/api/roles')
      .map(response => response.json())
      .catch(this.handleError);
  }

  private handleError (error: Response | any) {
    console.error('ApiRolesService::handleError', error);
    return Observable.throw(error);
  }

}
