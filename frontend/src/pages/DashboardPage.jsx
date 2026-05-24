import useAuth from "../hooks/useAuth";

function DashboardPage() {
  const { user } = useAuth();

  return (
    <section>
      <div className="mb-6">
        <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
          Inicio
        </p>
        <h1 className="mt-2 text-2xl font-bold text-slate-950">
          Bienvenido, {user?.name || "usuario"}
        </h1>
      </div>

      <div className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
          Dashboard
        </p>
        <h2 className="mt-2 text-xl font-bold">Panel principal</h2>
        <p className="mt-3 max-w-2xl text-slate-600">
          Este sera el inicio del sistema, donde se mostraran indicadores,
          resumenes y accesos rapidos del inventario.
        </p>
      </div>
    </section>
  );
}

export default DashboardPage;
