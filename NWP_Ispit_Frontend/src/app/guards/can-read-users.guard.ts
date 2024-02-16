import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../service/auth.service';

export const canReadUsersGuard: CanActivateFn = () => {
  const authService = inject(AuthService)

  const canActivate = () => {
    const flag = authService.hasPermission('can_read_users');

    if(!flag){
      alert("You do not have the permission to read users");
    }

    return flag
  }

  return canActivate();
}
