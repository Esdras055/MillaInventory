function SummaryCard({ accent = "emerald", icon: Icon, label, loading, value }) {
  const accentClasses = {
    amber: "bg-amber-50 text-amber-700",
    emerald: "bg-emerald-50 text-emerald-700",
    rose: "bg-rose-50 text-rose-700",
    sky: "bg-sky-50 text-sky-700",
    slate: "bg-slate-100 text-slate-700",
  };

  return (
    <article className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-slate-500">{label}</p>
          {loading ? (
            <div className="mt-3 h-8 w-24 animate-pulse rounded-md bg-slate-200" />
          ) : (
            <p className="mt-2 text-3xl font-bold text-slate-950">
              {Number(value || 0).toLocaleString("es-SV")}
            </p>
          )}
        </div>

        {Icon && (
          <div
            className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-lg ${
              accentClasses[accent] || accentClasses.emerald
            }`}
          >
            <Icon size={22} />
          </div>
        )}
      </div>
    </article>
  );
}

export default SummaryCard;
