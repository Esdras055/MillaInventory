import {
  Edit,
  Loader2,
  Plus,
  RefreshCw,
  Save,
  Tag,
  Trash2,
  X,
} from "lucide-react";

import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";

import AlertMessage from "../components/AlertMessage";

import {
  createMarca,
  deleteMarca,
  getCategorias,
  getMarcas,
  getProveedores,
  updateMarca,
} from "../api/marcasApi";

const initialForm = {
  nombre: "",
  categoriaIds: [],
  proveedorIds: [],
};

export default function MarcasPage() {
  const [marcas, setMarcas] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [proveedores, setProveedores] = useState([]);

  const [editingMarca, setEditingMarca] = useState(null);
  const [form, setForm] = useState(initialForm);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  const [isFormModalOpen, setIsFormModalOpen] = useState(false);

  const isEditing = Boolean(editingMarca);

  const sortedMarcas = useMemo(
    () => [...marcas].sort((a, b) => a.nombre.localeCompare(b.nombre)),
    [marcas]
  );

  async function loadData() {
    setIsLoading(true);
    try {
      const [marcasRes, categoriasRes, proveedoresRes] = await Promise.all([
        getMarcas(),
        getCategorias(),
        getProveedores(),
      ]);
      setMarcas(marcasRes.data);
      setCategorias(categoriasRes.data);
      setProveedores(proveedoresRes.data);
    } catch {
      setError("No se pudieron cargar los datos.");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  function openCreateModal() {
    setEditingMarca(null);
    setForm(initialForm);
    setIsFormModalOpen(true);
  }

  function startEdit(marca) {
    setEditingMarca(marca);
    setForm({
      nombre: marca.nombre,
      categoriaIds: marca.categorias?.map((c) => c.id) || [],
      proveedorIds: marca.proveedores?.map((p) => p.id) || [],
    });
    setIsFormModalOpen(true);
  }

  function handleInputChange(e) {
    const { name, value } = e.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();

    const payload = {
      nombre: form.nombre,
      categoriaIds: form.categoriaIds,
      proveedorIds: form.proveedorIds,
    };

    setIsSaving(true);
    try {
      if (isEditing) {
        await updateMarca(editingMarca.id, payload);
      } else {
        await createMarca(payload);
      }
      setIsFormModalOpen(false);
      setForm(initialForm);
      loadData();
    } catch {
      setError("No se pudo guardar la marca.");
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete(marca) {
    const result = await Swal.fire({
      icon: "warning",
      title: `¿Eliminar ${marca.nombre}?`,
      showCancelButton: true,
      confirmButtonText: "Eliminar",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    try {
      await deleteMarca(marca.id);
      loadData();
    } catch {
      setError("No se pudo eliminar la marca.");
    }
  }

  return (
    <section>

      <div className="mb-6 flex justify-between">
        <div>
          <p className="text-sm font-semibold uppercase text-emerald-700">
            Inventario
          </p>
          <h1 className="mt-2 text-2xl font-bold">Marcas</h1>
        </div>

        <div className="flex gap-2">
          <button
            className="rounded-lg border px-4 py-2"
            onClick={loadData}
          >
            <RefreshCw size={18} />
          </button>

          <button
            className="flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-white"
            onClick={openCreateModal}
          >
            <Plus size={18} />
            Nueva marca
          </button>
        </div>
      </div>

      <AlertMessage type="error" message={error} onClose={() => setError("")} />
      <AlertMessage type="success" message={success} onClose={() => setSuccess("")} />

      <section className="rounded-lg border bg-white shadow-sm">

        <div className="border-b px-5 py-4">
          <h2 className="font-bold">Marcas registradas</h2>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50">
              <tr>
                <th className="px-5 py-3">ID</th>
                <th className="px-5 py-3">Nombre</th>
                <th className="px-5 py-3">Categorías</th>
                <th className="px-5 py-3">Proveedores</th>
                <th className="px-5 py-3">Acciones</th>
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
                sortedMarcas.map((marca) => (
                  <tr key={marca.id}>
                    <td className="px-5 py-4">#{marca.id}</td>

                    <td className="px-5 py-4 font-medium">{marca.nombre}</td>

                    <td className="px-5 py-4">
                      {marca.categorias?.length > 0 ? (
                        <div className="flex flex-wrap gap-1">
                          {marca.categorias.map((c) => (
                            <span
                              key={c.id}
                              className="rounded-full bg-emerald-100 px-2 py-0.5 text-xs text-emerald-700"
                            >
                              {c.nombre}
                            </span>
                          ))}
                        </div>
                      ) : (
                        <span className="text-slate-400">—</span>
                      )}
                    </td>

                    <td className="px-5 py-4">
                      {marca.proveedores?.length > 0 ? (
                        marca.proveedores.map((p) => p.nombre).join(", ")
                      ) : (
                        <span className="text-slate-400">—</span>
                      )}
                    </td>

                    <td className="px-5 py-4">
                      <div className="flex gap-2">
                        <button
                          className="rounded-lg border p-2"
                          onClick={() => startEdit(marca)}
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          className="rounded-lg border border-red-200 p-2 text-red-600"
                          onClick={() => handleDelete(marca)}
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
                      <Tag size={20} />
                    </div>
                    <div>
                      <h2 className="text-xl font-bold">
                        {isEditing ? "Editar marca" : "Nueva marca"}
                      </h2>
                      <p className="text-sm text-slate-500">Datos de la marca</p>
                    </div>
                  </div>

                  <button
                    type="button"
                    onClick={() => setIsFormModalOpen(false)}
                    className="rounded-lg p-2 hover:bg-slate-100"
                  >
                    <X size={20} />
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
                      placeholder="Ej: Marca A"
                    />
                  </div>

                  <div>
                    <label className="mb-2 block text-sm font-semibold">
                      Categorías de productos
                    </label>
                    <select
                      multiple
                      className="h-36 w-full rounded-lg border px-4 py-3"
                      value={form.categoriaIds}
                      onChange={(e) =>
                        setForm((current) => ({
                          ...current,
                          categoriaIds: [...e.target.selectedOptions].map(
                            (opt) => Number(opt.value)
                          ),
                        }))
                      }
                    >
                      {categorias.map((c) => (
                        <option key={c.id} value={c.id}>
                          {c.nombre}
                        </option>
                      ))}
                    </select>
                    <p className="mt-1 text-xs text-slate-400">
                      Mantén Ctrl (o Cmd) para seleccionar varias
                    </p>
                  </div>

                  <div>
                    <label className="mb-2 block text-sm font-semibold">
                      Proveedores
                    </label>
                    <select
                      multiple
                      className="h-36 w-full rounded-lg border px-4 py-3"
                      value={form.proveedorIds}
                      onChange={(e) =>
                        setForm((current) => ({
                          ...current,
                          proveedorIds: [...e.target.selectedOptions].map(
                            (opt) => Number(opt.value)
                          ),
                        }))
                      }
                    >
                      {proveedores.map((p) => (
                        <option key={p.id} value={p.id}>
                          {p.nombre}
                        </option>
                      ))}
                    </select>
                    <p className="mt-1 text-xs text-slate-400">
                      Mantén Ctrl (o Cmd) para seleccionar varios
                    </p>
                  </div>

                </div>

                <div className="flex justify-end gap-3 border-t p-5">
                  <button
                    type="button"
                    onClick={() => setIsFormModalOpen(false)}
                    className="rounded-lg border px-4 py-2"
                  >
                    Cancelar
                  </button>

                  <button
                    disabled={isSaving}
                    className="flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-white"
                  >
                    {isSaving ? (
                      <Loader2 size={18} className="animate-spin" />
                    ) : (
                      <Save size={18} />
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
