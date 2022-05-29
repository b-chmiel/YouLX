import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Offer} from "../../../../models/offer";

@Component({
  selector: 'offers-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OfferComponent {
  @Input() offer!: Offer;
}
