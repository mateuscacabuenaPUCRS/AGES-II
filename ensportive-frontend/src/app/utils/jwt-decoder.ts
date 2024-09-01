import { jwtDecode } from 'jwt-decode';

export interface JwtPayload {
  sub: string;
  ROLES: string[];
  USER_ID: number;
  iat: number;
  exp: number;
}

export function getRolesFromToken(token: string): string[] {
  try {
    const decoded: JwtPayload = jwtDecode<JwtPayload>(token);
    return decoded.ROLES || [];
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return [];
  }
}

export function getUsernameFromToken(token: string): string {
  try {
    const decoded: JwtPayload = jwtDecode<JwtPayload>(token);
    return decoded.sub || '';
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return '';
  }
}

export function getUserIdFromToken(token: string): number | null {
  try {
    const decoded: JwtPayload = jwtDecode<JwtPayload>(token);
    return decoded.USER_ID || null;
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return null;
  }
}
