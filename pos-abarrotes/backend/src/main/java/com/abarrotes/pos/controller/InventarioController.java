package com.abarrotes.pos.controller;

import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    private final InventarioService service;
    public InventarioController(InventarioService service) { this.service = service; }
    @GetMapping public List<InventarioResponse> listar() { return service.listar(); }
    @GetMapping("/producto/{idProducto}") public InventarioResponse producto(@PathVariable Long idProducto) { return service.porProducto(idProducto); }
    @GetMapping("/bajo-stock") public List<InventarioResponse> bajoStock() { return service.bajoStock(); }
    @PostMapping("/ajuste") public InventarioResponse ajustar(@Valid @RequestBody AjusteInventarioRequest request) { return service.ajustar(request); }
}
