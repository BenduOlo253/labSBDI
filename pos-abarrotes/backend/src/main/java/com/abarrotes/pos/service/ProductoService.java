package com.abarrotes.pos.service;

import com.abarrotes.pos.exception.ApiException;
import com.abarrotes.pos.model.dto.ProductoRequest;
import com.abarrotes.pos.model.dto.ProductoResponse;
import com.abarrotes.pos.model.entity.*;
import com.abarrotes.pos.model.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadRepository;
    private final ImpuestoRepository impuestoRepository;
    private final SucursalRepository sucursalRepository;
    private final InventarioRepository inventarioRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, UnidadMedidaRepository unidadRepository, ImpuestoRepository impuestoRepository, SucursalRepository sucursalRepository, InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository; this.categoriaRepository = categoriaRepository; this.unidadRepository = unidadRepository; this.impuestoRepository = impuestoRepository; this.sucursalRepository = sucursalRepository; this.inventarioRepository = inventarioRepository;
    }

    public List<ProductoResponse> listar() { return productoRepository.findByActivoTrueOrderByNombreAsc().stream().map(this::toResponse).toList(); }
    public ProductoResponse obtener(Long id) { return toResponse(findProducto(id)); }
    public ProductoResponse obtenerPorCodigo(String codigo) { return toResponse(productoRepository.findByCodigoBarrasAndActivoTrue(codigo).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado", "No existe producto con código " + codigo))); }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        aplicar(producto, request);
        Producto guardado = productoRepository.save(producto);
        sucursalRepository.findAll().forEach(sucursal -> {
            Inventario inventario = new Inventario();
            inventario.sucursal = sucucursal(sucursal);
            inventario.producto = guardado;
            inventario.existencia = BigDecimal.ZERO;
            inventarioRepository.save(inventario);
        });
        return toResponse(guardado);
    }
    private Sucursal sucucursal(Sucursal s){ return s; }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) { Producto producto = findProducto(id); aplicar(producto, request); return toResponse(productoRepository.save(producto)); }

    @Transactional
    public void desactivar(Long id) { Producto producto = findProducto(id); producto.activo = false; productoRepository.save(producto); }

    public Producto findProducto(Long id) { return productoRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado", "No existe producto con id " + id)); }

    private void aplicar(Producto producto, ProductoRequest request) {
        producto.codigoBarras = request.codigoBarras(); producto.nombre = request.nombre(); producto.descripcion = request.descripcion();
        producto.categoria = categoriaRepository.findById(request.idCategoria()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Categoría no encontrada", "No existe categoría " + request.idCategoria()));
        producto.unidad = unidadRepository.findById(request.idUnidad()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Unidad no encontrada", "No existe unidad " + request.idUnidad()));
        producto.impuesto = impuestoRepository.findById(request.idImpuesto()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Impuesto no encontrado", "No existe impuesto " + request.idImpuesto()));
        producto.precioCompra = request.precioCompra(); producto.precioVenta = request.precioVenta(); producto.stockMinimo = request.stockMinimo(); producto.stockMaximo = request.stockMaximo();
        producto.controlaInventario = request.controlaInventario() == null || request.controlaInventario(); producto.activo = request.activo() == null || request.activo();
    }

    private ProductoResponse toResponse(Producto p) { return new ProductoResponse(p.idProducto, p.codigoBarras, p.nombre, p.descripcion, p.categoria.idCategoria, p.categoria.nombre, p.unidad.idUnidad, p.unidad.abreviatura, p.impuesto.idImpuesto, p.impuesto.nombre, p.impuesto.porcentaje, p.precioCompra, p.precioVenta, p.stockMinimo, p.stockMaximo, p.controlaInventario, p.activo); }
}
