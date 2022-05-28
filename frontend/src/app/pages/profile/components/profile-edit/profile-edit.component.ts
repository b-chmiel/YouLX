import {Component, Input, OnInit} from '@angular/core';
import Profile from '../../../../models/profile';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.scss'],
})
export class ProfileEditComponent implements OnInit {
  @Input() profile!: Profile;
  @Output() saveProfile = new EventEmitter<Profile>();
  form!: FormGroup;

  ngOnInit() {
    this.form = new FormGroup({
      firstName: new FormControl(this.profile.firstName, Validators.required),
      lastName: new FormControl(this.profile.lastName, Validators.required),
      email: new FormControl(this.profile.email, Validators.required)
    })
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }

    this.saveProfile.emit(this.form.value as Profile);
  }

  getEmailPlaceholder() {
    const firstName = this.form.controls["firstName"].value.length > 0 ? this.form.controls["firstName"].value : this.profile.firstName;
    const lastName = this.form.controls["lastName"].value.length > 0 ? this.form.controls["lastName"].value : this.profile.lastName;
    return `${firstName}.${lastName}@email.com`;
  }
}
