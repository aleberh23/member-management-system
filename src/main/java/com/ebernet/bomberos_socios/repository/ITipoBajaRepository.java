package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.TipoBaja;
import com.ebernet.bomberos_socios.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoBajaRepository extends JpaRepository<TipoBaja, Long>{
    @Query("SELECT t FROM tipo_baja t WHERE t.nombre = ?1")
    public TipoBaja findByNombre(String nombre);
}
