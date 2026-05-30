import api from "./axiosConfig";

export function getEntradas() {
  return api.get("/entradas");
}

export function createEntrada(values) {
  return api.post("/entradas", values);
}

export function updateEntrada(id, values) {
  return api.put(`/entradas/${id}`, values);
}

export function deleteEntrada(id) {
  return api.delete(`/entradas/${id}`);
}

export function getProductos() {
  return api.get("/productos");
}

export function getBodegas() {
  return api.get("/bodegas");
}

export function getProveedores() {
  return api.get("/proveedores");
}
