package com.ebernet.bomberos_socios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="deuda")
public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDeuda;
    private int cuota;
    private int anio;
    private int importe;
    @Column(name="fecha_generacion")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaGeneracion;
    @ManyToOne
    @JoinColumn(name = "id_socio_titular")
    private SocioTitular socioDeudor;
    @Column(name="paga")
    private boolean paga;
}
