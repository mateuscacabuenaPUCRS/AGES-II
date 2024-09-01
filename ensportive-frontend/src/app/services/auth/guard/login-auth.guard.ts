import { CanActivateFn, Router } from '@angular/router';
import { getRolesFromToken } from '../../../utils/jwt-decoder';
import { inject } from '@angular/core';

export const loginAuthGuard: CanActivateFn = () => {
  const router = inject(Router);
  if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
    const userToken = localStorage.getItem('userToken');

    if (userToken != null) {
      const roles = getRolesFromToken(userToken);
      if (roles.includes('ADMIN') || roles.includes('STUDENT')) {
        router.navigateByUrl('/calendario');
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  } else {
    return true;
  }
};
