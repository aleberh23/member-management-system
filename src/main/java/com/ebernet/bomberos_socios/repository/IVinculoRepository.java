package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Vinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IVinculoRepository extends JpaRepository<Vinculo, Long>{
    @Query("SELECT v FROM vinculo v WHERE v.nombre = ?1")
    public Vinculo findByNombre(String nombre);
    
}
