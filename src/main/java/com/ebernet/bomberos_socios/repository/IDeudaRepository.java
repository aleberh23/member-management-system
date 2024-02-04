package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Deuda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeudaRepository extends JpaRepository<Deuda, Long>{
    
    @Query("SELECT d FROM deuda d WHERE d.socioDeudor.nroSocio = ?1")
    public List<Deuda> findAllBySocioTitular(Long nroSocio);
    @Query("SELECT COUNT(d) FROM deuda d WHERE d.paga=false AND d.socioDeudor.nroSocio = ?1")
    public int countDeudasBySocioTitular(Long nroSocio);
    
}
