package com.ebernet.bomberos_socios.repository;

import com.ebernet.bomberos_socios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    @Query("SELECT us FROM usuario us WHERE us.usuario = ?1")
    public Usuario findUserByNombre(String nombre);
}
