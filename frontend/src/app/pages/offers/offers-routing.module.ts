import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OfferDetailsComponent} from './components/offer-details/offer-details.component';
import {OfferResolver} from './resolvers/offer.resolver';
import {OfferNewComponent} from './components/offer-new/offer-new.component';

const routes: Routes = [
  {
    path: 'offers/browse/:offerId',
    component: OfferDetailsComponent,
    resolve: {
      offer: OfferResolver,
    },
  },
  {
    path: 'offers/create',
    component: OfferNewComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OffersRoutingModule {
}
