import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToolbarComponent } from './components/toolbar/toolbar.component';
import {RouterModule} from '@angular/router';



@NgModule({
  declarations: [
    ToolbarComponent
  ],
    imports: [
        CommonModule,
        RouterModule,
    ],
  exports: [
    ToolbarComponent
  ]
})
export class LayoutModule { }
