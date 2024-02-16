import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canCreateUsersGuard } from './can-create-users.guard';

describe('canCreateUsersGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canCreateUsersGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
