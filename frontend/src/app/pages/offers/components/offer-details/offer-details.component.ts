import { Component, OnInit } from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {switchMap} from 'rxjs';

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

  publishOffer() {
    this.offers.publishOffer(this.offer!).pipe(switchMap(_ => this.offers.getOffer(this.offer!.id))).subscribe(offer => {
      this.offer = offer;
    })
  }

  getClassForStatus(status: string) {
    switch (status) {
      case "DRAFT":
        return "badge-info";
      case "OPEN":
        return "badge-success";
      case "CLOSED":
        return "badge-error";
      default:
        return "badge-info";
    }
  }
}
