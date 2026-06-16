import api from "./axiosConfig";

export const getMarcas = () => api.get("/marcas");

export const createMarca = (data) => api.post("/marcas", data);

export const updateMarca = (id, data) => api.put(`/marcas/${id}`, data);

export const deleteMarca = (id) => api.delete(`/marcas/${id}`);

export const getCategorias = () => api.get("/categorias");

export const getProveedores = () => api.get("/proveedores");
