import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {Offer} from '../../../models/offer';
import {OffersService} from '../../../services/offers.service';

@Injectable({
  providedIn: 'root'
})
export class OffersResolver implements Resolve<Offer[]> {
  constructor(private offers: OffersService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Offer[]> {
    return this.offers.getOffers(0, 8);
  }
}
