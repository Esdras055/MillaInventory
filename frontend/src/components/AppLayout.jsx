import {
  Boxes,
  Building2,
  ChartNoAxesCombined,
  FolderTree,
  Home,
  Landmark,
  LogOut,
  Menu,
  PackageCheck,
  PackagePlus,
  PackageX,
  Tags,
  Truck,
  Users,
  X,
} from "lucide-react";
import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const navigationItems = [
  { label: "Inicio", path: "/dashboard", icon: Home },
  { label: "Usuarios", path: "/usuarios", icon: Users, roles: ["ROLE_ADMIN"] },
  { label: "Productos", path: "/productos", icon: Boxes },
  { label: "Bodegas", path: "/bodegas", icon: Building2 },
  { label: "Categorias", path: "/categorias", icon: FolderTree },
  { label: "Marcas", path: "/marcas", icon: Tags },
  { label: "Proveedores", path: "/proveedores", icon: Truck },
  { label: "Entradas", path: "/entradas", icon: PackagePlus, roles: ["ROLE_ADMIN"] },
  { label: "Salidas", path: "/salidas", icon: PackageX, roles: ["ROLE_ADMIN"] },
  {
    label: "Reportes",
    path: "/reportes",
    icon: ChartNoAxesCombined,
    roles: ["ROLE_ADMIN", "ROLE_ANALYST"],
  },
];

function canViewItem(item, userRoles) {
  if (!item.roles) {
    return true;
  }

  return item.roles.some((role) => userRoles.includes(role));
}

function AppLayout() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { logout, user } = useAuth();
  const userRoles = user?.roles || [];

  return (
    <div className="min-h-screen bg-slate-100 text-slate-950">
      <aside
        className={`fixed inset-y-0 left-0 z-40 w-72 border-r border-slate-200 bg-white transition-transform duration-200 lg:translate-x-0 ${
          isMenuOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-full flex-col">
          <div className="flex min-h-16 items-center justify-between border-b border-slate-200 px-5">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-600 text-white">
                <PackageCheck size={22} />
              </div>
              <div>
                <p className="text-sm font-semibold text-emerald-700">Milla</p>
                <p className="text-base font-bold">Inventory</p>
              </div>
            </div>

            <button
              aria-label="Cerrar menu"
              className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 lg:hidden"
              onClick={() => setIsMenuOpen(false)}
              type="button"
            >
              <X size={20} />
            </button>
          </div>

          <nav className="flex-1 space-y-1 overflow-y-auto px-3 py-4">
            {navigationItems.filter((item) => canViewItem(item, userRoles)).map((item) => {
              const Icon = item.icon;

              return (
                <NavLink
                  className={({ isActive }) =>
                    `flex min-h-11 items-center gap-3 rounded-lg px-3 text-sm font-medium transition ${
                      isActive
                        ? "bg-emerald-50 text-emerald-700"
                        : "text-slate-600 hover:bg-slate-100 hover:text-slate-950"
                    }`
                  }
                  key={item.path}
                  onClick={() => setIsMenuOpen(false)}
                  to={item.path}
                >
                  <Icon size={19} />
                  {item.label}
                </NavLink>
              );
            })}
          </nav>

          <div className="border-t border-slate-200 p-4">
            <div className="mb-3 rounded-lg bg-slate-50 px-3 py-2">
              <p className="text-xs font-medium uppercase text-slate-500">Usuario</p>
              <p className="truncate text-sm font-semibold text-slate-800">
                {user?.name || "usuario"}
              </p>
            </div>
            <button
              className="flex min-h-10 w-full items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-medium text-slate-700 hover:bg-slate-50"
              onClick={logout}
              type="button"
            >
              <LogOut size={17} />
              Salir
            </button>
          </div>
        </div>
      </aside>

      {isMenuOpen && (
        <button
          aria-label="Cerrar menu"
          className="fixed inset-0 z-30 bg-slate-950/40 lg:hidden"
          onClick={() => setIsMenuOpen(false)}
          type="button"
        />
      )}

      <div className="lg:pl-72">
        <header className="sticky top-0 z-20 border-b border-slate-200 bg-white">
          <div className="flex min-h-16 items-center justify-between px-5">
            <button
              aria-label="Abrir menu"
              className="rounded-lg p-2 text-slate-600 hover:bg-slate-100 lg:hidden"
              onClick={() => setIsMenuOpen(true)}
              type="button"
            >
              <Menu size={22} />
            </button>
            <div className="hidden items-center gap-2 text-sm font-medium text-slate-500 lg:flex">
              <Landmark size={17} />
              Sistema de inventario
            </div>
            <div className="text-sm font-semibold text-slate-700">
              {user?.username || "usuario"}
            </div>
          </div>
        </header>

        <main className="mx-auto max-w-7xl px-5 py-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AppLayout;
