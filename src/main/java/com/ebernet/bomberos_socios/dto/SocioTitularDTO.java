package com.ebernet.bomberos_socios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocioTitularDTO {
    private String nombreCompleto;
    private String nroDocumento;
    private String fechaIngreso;
    private String localidad;    
}
