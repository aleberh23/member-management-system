package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
