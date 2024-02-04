package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.TipoBaja;
import com.ebernet.bomberos_socios.repository.ITipoBajaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoBajaService implements ITipoBajaService {
    
    @Autowired
    private ITipoBajaRepository tipobajarep;

    @Override
    public TipoBaja findByNombre(String nombre) {
        return tipobajarep.findByNombre(nombre);
    }

    @Override
    public List<TipoBaja> findAllTipoBaja() {
        return tipobajarep.findAll();
    }
    
}
