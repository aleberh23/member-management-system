package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Deuda;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.repository.IDeudaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeudaService implements IDeudaService {

    @Autowired
    private IDeudaRepository deudarep;

    @Override
    public List<Deuda> findByAllBySocioTitular(Long nroSocio) {
        return deudarep.findAllBySocioTitular(nroSocio);
    }

    @Override
    public Deuda findById(Long idDeuda) {
        return deudarep.findById(idDeuda).orElse(null);
    }

    @Override
    public void saveDeuda(Deuda deuda) {
        deudarep.save(deuda);
    }

    @Override
    public int countDeudasBySocioTitular(SocioTitular sociotit) {
        return deudarep.countDeudasBySocioTitular(sociotit.getNroSocio());
    }

}
