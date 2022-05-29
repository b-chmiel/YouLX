import { Directive, HostListener } from '@angular/core'
import { NavigationService } from '../../../services/navigation.service'

@Directive({
  selector: '[backButton]',
})
export class NavigationDirective {
  constructor(private navigation: NavigationService) {}

  @HostListener('click')
  onClick(): void {
    this.navigation.back()
  }
}
