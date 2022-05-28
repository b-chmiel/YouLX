import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './home.component';
import {ToolbarComponent} from './components/toolbar/toolbar.component';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatIconModule} from '@angular/material/icon';
import {OfferComponent} from './components/offer/offer.component';
import {OffersComponent} from './components/offers/offers.component';
import {RouterModule} from '@angular/router';

@NgModule({
  declarations: [
    HomeComponent,
    ToolbarComponent,
    OfferComponent,
    OffersComponent,
  ],
  imports: [
    CommonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatIconModule,
    RouterModule,
  ],
  exports: [
    HomeComponent,
  ],
})
export class HomeModule {
}
