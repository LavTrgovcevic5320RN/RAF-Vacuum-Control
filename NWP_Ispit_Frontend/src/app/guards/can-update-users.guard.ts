import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../service/auth.service';

export const canUpdateUsersGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);

  const canActivate = () => {
    const flag = authService.hasPermission('can_update_users');

    if(!flag){
      alert("You do not have the permission to read users");
    }

    return flag;
  }

  return canActivate();
};
