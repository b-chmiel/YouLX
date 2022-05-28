import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot,
} from '@angular/router';
import {map, Observable} from 'rxjs';
import Profile from '../../../models/profile';
import {AuthService} from '../../../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class ProfileResolver implements Resolve<Profile> {
  constructor(private auth: AuthService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Profile> {
    return this.auth.getProfileInfo().pipe(map(v => v as Profile));
  }
}
