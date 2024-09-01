import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { getRolesFromToken } from '../../../utils/jwt-decoder';

export const roleAuthGuard: CanActivateFn = () => {
  const router = inject(Router);

  if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
    const userToken = localStorage.getItem('userToken');
    if (userToken != null) {
      const roles = getRolesFromToken(userToken);
      if (roles.includes('ADMIN') || roles.includes('STUDENT')) {
        return true;
      } else {
        return false;
      }
    } else {
      router.navigateByUrl('/login');
      return false;
    }
  } else {
    return false;
  }
};
