import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Profile from '../models/profile';
import {catchError, Observable, of, tap} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly profileUrl = '/api/profile';
  private profile: Profile | null | undefined;

  constructor(private http: HttpClient) {
  }

  getProfileInfo(): Observable<Profile | null> {
    if (this.profile !== undefined) {
      return of(this.profile);
    }

    return this.http.get<Profile>(this.profileUrl).pipe(catchError(err => of(null)), tap(profile => this.profile = profile));
  }
}
