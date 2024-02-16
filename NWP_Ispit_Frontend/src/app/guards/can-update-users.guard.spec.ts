import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canUpdateUsersGuard } from './can-update-users.guard';

describe('canUpdateUsersGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canUpdateUsersGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
