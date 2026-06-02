export function renderTurno(turno, contenedor) {
    contenedor.innerHTML = turno ? `<div class="alert alert-success"><strong>Turno abierto #${turno.idTurno}</strong><br>Caja: ${turno.caja}<br>Monto inicial: $${Number(turno.montoInicial).toFixed(2)}<br>Apertura: ${new Date(turno.fechaApertura).toLocaleString()}</div>` : '<div class="alert alert-warning">No hay turno abierto.</div>';
}
export const showMsg = (el, msg, type='success') => { el.innerHTML = `<div class="alert alert-${type}">${msg}</div>`; };
