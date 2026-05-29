function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function downloadBlob(content, filename, type) {
  const blob = new Blob([content], { type });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");

  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

function getReportHtml({ generatedAt, rows, title }) {
  const tableRows = rows
    .map(
      (row) => `
        <tr>
          <td>${escapeHtml(row.fecha)}</td>
          <td>${escapeHtml(row.tipo)}</td>
          <td>${escapeHtml(row.producto)}</td>
          <td>${escapeHtml(row.bodega)}</td>
          <td>${escapeHtml(row.proveedor || "-")}</td>
          <td>${escapeHtml(row.cantidad)}</td>
          <td>${escapeHtml(row.precioAdquisicion || "-")}</td>
        </tr>
      `,
    )
    .join("");

  return `
    <!doctype html>
    <html>
      <head>
        <meta charset="utf-8" />
        <title>${escapeHtml(title)}</title>
        <style>
          body {
            color: #0f172a;
            font-family: Arial, sans-serif;
            margin: 32px;
          }
          h1 {
            font-size: 22px;
            margin: 0 0 8px;
          }
          p {
            color: #64748b;
            font-size: 12px;
            margin: 0 0 20px;
          }
          table {
            border-collapse: collapse;
            font-size: 12px;
            width: 100%;
          }
          th {
            background: #f1f5f9;
            color: #334155;
            text-align: left;
          }
          th, td {
            border: 1px solid #e2e8f0;
            padding: 8px;
          }
        </style>
      </head>
      <body>
        <h1>${escapeHtml(title)}</h1>
        <p>Generado: ${escapeHtml(generatedAt)}</p>
        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Tipo</th>
              <th>Producto</th>
              <th>Bodega</th>
              <th>Proveedor</th>
              <th>Cantidad</th>
              <th>Precio adquisición</th>
            </tr>
          </thead>
          <tbody>
            ${tableRows}
          </tbody>
        </table>
      </body>
    </html>
  `;
}

export function exportMovementsToExcel({ filename, generatedAt, rows, title }) {
  const html = getReportHtml({ generatedAt, rows, title });

  downloadBlob(
    html,
    `${filename}.xlsx`,
    "application/vnd.ms-excel;charset=utf-8",
  );
}

export function exportMovementsToPdf({ generatedAt, rows, title }) {
  const html = getReportHtml({ generatedAt, rows, title });
  const printWindow = window.open("", "_blank", "width=1100,height=800");

  if (!printWindow) {
    return;
  }

  printWindow.document.open();
  printWindow.document.write(html);
  printWindow.document.close();
  printWindow.focus();
  printWindow.print();
}
