import {
  Building2,
  Edit,
  Eye,
  Loader2,
  Plus,
  RefreshCw,
  Save,
  Trash2,
  X,
} from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";
import {
  createBodega,
  deleteBodega,
  getBodegas,
  getStockPorBodega,
  getUbicaciones,
  updateBodega,
} from "../api/bodegasApi";
import AlertMessage from "../components/AlertMessage";

const initialForm = {
  nombre: "",
  ubicacionId: "",
};

async function fetchBodegasData() {
  const [bodegasResponse, ubicacionesResponse] = await Promise.all([
    getBodegas(),
    getUbicaciones(),
  ]);

  return {
    bodegas: bodegasResponse.data,
    ubicaciones: ubicacionesResponse.data,
  };
}

function getErrorMessage(error, fallback) {
  return error.response?.data?.message || error.response?.data?.error || fallback;
}

function BodegasPage() {
  const [bodegas, setBodegas] = useState([]);
  const [editingBodega, setEditingBodega] = useState(null);
  const [error, setError] = useState("");
  const [form, setForm] = useState(initialForm);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [isStockLoading, setIsStockLoading] = useState(false);
  const [isStockModalOpen, setIsStockModalOpen] = useState(false);
  const [isFormModalOpen, setIsFormModalOpen] = useState(false);
  const [selectedBodega, setSelectedBodega] = useState(null);
  const [stockItems, setStockItems] = useState([]);
  const [success, setSuccess] = useState("");
  const [ubicaciones, setUbicaciones] = useState([]);

  const isEditing = Boolean(editingBodega);

  const sortedBodegas = useMemo(
    () => [...bodegas].sort((a, b) => a.nombre.localeCompare(b.nombre)),
    [bodegas],
  );

  async function loadData({ clearMessages = true } = {}) {
    if (clearMessages) {
      setError("");
      setSuccess("");
    }

    setIsLoading(true);

    try {
      const data = await fetchBodegasData();
      setBodegas(data.bodegas);
      setUbicaciones(data.ubicaciones);
    } catch (loadError) {
      setError(getErrorMessage(loadError, "No se pudieron cargar las bodegas."));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let ignore = false;

    fetchBodegasData()
      .then((data) => {
        if (!ignore) {
          setBodegas(data.bodegas);
          setUbicaciones(data.ubicaciones);
        }
      })
      .catch((loadError) => {
        if (!ignore) {
          setError(getErrorMessage(loadError, "No se pudieron cargar las bodegas."));
        }
      })
      .finally(() => {
        if (!ignore) {
          setIsLoading(false);
        }
      });

    return () => {
      ignore = true;
    };
  }, []);

  function handleInputChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  function closeFormModal() {
    setEditingBodega(null);
    setForm(initialForm);
    setError("");
    setIsFormModalOpen(false);
  }

  function openCreateModal() {
    setEditingBodega(null);
    setForm(initialForm);
    setError("");
    setSuccess("");
    setIsFormModalOpen(true);
  }

  function startEdit(bodega) {
    setEditingBodega(bodega);
    setForm({
      nombre: bodega.nombre || "",
      ubicacionId: String(bodega.ubicacionId),
    });
    setError("");
    setSuccess("");
    setIsFormModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    if (!form.nombre.trim() || !form.ubicacionId) {
      setError("Nombre y ubicacion son requeridos.");
      return;
    }

    const payload = {
      nombre: form.nombre.trim(),
      ubicacionId: Number(form.ubicacionId),
    };

    setIsSaving(true);

    try {
      if (isEditing) {
        await updateBodega(editingBodega.id, payload);
        setSuccess("Bodega actualizada correctamente.");
      } else {
        await createBodega(payload);
        setSuccess("Bodega creada correctamente.");
      }

      setEditingBodega(null);
      setForm(initialForm);
      setIsFormModalOpen(false);
      await loadData({ clearMessages: false });
    } catch (saveError) {
      setError(getErrorMessage(saveError, "No se pudo guardar la bodega."));
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete(bodega) {
    const result = await Swal.fire({
      cancelButtonColor: "#64748B",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#DC2626",
      confirmButtonText: "Eliminar",
      icon: "warning",
      showCancelButton: true,
      text: "Si tiene productos o movimientos asociados, el backend podria impedir eliminarla.",
      title: `Eliminar bodega ${bodega.nombre}?`,
    });

    if (!result.isConfirmed) {
      return;
    }

    setError("");
    setSuccess("");

    try {
      await deleteBodega(bodega.id);
      await loadData({ clearMessages: false });
      await Swal.fire({
        confirmButtonColor: "#059669",
        confirmButtonText: "Aceptar",
        icon: "success",
        text: "La bodega se elimino exitosamente.",
        title: "Bodega eliminada",
      });
    } catch (deleteError) {
      const message = getErrorMessage(deleteError, "No se pudo eliminar la bodega.");
      setError(message);
      await Swal.fire({
        confirmButtonColor: "#DC2626",
        confirmButtonText: "Aceptar",
        icon: "error",
        text: message,
        title: "No se pudo eliminar",
      });
    }
  }

  async function openStockModal(bodega) {
    setSelectedBodega(bodega);
    setStockItems([]);
    setIsStockModalOpen(true);
    setIsStockLoading(true);

    try {
      const { data } = await getStockPorBodega(bodega.id);
      setStockItems(data);
    } catch (stockError) {
      setError(getErrorMessage(stockError, "No se pudo cargar el stock de la bodega."));
    } finally {
      setIsStockLoading(false);
    }
  }

  function closeStockModal() {
    setSelectedBodega(null);
    setStockItems([]);
    setIsStockModalOpen(false);
  }

  return (
    <section>
      <div className="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
            Inventario
          </p>
          <h1 className="mt-2 text-2xl font-bold text-slate-950">Bodegas</h1>
        </div>

        <div className="flex flex-col gap-2 sm:flex-row">
          <button
            className="flex min-h-10 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-semibold text-slate-700 hover:bg-slate-50"
            onClick={loadData}
            type="button"
          >
            <RefreshCw size={17} />
            Actualizar
          </button>
          <button
            className="flex min-h-10 items-center justify-center gap-2 rounded-lg bg-emerald-600 px-3 text-sm font-semibold text-white hover:bg-emerald-700"
            onClick={openCreateModal}
            type="button"
          >
            <Plus size={17} />
            Nueva bodega
          </button>
        </div>
      </div>

      <AlertMessage message={error} onClose={() => setError("")} type="error" />
      <AlertMessage
        autoCloseMs={4000}
        message={success}
        onClose={() => setSuccess("")}
        type="success"
      />

      <section className="rounded-lg border border-slate-200 bg-white shadow-sm">
        <div className="border-b border-slate-200 px-5 py-4">
          <h2 className="text-base font-bold text-slate-950">Bodegas registradas</h2>
          <p className="mt-1 text-sm text-slate-500">
            {bodegas.length} bodegas disponibles
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full min-w-[760px] text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase text-slate-500">
              <tr>
                <th className="px-5 py-3 font-semibold">ID</th>
                <th className="px-5 py-3 font-semibold">Nombre</th>
                <th className="px-5 py-3 font-semibold">Municipio</th>
                <th className="px-5 py-3 font-semibold">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {isLoading &&
                Array.from({ length: 5 }).map((_, index) => (
                  <tr key={index}>
                    {Array.from({ length: 4 }).map((__, cellIndex) => (
                      <td className="px-5 py-4" key={cellIndex}>
                        <div className="h-4 w-24 animate-pulse rounded bg-slate-200" />
                      </td>
                    ))}
                  </tr>
                ))}

              {!isLoading &&
                sortedBodegas.map((bodega) => (
                  <tr className="hover:bg-slate-50" key={bodega.id}>
                    <td className="px-5 py-4 font-medium text-slate-800">
                      #{bodega.id}
                    </td>
                    <td className="px-5 py-4 text-slate-600">{bodega.nombre}</td>
                    <td className="px-5 py-4 text-slate-600">
                      {bodega.municipio || "-"}
                    </td>
                    <td className="px-5 py-4">
                      <div className="flex gap-2">
                        <button
                          className="rounded-lg border border-emerald-200 p-2 text-emerald-700 hover:bg-emerald-50"
                          onClick={() => openStockModal(bodega)}
                          title="Ver productos"
                          type="button"
                        >
                          <Eye size={17} />
                        </button>
                        <button
                          className="rounded-lg border border-slate-300 p-2 text-slate-600 hover:bg-slate-100"
                          onClick={() => startEdit(bodega)}
                          title="Editar bodega"
                          type="button"
                        >
                          <Edit size={17} />
                        </button>
                        <button
                          className="rounded-lg border border-red-200 p-2 text-red-600 hover:bg-red-50"
                          onClick={() => handleDelete(bodega)}
                          title="Eliminar bodega"
                          type="button"
                        >
                          <Trash2 size={17} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}

              {!isLoading && bodegas.length === 0 && (
                <tr>
                  <td className="px-5 py-8 text-center text-slate-500" colSpan={4}>
                    No hay bodegas registradas.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {isFormModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4 py-6">
          <div className="w-full max-w-xl rounded-lg border border-slate-200 bg-white shadow-xl">
            <form onSubmit={handleSubmit}>
              <div className="flex items-center justify-between gap-3 border-b border-slate-200 px-5 py-4">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-50 text-emerald-700">
                    <Building2 size={21} />
                  </div>
                  <div>
                    <h2 className="text-base font-bold text-slate-950">
                      {isEditing ? "Editar bodega" : "Crear bodega"}
                    </h2>
                    <p className="mt-1 text-sm text-slate-500">
                      Define el nombre y la ubicacion de la bodega.
                    </p>
                  </div>
                </div>

                <button
                  className="rounded-lg p-2 text-slate-500 hover:bg-slate-100"
                  onClick={closeFormModal}
                  type="button"
                >
                  <X size={20} />
                </button>
              </div>

              <div className="space-y-4 p-5">
                <div>
                  <label className="text-sm font-medium text-slate-700" htmlFor="nombre">
                    Nombre
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="nombre"
                    name="nombre"
                    onChange={handleInputChange}
                    type="text"
                    value={form.nombre}
                  />
                </div>

                <div>
                  <label
                    className="text-sm font-medium text-slate-700"
                    htmlFor="ubicacionId"
                  >
                    Ubicacion
                  </label>
                  <select
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="ubicacionId"
                    name="ubicacionId"
                    onChange={handleInputChange}
                    value={form.ubicacionId}
                  >
                    <option value="">Seleccionar ubicacion</option>
                    {ubicaciones.map((ubicacion) => (
                      <option key={ubicacion.id} value={ubicacion.id}>
                        {ubicacion.municipio}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="flex flex-col gap-2 border-t border-slate-200 px-5 py-4 sm:flex-row sm:justify-end">
                <button
                  className="flex min-h-11 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-4 text-sm font-semibold text-slate-700 hover:bg-slate-50"
                  onClick={closeFormModal}
                  type="button"
                >
                  <X size={18} />
                  Cancelar
                </button>
                <button
                  className="flex min-h-11 items-center justify-center gap-2 rounded-lg bg-emerald-600 px-4 text-sm font-semibold text-white hover:bg-emerald-700 disabled:cursor-not-allowed disabled:bg-slate-400"
                  disabled={isSaving}
                  type="submit"
                >
                  {isSaving ? (
                    <Loader2 className="animate-spin" size={18} />
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

      {isStockModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4 py-6">
          <div className="max-h-[92vh] w-full max-w-3xl overflow-y-auto rounded-lg border border-slate-200 bg-white shadow-xl">
            <div className="sticky top-0 z-10 flex items-center justify-between gap-3 border-b border-slate-200 bg-white px-5 py-4">
              <div>
                <h2 className="text-base font-bold text-slate-950">
                  Productos en {selectedBodega?.nombre}
                </h2>
                <p className="mt-1 text-sm text-slate-500">
                  Stock actual registrado para esta bodega.
                </p>
              </div>
              <button
                className="rounded-lg p-2 text-slate-500 hover:bg-slate-100"
                onClick={closeStockModal}
                type="button"
              >
                <X size={20} />
              </button>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full min-w-[560px] text-left text-sm">
                <thead className="bg-slate-50 text-xs uppercase text-slate-500">
                  <tr>
                    <th className="px-5 py-3 font-semibold">Producto</th>
                    <th className="px-5 py-3 font-semibold">Stock actual</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {isStockLoading &&
                    Array.from({ length: 4 }).map((_, index) => (
                      <tr key={index}>
                        <td className="px-5 py-4">
                          <div className="h-4 w-40 animate-pulse rounded bg-slate-200" />
                        </td>
                        <td className="px-5 py-4">
                          <div className="h-4 w-20 animate-pulse rounded bg-slate-200" />
                        </td>
                      </tr>
                    ))}

                  {!isStockLoading &&
                    stockItems.map((item) => (
                      <tr className="hover:bg-slate-50" key={item.productoId}>
                        <td className="px-5 py-4 font-medium text-slate-800">
                          {item.producto}
                        </td>
                        <td className="px-5 py-4">
                          <span className="inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700">
                            {item.stockActual}
                          </span>
                        </td>
                      </tr>
                    ))}

                  {!isStockLoading && stockItems.length === 0 && (
                    <tr>
                      <td className="px-5 py-8 text-center text-slate-500" colSpan={2}>
                        Esta bodega no tiene productos registrados.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </section>
  );
}

export default BodegasPage;
