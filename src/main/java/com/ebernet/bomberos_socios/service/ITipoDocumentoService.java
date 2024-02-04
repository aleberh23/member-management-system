package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.TipoDocumento;
import java.util.List;

public interface ITipoDocumentoService {
    public List<TipoDocumento> findAllTipos();
    public TipoDocumento findByNombre(String nombre);
}
