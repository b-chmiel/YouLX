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
import {PaginationComponent} from './components/pagination/pagination.component';
import {MatChipsModule} from '@angular/material/chips';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    HomeComponent,
    ToolbarComponent,
    OfferComponent,
    OffersComponent,
    PaginationComponent,
  ],
  imports: [
    CommonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatChipsModule,
    MatFormFieldModule,
    MatIconModule,
    RouterModule,
    ReactiveFormsModule,
  ],
  exports: [
    HomeComponent,
  ],
})
export class HomeModule {
}
