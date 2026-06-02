package com.abarrotes.pos.controller;

import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.service.CajaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/caja")
public class CajaController {
    private final CajaService service;
    public CajaController(CajaService service) { this.service = service; }
    @PostMapping("/abrir") public TurnoCajaResponse abrir(@Valid @RequestBody AbrirCajaRequest request) { return service.abrir(request); }
    @PostMapping("/cerrar") public TurnoCajaResponse cerrar(@Valid @RequestBody CerrarCajaRequest request) { return service.cerrar(request); }
    @GetMapping("/turno-activo/{idCaja}") public TurnoCajaResponse turnoActivo(@PathVariable Long idCaja) { return service.turnoActivo(idCaja); }
}
