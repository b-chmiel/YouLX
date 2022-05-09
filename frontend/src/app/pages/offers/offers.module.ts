import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OffersRoutingModule } from './offers-routing.module';
import { OfferDetailsComponent } from './components/offer-details/offer-details.component';
import { OfferNewComponent } from './components/offer-new/offer-new.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    OfferDetailsComponent,
    OfferNewComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    OffersRoutingModule
  ]
})
export class OffersModule { }
