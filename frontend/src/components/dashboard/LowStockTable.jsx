function LowStockTable({ items, loading }) {
  return (
    <section className="rounded-lg border border-slate-200 bg-white shadow-sm">
      <div className="border-b border-slate-200 px-5 py-4">
        <h2 className="text-base font-bold text-slate-950">Productos bajo stock</h2>
        <p className="mt-1 text-sm text-slate-500">
          Productos con existencias menores o iguales al mínimo definido.
        </p>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full min-w-[520px] text-left text-sm">
          <thead className="bg-slate-50 text-xs uppercase text-slate-500">
            <tr>
              <th className="px-5 py-3 font-semibold">Producto</th>
              <th className="px-5 py-3 font-semibold">Stock actual</th>
              <th className="px-5 py-3 font-semibold">Mínimo</th>
              <th className="px-5 py-3 font-semibold">Estado</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading &&
              Array.from({ length: 4 }).map((_, index) => (
                <tr key={index}>
                  <td className="px-5 py-4">
                    <div className="h-4 w-40 animate-pulse rounded bg-slate-200" />
                  </td>
                  <td className="px-5 py-4">
                    <div className="h-4 w-12 animate-pulse rounded bg-slate-200" />
                  </td>
                  <td className="px-5 py-4">
                    <div className="h-4 w-12 animate-pulse rounded bg-slate-200" />
                  </td>
                  <td className="px-5 py-4">
                    <div className="h-6 w-20 animate-pulse rounded-full bg-slate-200" />
                  </td>
                </tr>
              ))}

            {!loading &&
              items.map((item) => (
                <tr key={item.productoId} className="hover:bg-slate-50">
                  <td className="px-5 py-4 font-medium text-slate-800">
                    {item.producto}
                  </td>
                  <td className="px-5 py-4 text-slate-600">{item.stockActual}</td>
                  <td className="px-5 py-4 text-slate-600">{item.stockMinimo}</td>
                  <td className="px-5 py-4">
                    <span className="inline-flex rounded-full bg-amber-50 px-2.5 py-1 text-xs font-semibold text-amber-700">
                      Revisar
                    </span>
                  </td>
                </tr>
              ))}

            {!loading && items.length === 0 && (
              <tr>
                <td className="px-5 py-8 text-center text-slate-500" colSpan={4}>
                  No hay productos bajo stock.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </section>
  );
}

export default LowStockTable;
