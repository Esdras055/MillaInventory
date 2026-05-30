import {
  Boxes,
  Building2,
  PackageCheck,
  PackagePlus,
  PackageX,
  Truck,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  getMovimientos,
  getProductosBajoStock,
  getResumen,
} from "../api/reportesApi";
import LowStockTable from "../components/dashboard/LowStockTable";
import RecentMovementsTable from "../components/dashboard/RecentMovementsTable";
import SummaryCard from "../components/dashboard/SummaryCard";
import useAuth from "../hooks/useAuth";

const summaryCards = [
  {
    accent: "emerald",
    icon: Boxes,
    key: "totalProductos",
    label: "Total productos",
  },
  {
    accent: "sky",
    icon: Truck,
    key: "totalProveedores",
    label: "Total proveedores",
  },
  {
    accent: "slate",
    icon: Building2,
    key: "totalBodegas",
    label: "Total bodegas",
  },
  {
    accent: "emerald",
    icon: PackageCheck,
    key: "stockTotal",
    label: "Stock total",
  },
  {
    accent: "amber",
    icon: PackagePlus,
    key: "unidadesEntradasMes",
    label: "Entradas del mes",
  },
  {
    accent: "rose",
    icon: PackageX,
    key: "unidadesSalidasMes",
    label: "Salidas del mes",
  },
];

function formatDateParam(date) {
  return date.toISOString().slice(0, 10);
}

function getCurrentMonthRange() {
  const now = new Date();
  const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
  const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);

  return {
    fechaFin: formatDateParam(lastDay),
    fechaInicio: formatDateParam(firstDay),
  };
}

function DashboardPage() {
  const { user } = useAuth();
  const canViewOperationalReports = user?.roles?.some((role) =>
    ["ROLE_ADMIN", "ROLE_ANALYST"].includes(role),
  );
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [isOperationalLoading, setIsOperationalLoading] = useState(true);
  const [lowStockProducts, setLowStockProducts] = useState([]);
  const [recentMovements, setRecentMovements] = useState([]);
  const [resumen, setResumen] = useState(null);

  useEffect(() => {
    let ignore = false;

    async function loadDashboard() {
      setError("");
      setIsLoading(true);
      setIsOperationalLoading(canViewOperationalReports);

      try {
        const resumenResponse = await getResumen();

        if (!ignore) {
          setResumen(resumenResponse.data);
        }
      } catch {
        if (!ignore) {
          setError("No se pudo cargar el resumen del dashboard.");
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }

      if (!canViewOperationalReports) {
        if (!ignore) {
          setLowStockProducts([]);
          setRecentMovements([]);
          setIsOperationalLoading(false);
        }
        return;
      }

      try {
        const { fechaInicio, fechaFin } = getCurrentMonthRange();
        const [lowStockResponse, movementsResponse] = await Promise.all([
          getProductosBajoStock(10),
          getMovimientos(fechaInicio, fechaFin),
        ]);

        if (!ignore) {
          const sortedMovements = [...movementsResponse.data]
            .sort((a, b) => {
              const dateDiff = new Date(b.fecha) - new Date(a.fecha);
              return dateDiff || b.movimientoId - a.movimientoId;
            })
            .slice(0, 8);

          setLowStockProducts(lowStockResponse.data);
          setRecentMovements(sortedMovements);
        }
      } catch {
        if (!ignore) {
          setError("No se pudo cargar la informacion operativa del dashboard.");
        }
      } finally {
        if (!ignore) {
          setIsOperationalLoading(false);
        }
      }
    }

    loadDashboard();

    return () => {
      ignore = true;
    };
  }, [canViewOperationalReports]);

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

      {error && (
        <div className="mb-5 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {summaryCards.map((card) => (
          <SummaryCard
            accent={card.accent}
            icon={card.icon}
            key={card.key}
            label={card.label}
            loading={isLoading}
            value={resumen?.[card.key]}
          />
        ))}
      </div>

      {canViewOperationalReports && (
        <div className="mt-6 grid gap-6 xl:grid-cols-[minmax(0,1fr)_minmax(0,1.35fr)]">
          <LowStockTable items={lowStockProducts} loading={isOperationalLoading} />
          <RecentMovementsTable
            items={recentMovements}
            loading={isOperationalLoading}
          />
        </div>
      )}
    </section>
  );
}

export default DashboardPage;
