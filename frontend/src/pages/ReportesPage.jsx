import { Download, FileSpreadsheet, Loader2, Printer, Search } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { getMovimientos } from "../api/reportesApi";
import {
  exportMovementsToExcel,
  exportMovementsToPdf,
} from "../utils/reportExport";

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

function ReportesPage() {
  const initialRange = useMemo(() => getCurrentMonthRange(), []);
  const [error, setError] = useState("");
  const [fechaFin, setFechaFin] = useState(initialRange.fechaFin);
  const [fechaInicio, setFechaInicio] = useState(initialRange.fechaInicio);
  const [isLoading, setIsLoading] = useState(true);
  const [movimientos, setMovimientos] = useState([]);

  const reportTitle = `Reporte de movimientos ${fechaInicio} a ${fechaFin}`;
  const generatedAt = new Date().toLocaleString("es-SV");
  const hasRows = movimientos.length > 0;

  async function loadReport() {
    setError("");

    if (!fechaInicio || !fechaFin) {
      setError("Selecciona fecha de inicio y fecha fin.");
      return;
    }

    if (fechaInicio > fechaFin) {
      setError("La fecha de inicio no puede ser mayor que la fecha fin.");
      return;
    }

    setIsLoading(true);

    try {
      const { data } = await getMovimientos(fechaInicio, fechaFin);
      setMovimientos(data);
    } catch {
      setError("No se pudo cargar el reporte de movimientos.");
      setMovimientos([]);
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let ignore = false;

    getMovimientos(initialRange.fechaInicio, initialRange.fechaFin)
      .then(({ data }) => {
        if (!ignore) {
          setMovimientos(data);
        }
      })
      .catch(() => {
        if (!ignore) {
          setError("No se pudo cargar el reporte de movimientos.");
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
  }, [initialRange.fechaFin, initialRange.fechaInicio]);

  function handleExportExcel() {
    exportMovementsToExcel({
      filename: `reporte-movimientos-${fechaInicio}-${fechaFin}`,
      generatedAt,
      rows: movimientos,
      title: reportTitle,
    });
  }

  function handleExportPdf() {
    exportMovementsToPdf({
      generatedAt,
      rows: movimientos,
      title: reportTitle,
    });
  }

  return (
    <section>
      <div className="mb-6">
        <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
          Reportes
        </p>
        <h1 className="mt-2 text-2xl font-bold text-slate-950">
          Generación de reportes
        </h1>
      </div>

      <div className="mb-6 rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
        <div className="grid gap-4 md:grid-cols-[1fr_1fr_auto]">
          <div>
            <label
              className="text-sm font-medium text-slate-700"
              htmlFor="fechaInicio"
            >
              Fecha inicio
            </label>
            <input
              className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
              id="fechaInicio"
              onChange={(event) => setFechaInicio(event.target.value)}
              type="date"
              value={fechaInicio}
            />
          </div>

          <div>
            <label className="text-sm font-medium text-slate-700" htmlFor="fechaFin">
              Fecha fin
            </label>
            <input
              className="mt-2 min-h-11 w-full rounded-lg border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
              id="fechaFin"
              onChange={(event) => setFechaFin(event.target.value)}
              type="date"
              value={fechaFin}
            />
          </div>

          <div className="flex items-end">
            <button
              className="flex min-h-11 w-full items-center justify-center gap-2 rounded-lg bg-emerald-600 px-4 text-sm font-semibold text-white hover:bg-emerald-700 disabled:cursor-not-allowed disabled:bg-slate-400 md:w-auto"
              disabled={isLoading}
              onClick={loadReport}
              type="button"
            >
              {isLoading ? <Loader2 className="animate-spin" size={18} /> : <Search size={18} />}
              Consultar
            </button>
          </div>
        </div>

        {error && (
          <div className="mt-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}
      </div>

      <div className="rounded-lg border border-slate-200 bg-white shadow-sm">
        <div className="flex flex-col gap-4 border-b border-slate-200 px-5 py-4 md:flex-row md:items-center md:justify-between">
          <div>
            <h2 className="text-base font-bold text-slate-950">
              Reporte de movimientos
            </h2>
            <p className="mt-1 text-sm text-slate-500">
              {movimientos.length} registros encontrados
            </p>
          </div>

          <div className="flex flex-col gap-2 sm:flex-row">
            <button
              className="flex min-h-10 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-semibold text-slate-700 hover:bg-slate-50 disabled:cursor-not-allowed disabled:text-slate-400"
              disabled={!hasRows}
              onClick={handleExportPdf}
              type="button"
            >
              <Printer size={17} />
              PDF
            </button>
            <button
              className="flex min-h-10 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-semibold text-slate-700 hover:bg-slate-50 disabled:cursor-not-allowed disabled:text-slate-400"
              disabled={!hasRows}
              onClick={handleExportExcel}
              type="button"
            >
              <FileSpreadsheet size={17} />
              Excel
            </button>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full min-w-[860px] text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase text-slate-500">
              <tr>
                <th className="px-5 py-3 font-semibold">Fecha</th>
                <th className="px-5 py-3 font-semibold">Tipo</th>
                <th className="px-5 py-3 font-semibold">Producto</th>
                <th className="px-5 py-3 font-semibold">Bodega</th>
                <th className="px-5 py-3 font-semibold">Proveedor</th>
                <th className="px-5 py-3 font-semibold">Cantidad</th>
                <th className="px-5 py-3 font-semibold">Precio</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {isLoading &&
                Array.from({ length: 6 }).map((_, index) => (
                  <tr key={index}>
                    {Array.from({ length: 7 }).map((__, cellIndex) => (
                      <td className="px-5 py-4" key={cellIndex}>
                        <div className="h-4 w-24 animate-pulse rounded bg-slate-200" />
                      </td>
                    ))}
                  </tr>
                ))}

              {!isLoading &&
                movimientos.map((movimiento) => {
                  const isEntrada = movimiento.tipo === "ENTRADA";

                  return (
                    <tr
                      className="hover:bg-slate-50"
                      key={`${movimiento.tipo}-${movimiento.movimientoId}`}
                    >
                      <td className="px-5 py-4 text-slate-600">{movimiento.fecha}</td>
                      <td className="px-5 py-4">
                        <span
                          className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${
                            isEntrada
                              ? "bg-emerald-50 text-emerald-700"
                              : "bg-rose-50 text-rose-700"
                          }`}
                        >
                          {isEntrada ? "Entrada" : "Salida"}
                        </span>
                      </td>
                      <td className="px-5 py-4 font-medium text-slate-800">
                        {movimiento.producto}
                      </td>
                      <td className="px-5 py-4 text-slate-600">
                        {movimiento.bodega}
                      </td>
                      <td className="px-5 py-4 text-slate-600">
                        {movimiento.proveedor || "-"}
                      </td>
                      <td className="px-5 py-4 text-slate-600">
                        {movimiento.cantidad}
                      </td>
                      <td className="px-5 py-4 text-slate-600">
                        {movimiento.precioAdquisicion || "-"}
                      </td>
                    </tr>
                  );
                })}

              {!isLoading && movimientos.length === 0 && (
                <tr>
                  <td className="px-5 py-8 text-center text-slate-500" colSpan={7}>
                    No hay movimientos para el rango seleccionado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <div className="flex items-center gap-2 border-t border-slate-200 px-5 py-3 text-xs text-slate-500">
          <Download size={15} />
          Los reportes exportan los datos actualmente visibles en la tabla.
        </div>
      </div>
    </section>
  );
}

export default ReportesPage;
