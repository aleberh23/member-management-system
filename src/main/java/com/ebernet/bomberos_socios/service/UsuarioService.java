package com.ebernet.bomberos_socios.service;

import com.ebernet.bomberos_socios.model.Usuario;
import com.ebernet.bomberos_socios.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService{
    
    @Autowired
    private IUsuarioRepository usuariorep;

    @Override
    public boolean validar(String user, String pass) {
        if(usuariorep.findUserByNombre(user)==null){
            return false;
        }else{
            Usuario usr = usuariorep.findUserByNombre(user);
            if(usr.getContrasenia().equals(pass)){
                return true;
            }else{
                return false;
            }
        }
        
    }
    
}
