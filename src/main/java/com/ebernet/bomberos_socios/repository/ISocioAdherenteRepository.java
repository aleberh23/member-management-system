package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISocioAdherenteRepository extends JpaRepository<SocioAdherente, Long> {

    @Query("SELECT sa FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1")
    public List<SocioAdherente> findBySocioTitular(Long nroSocioTitular);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.fechaNacimiento >= ?2 AND sa.baja = false")  
    public Long countSociosMenores(Long nroSocioTitular, LocalDate fechaLimite);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.fechaNacimiento < ?2 AND sa.vinculo.nombre = 'Hijo/a' AND sa.baja = false")
    public Long countSociosMayores(Long nroSocioTitular, LocalDate fechaLimite);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.baja = false")
    public Long countSocios(Long nroSocioTitular);
}
