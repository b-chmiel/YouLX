import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LayoutModule} from './layout/layout.module';
import {HomeModule} from './pages/home/home.module';
import {HttpClientModule} from '@angular/common/http';
import {OffersModule} from './pages/offers/offers.module';
import {ProfileModule} from './pages/profile/profile.module';
import {MessagesModule} from './pages/messages/messages.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    HomeModule,
    HttpClientModule,
    OffersModule,
    ProfileModule,
    MessagesModule,
    AppRoutingModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
