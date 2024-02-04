package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoDocumentoRepository extends JpaRepository<TipoDocumento, Long>{
     @Query("SELECT t FROM tipo_documento t WHERE t.nombre = ?1")
     public TipoDocumento findByNombre(String nombre);
    
}
