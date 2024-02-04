package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Localidad;
import com.ebernet.bomberos_socios.repository.ILocalidadRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalidadService implements ILocalidadService{
    @Autowired
    private ILocalidadRepository localidadrep;

    @Override
    public List<Localidad> findAllLocalidades() {
        return localidadrep.findAll();
    }

    @Override
    public Localidad findByNombre(String nombre) {
        return localidadrep.findByNombre(nombre);
    }
}
