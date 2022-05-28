import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProfileComponent} from './profile.component';
import {RouterModule} from '@angular/router';
import { ProfileBriefComponent } from './components/profile-brief/profile-brief.component';
import { ProfileEditComponent } from './components/profile-edit/profile-edit.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    ProfileComponent,
    ProfileBriefComponent,
    ProfileEditComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  exports: [
    ProfileComponent
  ]
})
export class ProfileModule {
}
