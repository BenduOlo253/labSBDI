package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "turno_caja")
public class TurnoCaja {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_turno") public Long idTurno;
    @ManyToOne(optional=false) @JoinColumn(name="id_caja") public Caja caja;
    @ManyToOne(optional=false) @JoinColumn(name="id_usuario_apertura") public Usuario usuarioApertura;
    @ManyToOne @JoinColumn(name="id_usuario_cierre") public Usuario usuarioCierre;
    @Column(name="fecha_apertura") public LocalDateTime fechaApertura = LocalDateTime.now();
    @Column(name="fecha_cierre") public LocalDateTime fechaCierre;
    @Column(name="monto_inicial", precision=12, scale=2) public BigDecimal montoInicial = BigDecimal.ZERO;
    @Column(name="monto_final_sistema", precision=12, scale=2) public BigDecimal montoFinalSistema = BigDecimal.ZERO;
    @Column(name="monto_final_contado", precision=12, scale=2) public BigDecimal montoFinalContado = BigDecimal.ZERO;
    @Column(precision=12, scale=2) public BigDecimal diferencia = BigDecimal.ZERO;
    @Column(nullable=false) public String estado = "ABIERTO";
}
