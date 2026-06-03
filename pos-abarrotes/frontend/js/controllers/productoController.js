import { requireSession, logout } from '../services/api.js';
import { listarProductos, crearProducto, actualizarProducto, desactivarProducto } from '../services/productoApi.js';
import { renderProductos, showMsg } from '../views/productoView.js';
const usuario = requireSession();
document.getElementById('usuarioNombre').textContent = usuario?.nombre || usuario?.username || 'Usuario';
document.getElementById('logout').onclick = logout;
let productos = []; let editId = null;
const list = document.getElementById('listaProductos'), form = document.getElementById('productoForm'), msg = document.getElementById('msg'), buscar = document.getElementById('buscar');
async function cargar() { productos = await listarProductos(); pintar(); }
function pintar() { const q = buscar.value.toLowerCase(); renderProductos(productos.filter(p => p.nombre.toLowerCase().includes(q) || (p.codigoBarras || '').includes(q)), list, editar, borrar); }
function editar(id) { const p = productos.find(x => x.idProducto === id); editId = id; Object.keys(p).forEach(k => { if (form[k]) form[k].value = p[k] ?? ''; }); form.idCategoria.value=p.idCategoria; form.idUnidad.value=p.idUnidad; form.idImpuesto.value=p.idImpuesto; window.scrollTo(0,0); }
async function borrar(id) { if (!confirm('¿Desactivar producto?')) return; await desactivarProducto(id); showMsg(msg, 'Producto desactivado'); await cargar(); }
form.addEventListener('submit', async e => { e.preventDefault(); const data = Object.fromEntries(new FormData(form)); ['idCategoria','idUnidad','idImpuesto'].forEach(k=>data[k]=Number(data[k])); ['precioCompra','precioVenta','stockMinimo','stockMaximo'].forEach(k=>data[k]=Number(data[k])); data.controlaInventario = true; try { editId ? await actualizarProducto(editId, data) : await crearProducto(data); form.reset(); editId=null; showMsg(msg, 'Producto guardado'); await cargar(); } catch(err) { showMsg(msg, err.message, 'danger'); } });
buscar.addEventListener('input', pintar); cargar().catch(err => showMsg(msg, err.message, 'danger'));
