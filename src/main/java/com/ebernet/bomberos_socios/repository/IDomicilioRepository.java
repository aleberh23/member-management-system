package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDomicilioRepository extends JpaRepository<Domicilio, Long>{
    
}
