import {
  Check,
  Edit,
  Loader2,
  Plus,
  RefreshCw,
  Save,
  Trash2,
  UserCog,
  X,
} from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";
import {
  createUsuario,
  deleteUsuario,
  getRoles,
  getUsuarioRoles,
  getUsuarios,
  replaceUsuarioRoles,
  updateUsuario,
} from "../api/usuariosApi";
import AlertMessage from "../components/AlertMessage";
import useAuth from "../hooks/useAuth";

const emptyForm = {
  accountNonLocked: true,
  enabled: true,
  name: "",
  password: "",
  roleIds: [],
  username: "",
};

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

async function fetchUserManagementData() {
  const [usuariosResponse, rolesResponse] = await Promise.all([
    getUsuarios(),
    getRoles(),
  ]);

  const usuariosRoles = await Promise.all(
    usuariosResponse.data.map(async (usuario) => {
      const { data } = await getUsuarioRoles(usuario.id);
      return { ...usuario, roles: data };
    }),
  );

  return {
    roles: rolesResponse.data,
    usuarios: usuariosRoles,
  };
}

function getErrorMessage(error, fallback) {
  return error.response?.data?.message || error.response?.data?.error || fallback;
}

function UsuariosPage() {
  const [editingUser, setEditingUser] = useState(null);
  const [error, setError] = useState("");
  const [form, setForm] = useState(emptyForm);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [userLoginFlag, setUserLoginFlag] = useState(false);
  const [roles, setRoles] = useState([]);
  const [success, setSuccess] = useState("");
  const [usuarios, setUsuarios] = useState([]);
  const { user } = useAuth();

  const isEditing = Boolean(editingUser);

  const usuariosConRoles = useMemo(
    () =>
      usuarios.map((usuario) => ({
        ...usuario,
        rolesText:
          usuario.roles?.map((rol) => rol.nombre.replace("ROLE_", "")).join(", ") ||
          "Sin roles",
      })),
    [usuarios],
  );

  async function loadData({ clearMessages = true } = {}) {
    if (clearMessages) {
      setError("");
      setSuccess("");
    }
    setIsLoading(true);

    try {
      const data = await fetchUserManagementData();
      setUsuarios(data.usuarios);
      setRoles(data.roles);
    } catch (loadError) {
      setError(
        getErrorMessage(
          loadError,
          "No se pudo cargar la gestion de usuarios. Verifica que tu usuario tenga rol ADMIN.",
        ),
      );
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let ignore = false;

    fetchUserManagementData()
      .then((data) => {
        if (!ignore) {
          setUsuarios(data.usuarios);
          setRoles(data.roles);
        }
      })
      .catch((loadError) => {
        if (!ignore) {
          setError(
            getErrorMessage(
              loadError,
              "No se pudo cargar la gestion de usuarios. Verifica que tu usuario tenga rol ADMIN.",
            ),
          );
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

  function resetForm() {
    setEditingUser(null);
    setUserLoginFlag(false);
    setForm(emptyForm);
    setIsModalOpen(false);
    setError("");
    setSuccess("");
  }

  function openCreateModal() {
    setEditingUser(null);
    setUserLoginFlag(false);
    setForm(emptyForm);
    setError("");
    setSuccess("");
    setIsModalOpen(true);
  }

  function handleInputChange(event) {
    const { checked, name, type, value } = event.target;
    setForm((current) => ({
      ...current,
      [name]: type === "checkbox" ? checked : value,
    }));
  }

  function handleRoleChange(roleId) {
    setForm((current) => {
      const hasRole = current.roleIds.includes(roleId);

      return {
        ...current,
        roleIds: hasRole
          ? current.roleIds.filter((id) => id !== roleId)
          : [...current.roleIds, roleId],
      };
    });
  }

  function startEdit(usuario) {
    setEditingUser(usuario);

    setError("");
    setSuccess("");
    usuario.id === user.id ? setUserLoginFlag(true) : setUserLoginFlag(false);
    setForm({
      accountNonLocked: usuario.accountNonLocked,
      enabled: usuario.enabled,
      name: usuario.name || "",
      password: "",
      roleIds: usuario.roles?.map((rol) => rol.id) || [],
      username: usuario.username || "",
    });
    setIsModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    if (!form.name.trim() || !form.username.trim()) {
      setError("Nombre y usuario son requeridos.");
      return;
    }

    if (!emailRegex.test(form.username.trim())) {
      setError("El usuario debe ser un correo valido.");
      return;
    }

    if (!isEditing && !form.password.trim()) {
      setError("La contrasena es requerida para crear usuarios.");
      return;
    }

    if (form.roleIds.length === 0) {
      setError("Selecciona al menos un rol.");
      return;
    }

    setIsSaving(true);

    try {
      if (isEditing) {
        const payload = {
          accountNonLocked: form.accountNonLocked,
          enabled: form.enabled,
          name: form.name,
          username: form.username,
        };

        if (form.password.trim()) {
          payload.password = form.password;
        }

        await updateUsuario(editingUser.id, payload);
        await replaceUsuarioRoles(editingUser.id, form.roleIds);
        setSuccess("Usuario actualizado correctamente.");
      } else {
        const { data } = await createUsuario({
          name: form.name,
          password: form.password,
          roleIds: form.roleIds,
          username: form.username,
        });
        await replaceUsuarioRoles(data.id, form.roleIds);
        setSuccess("Usuario creado correctamente.");
      }

      setEditingUser(null);
      setForm(emptyForm);
      setIsModalOpen(false);
      await loadData({ clearMessages: false });
    } catch (saveError) {
      setError(getErrorMessage(saveError, "No se pudo guardar el usuario."));
    } finally {
      setIsSaving(false);
    }
  }

  function isLoginUser(usuario) {
    return usuario.id === user?.id;
  }

  async function handleDelete(usuario) {
    const result = await Swal.fire({
      cancelButtonColor: "#64748B",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#DC2626",
      confirmButtonText: "Eliminar",
      icon: "warning",
      showCancelButton: true,
      text: "Esta acción no se puede deshacer",
      title: `¿Eliminar a ${usuario.username}?`,
    });

    if (!result.isConfirmed) {
      return;
    }

    setError("");
    setSuccess("");

    try {
      await deleteUsuario(usuario.id);
      await loadData({ clearMessages: false });
      await Swal.fire({
        confirmButtonColor: "#059669",
        confirmButtonText: "Aceptar",
        icon: "success",
        text: "El usuario se elimino exitosamente.",
        title: "Usuario eliminado",
      });
    } catch (deleteError) {
      const message = getErrorMessage(deleteError, "No se pudo eliminar el usuario.");
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
            Administración
          </p>
          <h1 className="mt-2 text-2xl font-bold text-slate-950">
            Gestión de usuarios
          </h1>
        </div>

        <button
          className="flex min-h-10 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-semibold text-slate-700 hover:bg-slate-50"
          onClick={loadData}
          type="button"
        >
          <RefreshCw size={17} />
          Actualizar
        </button>
      </div>

      <AlertMessage message={error} onClose={() => setError("")} type="error" />
      <AlertMessage
        autoCloseMs={4000}
        message={success}
        onClose={() => setSuccess("")}
        type="success"
      />

      <div>
        <section className="rounded-lg border border-slate-200 bg-white shadow-sm">
          <div className="flex flex-col gap-4 border-b border-slate-200 px-5 py-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h2 className="text-base font-bold text-slate-950">Usuarios</h2>
              <p className="mt-1 text-sm text-slate-500">
                {usuarios.length} usuarios registrados
              </p>
            </div>

            <button
              className="flex min-h-10 items-center justify-center gap-2 rounded-lg bg-emerald-600 px-3 text-sm font-semibold text-white hover:bg-emerald-700"
              onClick={openCreateModal}
              type="button"
            >
              <Plus size={17} />
              Nuevo usuario
            </button>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full min-w-[860px] text-left text-sm">
              <thead className="bg-slate-50 text-xs uppercase text-slate-500">
                <tr>
                  <th className="px-5 py-3 font-semibold">Nombre</th>
                  <th className="px-5 py-3 font-semibold">Usuario</th>
                  <th className="px-5 py-3 font-semibold">Roles</th>
                  <th className="px-5 py-3 font-semibold">Estado</th>
                  <th className="px-5 py-3 font-semibold">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {isLoading &&
                  Array.from({ length: 5 }).map((_, index) => (
                    <tr key={index}>
                      {Array.from({ length: 5 }).map((__, cellIndex) => (
                        <td className="px-5 py-4" key={cellIndex}>
                          <div className="h-4 w-28 animate-pulse rounded bg-slate-200" />
                        </td>
                      ))}
                    </tr>
                  ))}

                {!isLoading &&
                  usuariosConRoles.map((usuario) => (
                    <tr className="hover:bg-slate-50" key={usuario.id}>
                      <td className="px-5 py-4 font-medium text-slate-800">
                        {usuario.name}
                      </td>
                      <td className="px-5 py-4 text-slate-600">{usuario.username}</td>
                      <td className="px-5 py-4 text-slate-600">{usuario.rolesText}</td>
                      <td className="px-5 py-4">
                        <div className="flex flex-wrap gap-2">
                          <span
                            className={`inline-flex items-center gap-1 rounded-full px-2.5 py-1 text-xs font-semibold ${
                              usuario.enabled
                                ? "bg-emerald-50 text-emerald-700"
                                : "bg-slate-100 text-slate-600"
                            }`}
                          >
                            {usuario.enabled ? <Check size={13} /> : <X size={13} />}
                            {usuario.enabled ? "Activo" : "Inactivo"}
                          </span>
                          {!usuario.accountNonLocked && (
                            <span className="inline-flex rounded-full bg-rose-50 px-2.5 py-1 text-xs font-semibold text-rose-700">
                              Bloqueado
                            </span>
                          )}
                        </div>
                      </td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          <button
                            className="rounded-lg border border-slate-300 p-2 text-slate-600 hover:bg-slate-100"
                            onClick={() => startEdit(usuario)}
                            title="Editar usuario"
                            type="button"
                          >
                            <Edit size={17} />
                          </button>
                          <button
                            disabled={isLoginUser(usuario)}
                            className={`rounded-lg border p-2 ${isLoginUser(usuario) ? "border-slate-300 text-slate-400" : "border-red-200 text-red-600 hover:bg-red-50"}`}
                            onClick={() => handleDelete(usuario)}
                            title="Eliminar usuario"
                            type="button"
                          >
                            <Trash2 size={17} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}

                {!isLoading && usuarios.length === 0 && (
                  <tr>
                    <td className="px-5 py-8 text-center text-slate-500" colSpan={5}>
                      No hay usuarios registrados.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4 py-6">
          <div className="max-h-[92vh] w-full max-w-2xl overflow-y-auto rounded-lg border border-slate-200 bg-white shadow-xl">
            <form onSubmit={handleSubmit}>
              <div className="sticky top-0 z-10 flex items-center justify-between gap-3 border-b border-slate-200 bg-white px-5 py-4">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-50 text-emerald-700">
                    <UserCog size={21} />
                  </div>
                  <div>
                    <h2 className="text-base font-bold text-slate-950">
                      {isEditing ? "Editar usuario" : "Crear usuario"}
                    </h2>
                    <p className="mt-1 text-sm text-slate-500">
                      Gestiona datos de acceso y roles.
                    </p>
                  </div>
                </div>

                <button
                  className="rounded-lg p-2 text-slate-500 hover:bg-slate-100"
                  onClick={resetForm}
                  type="button"
                >
                  <X size={20} />
                </button>
              </div>

              <div className="space-y-4 p-5">
                <div>
                  <label className="text-sm font-medium text-slate-700" htmlFor="name">
                    Nombre
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="name"
                    name="name"
                    onChange={handleInputChange}
                    type="text"
                    value={form.name}
                  />
                </div>

                <div>
                  <label className="text-sm font-medium text-slate-700" htmlFor="username">
                    Usuario
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    autoComplete="email"
                    id="username"
                    name="username"
                    onChange={handleInputChange}
                    type="email"
                    value={form.username}
                  />
                </div>

                <div>
                  <label className="text-sm font-medium text-slate-700" htmlFor="password">
                    Contraseña
                  </label>
                  <input
                    className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
                    id="password"
                    name="password"
                    onChange={handleInputChange}
                    placeholder={isEditing ? "Dejar vacia para no cambiar" : ""}
                    type="password"
                    value={form.password}
                  />
                </div>

                <div className="grid gap-3 sm:grid-cols-2">
                  <label className="flex min-h-11 items-center gap-2 rounded-lg border border-slate-200 px-3 text-sm font-medium text-slate-700">
                    <input
                      checked={form.enabled}
                      className="h-4 w-4 accent-emerald-600"
                      name="enabled"
                      onChange={handleInputChange}
                      type="checkbox"
                    />
                    Activo
                  </label>
                  <label className="flex min-h-11 items-center gap-2 rounded-lg border border-slate-200 px-3 text-sm font-medium text-slate-700">
                    <input
                      checked={form.accountNonLocked}
                      className="h-4 w-4 accent-emerald-600"
                      name="accountNonLocked"
                      onChange={handleInputChange}
                      type="checkbox"
                    />
                    No bloqueado
                  </label>
                </div>

                <div>
                  <p className="mb-2 text-sm font-medium text-slate-700">Roles</p>
                  <div className="grid gap-2 sm:grid-cols-2">
                    {roles.map((rol) => (
                      <label
                        className="flex min-h-11 items-center justify-between rounded-lg border border-slate-200 px-3 text-sm font-medium text-slate-700"
                        key={rol.id}
                      >
                        <span>{rol.nombre}</span>
                        <input
                          checked={form.roleIds.includes(rol.id)}
                          className="h-4 w-4 accent-emerald-600"
                          disabled={userLoginFlag && rol.nombre === "ROLE_ADMIN"}
                          onChange={() => handleRoleChange(rol.id)}
                          type="checkbox"
                        />
                      </label>
                    ))}
                  </div>
                </div>
              </div>

              <div className="sticky bottom-0 flex flex-col gap-2 border-t border-slate-200 bg-white px-5 py-4 sm:flex-row sm:justify-end">
                <button
                  className="flex min-h-11 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-4 text-sm font-semibold text-slate-700 hover:bg-slate-50"
                  onClick={resetForm}
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
    </section>
  );
}

export default UsuariosPage;
