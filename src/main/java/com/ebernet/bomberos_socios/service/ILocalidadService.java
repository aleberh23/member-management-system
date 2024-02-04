package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Localidad;
import java.util.List;

public interface ILocalidadService {
    public List<Localidad> findAllLocalidades();
    public Localidad findByNombre(String nombre);
    
}
