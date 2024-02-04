package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ILocalidadRepository extends JpaRepository<Localidad, Long>{
    @Query("SELECT l FROM localidad l WHERE l.nombre = ?1")
    public Localidad findByNombre(String nombre);
    
}
