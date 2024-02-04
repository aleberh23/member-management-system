package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Deuda;
import com.ebernet.bomberos_socios.model.SocioTitular;
import java.util.List;

public interface IDeudaService {
    
    public List<Deuda> findByAllBySocioTitular(Long nroSocio);
    public Deuda findById(Long idDeuda);
    public void saveDeuda(Deuda deuda);
    public int countDeudasBySocioTitular(SocioTitular sociotit);
    
}
