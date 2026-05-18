export function productCard(producto) {
  return `
    <article class="product-item">
      <div>
        <div class="product-main">
          <div>
            <strong>${producto.nombre}</strong>
            <span>${producto.categoria} · ${producto.marca}</span>
          </div>
          <span class="badge">$${Number(producto.precio).toFixed(2)}</span>
        </div>
        <p class="product-meta">Bodega: ${producto.bodega} · Stock: ${producto.stock} unidades</p>
      </div>

      <div class="product-actions">
        <button data-action="edit" data-id="${producto.id}">Editar</button>
        <button class="delete" data-action="delete" data-id="${producto.id}">Eliminar</button>
      </div>
    </article>
  `;
}

export function movementCard(movimiento) {
  const signo = movimiento.tipo === "Entrada" ? "+" : "-";

  return `
    <article class="movement-item">
      <div>
        <strong>${movimiento.tipo}</strong>
        <div>${movimiento.producto}</div>
      </div>
      <div>${signo}${movimiento.cantidad} · ${movimiento.fecha}</div>
    </article>
  `;
}
