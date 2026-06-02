import { requireSession } from '../services/api.js';
import { listarProductos, obtenerPorCodigo } from '../services/productoApi.js';
import { crearVenta } from '../services/ventaApi.js';
import { VentaCarrito } from '../models/Venta.js';
import { renderResultados, renderCarrito, showMsg } from '../views/ventaView.js';
const usuario = requireSession(); const carrito = new VentaCarrito(); let productos=[];
const res=document.getElementById('resultados'), cart=document.getElementById('carrito'), totalEl=document.getElementById('total'), msg=document.getElementById('msg'), buscar=document.getElementById('buscar');
function repintar(){ renderCarrito(carrito, cart, totalEl, (id,q)=>{carrito.cambiarCantidad(id,q); repintar(); calcCambio();}, id=>{carrito.quitar(id); repintar(); calcCambio();}); }
function add(id){ const p=productos.find(x=>x.idProducto===id); carrito.agregar(p,1); repintar(); calcCambio(); }
async function buscarProductos(){ const q=buscar.value.trim().toLowerCase(); if(!q) return renderResultados([],res,add); let encontrados = productos.filter(p=>p.nombre.toLowerCase().includes(q) || (p.codigoBarras||'').includes(q)); if(encontrados.length===0 && q.length>3){ try { const p=await obtenerPorCodigo(q); encontrados=[p]; } catch{} } renderResultados(encontrados,res,add); }
function calcCambio(){ const recibido=Number(document.getElementById('monto').value||0); document.getElementById('cambio').textContent = `$${Math.max(0, recibido-carrito.total()).toFixed(2)}`; }
buscar.addEventListener('input', buscarProductos); document.getElementById('monto').addEventListener('input', calcCambio);
document.getElementById('ventaForm').addEventListener('submit', async e => { e.preventDefault(); const turno=JSON.parse(localStorage.getItem('turnoActivo')||'null'); if(!turno) return showMsg(msg,'Abra caja antes de vender','danger'); if(carrito.items.length===0) return showMsg(msg,'Agregue productos','danger'); const monto=Number(document.getElementById('monto').value); const total=carrito.total(); const payload={ idSucursal:1, idCaja:1, idTurno:turno.idTurno, idUsuario:usuario.idUsuario, detalles:carrito.items.map(i=>({idProducto:i.producto.idProducto,cantidad:i.cantidad})), pagos:[{idMetodoPago:Number(document.getElementById('metodo').value), monto, referencia:document.getElementById('referencia').value || null, cambio:Math.max(0,monto-total)}] }; try { const venta=await crearVenta(payload); showMsg(msg,`Venta ${venta.folio} registrada por $${Number(venta.total).toFixed(2)}`); carrito.limpiar(); repintar(); e.target.reset(); calcCambio(); } catch(err){ showMsg(msg,err.message,'danger'); } });
productos = await listarProductos(); repintar();
