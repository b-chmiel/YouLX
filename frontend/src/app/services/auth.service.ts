import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import Profile from "../models/profile";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly loginUrl = "login";
  private readonly logoutUrl = "logout";
  private readonly profileUrl = "api/me";

  constructor(private http: HttpClient, private router: Router) {
  }

  login() {
    return this.router.navigateByUrl(this.loginUrl);
  }

  logout() {
    console.log("logout");
    return this.router.navigateByUrl(this.logoutUrl);
  }

  getProfileInfo() {
    return this.http.get<Profile>(this.profileUrl).pipe(tap(x => console.log(x.login)));
  }
}
