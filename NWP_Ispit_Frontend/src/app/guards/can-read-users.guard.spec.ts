import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canReadUsersGuard } from './can-read-users.guard';

describe('canReadUsersGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canReadUsersGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
