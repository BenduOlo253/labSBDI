package com.abarrotes.pos.service;

import com.abarrotes.pos.exception.ApiException;
import com.abarrotes.pos.model.dto.AjusteInventarioRequest;
import com.abarrotes.pos.model.dto.InventarioResponse;
import com.abarrotes.pos.model.entity.*;
import com.abarrotes.pos.model.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository; private final ProductoRepository productoRepository; private final SucursalRepository sucursalRepository; private final UsuarioRepository usuarioRepository; private final MovimientoInventarioRepository movimientoRepository;
    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository, SucursalRepository sucursalRepository, UsuarioRepository usuarioRepository, MovimientoInventarioRepository movimientoRepository) { this.inventarioRepository=inventarioRepository; this.productoRepository=productoRepository; this.sucursalRepository=sucursalRepository; this.usuarioRepository=usuarioRepository; this.movimientoRepository=movimientoRepository; }

    public List<InventarioResponse> listar() { return inventarioRepository.findByProducto_ActivoTrueOrderByProducto_NombreAsc().stream().map(this::toResponse).toList(); }
    public InventarioResponse porProducto(Long idProducto) { return toResponse(inventarioRepository.findByProducto_IdProducto(idProducto).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventario no encontrado", "No hay inventario para producto " + idProducto))); }
    public List<InventarioResponse> bajoStock() { return listar().stream().filter(i -> i.existencia().compareTo(i.stockMinimo()) <= 0).toList(); }

    @Transactional
    public InventarioResponse ajustar(AjusteInventarioRequest request) {
        Inventario inv = inventarioRepository.findBySucursal_IdSucursalAndProducto_IdProducto(request.idSucursal(), request.idProducto()).orElseGet(() -> nuevoInventario(request));
        Usuario usuario = usuarioRepository.findById(request.idUsuario()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado", "No existe usuario " + request.idUsuario()));
        BigDecimal anterior = inv.existencia;
        boolean salida = request.tipoMovimiento().equalsIgnoreCase("SALIDA");
        BigDecimal nueva = salida ? anterior.subtract(request.cantidad()) : anterior.add(request.cantidad());
        if (nueva.compareTo(BigDecimal.ZERO) < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Inventario insuficiente", "La salida excede la existencia disponible");
        inv.existencia = nueva; inv.fechaActualizacion = LocalDateTime.now(); inventarioRepository.save(inv);
        registrarMovimiento(inv.sucursal, inv.producto, usuario, request.tipoMovimiento().toUpperCase(), request.cantidad(), anterior, nueva, "AJUSTE", null, request.motivo());
        return toResponse(inv);
    }

    public void registrarMovimiento(Sucursal sucursal, Producto producto, Usuario usuario, String tipo, BigDecimal cantidad, BigDecimal anterior, BigDecimal nueva, String referenciaTipo, Long referenciaId, String motivo) {
        MovimientoInventario mov = new MovimientoInventario(); mov.sucursal=sucursal; mov.producto=producto; mov.usuario=usuario; mov.tipoMovimiento=tipo; mov.cantidad=cantidad; mov.existenciaAnterior=anterior; mov.existenciaNueva=nueva; mov.referenciaTipo=referenciaTipo; mov.referenciaId=referenciaId; mov.motivo=motivo; movimientoRepository.save(mov);
    }

    private Inventario nuevoInventario(AjusteInventarioRequest r) { Inventario i = new Inventario(); i.sucursal = sucursalRepository.findById(r.idSucursal()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Sucursal no encontrada", "No existe sucursal " + r.idSucursal())); i.producto = productoRepository.findById(r.idProducto()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado", "No existe producto " + r.idProducto())); i.existencia=BigDecimal.ZERO; return i; }
    private InventarioResponse toResponse(Inventario i) { return new InventarioResponse(i.idInventario, i.sucursal.idSucursal, i.sucursal.nombre, i.producto.idProducto, i.producto.codigoBarras, i.producto.nombre, i.existencia, i.producto.stockMinimo, i.fechaActualizacion); }
}
