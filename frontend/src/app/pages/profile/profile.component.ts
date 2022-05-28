import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Offer} from '../../models/offer';
import Profile from '../../models/profile';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  profile!: Profile;

  constructor(private route: ActivatedRoute, private auth: AuthService) { }

  ngOnInit(): void {
    this.profile = this.route.snapshot.data["profile"];
  }

  onSaveProfile(profile: Profile) {
    this.auth.saveProfileInfo(profile).subscribe(profile => {
      this.profile = profile;
    })
  }
}
