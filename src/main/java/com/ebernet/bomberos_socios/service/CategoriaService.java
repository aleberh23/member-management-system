package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.repository.ICategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService{
    
    @Autowired
    private ICategoriaRepository caterep;

    @Override
    public List<Categoria> findAllCategorias() {
        return caterep.findAll();
    }

    @Override
    public Categoria findByNombre(String nombre) {
        return caterep.findByNombre(nombre);
    }

    @Override
    public Categoria findById(Long id) {
        return caterep.findById(id).orElse(null);
    }

    @Override
    public Long findLastId() {
        return caterep.findLastId();
    }

    @Override
    public void save(Categoria cat) {
        caterep.save(cat);
    }
    
    
}
