package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.repository.ICategoriaRepository;
import com.ebernet.bomberos_socios.repository.ICobradorRepository;
import com.ebernet.bomberos_socios.repository.IDomicilioRepository;
import com.ebernet.bomberos_socios.repository.ILocalidadRepository;
import com.ebernet.bomberos_socios.repository.ISocioAdherenteRepository;
import com.ebernet.bomberos_socios.repository.ISocioTitularRepository;
import com.ebernet.bomberos_socios.repository.ITipoDocumentoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Sort;

@Service
public class SocioTitularService implements ISocioTitularService{
    
    @Autowired
    private ISocioTitularRepository sociotitrep;
    
    @Autowired
    private ISocioAdherenteRepository socioadrep;
    
    @Autowired
    private ITipoDocumentoRepository  tipodocrep;
    
    @Autowired
    private ICobradorRepository cobradorrep;
    
    @Autowired 
    private ICategoriaRepository categoriarep;
    
    @Autowired
    private IDomicilioRepository domiciliorep;
    
    @Autowired 
    private ILocalidadRepository localidadrep;

    @Override
    public void createSocioTitular(SocioTitular socio) {
        sociotitrep.save(socio);
    }

    @Override
    public SocioTitular findById(Long id) {
        return sociotitrep.findById(id).orElse(null);
    }

    @Override
    public List<SocioTitular> findAllSociosTitulares() {
        return sociotitrep.findAll(Sort.by("nroSocio"));
    }

    @Override
    public long count() {
        return sociotitrep.count();
    }

    @Override
    public Page<SocioTitular> findAllSociosTitulares(int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("nroSocio"));
  
        return sociotitrep.findAll(paging);
    }

    @Override
    public Page<SocioTitular> findByFilter(String filterText, boolean deBaja, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("nroSocio")); 
        return sociotitrep.findByFilter(filterText, deBaja, paging);
    }

    @Override
    public long countByFilter(String filterText, boolean deBaja) {
        return sociotitrep.countByFilter(filterText, deBaja);
    }

    @Override
    public List<SocioTitular> findAllSociosTitularesActivos() {
        return sociotitrep.findAllActivos();
    }

    @Override
    public List<SocioTitular> findAllSociosTitularesByAnioIngreso(int anio) {
        return sociotitrep.findAllByAnioIngreso(anio);
    }

    @Override
    public long getLastNroSocio() {
        return sociotitrep.getLastNroSocio();
    }
            
    
}
