import { Edit, Plus, Save, Trash2, Truck, X } from "lucide-react";
import { useEffect, useState } from "react";
import AlertMessage from "../components/AlertMessage";
import {
  createProveedor,
  deleteProveedor,
  getProveedores,
  updateProveedor,
} from "../api/proveedoresApi";

const initialForm = {
  nombre: "",
  telefono: "",
  correo: "",
};

function ProveedoresPage() {
  const [proveedores, setProveedores] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  async function loadProveedores() {
    setError("");
    setIsLoading(true);

    try {
      const response = await getProveedores();
      setProveedores(response.data);
    } catch (error) {
      console.error("Error al cargar proveedores:", error.response?.data || error);
      setError("No se pudieron cargar los proveedores.");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    loadProveedores();
  }, []);

  function getProveedorId(proveedor) {
  return (
    proveedor.proveedorId ||
    proveedor.idProveedor ||
    proveedor.id ||
    proveedor.proveedor_id
  );
}

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  }

  function limpiarFormulario() {
    setForm(initialForm);
    setEditingId(null);
  }

  function editarProveedor(proveedor) {
    setEditingId(getProveedorId(proveedor));

    setForm({
      nombre: proveedor.nombre || "",
      telefono: proveedor.telefono || "",
      correo: proveedor.email || proveedor.correo || "",
    });
  }

  async function guardarProveedor(event) {
    event.preventDefault();

    if (!form.nombre.trim()) {
      setError("El nombre del proveedor es obligatorio.");
      return;
    }

    if (!form.telefono.trim()) {
      setError("El teléfono del proveedor es obligatorio.");
      return;
    }

    if (!form.correo.trim()) {
      setError("El correo del proveedor es obligatorio.");
      return;
    }

    setError("");
    setSuccess("");
    setIsSaving(true);

const telefonoLimpio = form.telefono.replace(/\D/g, "");

if (telefonoLimpio.length !== 8) {
  setError("El teléfono debe tener 8 números. Ejemplo: 77984964");
  setIsSaving(false);
  return;
}

const proveedorData = {
  nombre: form.nombre.trim(),
  telefono: telefonoLimpio,
  email: form.correo.trim(),
};

    try {
      if (editingId) {
        await updateProveedor(editingId, proveedorData);
        setSuccess("Proveedor actualizado correctamente.");
      } else {
        await createProveedor(proveedorData);
        setSuccess("Proveedor registrado correctamente.");
      }

      limpiarFormulario();
      await loadProveedores();
    } catch (error) {
      console.error("Error al guardar proveedor:", error.response?.data || error);

      setError(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "No se pudo guardar el proveedor.",
      );
    } finally {
      setIsSaving(false);
    }
  }

  async function eliminarProveedor(id) {
    const confirmar = window.confirm(
      "¿Seguro que deseas eliminar este proveedor?",
    );

    if (!confirmar) return;

    setError("");
    setSuccess("");

    try {
      await deleteProveedor(id);
      setSuccess("Proveedor eliminado correctamente.");
      await loadProveedores();
    } catch (error) {
      console.error("Error al eliminar proveedor:", error.response?.data || error);

      setError(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "No se pudo eliminar el proveedor.",
      );
    }
  }

  return (
    <section>
      <div className="mb-6">
        <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
          MILLA INVENTORY
        </p>

        <h1 className="mt-2 text-2xl font-bold text-slate-950">
          Proveedores
        </h1>
      </div>

      <AlertMessage message={error} onClose={() => setError("")} type="error" />
      <AlertMessage
        message={success}
        onClose={() => setSuccess("")}
        type="success"
      />

      <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <p className="text-sm text-slate-600">
          Gestión de proveedores del inventario.
        </p>
      </div>

      <div className="mt-6 grid gap-6 xl:grid-cols-[420px_minmax(0,1fr)]">
        <form
          onSubmit={guardarProveedor}
          className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm"
        >
          <div className="mb-5 flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-50 text-emerald-700">
              <Truck size={22} />
            </div>

            <div>
              <h2 className="text-lg font-semibold text-slate-900">
                {editingId ? "Editar proveedor" : "Nuevo proveedor"}
              </h2>
              <p className="text-sm text-slate-500">
                Completa la información del proveedor.
              </p>
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <label className="mb-1 block text-sm font-medium text-slate-700">
                Nombre
              </label>
              <input
                name="nombre"
                value={form.nombre}
                onChange={handleChange}
                placeholder="Ej. Distribuidora Milla"
                className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-emerald-500 focus:ring-2 focus:ring-emerald-100"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium text-slate-700">
                Teléfono
              </label>
              <input
                name="telefono"
                value={form.telefono}
                onChange={handleChange}
                placeholder="Ej. 7777-7777"
                className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-emerald-500 focus:ring-2 focus:ring-emerald-100"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium text-slate-700">
                Correo
              </label>
              <input
                name="correo"
                type="email"
                value={form.correo}
                onChange={handleChange}
                placeholder="proveedor@email.com"
                className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-emerald-500 focus:ring-2 focus:ring-emerald-100"
              />
            </div>
          </div>

          <div className="mt-5 flex gap-3">
            <button
              type="submit"
              disabled={isSaving}
              className="inline-flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {editingId ? <Save size={17} /> : <Plus size={17} />}
              {isSaving
                ? "Guardando..."
                : editingId
                  ? "Actualizar"
                  : "Guardar"}
            </button>

            {editingId && (
              <button
                type="button"
                onClick={limpiarFormulario}
                className="inline-flex items-center gap-2 rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-100"
              >
                <X size={17} />
                Cancelar
              </button>
            )}
          </div>
        </form>

        <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="mb-5">
            <h2 className="text-lg font-semibold text-slate-900">
              Lista de proveedores
            </h2>
            <p className="text-sm text-slate-500">
              Proveedores registrados en el sistema.
            </p>
          </div>

          {isLoading ? (
            <p className="text-sm text-slate-500">Cargando proveedores...</p>
          ) : proveedores.length === 0 ? (
            <div className="rounded-lg border border-dashed border-slate-300 p-8 text-center">
              <Truck className="mx-auto mb-3 text-slate-400" size={42} />
              <p className="font-medium text-slate-700">
                No hay proveedores registrados.
              </p>
              <p className="mt-1 text-sm text-slate-500">
                Agrega un proveedor para comenzar.
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                  <tr>
                    <th className="px-4 py-3 text-left text-xs font-semibold uppercase text-slate-500">
                      Nombre
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-semibold uppercase text-slate-500">
                      Teléfono
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-semibold uppercase text-slate-500">
                      Correo
                    </th>
                    <th className="px-4 py-3 text-right text-xs font-semibold uppercase text-slate-500">
                      Acciones
                    </th>
                  </tr>
                </thead>

                <tbody className="divide-y divide-slate-200 bg-white">
                  {proveedores.map((proveedor) => {
                    const id = getProveedorId(proveedor);

                    return (
                      <tr key={id}>
                        <td className="px-4 py-3 text-sm font-medium text-slate-900">
                          {proveedor.nombre}
                        </td>

                        <td className="px-4 py-3 text-sm text-slate-700">
                          {proveedor.telefono || "Sin teléfono"}
                        </td>

                        <td className="px-4 py-3 text-sm text-slate-700">
                          {proveedor.email || proveedor.correo || "Sin correo"}
                        </td>

                        <td className="px-4 py-3 text-right">
                          <button
                            type="button"
                            onClick={() => editarProveedor(proveedor)}
                            className="mr-2 inline-flex items-center gap-1 rounded-lg border border-slate-300 px-3 py-1.5 text-sm font-medium text-slate-700 hover:bg-slate-100"
                          >
                            <Edit size={15} />
                        
                          </button>

                          <button
                            type="button"
                            onClick={() => eliminarProveedor(id)}
                            className="inline-flex items-center gap-1 rounded-lg border border-rose-300 px-3 py-1.5 text-sm font-medium text-rose-600 hover:bg-rose-50"
                          >
                            <Trash2 size={15} />
                        
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </section>
  );
}

export default ProveedoresPage;