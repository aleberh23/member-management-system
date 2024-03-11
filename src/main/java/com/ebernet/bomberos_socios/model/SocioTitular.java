package com.ebernet.bomberos_socios.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="socio_titular")
public class SocioTitular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="nro_socio")
    private Long nroSocio;
    @Column(name="nombre_completo")
    private String nombreCompleto;
    @ManyToOne
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDoc;
    @Column(name="nro_documento")
    private Long nroDocumento;
    @Column(name="cuil")
    private Long nroCuil;
    @Column(name="fecha_nacimiento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;
    @Column(name="fecha_ingreso")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaIngreso;
    @Column(name="fecha_baja")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaBaja;
    private boolean baja;
    @ManyToOne
    @JoinColumn(name = "id_tipo_baja")
    private TipoBaja tipoBaja;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "id_cobrador")
    private Cobrador cobrador;
    @OneToMany(mappedBy = "socioDeudor", cascade = CascadeType.ALL)
    private List<Deuda> deudas;
    @OneToMany(mappedBy = "socioTitular", cascade = CascadeType.ALL)
    private List<SocioAdherente>sociosAdherentes;
    
}
