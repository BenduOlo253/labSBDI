export function renderResultados(productos, contenedor, onAdd) {
    contenedor.innerHTML = productos.map(p => `<div class="product-row d-flex justify-content-between align-items-center"><div><strong>${p.nombre}</strong><div class="small-muted">${p.codigoBarras || ''}</div><span>$${Number(p.precioVenta).toFixed(2)}</span></div><button class="btn btn-success btn-sm" data-add="${p.idProducto}">Agregar</button></div>`).join('');
    contenedor.querySelectorAll('[data-add]').forEach(b => b.onclick = () => onAdd(Number(b.dataset.add)));
}
export function renderCarrito(carrito, contenedor, totalEl, onQty, onRemove) {
    contenedor.innerHTML = carrito.items.map(i => `<div class="cart-row"><div class="d-flex justify-content-between"><strong>${i.producto.nombre}</strong><button class="btn btn-outline-danger btn-sm" data-remove="${i.producto.idProducto}">×</button></div><div class="row g-2 align-items-center mt-1"><div class="col-5"><input class="form-control form-control-sm" type="number" step="0.001" min="0.001" value="${i.cantidad}" data-qty="${i.producto.idProducto}"></div><div class="col text-end">$${(Number(i.producto.precioVenta)*i.cantidad).toFixed(2)}</div></div></div>`).join('') || '<p class="text-muted">Carrito vacío.</p>';
    totalEl.textContent = `$${carrito.total().toFixed(2)}`;
    contenedor.querySelectorAll('[data-qty]').forEach(input => input.onchange = () => onQty(Number(input.dataset.qty), Number(input.value)));
    contenedor.querySelectorAll('[data-remove]').forEach(btn => btn.onclick = () => onRemove(Number(btn.dataset.remove)));
}
export const showMsg = (el, msg, type='success') => { el.innerHTML = `<div class="alert alert-${type}">${msg}</div>`; };
