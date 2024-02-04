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
@Entity(name="socio_adherente")
public class SocioAdherente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="nro_socio_adherente")
    private Long nroSocio;
    @Column(name="nombre_completo")
    private String nombreCompleto;
    @ManyToOne
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDoc;
    @Column(name="nro_documento")
    private Long nroDocumento;
    @Column(name="fecha_nacimiento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;
    @Column(name="fecha_baja")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaBaja;
    private boolean baja;
    @ManyToOne
    @JoinColumn(name = "id_tipo_baja")
    private TipoBaja tipoBaja;
    @ManyToOne
    @JoinColumn(name = "id_socio_titular")
    private SocioTitular socioTitular;
    @ManyToOne
    @JoinColumn(name = "id_vinculo")
    private Vinculo vinculo;
}
