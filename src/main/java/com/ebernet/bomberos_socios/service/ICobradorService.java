package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Cobrador;
import java.util.List;

public interface ICobradorService {
    public List<Cobrador> findAllCobradores();
    public Cobrador findByNombre(String nombre);
}
