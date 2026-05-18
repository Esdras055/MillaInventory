let productos = [
  {
    id: 1,
    nombre: "Silla ejecutiva",
    categoria: "Sillas",
    marca: "Milla Home",
    bodega: "Bodega Central",
    precio: 85.5,
    stock: 18
  },
  {
    id: 2,
    nombre: "Escritorio modular",
    categoria: "Escritorios",
    marca: "Office Plus",
    bodega: "Bodega Santa Ana",
    precio: 145,
    stock: 9
  },
  {
    id: 3,
    nombre: "Archivador metálico",
    categoria: "Archivadores",
    marca: "MetalPro",
    bodega: "Bodega San Salvador",
    precio: 110,
    stock: 12
  }
];

const movimientos = [
  { id: 1, tipo: "Entrada", producto: "Silla ejecutiva", cantidad: 10, fecha: "2026-05-17" },
  { id: 2, tipo: "Salida", producto: "Escritorio modular", cantidad: 2, fecha: "2026-05-16" },
  { id: 3, tipo: "Entrada", producto: "Archivador metálico", cantidad: 5, fecha: "2026-05-15" }
];

const delay = (data) =>
  new Promise((resolve) => setTimeout(() => resolve(structuredClone(data)), 250));

export async function getProductos() {
  return delay(productos);
}

export async function createProducto(producto) {
  const nuevo = {
    ...producto,
    id: productos.length ? Math.max(...productos.map((item) => item.id)) + 1 : 1
  };
  productos = [nuevo, ...productos];
  return delay(nuevo);
}

export async function updateProducto(id, productoActualizado) {
  productos = productos.map((producto) =>
    producto.id === Number(id) ? { ...producto, ...productoActualizado, id: Number(id) } : producto
  );
  return delay(productos.find((producto) => producto.id === Number(id)));
}

export async function deleteProducto(id) {
  productos = productos.filter((producto) => producto.id !== Number(id));
  return delay({ eliminado: true, id: Number(id) });
}

export async function getMovimientos() {
  return delay(movimientos);
}
