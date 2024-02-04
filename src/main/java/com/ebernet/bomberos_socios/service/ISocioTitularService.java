package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.SocioTitular;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ISocioTitularService {
    public void createSocioTitular(SocioTitular socio);
    public SocioTitular findById(Long id);
    public List<SocioTitular> findAllSociosTitulares();
    public List<SocioTitular> findAllSociosTitularesActivos();
    public long count();
    public Page<SocioTitular> findAllSociosTitulares(int page, int size);
    public Page<SocioTitular> findByFilter(String filterText, boolean deBaja, int page, int size);
    public long countByFilter(String filterText, boolean deBaja);
}
