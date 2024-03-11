package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.repository.ISocioAdherenteRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocioAdherenteService implements ISocioAdherenteService{
    
    @Autowired
    private ISocioAdherenteRepository socioadhrep;

    @Override
    public void saveSocioAdherente(SocioAdherente socioadh) {
        socioadhrep.save(socioadh);
    }

    @Override
    public List<SocioAdherente> findAllByIdTitular(Long idTitular) {
        return socioadhrep.findBySocioTitular(idTitular);
    }

    @Override
    public boolean tieneSociosMenores(Long nroSocioTitular) {
        LocalDate fechaLimite = LocalDate.now().minusYears(18); 
        Long cantidad = socioadhrep.countSociosMenores(nroSocioTitular, fechaLimite);
        if(cantidad>=1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean tieneSociosMayores(Long nroSocioTitular) {
        LocalDate fechaLimite = LocalDate.now().minusYears(18); 
        Long cantidad = socioadhrep.countSociosMayores(nroSocioTitular, fechaLimite);
        System.out.println("CANTIDAD DE MAYORES: "+cantidad);
        if(cantidad>=1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean tieneSocios(Long nroSocioTitular) {
        Long cantidad = socioadhrep.countSocios(nroSocioTitular);
        if(cantidad>=1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<SocioAdherente> findAllActivosByIdTitular(Long idTitular) {
        return socioadhrep.findActivosBySocioTitular(idTitular);
    }

    @Override
    public List<SocioAdherente> findSociosCumplen18() {
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        LocalDate fechaFin = LocalDate.now().plusMonths(1);
        return socioadhrep.findSociosCumplen18(fechaInicio, fechaFin);
    }

    @Override
    public List<SocioAdherente> findAllHijosActivosByIdTitular(Long idTitular) {
        return socioadhrep.findHijosActivosBySocioTitular(idTitular);
    }
    
}
