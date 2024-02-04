package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Cobrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICobradorRepository extends JpaRepository<Cobrador, Long>{
    @Query("SELECT c FROM cobrador c WHERE c.nombre = ?1")
    public Cobrador findByNombre(String nombre);   
    
}
