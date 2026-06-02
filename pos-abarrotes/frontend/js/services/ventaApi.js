import { request } from './api.js';
export const crearVenta = (venta) => request('/ventas', { method: 'POST', body: JSON.stringify(venta) });
export const listarVentas = () => request('/ventas');
export const ventasPorTurno = (idTurno) => request(`/ventas/turno/${idTurno}`);
