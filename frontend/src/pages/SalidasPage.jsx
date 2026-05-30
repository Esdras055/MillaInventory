import {
  Edit,
  Loader2,
  PackageX,
  Plus,
  RefreshCw,
  Save,
  Trash2,
  X,
} from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";
import {
  createSalida,
  deleteSalida,
  getBodegas,
  getProductos,
  getSalidas,
  updateSalida,
} from "../api/salidasApi";

function today() {
  return new Date().toISOString().slice(0, 10);
}

const initialForm = {
  bodegaId: "",
  cantidad: "",
  fecha: today(),
  productoId: "",
};

async function fetchSalidasData() {
  const [salidasResponse, productosResponse, bodegasResponse] = await Promise.all([
    getSalidas(),
    getProductos(),
    getBodegas(),
  ]);

  return {
    bodegas: bodegasResponse.data,
    productos: productosResponse.data,
    salidas: salidasResponse.data,
  };
}

function getErrorMessage(error, fallback) {
  return error.response?.data?.message || error.response?.data?.error || fallback;
}

function SalidasPage() {
  const [bodegas, setBodegas] = useState([]);
  const [editingSalida, setEditingSalida] = useState(null);
  const [error, setError] = useState("");
  const [form, setForm] = useState(initialForm);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [productos, setProductos] = useState([]);
  const [salidas, setSalidas] = useState([]);
  const [success, setSuccess] = useState("");

  const isEditing = Boolean(editingSalida);

  const sortedSalidas = useMemo(
    () =>
      [...salidas].sort((a, b) => {
        const dateDiff = new Date(b.fecha) - new Date(a.fecha);
        return dateDiff || b.id - a.id;
      }),
    [salidas],
  );

  async function loadData({ clearMessages = true } = {}) {
    if (clearMessages) {
      setError("");
      setSuccess("");
    }

    setIsLoading(true);

    try {
      const data = await fetchSalidasData();
      setSalidas(data.salidas);
      setProductos(data.productos);
      setBodegas(data.bodegas);
    } catch (loadError) {
      setError(getErrorMessage(loadError, "No se pudieron cargar las salidas."));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let ignore = false;

    fetchSalidasData()
      .then((data) => {
        if (!ignore) {
          setSalidas(data.salidas);
          setProductos(data.productos);
          setBodegas(data.bodegas);
        }
      })
      .catch((loadError) => {
        if (!ignore) {
          setError(getErrorMessage(loadError, "No se pudieron cargar las salidas."));
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

  function closeModal() {
    setEditingSalida(null);
    setForm(initialForm);
    setIsModalOpen(false);
    setError("");
  }

  function openCreateModal() {
    setEditingSalida(null);
    setForm(initialForm);
    setError("");
    setSuccess("");
    setIsModalOpen(true);
  }

  function startEdit(salida) {
    setEditingSalida(salida);
    setForm({
      bodegaId: String(salida.bodegaId),
      cantidad: String(salida.cantidad),
      fecha: salida.fecha,
      productoId: String(salida.productoId),
    });
    setError("");
    setSuccess("");
    setIsModalOpen(true);
  }

  function handleInputChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    if (!form.productoId || !form.bodegaId || !form.fecha || !form.cantidad) {
      setError("Producto, bodega, fecha y cantidad son requeridos.");
      return;
    }

    const cantidad = Number(form.cantidad);

    if (!Number.isInteger(cantidad) || cantidad <= 0) {
      setError("La cantidad debe ser un numero entero mayor que cero.");
      return;
    }

    const payload = {
      bodegaId: Number(form.bodegaId),
      cantidad,
      fecha: form.fecha,
      productoId: Number(form.productoId),
    };

    setIsSaving(true);

    try {
      if (isEditing) {
        await updateSalida(editingSalida.id, payload);
        setSuccess("Salida actualizada correctamente.");
      } else {
        await createSalida(payload);
        setSuccess("Salida registrada correctamente.");
      }

      setEditingSalida(null);
      setForm(initialForm);
      setIsModalOpen(false);
      await loadData({ clearMessages: false });
    } catch (saveError) {
      setError(getErrorMessage(saveError, "No se pudo guardar la salida."));
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete(salida) {
    const result = await Swal.fire({
      cancelButtonColor: "#64748B",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#DC2626",
      confirmButtonText: "Eliminar",
      icon: "warning",
      showCancelButton: true,
      text: "La cantidad sera devuelta al stock de la bodega",
      title: `¿Eliminar salida #${salida.id}?`,
    });

    if (!result.isConfirmed) {
      return;
    }

    setError("");
    setSuccess("");

    try {
      await deleteSalida(salida.id);
      await loadData({ clearMessages: false });
      await Swal.fire({
        confirmButtonColor: "#059669",
        confirmButtonText: "Aceptar",
        icon: "success",
        text: "La salida se elimino exitosamente.",
        title: "Salida eliminada",
      });
    } catch (deleteError) {
      const message = getErrorMessage(deleteError, "No se pudo eliminar la salida.");
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

  return (
    <section>
      <div className="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
            Inventario
          </p>
          <h1 className="mt-2 text-2xl font-bold text-slate-950">Salidas</h1>
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
            Nueva salida
          </button>
        </div>
      </div>

      {error && (
        <div className="mb-5 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      {success && (
        <div className="mb-5 rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
          {success}
        </div>
      )}

      <section className="rounded-lg border border-slate-200 bg-white shadow-sm">
        <div className="border-b border-slate-200 px-5 py-4">
          <h2 className="text-base font-bold text-slate-950">Salidas registradas</h2>
          <p className="mt-1 text-sm text-slate-500">
            {salidas.length} movimientos de salida
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full min-w-[860px] text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase text-slate-500">
              <tr>
                <th className="px-5 py-3 font-semibold">ID</th>
                <th className="px-5 py-3 font-semibold">Fecha</th>
                <th className="px-5 py-3 font-semibold">Producto</th>
                <th className="px-5 py-3 font-semibold">Bodega</th>
                <th className="px-5 py-3 font-semibold">Cantidad</th>
                <th className="px-5 py-3 font-semibold">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {isLoading &&
                Array.from({ length: 5 }).map((_, index) => (
                  <tr key={index}>
                    {Array.from({ length: 6 }).map((__, cellIndex) => (
                      <td className="px-5 py-4" key={cellIndex}>
                        <div className="h-4 w-24 animate-pulse rounded bg-slate-200" />
                      </td>
                    ))}
                  </tr>
                ))}

              {!isLoading &&
                sortedSalidas.map((salida) => (
                  <tr className="hover:bg-slate-50" key={salida.id}>
                    <td className="px-5 py-4 font-medium text-slate-800">
                      #{salida.id}
                    </td>
                    <td className="px-5 py-4 text-slate-600">{salida.fecha}</td>
                    <td className="px-5 py-4 text-slate-600">
                      {salida.productoNombre}
                    </td>
                    <td className="px-5 py-4 text-slate-600">
                      {salida.bodegaNombre}
                    </td>
                    <td className="px-5 py-4">
                      <span className="inline-flex rounded-full bg-rose-50 px-2.5 py-1 text-xs font-semibold text-rose-700">
                        {salida.cantidad}
                      </span>
                    </td>
                    <td className="px-5 py-4">
                      <div className="flex gap-2">
                        <button
                          className="rounded-lg border border-slate-300 p-2 text-slate-600 hover:bg-slate-100"
                          onClick={() => startEdit(salida)}
                          title="Editar salida"
                          type="button"
                        >
                          <Edit size={17} />
                        </button>
                        <button
                          className="rounded-lg border border-red-200 p-2 text-red-600 hover:bg-red-50"
                          onClick={() => handleDelete(salida)}
                          title="Eliminar salida"
                          type="button"
                        >
                          <Trash2 size={17} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}

              {!isLoading && salidas.length === 0 && (
                <tr>
                  <td className="px-5 py-8 text-center text-slate-500" colSpan={6}>
                    No hay salidas registradas.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4 py-6">
          <div className="max-h-[92vh] w-full max-w-2xl overflow-y-auto rounded-lg border border-slate-200 bg-white shadow-xl">
            <form onSubmit={handleSubmit}>
              <div className="sticky top-0 z-10 flex items-center justify-between gap-3 border-b border-slate-200 bg-white px-5 py-4">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-rose-50 text-rose-700">
                    <PackageX size={21} />
                  </div>
                  <div>
                    <h2 className="text-base font-bold text-slate-950">
                      {isEditing ? "Editar salida" : "Registrar salida"}
                    </h2>
                    <p className="mt-1 text-sm text-slate-500">
                      Registra productos que salen de una bodega.
                    </p>
                  </div>
                </div>

                <button
                  className="rounded-lg p-2 text-slate-500 hover:bg-slate-100"
                  onClick={closeModal}
                  type="button"
                >
                  <X size={20} />
                </button>
              </div>

              <div className="grid gap-4 p-5 sm:grid-cols-2">
                <div>
                  <label
                    className="text-sm font-medium text-slate-700"
                    htmlFor="productoId"
                  >
                    Producto
                  </label>
                  <select
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="productoId"
                    name="productoId"
                    onChange={handleInputChange}
                    value={form.productoId}
                  >
                    <option value="">Seleccionar producto</option>
                    {productos.map((producto) => (
                      <option key={producto.id} value={producto.id}>
                        {producto.nombre}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label
                    className="text-sm font-medium text-slate-700"
                    htmlFor="bodegaId"
                  >
                    Bodega
                  </label>
                  <select
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="bodegaId"
                    name="bodegaId"
                    onChange={handleInputChange}
                    value={form.bodegaId}
                  >
                    <option value="">Seleccionar bodega</option>
                    {bodegas.map((bodega) => (
                      <option key={bodega.id} value={bodega.id}>
                        {bodega.nombre}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="text-sm font-medium text-slate-700" htmlFor="fecha">
                    Fecha
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="fecha"
                    name="fecha"
                    onChange={handleInputChange}
                    type="date"
                    value={form.fecha}
                  />
                </div>

                <div>
                  <label
                    className="text-sm font-medium text-slate-700"
                    htmlFor="cantidad"
                  >
                    Cantidad
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="cantidad"
                    min="1"
                    name="cantidad"
                    onChange={handleInputChange}
                    type="number"
                    value={form.cantidad}
                  />
                </div>
              </div>

              <div className="sticky bottom-0 flex flex-col gap-2 border-t border-slate-200 bg-white px-5 py-4 sm:flex-row sm:justify-end">
                <button
                  className="flex min-h-11 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-4 text-sm font-semibold text-slate-700 hover:bg-slate-50"
                  onClick={closeModal}
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
                  {isEditing ? "Guardar" : "Registrar"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}

export default SalidasPage;
