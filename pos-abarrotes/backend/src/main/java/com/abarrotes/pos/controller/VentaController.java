package com.abarrotes.pos.controller;

import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService service;
    public VentaController(VentaService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public VentaResponse crear(@Valid @RequestBody VentaRequest request) { return service.crear(request); }
    @GetMapping public List<VentaResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public VentaResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @GetMapping("/turno/{idTurno}") public List<VentaResponse> turno(@PathVariable Long idTurno) { return service.porTurno(idTurno); }
}
