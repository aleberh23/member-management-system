package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long>{
    @Query("SELECT c FROM categoria c WHERE c.nombre = ?1")
    public Categoria findByNombre(String nombre);
    
}
