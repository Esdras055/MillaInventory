import { Eye, EyeOff, LockKeyhole, PackageCheck, User } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import AlertMessage from "../components/AlertMessage";
import useAuth from "../hooks/useAuth";

function LoginPage() {
  const [mode, setMode] = useState("login");
  const [showPassword, setShowPassword] = useState(false);
  const [serverError, setServerError] = useState("");
  const { isAuthenticated, login, registerUser } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const redirectTo = location.state?.from?.pathname || "/dashboard";

  const {
    formState: { errors, isSubmitting },
    getValues,
    handleSubmit,
    register,
    reset,
  } = useForm({
    defaultValues: {
      confirmPassword: "",
      name: "",
      password: "",
      username: "",
    },
  });

  if (isAuthenticated) {
    return <Navigate to={redirectTo} replace />;
  }

  async function onSubmit(values) {
    setServerError("");

    try {
      if (mode === "register") {
        await registerUser({
          name: values.name,
          password: values.password,
          username: values.username,
        });
      }

      await login({
        password: values.password,
        username: values.username,
      });
      navigate(redirectTo, { replace: true });
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        "No se pudo iniciar sesion. Revisa tus credenciales.";
      setServerError(message);
    }
  }

  function changeMode(nextMode) {
    setMode(nextMode);
    setServerError("");
    reset({
      confirmPassword: "",
      name: "",
      password: "",
      username: "",
    });
  }

  return (
    <main className="min-h-screen bg-slate-100 text-slate-950">
      <div className="mx-auto grid min-h-screen w-full max-w-6xl items-center gap-10 px-5 py-8 lg:grid-cols-[1fr_420px]">
        <section className="hidden lg:block">
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-emerald-600 text-white">
              <PackageCheck size={26} />
            </div>
            <div>
              <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
                Milla Inventory
              </p>
              <h1 className="mt-2 max-w-xl text-5xl font-bold leading-tight">
                Control de inventario para bodegas, productos y movimientos.
              </h1>
            </div>
          </div>
          <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-600">
            Accede con tu usuario para administrar catalogos, entradas, salidas
            y reportes desde un panel centralizado.
          </p>
        </section>

        <section className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm sm:p-8">
          <div className="mb-8 flex items-center gap-3 lg:hidden">
            <div className="flex h-11 w-11 items-center justify-center rounded-lg bg-emerald-600 text-white">
              <PackageCheck size={24} />
            </div>
            <div>
              <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
                Milla Inventory
              </p>
              <h1 className="text-xl font-bold">
                {mode === "login" ? "Iniciar sesión" : "Crear cuenta"}
              </h1>
            </div>
          </div>

          <div className="mb-8 hidden lg:block">
            <h2 className="text-2xl font-bold">
              {mode === "login" ? "Iniciar sesión" : "Crear cuenta"}
            </h2>
            <p className="mt-2 text-sm text-slate-600">
              {mode === "login"
                ? "Ingresa tus credenciales para continuar"
                : "Registra tu usuario para acceder al sistema"}
            </p>
          </div>

          <div className="mb-6 grid grid-cols-2 rounded-lg bg-slate-100 p-1">
            <button
              className={`min-h-10 rounded-md text-sm font-semibold transition ${
                mode === "login"
                  ? "bg-white text-emerald-700 shadow-sm"
                  : "text-slate-600 hover:text-slate-900"
              }`}
              onClick={() => changeMode("login")}
              type="button"
            >
              Iniciar sesión
            </button>
            <button
              className={`min-h-10 rounded-md text-sm font-semibold transition ${
                mode === "register"
                  ? "bg-white text-emerald-700 shadow-sm"
                  : "text-slate-600 hover:text-slate-900"
              }`}
              onClick={() => changeMode("register")}
              type="button"
            >
              Crear cuenta
            </button>
          </div>

          <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
            {mode === "register" && (
              <div>
                <label className="text-sm font-medium text-slate-700" htmlFor="name">
                  Nombre
                </label>
                <div className="mt-2 flex items-center rounded-lg border border-slate-300 bg-white px-3 focus-within:border-emerald-600 focus-within:ring-2 focus-within:ring-emerald-100">
                  <User className="text-slate-400" size={18} />
                  <input
                    autoComplete="name"
                    className="min-h-11 w-full bg-transparent px-3 text-sm outline-none"
                    id="name"
                    type="text"
                    {...register("name", {
                      required: "El nombre es requerido",
                    })}
                  />
                </div>
                {errors.name && (
                  <p className="mt-2 text-sm text-red-600">{errors.name.message}</p>
                )}
              </div>
            )}

            <div>
              <label className="text-sm font-medium text-slate-700" htmlFor="username">
                Usuario
              </label>
              <div className="mt-2 flex items-center rounded-lg border border-slate-300 bg-white px-3 focus-within:border-emerald-600 focus-within:ring-2 focus-within:ring-emerald-100">
                <User className="text-slate-400" size={18} />
                <input
                  autoComplete="username"
                  className="min-h-11 w-full bg-transparent px-3 text-sm outline-none"
                  id="username"
                  type="text"
                  {...register("username", {
                    required: "El usuario es requerido",
                  })}
                />
              </div>
              {errors.username && (
                <p className="mt-2 text-sm text-red-600">{errors.username.message}</p>
              )}
            </div>

            <div>
              <label className="text-sm font-medium text-slate-700" htmlFor="password">
                Contrasena
              </label>
              <div className="mt-2 flex items-center rounded-lg border border-slate-300 bg-white px-3 focus-within:border-emerald-600 focus-within:ring-2 focus-within:ring-emerald-100">
                <LockKeyhole className="text-slate-400" size={18} />
                <input
                  autoComplete="current-password"
                  className="min-h-11 w-full bg-transparent px-3 text-sm outline-none"
                  id="password"
                  type={showPassword ? "text" : "password"}
                  {...register("password", {
                    required: "La contrasena es requerida",
                    minLength:
                      mode === "register"
                        ? {
                            message: "La contrasena debe tener al menos 6 caracteres",
                            value: 6,
                          }
                        : undefined,
                  })}
                />
                <button
                  aria-label={showPassword ? "Ocultar contrasena" : "Mostrar contrasena"}
                  className="rounded-md p-1 text-slate-500 hover:bg-slate-100 hover:text-slate-700"
                  onClick={() => setShowPassword((value) => !value)}
                  type="button"
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
              {errors.password && (
                <p className="mt-2 text-sm text-red-600">{errors.password.message}</p>
              )}
            </div>

            {mode === "register" && (
              <div>
                <label
                  className="text-sm font-medium text-slate-700"
                  htmlFor="confirmPassword"
                >
                  Confirmar contrasena
                </label>
                <div className="mt-2 flex items-center rounded-lg border border-slate-300 bg-white px-3 focus-within:border-emerald-600 focus-within:ring-2 focus-within:ring-emerald-100">
                  <LockKeyhole className="text-slate-400" size={18} />
                  <input
                    autoComplete="new-password"
                    className="min-h-11 w-full bg-transparent px-3 text-sm outline-none"
                    id="confirmPassword"
                    type={showPassword ? "text" : "password"}
                    {...register("confirmPassword", {
                      required: "Confirma la contrasena",
                      validate: (value) =>
                        value === getValues("password") || "Las contrasenas no coinciden",
                    })}
                  />
                </div>
                {errors.confirmPassword && (
                  <p className="mt-2 text-sm text-red-600">
                    {errors.confirmPassword.message}
                  </p>
                )}
              </div>
            )}

            <AlertMessage
              className="mb-0"
              message={serverError}
              onClose={() => setServerError("")}
              type="error"
            />

            <button
              className="flex min-h-11 w-full items-center justify-center rounded-lg bg-emerald-600 px-4 text-sm font-semibold text-white transition hover:bg-emerald-700 disabled:cursor-not-allowed disabled:bg-slate-400"
              disabled={isSubmitting}
              type="submit"
            >
              {isSubmitting
                ? mode === "login"
                  ? "Ingresando..."
                  : "Creando cuenta..."
                : mode === "login"
                  ? "Ingresar"
                  : "Crear cuenta"}
            </button>
          </form>
        </section>
      </div>
    </main>
  );
}

export default LoginPage;
