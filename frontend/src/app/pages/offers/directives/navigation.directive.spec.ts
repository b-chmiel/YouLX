import { NavigationDirective } from './navigation.directive';
import {NavigationService} from '../../../services/navigation.service';

describe('NavigationDirective', () => {
  it('should create an instance', () => {
    const directive = new NavigationDirective({} as NavigationService);
    expect(directive).toBeTruthy();
  });
});
