package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Cobrador;
import com.ebernet.bomberos_socios.service.ICobradorService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditarCobradorController implements Initializable{
    
    private Cobrador cobrador;
    
    @Autowired
    private ICobradorService cobradorser;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextField txtNombre, txtNroTelefono;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private Button btnCancelar, btnGuardar;
    
    @FXML
    private void guardarCobrador(){
        cobrador.setNombre(txtNombre.getText());
        cobrador.setNroTelefono(Long.parseLong(txtNroTelefono.getText()));
        
        cobradorser.saveCobrador(cobrador);
        
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
    @FXML
    private void cancelar(){
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
     private void validarNroTelefono(String nuevoValor) {
    // Utiliza una expresión regular para verificar si el texto contiene solo números
        if (!nuevoValor.matches("\\d*")) {
            // Si no son números, borra la última letra ingresada
            txtNroTelefono.setText(nuevoValor.replaceAll("[^\\d]", ""));
        }
    // Limitar la longitud a 8 caracteres
        if (txtNroTelefono.getText().length() > 15) {
            txtNroTelefono.setText(txtNroTelefono.getText().substring(0, 15));
        }
    }
    
    private boolean validarCamposCompletos() {
        // Verificar que los campos de texto no estén vacíos
        boolean nroTelefonoCompleto = !txtNroTelefono.getText().isEmpty();
        boolean nombreCompleto = !txtNombre.getText().isEmpty();
        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return nroTelefonoCompleto && nombreCompleto;
    }

    //habilotar o deshabilitar btnguardar
    private void validarBtnGuardar() {
        if (validarCamposCompletos() == true) {
            System.out.println("CAMPOS COMPLETOS TRUE");
            btnGuardar.setDisable(false);
        } else {
            System.out.println("CAMPOS COMPLETOS FALSE");
            btnGuardar.setDisable(true);
        }
    }
    
    
    public void initData(Cobrador cob){
        this.cobrador = cob;
        txtNombre.setText(cobrador.getNombre());
        txtNroTelefono.setText(cobrador.getNroTelefono().toString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtNroTelefono.textProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Llama al método de validación cada vez que el texto cambia
                validarNroTelefono(newValue);
                validarBtnGuardar();
            }
        });
        
        txtNombre.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                validarBtnGuardar();
            }
            
        });
    }
    
}
