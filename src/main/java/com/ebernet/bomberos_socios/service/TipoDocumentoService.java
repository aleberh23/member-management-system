package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.TipoDocumento;
import com.ebernet.bomberos_socios.repository.ITipoDocumentoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoDocumentoService implements ITipoDocumentoService{
    
    @Autowired
    private ITipoDocumentoRepository tipodocrep;

    @Override
    public List<TipoDocumento> findAllTipos() {
        return tipodocrep.findAll();
    }

    @Override
    public TipoDocumento findByNombre(String nombre) {
        return tipodocrep.findByNombre(nombre);
    }
}
