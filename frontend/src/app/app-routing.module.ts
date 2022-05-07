import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {OffersResolver} from './pages/home/resolvers/offers.resolver';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    resolve: {
      offers: OffersResolver
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
