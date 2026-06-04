import axios from "axios";
import {
  clearStoredAuth,
  getStoredToken,
  isTokenExpired,
  notifyUnauthorized,
} from "../utils/authStorage";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = getStoredToken();

  if (token) {
    if (isTokenExpired(token)) {
      clearStoredAuth();
      notifyUnauthorized();
      return Promise.reject(new Error("La sesion ha expirado."));
    }

    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      clearStoredAuth();
      notifyUnauthorized();
    }

    return Promise.reject(error);
  },
);

export default api;
