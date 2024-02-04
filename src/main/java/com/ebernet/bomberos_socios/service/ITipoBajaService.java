package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.TipoBaja;
import java.util.List;

public interface ITipoBajaService {
    public TipoBaja findByNombre(String nombre);
    public List<TipoBaja> findAllTipoBaja ();
    
}
