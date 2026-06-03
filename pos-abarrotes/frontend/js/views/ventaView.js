const money = (value) => `$${Number(value || 0).toFixed(2)}`;
const qty = (value) => Number(value || 0).toFixed(3).replace(/\.000$/, '');

function escapeHtml(value = '') {
    return String(value).replace(/[&<>'"]/g, (char) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[char]));
}

export function renderResultados(productos, contenedor, onAdd, conteoEl) {
    if (conteoEl) conteoEl.textContent = `${productos.length} resultado${productos.length === 1 ? '' : 's'}`;

    contenedor.innerHTML = productos.length ? productos.map((p) => {
        const existencia = p.existencia ?? p.stockDisponible;
        const bajoStock = existencia !== undefined && Number(existencia) <= Number(p.stockMinimo || 0);
        return `<article class="product-card">
            <div>
                <h3 class="h6 mb-1">${escapeHtml(p.nombre)}</h3>
                <div class="small-muted">Código: ${escapeHtml(p.codigoBarras || 'Sin código')}</div>
                <div class="d-flex flex-wrap gap-2 mt-2">
                    <span class="badge text-bg-primary">${money(p.precioVenta)}</span>
                    <span class="badge ${bajoStock ? 'text-bg-warning' : 'text-bg-light border'}">Stock: ${existencia === undefined ? 'N/D' : qty(existencia)}</span>
                    <span class="badge text-bg-light border">IVA: ${Number(p.porcentajeImpuesto || 0).toFixed(2)}%</span>
                </div>
            </div>
            <button class="btn btn-success btn-lg" data-add="${p.idProducto}">Agregar</button>
        </article>`;
    }).join('') : '<div class="empty-state">Busca por código, nombre o usa la cámara para comenzar.</div>';

    contenedor.querySelectorAll('[data-add]').forEach((button) => {
        button.addEventListener('click', () => onAdd(Number(button.dataset.add)));
    });
}

export function renderCarrito(venta, contenedor, totals, onQty, onRemove) {
    if (venta.detalles.length === 0) {
        contenedor.innerHTML = '<div class="empty-state">Carrito vacío. Agrega productos para iniciar la venta.</div>';
    } else {
        contenedor.innerHTML = `<table class="table align-middle mb-0">
            <thead><tr><th>Producto</th><th class="text-center">Cant.</th><th class="text-end">Precio</th><th class="text-end">Total</th><th></th></tr></thead>
            <tbody>${venta.detalles.map((item) => {
                const linea = Number(item.producto.precioVenta || 0) * item.cantidad;
                return `<tr>
                    <td><strong>${escapeHtml(item.producto.nombre)}</strong><div class="small-muted">${escapeHtml(item.producto.codigoBarras || '')}</div></td>
                    <td class="cart-qty"><input class="form-control form-control-sm" type="number" min="0.001" step="0.001" value="${item.cantidad}" data-qty="${item.producto.idProducto}"></td>
                    <td class="text-end">${money(item.producto.precioVenta)}</td>
                    <td class="text-end fw-semibold">${money(linea)}</td>
                    <td class="text-end"><button class="btn btn-outline-danger btn-sm" type="button" data-remove="${item.producto.idProducto}">Eliminar</button></td>
                </tr>`;
            }).join('')}</tbody>
        </table>`;
    }

    totals.subtotal.textContent = money(venta.subtotal);
    totals.impuesto.textContent = money(venta.impuestoTotal);
    totals.total.textContent = money(venta.total);

    contenedor.querySelectorAll('[data-qty]').forEach((input) => {
        input.addEventListener('change', () => onQty(Number(input.dataset.qty), Number(input.value)));
    });
    contenedor.querySelectorAll('[data-remove]').forEach((button) => {
        button.addEventListener('click', () => onRemove(Number(button.dataset.remove)));
    });
}

export function renderMetodosPago(metodos, select) {
    select.innerHTML = metodos.map((metodo) => `<option value="${metodo.idMetodoPago}" data-requiere-referencia="${metodo.requiereReferencia}">${escapeHtml(metodo.nombre)}</option>`).join('');
}

export function renderTurno(turno, cajaEstado, turnoAcciones) {
    if (turno) {
        cajaEstado.className = 'badge text-bg-success px-3 py-2';
        cajaEstado.textContent = `Caja abierta · Turno ${turno.idTurno}`;
        turnoAcciones.innerHTML = '<span class="alert alert-success py-2 px-3 mb-0 d-inline-block">Caja lista para vender.</span>';
        return;
    }

    cajaEstado.className = 'badge text-bg-danger px-3 py-2';
    cajaEstado.textContent = 'Caja cerrada';
    turnoAcciones.innerHTML = '<div class="alert alert-warning py-2 px-3 mb-0 d-flex align-items-center gap-2 flex-wrap"><span>No hay caja abierta. Abre caja antes de vender.</span><a class="btn btn-warning btn-sm" href="caja.html">Abrir caja</a></div>';
}

export function showMsg(el, msg, type = 'success') {
    el.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">${escapeHtml(msg)}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button></div>`;
}

export { money };
