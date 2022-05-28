import { Component, OnInit } from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';

@Component({
  selector: 'app-offer-details',
  templateUrl: './offer-details.component.html',
  styleUrls: ['./offer-details.component.scss']
})
export class OfferDetailsComponent implements OnInit {

  offer: Offer | null = null;

  constructor(private route: ActivatedRoute, private offers: OffersService) { }

  ngOnInit(): void {
    this.offer = this.route.snapshot.data["offer"];
  }

  closeOffer() {
    this.offers.closeOffer(this.offer!).subscribe(offer => {
      this.offer = offer;
    });
  }
}
