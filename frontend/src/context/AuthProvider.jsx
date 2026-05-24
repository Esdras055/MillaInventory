import { useCallback, useMemo, useState } from "react";
import api from "../api/axiosConfig";
import AuthContext from "./authContext";

function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("authToken"));
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem("authUser");
    return storedUser ? JSON.parse(storedUser) : null;
  });

  const isAuthenticated = Boolean(token);

  const login = useCallback(async (credentials) => {
    const { data } = await api.post("/auth/login", credentials);
    const authenticatedUser = {
      id: data.id,
      username: data.username,
      name: data.name,
    };

    localStorage.setItem("authToken", data.token);
    localStorage.setItem("authUser", JSON.stringify(authenticatedUser));
    setToken(data.token);
    setUser(authenticatedUser);

    return authenticatedUser;
  }, []);

  const registerUser = useCallback(async (values) => {
    const { data } = await api.post("/auth/register", values);
    return data;
  }, []);

  const logout = useCallback(async () => {
    const currentToken = localStorage.getItem("authToken");

    try {
      if (currentToken) {
        await api.post("/auth/logout");
      }
    } finally {
      localStorage.removeItem("authToken");
      localStorage.removeItem("authUser");
      setToken(null);
      setUser(null);
    }
  }, []);

  const value = useMemo(
    () => ({
      isAuthenticated,
      login,
      logout,
      registerUser,
      token,
      user,
    }),
    [isAuthenticated, login, logout, registerUser, token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
