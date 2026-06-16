import {
  Edit,
  Loader2,
  Package,
  Plus,
  RefreshCw,
  Save,
  Trash2,
  X,
} from "lucide-react";

import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";

import AlertMessage from "../components/AlertMessage";

import {
  createProducto,
  deleteProducto,
  getCategorias,
  getMarcas,
  getProductos,
  updateProducto,
} from "../api/productosApi";

const initialForm = {
  nombre: "",
  categoriaId: "",
  precio: "",
  marcaIds: [],
};

export default function ProductosPage() {
  const [productos, setProductos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [marcas, setMarcas] = useState([]);

  const [editingProducto, setEditingProducto] = useState(null);

  const [form, setForm] = useState(initialForm);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  const [isFormModalOpen, setIsFormModalOpen] = useState(false);

  const isEditing = Boolean(editingProducto);

  const sortedProductos = useMemo(
    () =>
      [...productos].sort((a, b) =>
        a.nombre.localeCompare(b.nombre)
      ),
    [productos]
  );

  async function loadData() {
    setIsLoading(true);

    try {
      const [
        productosResponse,
        categoriasResponse,
        marcasResponse,
      ] = await Promise.all([
        getProductos(),
        getCategorias(),
        getMarcas(),
      ]);

      setProductos(productosResponse.data);
      setCategorias(categoriasResponse.data);
      setMarcas(marcasResponse.data);
    } catch {
      setError("No se pudieron cargar los productos.");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  function openCreateModal() {
    setEditingProducto(null);
    setForm(initialForm);
    setIsFormModalOpen(true);
  }

  function startEdit(producto) {
    setEditingProducto(producto);

    setForm({
      nombre: producto.nombre,
      categoriaId: String(producto.categoriaId),
      precio: producto.precio,
      marcaIds:
        producto.marcas?.map((m) => m.id) || [],
    });

    setIsFormModalOpen(true);
  }

  function handleInputChange(e) {
    const { name, value } = e.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
  e.preventDefault();

  const payload = {
    nombre: form.nombre,
    categoriaId: Number(form.categoriaId),
    precio: Number(form.precio),
    marcaIds: form.marcaIds,
  };

  console.log(payload);

  setIsSaving(true);

  try {

    if (isEditing) {

      await updateProducto(
        editingProducto.id,
        payload
      );

    } else {

      await createProducto(payload);

    }

    setIsFormModalOpen(false);

    setForm(initialForm);

    loadData();

  } catch (error) {

    console.log("ERROR COMPLETO:");
    console.log(error);

    console.log("RESPUESTA:");
    console.log(error.response);

    console.log("DATA:");
    console.log(error.response?.data);

    setError("No se pudo guardar.");

  } finally {

    setIsSaving(false);

  }
}

  async function handleDelete(producto) {
    const result = await Swal.fire({
      icon: "warning",
      title: `Eliminar ${producto.nombre}?`,
      showCancelButton: true,
      confirmButtonText: "Eliminar",
    });

    if (!result.isConfirmed) return;

    await deleteProducto(producto.id);

    loadData();
  }

  return (
    <section>

      <div className="mb-6 flex justify-between">

        <div>
          <p className="text-sm font-semibold uppercase text-emerald-700">
            Inventario
          </p>

          <h1 className="mt-2 text-2xl font-bold">
            Productos
          </h1>
        </div>

        <div className="flex gap-2">

          <button
            className="rounded-lg border px-4 py-2"
            onClick={loadData}
          >
            <RefreshCw size={18} />
          </button>

          <button
            className="rounded-lg bg-emerald-600 px-4 py-2 text-white"
            onClick={openCreateModal}
          >
            <Plus size={18} />
            Nuevo producto
          </button>

        </div>

      </div>

      <AlertMessage
        type="error"
        message={error}
        onClose={() => setError("")}
      />

      <AlertMessage
        type="success"
        message={success}
        onClose={() => setSuccess("")}
      />

      <section className="rounded-lg border bg-white shadow-sm">

        <div className="border-b px-5 py-4">
          <h2 className="font-bold">
            Productos registrados
          </h2>
        </div>

        <div className="overflow-x-auto">

          <table className="w-full text-sm">

            <thead className="bg-slate-50">

              <tr>

                <th className="px-5 py-3">
                  ID
                </th>

                <th className="px-5 py-3">
                  Nombre
                </th>

                <th className="px-5 py-3">
                  Categoria
                </th>

                <th className="px-5 py-3">
                  Precio
                </th>

                <th className="px-5 py-3">
                  Marcas
                </th>

                <th className="px-5 py-3">
                  Acciones
                </th>

              </tr>

            </thead>

            <tbody>

              {isLoading &&
                Array.from({ length: 5 }).map((_, i) => (
                  <tr key={i}>
                    <td className="px-5 py-4">
                      <div className="h-4 w-20 animate-pulse rounded bg-slate-200" />
                    </td>
                  </tr>
                ))}

              {!isLoading &&
                sortedProductos.map((producto) => (

                  <tr key={producto.id}>

                    <td className="px-5 py-4">
                      #{producto.id}
                    </td>

                    <td className="px-5 py-4">
                      {producto.nombre}
                    </td>

                    <td className="px-5 py-4">
                      {producto.categoriaNombre}
                    </td>

                    <td className="px-5 py-4">
                      ${producto.precio}
                    </td>

                    <td className="px-5 py-4">

                      {producto.marcas
                        ?.map((m) => m.nombre)
                        .join(", ")}

                    </td>

                    <td className="px-5 py-4">

                      <div className="flex gap-2">

                        <button
                          className="rounded-lg border p-2"
                          onClick={() =>
                            startEdit(producto)
                          }
                        >
                          <Edit size={16} />
                        </button>

                        <button
                          className="rounded-lg border border-red-200 p-2 text-red-600"
                          onClick={() =>
                            handleDelete(producto)
                          }
                        >
                          <Trash2 size={16} />
                        </button>

                      </div>

                    </td>

                  </tr>

                ))}

            </tbody>

          </table>

        </div>
{isFormModalOpen && (
<div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4">

<div className="w-full max-w-lg rounded-xl bg-white shadow-xl">

<form onSubmit={handleSubmit}>

<div className="flex items-center justify-between border-b p-5">

<div className="flex items-center gap-3">

<div className="rounded-lg bg-emerald-100 p-3 text-emerald-700">
<Package size={20}/>
</div>

<div>

<h2 className="font-bold text-xl">
{isEditing ? "Editar producto" : "Nuevo producto"}
</h2>

<p className="text-sm text-slate-500">
Datos del producto
</p>

</div>

</div>

<button
type="button"
onClick={() => setIsFormModalOpen(false)}
className="rounded-lg p-2 hover:bg-slate-100"
>
<X size={20}/>
</button>

</div>

<div className="space-y-5 p-6">

<div>

<label className="mb-2 block text-sm font-semibold">
Nombre
</label>

<input
name="nombre"
value={form.nombre}
onChange={handleInputChange}
className="w-full rounded-lg border px-4 py-3"
/>

</div>

<div>

<label className="mb-2 block text-sm font-semibold">
Categoría
</label>

<select
name="categoriaId"
value={form.categoriaId}
onChange={handleInputChange}
className="w-full rounded-lg border px-4 py-3"
>

<option value="">
Seleccionar
</option>

{categorias.map((categoria)=>(

<option
key={categoria.id}
value={categoria.id}
>

{categoria.nombre}

</option>

))}

</select>

</div>

<div>

<label className="mb-2 block text-sm font-semibold">
Precio
</label>

<input
type="number"
name="precio"
value={form.precio}
onChange={handleInputChange}
className="w-full rounded-lg border px-4 py-3"
/>

</div>

<div>

<label className="mb-2 block text-sm font-semibold">
  Marcas
</label>

<select
  multiple
  className="h-40 w-full rounded-lg border px-4 py-3"
  value={form.marcaIds}
  onChange={(e) =>
    setForm((current) => ({
      ...current,
      marcaIds: [...e.target.selectedOptions].map(
        (option) => Number(option.value)
      ),
    }))
  }
>

  {marcas.map((marca) => (

    <option
      key={marca.id}
      value={marca.id}
    >
      {marca.nombre}
    </option>

  ))}

</select>

</div>

</div>

<div className="flex justify-end gap-3 border-t p-5">

<button
type="button"
onClick={()=>setIsFormModalOpen(false)}
className="rounded-lg border px-4 py-2"
>

Cancelar

</button>

<button
disabled={isSaving}
className="flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-white"
>

{isSaving ? (

<Loader2
size={18}
className="animate-spin"
/>

) : (

<Save size={18}/>

)}

{isEditing ? "Guardar" : "Crear"}

</button>

</div>

</form>

</div>

</div>
)}
      </section>

    </section>
  );
}