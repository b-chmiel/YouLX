import { Component, OnInit } from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';

@Component({
  selector: 'app-browse-my-offers',
  templateUrl: './browse-my-offers.component.html',
  styleUrls: ['./browse-my-offers.component.scss']
})
export class BrowseMyOffersComponent implements OnInit {
  offers: Offer[] = [];
  page: number = 0;
  size: number = 10;

  constructor(private route: ActivatedRoute, private offersService: OffersService) {
  }

  ngOnInit() {
    this.offers = this.route.snapshot.data['offers'];
  }
}
