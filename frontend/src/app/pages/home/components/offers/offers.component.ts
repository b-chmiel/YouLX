import {Component, OnInit} from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'home-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.scss'],
})
export class OffersComponent implements OnInit {
  offers: Offer[] = [];

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.offers = this.route.snapshot.data['offers'];
  }
}
