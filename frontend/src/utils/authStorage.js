import { jwtDecode } from "jwt-decode";

const AUTH_TOKEN_KEY = "authToken";
const AUTH_USER_KEY = "authUser";
const UNAUTHORIZED_EVENT = "auth:unauthorized";

export function clearStoredAuth() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(AUTH_USER_KEY);
}

export function getStoredToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY);
}

export function getStoredUser() {
  const storedUser = localStorage.getItem(AUTH_USER_KEY);

  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser);
  } catch {
    localStorage.removeItem(AUTH_USER_KEY);
    return null;
  }
}

export function isTokenExpired(token) {
  if (!token) {
    return true;
  }

  try {
    const { exp } = jwtDecode(token);

    if (!exp) {
      return true;
    }

    return exp * 1000 <= Date.now();
  } catch {
    return true;
  }
}

export function setStoredAuth(token, user) {
  localStorage.setItem(AUTH_TOKEN_KEY, token);
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user));
}

export function notifyUnauthorized() {
  window.dispatchEvent(new Event(UNAUTHORIZED_EVENT));
}

export { AUTH_TOKEN_KEY, AUTH_USER_KEY, UNAUTHORIZED_EVENT };
