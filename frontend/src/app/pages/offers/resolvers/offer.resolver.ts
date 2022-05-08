import { Injectable } from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {Offer} from '../../home/models/offer';
import {OffersService} from '../../../services/offers.service';

@Injectable({
  providedIn: 'root'
})
export class OfferResolver implements Resolve<Offer | null> {
  constructor(private offers: OffersService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Offer | null> {
    const offerId = route.paramMap.get("offerId");

    if (offerId == null) {
      return of(null);
    }

    return this.offers.getOffer(offerId);
  }
}
