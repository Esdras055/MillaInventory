import api from "./axiosConfig";

export function getResumen() {
  return api.get("/reportes/resumen");
}

export function getProductosBajoStock(minimo = 10) {
  return api.get("/reportes/productos-bajo-stock", {
    params: { minimo },
  });
}

export function getMovimientos(fechaInicio, fechaFin) {
  return api.get("/reportes/movimientos", {
    params: { fechaInicio, fechaFin },
  });
}
