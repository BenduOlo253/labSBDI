package com.abarrotes.pos.controller;

import com.abarrotes.pos.model.dto.MetodoPagoResponse;
import com.abarrotes.pos.service.MetodoPagoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {
    private final MetodoPagoService service;

    public MetodoPagoController(MetodoPagoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MetodoPagoResponse> listar() {
        return service.listarActivos();
    }
}
