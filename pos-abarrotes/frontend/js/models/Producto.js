export class Producto {
    constructor(data = {}) { Object.assign(this, data); }
    get etiqueta() { return `${this.nombre} - $${Number(this.precioVenta || 0).toFixed(2)}`; }
}
