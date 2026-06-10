import api from "./axiosConfig";

export function getProveedores() {
  return api.get("/proveedores");
}

export function createProveedor(values) {
  return api.post("/proveedores", values);
}

export function updateProveedor(id, values) {
  return api.put(`/proveedores/${id}`, values);
}

export function deleteProveedor(id) {
  return api.delete(`/proveedores/${id}`);
}