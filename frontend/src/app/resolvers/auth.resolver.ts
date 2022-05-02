import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {AuthService} from "../services/auth.service";
import Profile from "../models/profile";

@Injectable({
  providedIn: 'root'
})
export class AuthResolver implements Resolve<Profile> {
  constructor(private auth: AuthService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Profile> {
    return this.auth.getProfileInfo();
  }
}
