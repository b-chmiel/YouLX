import {AfterViewInit, Component, ElementRef, EventEmitter, Output, ViewChild} from '@angular/core';
import {debounceTime, distinctUntilChanged, filter, fromEvent, Observable, tap} from 'rxjs';
import Profile from '../../../../models/profile';

@Component({
  selector: 'home-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
})
export class ToolbarComponent implements AfterViewInit {
  options: string[] = ['One', 'Two', 'Three'];
  @ViewChild('input', {static: true}) input!: ElementRef;
  @Output() search = new EventEmitter<string>();

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
