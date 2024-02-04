package com.ebernet.bomberos_socios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="tipo_baja")
public class TipoBaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoBaja;
    private String nombre;
}
