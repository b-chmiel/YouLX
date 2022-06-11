import {AfterViewInit, Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {debounceTime, distinctUntilChanged, filter, fromEvent, map, Observable, startWith, tap} from 'rxjs';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {FormControl} from '@angular/forms';
import {MatChipInputEvent} from '@angular/material/chips';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'home-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
})
export class ToolbarComponent implements AfterViewInit, OnInit {
  options: string[] = ['One', 'Two', 'Three'];
  @ViewChild('input', {static: true}) input!: ElementRef;
  @Output() search = new EventEmitter<string>();
  @Output() searchTags = new EventEmitter<string[]>();

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

    this.searchTags.emit(this.tags);
  }

  remove(fruit: string): void {
    const index = this.tags.indexOf(fruit);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }

    this.searchTags.emit(this.tags);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.push(event.option.viewValue);
    this.fruitInput.nativeElement.value = '';
    this.tagCtrl.setValue(null);
    this.searchTags.emit(this.tags);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }

  ngOnInit() {
    this.allTags = this.route.snapshot.data['tags'];
  }

  constructor(private route: ActivatedRoute) {
    this.filteredTags = this.tagCtrl.valueChanges.pipe(
      startWith(null),
      map((fruit: string | null) => (fruit ? this._filter(fruit) : this.allTags.slice())),
    );
  }

  ngAfterViewInit() {
    // server-side search
    const observable: Observable<KeyboardEvent> = fromEvent(this.input.nativeElement, 'keyup');

    observable.pipe(
      filter(Boolean),
      debounceTime(200),
      distinctUntilChanged<KeyboardEvent>(),
      tap(_ => {
        const value = this.input.nativeElement.value;
        this.search.emit(value);
      }),
    )
      .subscribe();
  }
}
