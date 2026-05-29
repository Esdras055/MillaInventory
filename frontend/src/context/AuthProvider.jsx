import { useCallback, useEffect, useMemo, useState } from "react";
import api from "../api/axiosConfig";
import {
  clearStoredAuth,
  getStoredToken,
  getStoredUser,
  isTokenExpired,
  setStoredAuth,
  UNAUTHORIZED_EVENT,
} from "../utils/authStorage";
import AuthContext from "./authContext";

function AuthProvider({ children }) {
  const [token, setToken] = useState(() => {
    const storedToken = getStoredToken();

    if (isTokenExpired(storedToken)) {
      clearStoredAuth();
      return null;
    }

    return storedToken;
  });
  const [user, setUser] = useState(() => {
    const storedToken = getStoredToken();

    if (isTokenExpired(storedToken)) {
      return null;
    }

    return getStoredUser();
  });

  const isAuthenticated = Boolean(token);

  const clearSession = useCallback(() => {
    clearStoredAuth();
    setToken(null);
    setUser(null);
  }, []);

  const login = useCallback(async (credentials) => {
    const { data } = await api.post("/auth/login", credentials);
    const authenticatedUser = {
      id: data.id,
      username: data.username,
      name: data.name,
    };

    setStoredAuth(data.token, authenticatedUser);
    setToken(data.token);
    setUser(authenticatedUser);

    return authenticatedUser;
  }, []);

  const registerUser = useCallback(async (values) => {
    const { data } = await api.post("/auth/register", values);
    return data;
  }, []);

  const logout = useCallback(async () => {
    const currentToken = getStoredToken();

    try {
      if (currentToken && !isTokenExpired(currentToken)) {
        await api.post("/auth/logout");
      }
    } finally {
      clearSession();
    }
  }, [clearSession]);

  useEffect(() => {
    window.addEventListener(UNAUTHORIZED_EVENT, clearSession);

    return () => {
      window.removeEventListener(UNAUTHORIZED_EVENT, clearSession);
    };
  }, [clearSession]);

  const value = useMemo(
    () => ({
      clearSession,
      isAuthenticated,
      login,
      logout,
      registerUser,
      token,
      user,
    }),
    [clearSession, isAuthenticated, login, logout, registerUser, token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
