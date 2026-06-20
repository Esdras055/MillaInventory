import {
  Edit,
  Loader2,
  Plus,
  RefreshCw,
  Save,
  Trash2,
  Truck,
  X,
} from "lucide-react";

import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";

import AlertMessage from "../components/AlertMessage";

import {
  createProveedor,
  deleteProveedor,
  getProveedores,
  updateProveedor,
} from "../api/proveedoresApi";

const initialForm = {
  nombre: "",
  email: "",
  telefono: "",
};

export default function ProveedoresPage() {
  const [proveedores, setProveedores] = useState([]);

  const [editingProveedor, setEditingProveedor] = useState(null);
  const [form, setForm] = useState(initialForm);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  const [isFormModalOpen, setIsFormModalOpen] = useState(false);
  const [telefonoError, setTelefonoError] = useState("");

  const TELEFONO_REGEX = /^\+?[\d\s\-().]{7,20}$/;

  const isEditing = Boolean(editingProveedor);

  const sortedProveedores = useMemo(
    () => [...proveedores].sort((a, b) => a.nombre.localeCompare(b.nombre)),
    [proveedores]
  );

  async function loadData() {
    setIsLoading(true);
    try {
      const res = await getProveedores();
      setProveedores(res.data);
    } catch {
      setError("No se pudieron cargar los proveedores.");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  function openCreateModal() {
    setEditingProveedor(null);
    setForm(initialForm);
    setTelefonoError("");
    setIsFormModalOpen(true);
  }

  function startEdit(proveedor) {
    setEditingProveedor(proveedor);
    setForm({
      nombre: proveedor.nombre,
      email: proveedor.email,
      telefono: proveedor.telefono,
    });
    setTelefonoError("");
    setIsFormModalOpen(true);
  }

  function handleInputChange(e) {
    const { name, value } = e.target;
    setForm((current) => ({ ...current, [name]: value }));
    if (name === "telefono") {
      setTelefonoError(
        value && !TELEFONO_REGEX.test(value)
          ? "Formato inválido. Ej: +503 7000-0000"
          : ""
      );
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (telefonoError) return;

    const payload = {
      nombre: form.nombre,
      email: form.email,
      telefono: form.telefono,
    };

    setIsSaving(true);
    try {
      if (isEditing) {
        await updateProveedor(editingProveedor.id, payload);
      } else {
        await createProveedor(payload);
      }
      setIsFormModalOpen(false);
      setForm(initialForm);
      loadData();
    } catch (err) {
      const msg = err?.response?.data?.message || "No se pudo guardar el proveedor.";
      setError(msg);
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete(proveedor) {
    const result = await Swal.fire({
      icon: "warning",
      title: `¿Eliminar ${proveedor.nombre}?`,
      text: "Se eliminará también su vínculo con las marcas.",
      showCancelButton: true,
      confirmButtonText: "Eliminar",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    try {
      await deleteProveedor(proveedor.id);
      loadData();
    } catch (err) {
      const msg = err?.response?.data?.message || "No se pudo eliminar el proveedor.";
      setError(msg);
    }
  }

  return (
    <section>

      <div className="mb-6 flex justify-between">
        <div>
          <p className="text-sm font-semibold uppercase text-emerald-700">
            Inventario
          </p>
          <h1 className="mt-2 text-2xl font-bold">Proveedores</h1>
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
            Nuevo proveedor
          </button>
        </div>
      </div>

      <AlertMessage type="error" message={error} onClose={() => setError("")} />
      <AlertMessage type="success" message={success} onClose={() => setSuccess("")} />

      <section className="rounded-lg border bg-white shadow-sm">

        <div className="border-b px-5 py-4">
          <h2 className="font-bold">Proveedores registrados</h2>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50">
              <tr>
                <th className="px-5 py-3 text-left">ID</th>
                <th className="px-5 py-3 text-left">Nombre</th>
                <th className="px-5 py-3 text-left">Email</th>
                <th className="px-5 py-3 text-left">Teléfono</th>
                <th className="px-5 py-3 text-left">Acciones</th>
              </tr>
            </thead>

            <tbody>
              {isLoading &&
                Array.from({ length: 5 }).map((_, i) => (
                  <tr key={i}>
                    <td colSpan={5} className="px-5 py-4">
                      <div className="h-4 w-full animate-pulse rounded bg-slate-200" />
                    </td>
                  </tr>
                ))}

              {!isLoading &&
                sortedProveedores.map((proveedor) => (
                  <tr key={proveedor.id} className="border-t">
                    <td className="px-5 py-4 text-slate-500">#{proveedor.id}</td>
                    <td className="px-5 py-4 font-medium">{proveedor.nombre}</td>
                    <td className="px-5 py-4 text-slate-600">{proveedor.email}</td>
                    <td className="px-5 py-4 text-slate-600">{proveedor.telefono}</td>
                    <td className="px-5 py-4">
                      <div className="flex gap-2">
                        <button
                          className="rounded-lg border p-2 hover:bg-slate-50"
                          onClick={() => startEdit(proveedor)}
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          className="rounded-lg border border-red-200 p-2 text-red-600 hover:bg-red-50"
                          onClick={() => handleDelete(proveedor)}
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}

              {!isLoading && sortedProveedores.length === 0 && (
                <tr>
                  <td colSpan={5} className="px-5 py-10 text-center text-slate-400">
                    No hay proveedores registrados.
                  </td>
                </tr>
              )}
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
                      <Truck size={20} />
                    </div>
                    <div>
                      <h2 className="text-xl font-bold">
                        {isEditing ? "Editar proveedor" : "Nuevo proveedor"}
                      </h2>
                      <p className="text-sm text-slate-500">Datos del proveedor</p>
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
                      required
                      className="w-full rounded-lg border px-4 py-3"
                      placeholder="Ej: Distribuidora XYZ"
                    />
                  </div>

                  <div>
                    <label className="mb-2 block text-sm font-semibold">
                      Email
                    </label>
                    <input
                      name="email"
                      type="email"
                      value={form.email}
                      onChange={handleInputChange}
                      required
                      className="w-full rounded-lg border px-4 py-3"
                      placeholder="Ej: contacto@distribuidora.com"
                    />
                  </div>

                  <div>
                    <label className="mb-2 block text-sm font-semibold">
                      Teléfono
                    </label>
                    <input
                      name="telefono"
                      value={form.telefono}
                      onChange={handleInputChange}
                      required
                      className={`w-full rounded-lg border px-4 py-3 ${telefonoError ? "border-red-400 focus:outline-red-400" : ""}`}
                      placeholder="Ej: +503 7000-0000"
                    />
                    {telefonoError && (
                      <p className="mt-1 text-xs text-red-500">{telefonoError}</p>
                    )}
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
                    disabled={isSaving || Boolean(telefonoError)}
                    className="flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-white disabled:opacity-60"
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
