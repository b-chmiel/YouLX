import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './home.component';
import {ToolbarComponent} from './components/toolbar/toolbar.component';
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [
    HomeComponent,
    ToolbarComponent
  ],
  imports: [
    CommonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatIconModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule {
}
