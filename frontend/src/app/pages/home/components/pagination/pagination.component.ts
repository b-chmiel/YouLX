import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'home-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent {
  @Input() page: number = 0;
  @Output() nextPage = new EventEmitter<number>()
  @Output() prevPage = new EventEmitter<number>()

  onNextPage() {
    this.nextPage.emit(this.page + 1)
  }

  onPrevPage() {
    this.prevPage.emit(this.page - 1)
  }
}
