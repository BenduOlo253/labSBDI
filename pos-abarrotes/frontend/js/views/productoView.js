export function renderProductos(productos, contenedor, onEdit, onDelete) {
    contenedor.innerHTML = productos.map(p => `
        <div class="product-row">
            <div class="d-flex justify-content-between gap-2">
                <div><strong>${p.nombre}</strong><div class="small-muted">${p.codigoBarras || 'Sin código'} · ${p.categoria}</div><div>$${Number(p.precioVenta).toFixed(2)}</div></div>
                <div class="btn-group btn-group-sm align-self-start"><button class="btn btn-outline-primary" data-edit="${p.idProducto}">Editar</button><button class="btn btn-outline-danger" data-del="${p.idProducto}">Baja</button></div>
            </div>
        </div>`).join('') || '<p class="text-muted">Sin productos.</p>';
    contenedor.querySelectorAll('[data-edit]').forEach(b => b.onclick = () => onEdit(Number(b.dataset.edit)));
    contenedor.querySelectorAll('[data-del]').forEach(b => b.onclick = () => onDelete(Number(b.dataset.del)));
}
export const showMsg = (el, msg, type='success') => { el.innerHTML = `<div class="alert alert-${type}">${msg}</div>`; };
