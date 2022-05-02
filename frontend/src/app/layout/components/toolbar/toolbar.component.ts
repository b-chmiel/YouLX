import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {Observable} from 'rxjs';
import Profile from '../../../models/profile';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
})
export class ToolbarComponent implements OnInit {
  profile$!: Observable<Profile | null>;

  constructor(private auth: AuthService) {
  }

  ngOnInit() {
    this.profile$ = this.auth.getProfileInfo();
  }
}
