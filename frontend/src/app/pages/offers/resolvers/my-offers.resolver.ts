import { Injectable } from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable } from 'rxjs';
import {PaginatedOffers} from '../../../models/offer';
import {OffersService} from '../../../services/offers.service';

@Injectable({
  providedIn: 'root'
})
export class MyOffersResolver implements Resolve<PaginatedOffers> {
  constructor(private offers: OffersService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PaginatedOffers> {
    return this.offers.getOwnOffers(0, 6);
  }
}
