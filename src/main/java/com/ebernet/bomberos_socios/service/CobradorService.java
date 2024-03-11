package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Cobrador;
import com.ebernet.bomberos_socios.repository.ICobradorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CobradorService implements ICobradorService{
    
    @Autowired
    private ICobradorRepository cobradorrep;

    @Override
    public List<Cobrador> findAllCobradores() {
       return cobradorrep.findAll();
    }

    @Override
    public Cobrador findByNombre(String nombre) {
        return cobradorrep.findByNombre(nombre);
    }

    @Override
    public Cobrador findById(Long id) {
        return cobradorrep.findById(id).orElse(null);
    }

    @Override
    public void saveCobrador(Cobrador cobrador) {
        cobradorrep.save(cobrador);
    }
    
    
}
