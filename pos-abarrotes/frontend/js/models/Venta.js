export class Venta {
    constructor() {
        this.detalles = [];
        this.subtotal = 0;
        this.impuestoTotal = 0;
        this.total = 0;
    }

    agregarProducto(producto, cantidad = 1) {
        if (!producto) return;
        const cantidadNumerica = Math.max(0.001, Number(cantidad) || 1);
        const item = this.detalles.find((detalle) => detalle.producto.idProducto === producto.idProducto);

        if (item) item.cantidad += cantidadNumerica;
        else this.detalles.push({ producto, cantidad: cantidadNumerica });

        this.calcularTotales();
    }

    actualizarCantidad(idProducto, cantidad) {
        const item = this.detalles.find((detalle) => detalle.producto.idProducto === idProducto);
        if (!item) return;
        item.cantidad = Math.max(0.001, Number(cantidad) || 0.001);
        this.calcularTotales();
    }

    eliminarProducto(idProducto) {
        this.detalles = this.detalles.filter((detalle) => detalle.producto.idProducto !== idProducto);
        this.calcularTotales();
    }

    calcularTotales() {
        this.subtotal = this.detalles.reduce((sum, item) => sum + Number(item.producto.precioVenta || 0) * item.cantidad, 0);
        this.impuestoTotal = this.detalles.reduce((sum, item) => {
            const linea = Number(item.producto.precioVenta || 0) * item.cantidad;
            return sum + (linea * Number(item.producto.porcentajeImpuesto || 0) / 100);
        }, 0);
        this.total = this.subtotal + this.impuestoTotal;
        return { subtotal: this.subtotal, impuestoTotal: this.impuestoTotal, total: this.total };
    }

    calcularCambio(montoRecibido) {
        return Math.max(0, Number(montoRecibido || 0) - this.total);
    }

    limpiar() {
        this.detalles = [];
        this.calcularTotales();
    }

    estaVacia() {
        return this.detalles.length === 0;
    }
}

export class VentaCarrito {
    constructor() { this.items = []; }
    agregar(producto, cantidad = 1) {
        if (!producto) return;
        const item = this.items.find((i) => i.producto.idProducto === producto.idProducto);
        if (item) item.cantidad += Number(cantidad);
        else this.items.push({ producto, cantidad: Number(cantidad) });
    }
    quitar(idProducto) { this.items = this.items.filter((i) => i.producto.idProducto !== idProducto); }
    cambiarCantidad(idProducto, cantidad) {
        const item = this.items.find((i) => i.producto.idProducto === idProducto);
        if (item) item.cantidad = Math.max(0.001, Number(cantidad));
    }
    subtotal() { return this.items.reduce((sum, i) => sum + Number(i.producto.precioVenta || 0) * i.cantidad, 0); }
    impuesto() { return this.items.reduce((sum, i) => sum + (Number(i.producto.precioVenta || 0) * i.cantidad * Number(i.producto.porcentajeImpuesto || 0) / 100), 0); }
    total() { return this.subtotal() + this.impuesto(); }
    limpiar() { this.items = []; }
}
