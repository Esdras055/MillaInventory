import api from "./axiosConfig";

export function getBodegas() {
  return api.get("/bodegas");
}

export function createBodega(values) {
  return api.post("/bodegas", values);
}

export function updateBodega(id, values) {
  return api.put(`/bodegas/${id}`, values);
}

export function deleteBodega(id) {
  return api.delete(`/bodegas/${id}`);
}

export function getUbicaciones() {
  return api.get("/ubicaciones");
}

export function getStockPorBodega(id) {
  return api.get(`/reportes/stock/bodega/${id}`);
}
