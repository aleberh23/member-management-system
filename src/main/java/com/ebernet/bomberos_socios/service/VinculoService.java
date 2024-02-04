package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Vinculo;
import com.ebernet.bomberos_socios.repository.IVinculoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VinculoService implements IVinculoService{
    
    @Autowired 
    private IVinculoRepository vinculorep;

    @Override
    public List<Vinculo> findAllVinculos() {
        return vinculorep.findAll();
    }

    @Override
    public Vinculo findByNombre(String nombreVinculo) {
        return vinculorep.findByNombre(nombreVinculo);
    }
    
}
