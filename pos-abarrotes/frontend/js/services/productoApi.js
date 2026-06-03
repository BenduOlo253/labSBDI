import { request } from './api.js';
export const listarProductos = (buscar = '') => {
    const q = buscar.trim();
    return request(q ? `/productos?buscar=${encodeURIComponent(q)}` : '/productos');
};
export const buscarProductos = (texto) => request(`/productos/buscar?texto=${encodeURIComponent(texto)}`);
export const obtenerPorCodigo = (codigo) => request(`/productos/codigo/${encodeURIComponent(codigo)}`);
export const crearProducto = (producto) => request('/productos', { method: 'POST', body: JSON.stringify(producto) });
export const actualizarProducto = (id, producto) => request(`/productos/${id}`, { method: 'PUT', body: JSON.stringify(producto) });
export const desactivarProducto = (id) => request(`/productos/${id}`, { method: 'DELETE' });
