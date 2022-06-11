import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OfferDetailsComponent} from './components/offer-details/offer-details.component';
import {OfferResolver} from './resolvers/offer.resolver';
import {OfferNewComponent} from './components/offer-new/offer-new.component';
import {BrowseMyOffersComponent} from './components/browse-my-offers/browse-my-offers.component';
import {MyOffersResolver} from './resolvers/my-offers.resolver';
import {TagsResolver} from '../../resolvers/tags.resolver';

const routes: Routes = [
  {
    path: 'offers/browse/:offerId',
    component: OfferDetailsComponent,
    resolve: {
      offer: OfferResolver,
    },
  },
  {
    path: 'offers/owned',
    component: BrowseMyOffersComponent,
    resolve: {
      offers: MyOffersResolver
    }
  },
  {
    path: 'offers/owned/:offerId/edit',
    component: OfferNewComponent,
    resolve: {
      offer: OfferResolver,
      tags: TagsResolver
    },
  },
  {
    path: 'offers/owned/:offerId',
    component: OfferDetailsComponent,
    data: {
      canEdit: true
    },
    resolve: {
      offer: OfferResolver,
    },
  },
  {
    path: 'offers/create',
    component: OfferNewComponent,
    resolve: {
      tags: TagsResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OffersRoutingModule {
}
