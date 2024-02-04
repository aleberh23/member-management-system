package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.model.TipoBaja;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.service.ITipoBajaService;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BajaSocioController implements Initializable {
    
    private SocioTitular socio;
    
    @Autowired
    private ISocioTitularService sociotitser;
    
    @Autowired
    private ITipoBajaService tipobajaser;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private DatePicker fechaBaja;
    
    @FXML
    private ComboBox<TipoBaja> cmbxTipoBaja;
    
    @FXML
    private Button btnBaja;
    
    @FXML
    private Button btnCancelar;
    
    //metodo baja
    @FXML
    private void bajaSocio(){
        socio.setBaja(true);
        socio.setFechaBaja(fechaBaja.getValue());
        socio.setTipoBaja(cmbxTipoBaja.getValue());
        
        //SE UTILIZA EL MISMO METODO PARA CREAR QUE PARA EDITAR, ME DIO PAJA
        sociotitser.createSocioTitular(socio);
        
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
    //converter
    //tipo baja
    private final StringConverter<TipoBaja> converterTipoBaja = new StringConverter<TipoBaja>() {
        @Override
        public String toString(TipoBaja tipobaja) {
            return (tipobaja != null) ? tipobaja.getNombre() : "";
        }

        @Override
        public TipoBaja fromString(String string) {
            return tipobajaser.findByNombre(string);
        }
    };
    
    //fecha
    private final StringConverter<LocalDate> converter = new StringConverter<>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };
    
    private boolean validarCamposCompletos() {
        boolean formatoFechaBajaCorrecto = fechaBaja.getEditor().getText().matches("\\d{2}/\\d{2}/\\d{4}");
        boolean tipoBajaSeleccionado = cmbxTipoBaja.getSelectionModel().getSelectedItem()!=null;
        
        return formatoFechaBajaCorrecto && tipoBajaSeleccionado;
    }
    
    //habilotar o deshabilitar btnBaja
    private void validarBtnBaja() {
        if (validarCamposCompletos() == true) {
            System.out.println("CAMPOS COMPLETOS TRUE");
            btnBaja.setDisable(false);
        } else {
            System.out.println("CAMPOS COMPLETOS FALSE");
            btnBaja.setDisable(true);
        }
    }    
    
    public void initData(SocioTitular socioTit){
        this.socio = socioTit;
        lblTitulo.setText("Baja de socio: "+socio.getNroSocio()+" - "+socio.getNombreCompleto());
        
        // Obtén el Stage desde la escena
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Configura la propiedad de resizable a false
        stage.setResizable(false);
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //llamar a validacion de campos.
        validarBtnBaja();
        //cuando se modifica llamar a validacion de campos
        //fechaBaja
        fechaBaja.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validarBtnBaja());
        fechaBaja.valueProperty().addListener((observable, oldValue, newValue) -> validarBtnBaja());
        //combobox
        cmbxTipoBaja.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnBaja());
        //llenar cmbx tipo baja
        //traer lista
        List<TipoBaja> tiposbaja = tipobajaser.findAllTipoBaja();
        // Convertir la lista a una ObservableList
        ObservableList<TipoBaja> tiposbajaObservableList = FXCollections.observableArrayList(tiposbaja);
        // Asignar la lista al ComboBox
        cmbxTipoBaja.setConverter(converterTipoBaja);
        cmbxTipoBaja.setItems(tiposbajaObservableList);      
    }    
    
}
