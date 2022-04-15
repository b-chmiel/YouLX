import { Component } from '@angular/core';
import {Offer} from "../../models/offer";

@Component({
  selector: 'home-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.scss']
})
export class OffersComponent {
  offers: Offer[] = [
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {}
  ];
}
