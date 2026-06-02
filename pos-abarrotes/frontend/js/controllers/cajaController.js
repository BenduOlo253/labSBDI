import { requireSession } from '../services/api.js';
import { abrirCaja, cerrarCaja, turnoActivo } from '../services/cajaApi.js';
import { renderTurno, showMsg } from '../views/cajaView.js';
const usuario = requireSession(); const turnoBox = document.getElementById('turnoBox'), msg = document.getElementById('msg');
async function cargar() { try { const t = await turnoActivo(1); localStorage.setItem('turnoActivo', JSON.stringify(t)); renderTurno(t, turnoBox); } catch { localStorage.removeItem('turnoActivo'); renderTurno(null, turnoBox); } }
document.getElementById('abrirForm').addEventListener('submit', async e => { e.preventDefault(); try { const t = await abrirCaja({ idCaja:1, idUsuario:usuario.idUsuario, montoInicial:Number(e.target.montoInicial.value) }); localStorage.setItem('turnoActivo', JSON.stringify(t)); showMsg(msg,'Caja abierta'); await cargar(); } catch(err) { showMsg(msg,err.message,'danger'); } });
document.getElementById('cerrarForm').addEventListener('submit', async e => { e.preventDefault(); const t = JSON.parse(localStorage.getItem('turnoActivo') || 'null'); if(!t) return showMsg(msg,'No hay turno abierto','danger'); try { const cerrado = await cerrarCaja({ idTurno:t.idTurno, idUsuario:usuario.idUsuario, montoFinalContado:Number(e.target.montoFinalContado.value) }); localStorage.removeItem('turnoActivo'); showMsg(msg,`Caja cerrada. Diferencia: $${Number(cerrado.diferencia).toFixed(2)}`); await cargar(); } catch(err) { showMsg(msg,err.message,'danger'); } });
cargar();
