import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatChipsModule} from '@angular/material/chips';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {OffersRoutingModule} from './offers-routing.module';
import {OfferDetailsComponent} from './components/offer-details/offer-details.component';
import {OfferNewComponent} from './components/offer-new/offer-new.component';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowseMyOffersComponent} from './components/browse-my-offers/browse-my-offers.component';
import {OfferComponent} from './components/offer/offer.component';

@NgModule({
  declarations: [
    OfferDetailsComponent,
    OfferNewComponent,
    BrowseMyOffersComponent,
    OfferComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatChipsModule,
    OffersRoutingModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatIconModule,
  ],
})
export class OffersModule {
}
