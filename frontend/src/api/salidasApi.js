import api from "./axiosConfig";

export function getSalidas() {
  return api.get("/salidas");
}

export function createSalida(values) {
  return api.post("/salidas", values);
}

export function updateSalida(id, values) {
  return api.put(`/salidas/${id}`, values);
}

export function deleteSalida(id) {
  return api.delete(`/salidas/${id}`);
}

export function getProductos() {
  return api.get("/productos");
}

export function getBodegas() {
  return api.get("/bodegas");
}
