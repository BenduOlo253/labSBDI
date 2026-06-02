package com.abarrotes.pos.service;

import com.abarrotes.pos.exception.ApiException;
import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.model.entity.*;
import com.abarrotes.pos.model.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository; private final VentaDetalleRepository detalleRepository; private final VentaPagoRepository pagoRepository; private final ProductoRepository productoRepository; private final InventarioRepository inventarioRepository; private final SucursalRepository sucursalRepository; private final CajaRepository cajaRepository; private final UsuarioRepository usuarioRepository; private final MetodoPagoRepository metodoPagoRepository; private final CajaService cajaService; private final InventarioService inventarioService;
    public VentaService(VentaRepository ventaRepository, VentaDetalleRepository detalleRepository, VentaPagoRepository pagoRepository, ProductoRepository productoRepository, InventarioRepository inventarioRepository, SucursalRepository sucursalRepository, CajaRepository cajaRepository, UsuarioRepository usuarioRepository, MetodoPagoRepository metodoPagoRepository, CajaService cajaService, InventarioService inventarioService) { this.ventaRepository=ventaRepository; this.detalleRepository=detalleRepository; this.pagoRepository=pagoRepository; this.productoRepository=productoRepository; this.inventarioRepository=inventarioRepository; this.sucursalRepository=sucursalRepository; this.cajaRepository=cajaRepository; this.usuarioRepository=usuarioRepository; this.metodoPagoRepository=metodoPagoRepository; this.cajaService=cajaService; this.inventarioService=inventarioService; }

    @Transactional
    public VentaResponse crear(VentaRequest request) {
        if (request.detalles() == null || request.detalles().isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "Venta sin productos", "Agregue al menos un producto");
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Sucursal no encontrada", "No existe sucursal " + request.idSucursal()));
        Caja caja = cajaRepository.findById(request.idCaja()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Caja no encontrada", "No existe caja " + request.idCaja()));
        Usuario usuario = usuarioRepository.findById(request.idUsuario()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado", "No existe usuario " + request.idUsuario()));
        TurnoCaja turno = cajaService.validarTurnoAbierto(request.idTurno(), request.idCaja());
        Venta venta = new Venta(); venta.folio = siguienteFolio(); venta.sucursal=sucursal; venta.caja=caja; venta.turno=turno; venta.usuario=usuario; venta.fecha=LocalDateTime.now(); venta.estado="PAGADA";
        BigDecimal subtotal = BigDecimal.ZERO, impuestoTotal = BigDecimal.ZERO;
        Venta guardada = ventaRepository.save(venta);
        for (VentaDetalleRequest detReq : request.detalles()) {
            Producto producto = productoRepository.findById(detReq.idProducto()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado", "No existe producto " + detReq.idProducto()));
            if (!Boolean.TRUE.equals(producto.activo)) throw new ApiException(HttpStatus.BAD_REQUEST, "Producto inactivo", "No se puede vender " + producto.nombre);
            Inventario inv = inventarioRepository.findBySucursal_IdSucursalAndProducto_IdProducto(request.idSucursal(), producto.idProducto).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventario no encontrado", "No hay inventario para " + producto.nombre));
            if (Boolean.TRUE.equals(producto.controlaInventario) && inv.existencia.compareTo(detReq.cantidad()) < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Inventario insuficiente", producto.nombre + " solo tiene " + inv.existencia);
            BigDecimal lineaSubtotal = producto.precioVenta.multiply(detReq.cantidad()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineaImpuesto = lineaSubtotal.multiply(producto.impuesto.porcentaje).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            VentaDetalle detalle = new VentaDetalle(); detalle.venta=guardada; detalle.producto=producto; detalle.cantidad=detReq.cantidad(); detalle.precioUnitario=producto.precioVenta; detalle.subtotal=lineaSubtotal; detalle.impuesto=lineaImpuesto; detalle.total=lineaSubtotal.add(lineaImpuesto); detalleRepository.save(detalle);
            subtotal = subtotal.add(lineaSubtotal); impuestoTotal = impuestoTotal.add(lineaImpuesto);
            if (Boolean.TRUE.equals(producto.controlaInventario)) {
                BigDecimal anterior = inv.existencia; BigDecimal nueva = anterior.subtract(detReq.cantidad()); inv.existencia=nueva; inv.fechaActualizacion=LocalDateTime.now(); inventarioRepository.save(inv);
                inventarioService.registrarMovimiento(sucursal, producto, usuario, "VENTA", detReq.cantidad(), anterior, nueva, "VENTA", guardada.idVenta, "Venta " + guardada.folio);
            }
        }
        BigDecimal total = subtotal.add(impuestoTotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pagado = request.pagos().stream().map(VentaPagoRequest::monto).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (pagado.compareTo(total) < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Pago insuficiente", "Total " + total + ", pagado " + pagado);
        guardada.subtotal=subtotal; guardada.impuestoTotal=impuestoTotal; guardada.total=total; ventaRepository.save(guardada);
        for (VentaPagoRequest pagoReq : request.pagos()) {
            MetodoPago metodo = metodoPagoRepository.findById(pagoReq.idMetodoPago()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Método de pago no encontrado", "No existe método " + pagoReq.idMetodoPago()));
            VentaPago pago = new VentaPago(); pago.venta=guardada; pago.metodoPago=metodo; pago.monto=pagoReq.monto(); pago.referencia=pagoReq.referencia(); pago.cambio=pagoReq.cambio()==null?BigDecimal.ZERO:pagoReq.cambio(); pagoRepository.save(pago);
        }
        return toResponse(guardada);
    }

    public List<VentaResponse> listar() { return ventaRepository.findAll().stream().map(this::toResponse).toList(); }
    public VentaResponse obtener(Long id) { return toResponse(ventaRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Venta no encontrada", "No existe venta " + id))); }
    public List<VentaResponse> porTurno(Long idTurno) { return ventaRepository.findByTurno_IdTurnoOrderByFechaDesc(idTurno).stream().map(this::toResponse).toList(); }
    private String siguienteFolio() { return "V-%06d".formatted(ventaRepository.count() + 1); }
    private VentaResponse toResponse(Venta v) { return new VentaResponse(v.idVenta, v.folio, v.subtotal, v.impuestoTotal, v.total, v.estado, v.fecha); }
}
