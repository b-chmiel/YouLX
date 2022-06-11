import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CreateOfferDto, Offer} from '../../../../models/offer';
import {OffersService} from '../../../../services/offers.service';
import {ActivatedRoute, Router} from '@angular/router';
import {map, Observable, startWith, switchMap} from 'rxjs';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';

@Component({
  selector: 'app-offer-new',
  templateUrl: './offer-new.component.html',
  styleUrls: ['./offer-new.component.scss'],
})
export class OfferNewComponent implements OnInit {
  fetching = false;
  error: string | null = null;
  form!: FormGroup;
  images: { file?: File, url: string, id?: string }[] = [];
  delete: string[] = [];
  message?: string;

  separatorKeysCodes: number[] = [ENTER, COMMA];
  tagCtrl = new FormControl('');
  filteredTags: Observable<string[]>;
  tags: string[] = [];
  allTags: string[] = ['Apple', 'Lemon', 'Lime', 'Orange', 'Strawberry'];

  @ViewChild('tagInput') fruitInput!: ElementRef<HTMLInputElement>;

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();

    this.tagCtrl.setValue(null);
  }

  remove(fruit: string): void {
    const index = this.tags.indexOf(fruit);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.push(event.option.viewValue);
    this.fruitInput.nativeElement.value = '';
    this.tagCtrl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }

  constructor(private offers: OffersService, private router: Router, private route: ActivatedRoute) {
    this.filteredTags = this.tagCtrl.valueChanges.pipe(
      startWith(null),
      map((fruit: string | null) => (fruit ? this._filter(fruit) : this.allTags.slice())),
    );
  }

  ngOnInit() {
    this.allTags = this.route.snapshot.data['tags'];
    const offer = this.route.snapshot.data['offer'] as Offer;
    if (offer && offer.tags && offer.tags.length > 0) {
      this.tags = offer.tags.map(tag => tag.name);
    }
    if (offer?.imageUrls && offer.imageUrls.length > 0) {
      this.images = offer.imageUrls.map(value => {
        const split = value.split('/');
        return {url: value, id: split[split.length - 1]};
      });
    }
    this.form = new FormGroup({
      id: new FormControl(offer?.id ?? ''),
      name: new FormControl(offer?.name ?? '', Validators.required),
      description: new FormControl(offer?.description ?? '', Validators.required),
      price: new FormControl(offer?.price, Validators.min(0)),
    });
  }

  onSubmit() {
    if (!this.form.valid || this.fetching) {
      return;
    }

    this.fetching = true;
    this.form.value.tags = this.tags;
    if (this.form.value.id) {
      let observable = this.offers.updateOffer(this.form.value).pipe(map(offer => offer ?? this.form.value));

      if (this.images.length > 0) {
        for (let i = 0; i < this.images.length; i++) {
          const image = this.images[i];
          if (image.file) {
            observable = observable.pipe(switchMap(offer => {
              return this.offers.uploadPhoto(offer.id, image.file as File).pipe(map(_ => offer));
            }));
          }
        }
      }

      if (this.delete.length > 0) {
        for (let i = 0; i < this.delete.length; i++) {
          const imageId = this.delete[i];
          observable = observable.pipe(switchMap(offer => {
            return this.offers.deletePhoto(offer.id, imageId).pipe(map(_ => offer));
          }));
        }
      }

      observable.subscribe(result => {
        this.router.navigateByUrl(`/offers/owned/${result.id}`);
        this.fetching = false;
      });
    } else {
      const offer = this.form.value as CreateOfferDto;
      if (offer.price == 0) {
        delete offer.price;
      }
      let observable = this.offers.createOffer(offer);

      if (this.images.length > 0) {
        for (let i = 0; i < this.images.length; i++) {
          const image = this.images[i];
          if (image.file) {
            observable = observable.pipe(switchMap(offerId => {
              const id = offerId as string;
              return this.offers.uploadPhoto(id, image.file as File).pipe(map(_ => id));
            }));
          }
        }
      }

      observable.subscribe(result => {
        if (result) {
          this.router.navigateByUrl(`/offers/owned/${result}`);
        } else {
          this.error = 'Could not create offer. Perhaps try again?';
        }

        this.fetching = false;
      });
    }
  }

  removeImage(i: number) {
    const image = this.images[i];
    if (image.id) {
      this.delete.push(image.id);
    }
    this.images = this.images.filter((_, index) => index != i);
  }

  onImagesChange($event: Event) {
    const input = $event.target as HTMLInputElement;
    const files = input.files;

    if (!files) {
      return;
    }

    for (let i = 0; i < files.length; i++) {
      const reader = new FileReader();
      const file = files[i];
      // @ts-ignore
      reader.onload = e => this.images.push({file: file, url: e.target!.result});
      reader.readAsDataURL(file);
    }
  }
}
