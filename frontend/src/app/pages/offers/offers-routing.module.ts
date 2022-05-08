import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OfferDetailsComponent} from './components/offer-details/offer-details.component';
import {OfferResolver} from './resolvers/offer.resolver';

const routes: Routes = [
  {
    path: 'offers/:offerId',
    component: OfferDetailsComponent,
    resolve: {
      offer: OfferResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OffersRoutingModule {
}
