package com.abarrotes.pos.service;

import com.abarrotes.pos.model.dto.MetodoPagoResponse;
import com.abarrotes.pos.model.entity.MetodoPago;
import com.abarrotes.pos.model.repository.MetodoPagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodoPagoService {
    private final MetodoPagoRepository repository;

    public MetodoPagoService(MetodoPagoRepository repository) {
        this.repository = repository;
    }

    public List<MetodoPagoResponse> listarActivos() {
        return repository.findByActivoTrueOrderByNombreAsc().stream().map(this::toResponse).toList();
    }

    private MetodoPagoResponse toResponse(MetodoPago metodo) {
        return new MetodoPagoResponse(metodo.idMetodoPago, metodo.nombre, metodo.requiereReferencia, metodo.activo);
    }
}
