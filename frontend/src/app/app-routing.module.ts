import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {OffersResolver} from './pages/home/resolvers/offers.resolver';
import {ProfileComponent} from './pages/profile/profile.component';
import {ProfileResolver} from './pages/profile/resolvers/profile.resolver';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    resolve: {
      offers: OffersResolver
    }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    resolve: {
      profile: ProfileResolver
    }
  },
  {
    path: '**',
    redirectTo: '/home',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
