import { request } from './api.js';
export const listarInventario = () => request('/inventario');
export const listarBajoStock = () => request('/inventario/bajo-stock');
export const ajustarInventario = (ajuste) => request('/inventario/ajuste', { method: 'POST', body: JSON.stringify(ajuste) });
