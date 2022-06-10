import { Component, OnInit } from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {Observable, switchMap} from 'rxjs';
import {AuthService} from '../../../../services/auth.service';
import Profile from '../../../../models/profile';

@Component({
  selector: 'app-offer-details',
  templateUrl: './offer-details.component.html',
  styleUrls: ['./offer-details.component.scss']
})
export class OfferDetailsComponent implements OnInit {

  offer: Offer | null = null;
  canEdit!: boolean;
  loggedInUser$!: Observable<Profile | null>;


  constructor(private route: ActivatedRoute, private offers: OffersService, private auth: AuthService) { }

  ngOnInit(): void {
    this.offer = this.route.snapshot.data["offer"];
    this.canEdit = this.route.snapshot.data["canEdit"] as boolean && this.offer?.status != 'CLOSED';
    this.loggedInUser$ = this.auth.getProfileInfo();
  }

  closeOffer() {
    this.offers.closeOffer(this.offer!).subscribe(offer => {
      this.offer = offer;
      this.canEdit = false;
    });
  }

  scrollTo(element: any): void {
    (document.getElementById(element) as HTMLElement).scrollIntoView({behavior: "smooth", block: "nearest", inline: "nearest"});
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
