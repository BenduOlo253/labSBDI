import { request } from './api.js';
export const listarProductos = () => request('/productos');
export const obtenerPorCodigo = (codigo) => request(`/productos/codigo/${encodeURIComponent(codigo)}`);
export const crearProducto = (producto) => request('/productos', { method: 'POST', body: JSON.stringify(producto) });
export const actualizarProducto = (id, producto) => request(`/productos/${id}`, { method: 'PUT', body: JSON.stringify(producto) });
export const desactivarProducto = (id) => request(`/productos/${id}`, { method: 'DELETE' });
