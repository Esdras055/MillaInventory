import { productService, movementService } from "./services/inventoryService.js";
import { productCard, movementCard } from "./ui/render.js";

const state = {
  productos: [],
  filtro: "",
  productoAEliminar: null
};

const productList = document.querySelector("#productList");
const movementList = document.querySelector("#movementList");
const productForm = document.querySelector("#productForm");
const searchInput = document.querySelector("#searchInput");
const toast = document.querySelector("#toast");
const apiLog = document.querySelector("#apiLog");

const productId = document.querySelector("#productId");
const nombre = document.querySelector("#nombre");
const categoria = document.querySelector("#categoria");
const marca = document.querySelector("#marca");
const bodega = document.querySelector("#bodega");
const precio = document.querySelector("#precio");
const stock = document.querySelector("#stock");

const formTitle = document.querySelector("#formTitle");
const formEndpoint = document.querySelector("#formEndpoint");
const saveBtn = document.querySelector("#saveBtn");
const cancelBtn = document.querySelector("#cancelBtn");
const openCreateBtn = document.querySelector("#openCreateBtn");

const deleteModal = document.querySelector("#deleteModal");
const deleteText = document.querySelector("#deleteText");
const confirmDeleteBtn = document.querySelector("#confirmDeleteBtn");
const closeDeleteBtn = document.querySelector("#closeDeleteBtn");

function addLog(text, type = "ok") {
  const line = document.createElement("div");
  line.className = `log-line ${type}`;
  line.textContent = text;
  apiLog.prepend(line);
}

function showToast(message) {
  toast.textContent = message;
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 1800);
}

function renderStats() {
  const total = state.productos.length;
  const stockTotal = state.productos.reduce((acc, producto) => acc + Number(producto.stock), 0);
  const bodegas = new Set(state.productos.map((producto) => producto.bodega)).size;

  document.querySelector("#totalProductos").textContent = total;
  document.querySelector("#stockTotal").textContent = stockTotal;
  document.querySelector("#bodegasActivas").textContent = bodegas;
}

function renderProducts() {
  const productosFiltrados = state.productos.filter((producto) => {
    const texto = `${producto.nombre} ${producto.categoria} ${producto.marca} ${producto.bodega}`;
    return texto.toLowerCase().includes(state.filtro.toLowerCase());
  });

  productList.innerHTML = productosFiltrados.length
    ? productosFiltrados.map(productCard).join("")
    : `<p class="product-meta">No se encontraron productos.</p>`;

  renderStats();
}

function renderMovements(movimientos) {
  movementList.innerHTML = movimientos.map(movementCard).join("");
}

function resetForm() {
  productForm.reset();
  productId.value = "";
  formTitle.textContent = "Crear producto";
  formEndpoint.textContent = "POST /api/productos";
  saveBtn.textContent = "Guardar";
}

function fillForm(producto) {
  productId.value = producto.id;
  nombre.value = producto.nombre;
  categoria.value = producto.categoria;
  marca.value = producto.marca;
  bodega.value = producto.bodega;
  precio.value = producto.precio;
  stock.value = producto.stock;

  formTitle.textContent = "Editar producto";
  formEndpoint.textContent = `PUT /api/productos/${producto.id}`;
  saveBtn.textContent = "Actualizar";

  document.querySelector(".form-card").scrollIntoView({ behavior: "smooth", block: "start" });
}

async function loadInitialData() {
  state.productos = await productService.listar();
  const movimientos = await movementService.listar();

  addLog("GET /api/productos · 200 OK");
  addLog("GET /api/movimientos · 200 OK");
  renderProducts();
  renderMovements(movimientos);
}

productForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const producto = {
    nombre: nombre.value.trim(),
    categoria: categoria.value,
    marca: marca.value.trim(),
    bodega: bodega.value,
    precio: Number(precio.value),
    stock: Number(stock.value)
  };

  if (productId.value) {
    await productService.editar(productId.value, producto);
    addLog(`PUT /api/productos/${productId.value} · 200 OK`, "warn");
    showToast("Producto actualizado correctamente");
  } else {
    await productService.crear(producto);
    addLog("POST /api/productos · 201 CREATED", "ok");
    showToast("Producto creado correctamente");
  }

  state.productos = await productService.listar();
  renderProducts();
  resetForm();
});

productList.addEventListener("click", (event) => {
  const button = event.target.closest("button");
  if (!button) return;

  const id = Number(button.dataset.id);
  const producto = state.productos.find((item) => item.id === id);

  if (button.dataset.action === "edit") {
    fillForm(producto);
  }

  if (button.dataset.action === "delete") {
    state.productoAEliminar = producto;
    deleteText.textContent = `¿Deseas eliminar "${producto.nombre}" del inventario?`;
    deleteModal.classList.add("show");
    deleteModal.setAttribute("aria-hidden", "false");
  }
});

confirmDeleteBtn.addEventListener("click", async () => {
  if (!state.productoAEliminar) return;

  await productService.eliminar(state.productoAEliminar.id);
  addLog(`DELETE /api/productos/${state.productoAEliminar.id} · 200 OK`, "delete");
  showToast("Producto eliminado correctamente");

  state.productos = await productService.listar();
  renderProducts();

  state.productoAEliminar = null;
  deleteModal.classList.remove("show");
  deleteModal.setAttribute("aria-hidden", "true");
});

closeDeleteBtn.addEventListener("click", () => {
  state.productoAEliminar = null;
  deleteModal.classList.remove("show");
  deleteModal.setAttribute("aria-hidden", "true");
});

cancelBtn.addEventListener("click", resetForm);

openCreateBtn.addEventListener("click", () => {
  resetForm();
  document.querySelector(".form-card").scrollIntoView({ behavior: "smooth", block: "start" });
});

searchInput.addEventListener("input", (event) => {
  state.filtro = event.target.value;
  renderProducts();
});

loadInitialData();
