package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "unidad_medida")
public class UnidadMedida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_unidad") public Long idUnidad;
    @Column(nullable=false) public String nombre;
    @Column(nullable=false) public String abreviatura;
    @Column(name="permite_decimales") public Boolean permiteDecimales = false;
}
