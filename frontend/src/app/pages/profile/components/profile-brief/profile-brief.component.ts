import {Component, Input} from '@angular/core';
import Profile from '../../../../models/profile';

@Component({
  selector: 'app-profile-brief',
  templateUrl: './profile-brief.component.html',
  styleUrls: ['./profile-brief.component.scss'],
})
export class ProfileBriefComponent {
  @Input() profile!: Profile;
}
