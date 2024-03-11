package com.ebernet.bomberos_socios.dto;

import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.model.SocioTitular;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
   private int id;
   private Long nroSocio;
   private SocioTitular socioTitular;
   private SocioAdherente socioAdherente;
   private LocalDate fechaCumpleaños;   
   private Categoria catSugerida;
   private Categoria catActual;
}
