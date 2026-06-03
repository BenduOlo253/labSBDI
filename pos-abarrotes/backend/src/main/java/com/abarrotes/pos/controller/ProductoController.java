package com.abarrotes.pos.controller;

import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }
    @GetMapping public List<ProductoResponse> listar(@RequestParam(required = false) String buscar) { return service.listar(buscar); }
    @GetMapping("/buscar") public List<ProductoResponse> buscar(@RequestParam String texto) { return service.buscarPorNombre(texto); }
    @GetMapping("/{id}") public ProductoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @GetMapping("/codigo/{codigoBarras}") public ProductoResponse codigo(@PathVariable String codigoBarras) { return service.obtenerPorCodigo(codigoBarras); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) { return service.crear(request); }
    @PutMapping("/{id}") public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) { return service.actualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}
