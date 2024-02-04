package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Domicilio;
import com.ebernet.bomberos_socios.repository.IDomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomicilioService implements IDomicilioService{
    @Autowired
    private IDomicilioRepository domrep;

    @Override
    public void saveDomicilio(Domicilio domicilio) {
        domrep.save(domicilio);
    }
    
   
}
