import { requireSession, logout } from '../services/api.js';
import { listarInventario, listarBajoStock, ajustarInventario } from '../services/inventarioApi.js';
import { renderInventario, showMsg } from '../views/inventarioView.js';
const usuario = requireSession(); document.getElementById('usuarioNombre').textContent = usuario?.nombre || usuario?.username || 'Usuario';
document.getElementById('logout').onclick = logout;
 const list = document.getElementById('listaInventario'), msg = document.getElementById('msg'), form = document.getElementById('ajusteForm');
async function cargar(bajo=false) { renderInventario(bajo ? await listarBajoStock() : await listarInventario(), list); }
document.getElementById('btnBajoStock').onclick = () => cargar(true).catch(e => showMsg(msg,e.message,'danger'));
document.getElementById('btnTodos').onclick = () => cargar().catch(e => showMsg(msg,e.message,'danger'));
form.addEventListener('submit', async e => { e.preventDefault(); const data = Object.fromEntries(new FormData(form)); data.idSucursal=1; data.idUsuario=usuario.idUsuario; data.idProducto=Number(data.idProducto); data.cantidad=Number(data.cantidad); try { await ajustarInventario(data); showMsg(msg,'Inventario actualizado'); form.reset(); await cargar(); } catch(err) { showMsg(msg,err.message,'danger'); } });
cargar().catch(e => showMsg(msg,e.message,'danger'));
