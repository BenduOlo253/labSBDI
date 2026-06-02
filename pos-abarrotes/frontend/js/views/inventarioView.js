export function renderInventario(items, contenedor) {
    contenedor.innerHTML = items.map(i => `<div class="product-row"><strong>${i.producto}</strong><div class="small-muted">${i.codigoBarras || ''}</div><div class="d-flex justify-content-between"><span>Existencia: ${Number(i.existencia).toFixed(3)}</span><span>Mínimo: ${Number(i.stockMinimo).toFixed(3)}</span></div></div>`).join('') || '<p class="text-muted">Sin inventario.</p>';
}
export const showMsg = (el, msg, type='success') => { el.innerHTML = `<div class="alert alert-${type}">${msg}</div>`; };
