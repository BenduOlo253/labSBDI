package com.abarrotes.pos.service;

import com.abarrotes.pos.exception.ApiException;
import com.abarrotes.pos.model.dto.*;
import com.abarrotes.pos.model.entity.*;
import com.abarrotes.pos.model.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CajaService {
    private final CajaRepository cajaRepository; private final UsuarioRepository usuarioRepository; private final TurnoCajaRepository turnoRepository; private final VentaRepository ventaRepository;
    public CajaService(CajaRepository cajaRepository, UsuarioRepository usuarioRepository, TurnoCajaRepository turnoRepository, VentaRepository ventaRepository) { this.cajaRepository=cajaRepository; this.usuarioRepository=usuarioRepository; this.turnoRepository=turnoRepository; this.ventaRepository=ventaRepository; }

    @Transactional
    public TurnoCajaResponse abrir(AbrirCajaRequest request) {
        turnoRepository.findByCaja_IdCajaAndEstado(request.idCaja(), "ABIERTO").ifPresent(t -> { throw new ApiException(HttpStatus.BAD_REQUEST, "Caja ya abierta", "La caja ya tiene el turno " + t.idTurno + " abierto"); });
        Caja caja = cajaRepository.findById(request.idCaja()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Caja no encontrada", "No existe caja " + request.idCaja()));
        Usuario usuario = usuarioRepository.findById(request.idUsuario()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado", "No existe usuario " + request.idUsuario()));
        TurnoCaja turno = new TurnoCaja(); turno.caja=caja; turno.usuarioApertura=usuario; turno.montoInicial=request.montoInicial(); return toResponse(turnoRepository.save(turno));
    }

    @Transactional
    public TurnoCajaResponse cerrar(CerrarCajaRequest request) {
        TurnoCaja turno = turnoRepository.findById(request.idTurno()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Turno no encontrado", "No existe turno " + request.idTurno()));
        if (!"ABIERTO".equals(turno.estado)) throw new ApiException(HttpStatus.BAD_REQUEST, "Turno cerrado", "El turno no está abierto");
        Usuario usuario = usuarioRepository.findById(request.idUsuario()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado", "No existe usuario " + request.idUsuario()));
        BigDecimal ventas = ventaRepository.findByTurno_IdTurnoOrderByFechaDesc(turno.idTurno).stream().filter(v -> "PAGADA".equals(v.estado)).map(v -> v.total).reduce(BigDecimal.ZERO, BigDecimal::add);
        turno.usuarioCierre=usuario; turno.fechaCierre=LocalDateTime.now(); turno.montoFinalSistema=turno.montoInicial.add(ventas); turno.montoFinalContado=request.montoFinalContado(); turno.diferencia=turno.montoFinalContado.subtract(turno.montoFinalSistema); turno.estado="CERRADO"; return toResponse(turnoRepository.save(turno));
    }

    public TurnoCajaResponse turnoActivo(Long idCaja) { return toResponse(turnoRepository.findByCaja_IdCajaAndEstado(idCaja, "ABIERTO").orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe turno abierto", "La caja " + idCaja + " no tiene turno abierto"))); }
    public TurnoCaja validarTurnoAbierto(Long idTurno, Long idCaja) { TurnoCaja turno = turnoRepository.findById(idTurno).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe turno abierto", "No existe turno " + idTurno)); if (!"ABIERTO".equals(turno.estado) || !turno.caja.idCaja.equals(idCaja)) throw new ApiException(HttpStatus.BAD_REQUEST, "No existe turno abierto", "Debe abrir caja antes de vender"); return turno; }
    private TurnoCajaResponse toResponse(TurnoCaja t) { return new TurnoCajaResponse(t.idTurno, t.caja.idCaja, t.caja.nombre, t.usuarioApertura.idUsuario, t.usuarioApertura.nombre, t.fechaApertura, t.fechaCierre, t.montoInicial, t.montoFinalSistema, t.montoFinalContado, t.diferencia, t.estado); }
}
