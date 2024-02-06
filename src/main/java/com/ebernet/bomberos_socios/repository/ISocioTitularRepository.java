package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.SocioTitular;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISocioTitularRepository extends JpaRepository<SocioTitular, Long> {

    public Page<SocioTitular> findAll(Pageable pageable);
    
    @Query("SELECT st FROM socio_titular st WHERE st.baja=false")
    public List<SocioTitular> findAllActivos();

    @Query("SELECT st FROM socio_titular st "
            + "JOIN st.categoria c "
            + "JOIN st.domicilio d "
            + "JOIN st.cobrador co "
            + "WHERE LOWER(CONCAT(st.nombreCompleto, st.nroSocio, st.nroDocumento, "
            + "c.id, c.nombre, "
            + "TO_CHAR(st.fechaIngreso, 'DD/MM/YYYY'), "
            + "TO_CHAR(st.fechaNacimiento, 'DD/MM/YYYY'), "
            + "d.calle, d.nro)) "
            + " LIKE %?1% AND st.baja = ?2")
    public Page<SocioTitular> findByFilter(String filterText, boolean deBaja, Pageable pageable);

    @Query("SELECT COUNT(st) FROM socio_titular st "
            + "JOIN st.categoria c "
            + "JOIN st.domicilio d "
            + "JOIN st.cobrador co "
            + "WHERE LOWER(CONCAT(st.nombreCompleto, st.nroSocio, st.nroDocumento, "
            + "c.id, c.nombre, "
            + "TO_CHAR(st.fechaIngreso, 'DD/MM/YYYY'), "
            + "TO_CHAR(st.fechaNacimiento, 'DD/MM/YYYY'), "
            + "d.calle, d.nro)) "
            +  "LIKE %?1% AND st.baja = ?2")
    public Long countByFilter(String filtro, boolean baja);
    
    @Query("SELECT st FROM socio_titular st WHERE YEAR(st.fechaIngreso) = ?1")  
    public List<SocioTitular> findAllByAnioIngreso(int anio);
    
    @Query("SELECT MAX(st.nroSocio) FROM socio_titular st")
    public long getLastNroSocio();
}
