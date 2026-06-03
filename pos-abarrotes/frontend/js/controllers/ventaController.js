import { requireSession, logout } from '../services/api.js';
import { listarProductos, obtenerPorCodigo } from '../services/productoApi.js';
import { listarInventario } from '../services/inventarioApi.js';
import { turnoActivo as obtenerTurnoActivo } from '../services/cajaApi.js';
import { crearVenta } from '../services/ventaApi.js';
import { listarMetodosPago } from '../services/metodoPagoApi.js';
import { startScanner, stopScanner } from '../services/barcodeScanner.js';
import { Venta } from '../models/Venta.js';
import { renderResultados, renderCarrito, renderMetodosPago, renderTurno, showMsg, money } from '../views/ventaView.js';

const ID_SUCURSAL = 1;
const ID_CAJA = 1;

const usuario = requireSession();
const venta = new Venta();
let productos = [];
let turno = null;

const els = {
    msg: document.getElementById('msg'),
    usuarioNombre: document.getElementById('usuarioNombre'),
    cajaEstado: document.getElementById('cajaEstado'),
    turnoAcciones: document.getElementById('turnoAcciones'),
    codigoInput: document.getElementById('codigoInput'),
    nombreInput: document.getElementById('nombreInput'),
    btnBuscar: document.getElementById('btnBuscar'),
    btnEscanear: document.getElementById('btnEscanear'),
    resultados: document.getElementById('resultados'),
    resultadosConteo: document.getElementById('resultadosConteo'),
    carrito: document.getElementById('carrito'),
    btnVaciar: document.getElementById('btnVaciar'),
    ventaForm: document.getElementById('ventaForm'),
    metodo: document.getElementById('metodo'),
    referencia: document.getElementById('referencia'),
    monto: document.getElementById('monto'),
    cambio: document.getElementById('cambio'),
    subtotal: document.getElementById('subtotal'),
    impuesto: document.getElementById('impuesto'),
    total: document.getElementById('total'),
    btnCobrar: document.getElementById('btnCobrar'),
    scannerModal: document.getElementById('scannerModal'),
    scannerVideo: document.getElementById('scannerVideo'),
    scannerMsg: document.getElementById('scannerMsg')
};

function enriquecerConInventario(productosBase, inventario) {
    const stockPorProducto = new Map(inventario.map((item) => [item.idProducto, item]));
    return productosBase.map((producto) => {
        const stock = stockPorProducto.get(producto.idProducto);
        return {
            ...producto,
            existencia: stock?.existencia,
            stockDisponible: stock?.existencia
        };
    });
}

function setScannerMessage(message, type = 'info') {
    els.scannerMsg.innerHTML = `<div class="alert alert-${type} py-2 mb-0">${message}</div>`;
}

function actualizarCambio() {
    els.cambio.textContent = money(venta.calcularCambio(els.monto.value));
    actualizarEstadoCobro();
}

function actualizarEstadoCobro() {
    els.btnCobrar.disabled = venta.estaVacia() || !turno || venta.total <= 0;
}

function repintarCarrito() {
    renderCarrito(
        venta,
        els.carrito,
        { subtotal: els.subtotal, impuesto: els.impuesto, total: els.total },
        (idProducto, cantidad) => {
            venta.actualizarCantidad(idProducto, cantidad);
            repintarCarrito();
        },
        (idProducto) => {
            venta.eliminarProducto(idProducto);
            repintarCarrito();
        }
    );
    actualizarCambio();
}

function buscarLocal(texto) {
    const q = texto.trim().toLowerCase();
    if (!q) return [];
    return productos.filter((producto) =>
        producto.nombre.toLowerCase().includes(q) || String(producto.codigoBarras || '').includes(q)
    );
}

function agregarProducto(producto) {
    if (!producto) return;
    const existencia = producto.existencia ?? producto.stockDisponible;
    const itemActual = venta.detalles.find((item) => item.producto.idProducto === producto.idProducto);
    const cantidadNueva = Number(itemActual?.cantidad || 0) + 1;

    if (producto.controlaInventario && existencia !== undefined && cantidadNueva > Number(existencia)) {
        showMsg(els.msg, `No hay stock suficiente de ${producto.nombre}. Disponible: ${existencia}`, 'warning');
        return;
    }

    venta.agregarProducto(producto, 1);
    repintarCarrito();
    showMsg(els.msg, `${producto.nombre} agregado al carrito.`, 'success');
    els.codigoInput.value = '';
    els.nombreInput.value = '';
    els.codigoInput.focus();
}

function agregarProductoPorId(idProducto) {
    agregarProducto(productos.find((producto) => producto.idProducto === idProducto));
}

async function buscarProductoPorCodigo(codigo, agregarAutomaticamente = false) {
    if (!codigo) return;
    try {
        let producto = productos.find((item) => String(item.codigoBarras || '') === codigo);
        if (!producto) {
            const remoto = await obtenerPorCodigo(codigo);
            producto = enriquecerConInventario([remoto], await listarInventario())[0];
            productos = [producto, ...productos.filter((item) => item.idProducto !== producto.idProducto)];
        }
        renderResultados([producto], els.resultados, agregarProductoPorId, els.resultadosConteo);
        if (agregarAutomaticamente) agregarProducto(producto);
    } catch (error) {
        renderResultados([], els.resultados, agregarProductoPorId, els.resultadosConteo);
        showMsg(els.msg, error.message || 'Producto no encontrado por código.', 'warning');
    }
}

async function ejecutarBusqueda() {
    const codigo = els.codigoInput.value.trim();
    const nombre = els.nombreInput.value.trim();

    if (codigo) {
        await buscarProductoPorCodigo(codigo, false);
        return;
    }

    const encontrados = buscarLocal(nombre);
    renderResultados(encontrados, els.resultados, agregarProductoPorId, els.resultadosConteo);
    if (!encontrados.length && nombre) showMsg(els.msg, 'No se encontraron productos con ese nombre.', 'warning');
}

