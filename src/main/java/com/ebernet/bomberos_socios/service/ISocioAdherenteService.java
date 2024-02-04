package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import java.util.List;

public interface ISocioAdherenteService {
    public void saveSocioAdherente (SocioAdherente socioadh);   
    public List<SocioAdherente> findAllByIdTitular(Long idTitular);
    public boolean tieneSociosMenores(Long nroSocioTitular);
    public boolean tieneSociosMayores(Long nroSocioTitular);
    public boolean tieneSocios(Long nroSocioTitular);
}
