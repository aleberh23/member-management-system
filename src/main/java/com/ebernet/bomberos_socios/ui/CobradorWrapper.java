package com.ebernet.bomberos_socios.ui;

import com.ebernet.bomberos_socios.model.Cobrador;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class CobradorWrapper {
    
    private final Cobrador cobrador;
    
    public CobradorWrapper(Cobrador cobrador){
        this.cobrador = cobrador;
    }
    
    public StringProperty idCobradorProperty(){
        return new SimpleStringProperty((cobrador.getIdCobrador() != null) ? cobrador.getIdCobrador().toString() : "");
    }
    
    public StringProperty nombreProperty(){
        return new SimpleStringProperty((cobrador.getNombre() != null) ? cobrador.getNombre() : "");
    }
    
    public StringProperty nroTelefonoProperty(){
        return new SimpleStringProperty((cobrador.getNroTelefono() != null) ? cobrador.getNroTelefono().toString() : "");
    }
    
}
