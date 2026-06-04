function ModulePage({ description, title }) {
  return (
    <section>
      <div className="mb-6">
        <p className="text-sm font-semibold uppercase tracking-wide text-emerald-700">
          Milla Inventory
        </p>
        <h1 className="mt-2 text-2xl font-bold text-slate-950">{title}</h1>
      </div>

      <div className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <p className="text-slate-600">{description}</p>
      </div>
    </section>
  );
}

export default ModulePage;
