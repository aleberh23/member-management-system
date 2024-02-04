package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Vinculo;
import java.util.List;

public interface IVinculoService {
    public List<Vinculo> findAllVinculos();
    public Vinculo findByNombre(String nombreVinculo);
}
