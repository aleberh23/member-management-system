package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Categoria;
import java.util.List;

public interface ICategoriaService {
    
    public List<Categoria> findAllCategorias();
    public Categoria findByNombre(String nombre);
    public Categoria findById(Long id);
    public Long findLastId();
    public void save(Categoria cat);
    
}
