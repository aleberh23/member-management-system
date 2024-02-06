package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.service.ICategoriaService;
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
public class CargaCategoriaController implements Initializable {
    
    @Autowired
    private ICategoriaService catser;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private TextField txtNombre, txtImporte;
    
    @FXML
    private Button btnCancelar, btnGuardar;
    
    @FXML
    private void guardarCategoria(){
        Categoria cat = new Categoria();
        cat.setNombre(txtNombre.getText());
        cat.setImporte(Integer.parseInt(txtImporte.getText()));
        
        catser.save(cat);
        
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
    
    private void validarImporte(String nuevoValor) {
    // Utiliza una expresión regular para verificar si el texto contiene solo números
    if (!nuevoValor.matches("\\d*")) {
        // Si no son números, borra la última letra ingresada
        txtImporte.setText(nuevoValor.replaceAll("[^\\d]", ""));
    }
    // Limitar la longitud a 8 caracteres
        if (txtImporte.getText().length() > 10) {
            txtImporte.setText(txtImporte.getText().substring(0, 10));
        }
    }
    
    private boolean validarCamposCompletos() {
        // Verificar que los campos de texto no estén vacíos
        boolean importeCompleto = !txtImporte.getText().isEmpty();
        boolean nombreCompleto = !txtNombre.getText().isEmpty();
        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return importeCompleto && nombreCompleto;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGuardar.setDisable(true);
        lblTitulo.setText("Carga de categoria ID: " + (catser.findLastId() + 1));

        txtImporte.textProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Llama al método de validación cada vez que el texto cambia
                validarImporte(newValue);
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
