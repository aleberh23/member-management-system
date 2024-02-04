package com.ebernet.bomberos_socios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReciboDTO {
    private String nroSocio;
    private String nombreTitular;
    private String direccion;
    private String periodo;
    private String categoria;
    private String importe;
    
}
