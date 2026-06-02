import { request } from './api.js';
export const abrirCaja = (payload) => request('/caja/abrir', { method: 'POST', body: JSON.stringify(payload) });
export const cerrarCaja = (payload) => request('/caja/cerrar', { method: 'POST', body: JSON.stringify(payload) });
export const turnoActivo = (idCaja) => request(`/caja/turno-activo/${idCaja}`);
