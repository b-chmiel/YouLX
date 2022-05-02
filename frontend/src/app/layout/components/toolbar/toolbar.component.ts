import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import Profile from "../../../models/profile";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit{
  profile$!: Observable<Profile>;

  constructor(private auth: AuthService) {
  }

  ngOnInit() {
    this.profile$ = this.auth.getProfileInfo();
  }

  async login() {
    await this.auth.login();
  }

  async logout() {
    await this.auth.logout();
  }
}
