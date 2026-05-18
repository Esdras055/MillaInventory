import {
  getProductos,
  createProducto,
  updateProducto,
  deleteProducto,
  getMovimientos
} from "../api/mockApi.js";

export const productService = {
  listar: getProductos,
  crear: createProducto,
  editar: updateProducto,
  eliminar: deleteProducto
};

export const movementService = {
  listar: getMovimientos
};
