export class VentaCarrito {
    constructor() { this.items = []; }
    agregar(producto, cantidad = 1) {
        const item = this.items.find(i => i.producto.idProducto === producto.idProducto);
        if (item) item.cantidad += Number(cantidad); else this.items.push({ producto, cantidad: Number(cantidad) });
    }
    quitar(idProducto) { this.items = this.items.filter(i => i.producto.idProducto !== idProducto); }
    cambiarCantidad(idProducto, cantidad) { const item = this.items.find(i => i.producto.idProducto === idProducto); if (item) item.cantidad = Math.max(0.001, Number(cantidad)); }
    subtotal() { return this.items.reduce((sum, i) => sum + Number(i.producto.precioVenta) * i.cantidad, 0); }
    impuesto() { return this.items.reduce((sum, i) => sum + (Number(i.producto.precioVenta) * i.cantidad * Number(i.producto.porcentajeImpuesto || 0) / 100), 0); }
    total() { return this.subtotal() + this.impuesto(); }
    limpiar() { this.items = []; }
}
