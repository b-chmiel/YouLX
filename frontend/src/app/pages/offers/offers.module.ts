import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {OffersRoutingModule} from './offers-routing.module';
import {OfferDetailsComponent} from './components/offer-details/offer-details.component';
import {OfferNewComponent} from './components/offer-new/offer-new.component';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowseMyOffersComponent} from './components/browse-my-offers/browse-my-offers.component';
import {OfferComponent} from './components/offer/offer.component';
import {NavigationDirective} from './directives/navigation.directive';

@NgModule({
  declarations: [
    OfferDetailsComponent,
    OfferNewComponent,
    BrowseMyOffersComponent,
    OfferComponent,
    NavigationDirective,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    OffersRoutingModule,
  ],
})
export class OffersModule {
}
