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
    @Query("SELECT sa FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.baja=false")
    public List<SocioAdherente> findActivosBySocioTitular(Long nroSocioTitular);
    @Query("SELECT sa FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.baja=false AND sa.vinculo.id = 3")
    public List<SocioAdherente> findHijosActivosBySocioTitular(Long nroSocioTitular);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.fechaNacimiento >= ?2 AND sa.baja = false")  
    public Long countSociosMenores(Long nroSocioTitular, LocalDate fechaLimite);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.fechaNacimiento < ?2 AND sa.vinculo.nombre = 'Hijo/a' AND sa.baja = false")
    public Long countSociosMayores(Long nroSocioTitular, LocalDate fechaLimite);
    @Query("SELECT COUNT(sa) FROM socio_adherente sa WHERE sa.socioTitular.nroSocio = ?1 AND sa.baja = false")
    public Long countSocios(Long nroSocioTitular);
    @Query("SELECT sa FROM socio_adherente sa WHERE DATEADD(YEAR, 18, sa.fechaNacimiento) BETWEEN ?1 AND ?2 AND sa.baja = false AND sa.vinculo.id = 3")
    public List<SocioAdherente> findSociosCumplen18(LocalDate fechaInicio, LocalDate fechaFin);
}