async function cargarProductos() {
    try {
        const [productosBase, inventario] = await Promise.all([listarProductos(), listarInventario()]);
        productos = enriquecerConInventario(productosBase, inventario);
        renderResultados(productos.slice(0, 8), els.resultados, agregarProductoPorId, els.resultadosConteo);
    } catch (error) {
        showMsg(els.msg, `No se pudieron cargar productos: ${error.message}`, 'danger');
    }
}

async function cargarTurnoActivo() {
    try {
        turno = await obtenerTurnoActivo(ID_CAJA);
        localStorage.setItem('turnoActivo', JSON.stringify(turno));
    } catch {
        turno = null;
        localStorage.removeItem('turnoActivo');
    }
    renderTurno(turno, els.cajaEstado, els.turnoAcciones);
    actualizarEstadoCobro();
}

async function cargarMetodosPago() {
    try {
        const metodos = await listarMetodosPago();
        renderMetodosPago(metodos, els.metodo);
    } catch {
        renderMetodosPago([
            { idMetodoPago: 1, nombre: 'Efectivo', requiereReferencia: false },
            { idMetodoPago: 2, nombre: 'Tarjeta', requiereReferencia: true },
            { idMetodoPago: 3, nombre: 'Transferencia', requiereReferencia: true }
        ], els.metodo);
        showMsg(els.msg, 'No se pudieron cargar métodos de pago del backend; se usan valores predeterminados.', 'warning');
    }
}

async function finalizarVenta() {
    if (!usuario) return showMsg(els.msg, 'Sesión inválida. Inicia sesión nuevamente.', 'danger');
    if (!turno) return showMsg(els.msg, 'No hay caja abierta. Abre caja antes de vender.', 'danger');
    if (venta.estaVacia()) return showMsg(els.msg, 'Agrega productos al carrito antes de cobrar.', 'warning');
    if (!els.metodo.value) return showMsg(els.msg, 'Selecciona método de pago.', 'warning');

    const metodoTexto = els.metodo.selectedOptions[0]?.textContent?.toLowerCase() || '';
    const monto = Number(els.monto.value || 0);
    if (metodoTexto.includes('efectivo') && monto < venta.total) {
        return showMsg(els.msg, 'El monto recibido en efectivo no cubre el total.', 'warning');
    }

    const montoPago = metodoTexto.includes('efectivo') ? monto : venta.total;
    const payload = {
        idSucursal: ID_SUCURSAL,
        idCaja: ID_CAJA,
        idTurno: turno.idTurno,
        idUsuario: usuario.idUsuario,
        detalles: venta.detalles.map((item) => ({ idProducto: item.producto.idProducto, cantidad: item.cantidad })),
        pagos: [{
            idMetodoPago: Number(els.metodo.value),
            monto: Number(montoPago.toFixed(2)),
            referencia: els.referencia.value.trim() || null,
            cambio: Number(venta.calcularCambio(monto).toFixed(2))
        }]
    };

    try {
        els.btnCobrar.disabled = true;
        const respuesta = await crearVenta(payload);
        showMsg(els.msg, `Venta registrada correctamente. Folio: ${respuesta.folio}. Total: ${money(respuesta.total)}.`, 'success');
        venta.limpiar();
        els.ventaForm.reset();
        await cargarProductos();
        repintarCarrito();
        els.codigoInput.focus();
    } catch (error) {
        showMsg(els.msg, error.message || 'No se pudo registrar la venta.', 'danger');
        actualizarEstadoCobro();
    }
}

function configurarEventos() {
    els.usuarioNombre.textContent = usuario?.nombre || usuario?.username || 'Usuario';
    document.getElementById('logout').addEventListener('click', logout);
    els.btnBuscar.addEventListener('click', ejecutarBusqueda);
    els.codigoInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            buscarProductoPorCodigo(els.codigoInput.value.trim(), true);
        }
    });
    els.nombreInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            ejecutarBusqueda();
        }
    });
    els.nombreInput.addEventListener('input', ejecutarBusqueda);
    els.monto.addEventListener('input', actualizarCambio);
    els.btnVaciar.addEventListener('click', () => {
        if (!venta.estaVacia() && window.confirm('¿Vaciar carrito de venta?')) {
            venta.limpiar();
            repintarCarrito();
            els.codigoInput.focus();
        }
    });
    els.ventaForm.addEventListener('submit', (event) => {
        event.preventDefault();
        finalizarVenta();
    });

    els.scannerModal.addEventListener('shown.bs.modal', async () => {
        setScannerMessage('Solicitando permiso de cámara...', 'info');
        try {
            const engine = await startScanner(els.scannerVideo, async (codigo) => {
                els.codigoInput.value = codigo;
                setScannerMessage(`Código detectado: ${codigo}`, 'success');
                await buscarProductoPorCodigo(codigo, true);
                bootstrap.Modal.getInstance(els.scannerModal)?.hide();
            });
            setScannerMessage(`Escáner activo con ${engine}. Apunta la cámara al código.`, 'info');
        } catch (error) {
            setScannerMessage(error.message || 'No fue posible abrir la cámara.', 'danger');
        }
    });
    els.scannerModal.addEventListener('hidden.bs.modal', () => {
        stopScanner();
        els.scannerVideo.srcObject = null;
        els.codigoInput.focus();
    });
}

async function init() {
    configurarEventos();
    repintarCarrito();
    await Promise.all([cargarProductos(), cargarTurnoActivo(), cargarMetodosPago()]);
    els.codigoInput.focus();
}

init();
