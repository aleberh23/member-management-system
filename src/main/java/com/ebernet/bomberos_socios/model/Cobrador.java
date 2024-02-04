package com.ebernet.bomberos_socios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="cobrador")
public class Cobrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cobrador")
    private Long idCobrador;
    private String nombre;
    @Column(name="nro_telefono")
    private Long nroTelefono;
    @OneToMany(mappedBy = "cobrador")
    private List<SocioTitular>socios;
            
}
