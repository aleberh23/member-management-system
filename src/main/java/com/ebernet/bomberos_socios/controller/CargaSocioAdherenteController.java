package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.model.TipoDocumento;
import com.ebernet.bomberos_socios.model.Vinculo;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.service.ISocioAdherenteService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.service.ITipoDocumentoService;
import com.ebernet.bomberos_socios.service.IVinculoService;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CargaSocioAdherenteController implements Initializable {

    private SocioTitular sociotit;

    @Autowired
    private ISocioTitularService sociotitser;

    @Autowired
    private ISocioAdherenteService socioadhser;

    @Autowired
    private ITipoDocumentoService tipodocser;

    @Autowired
    private IVinculoService vinculoser;

    @Autowired
    private ICategoriaService categoriaser;

    @Autowired
    private IndexController index;

    //datos adherente
    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<TipoDocumento> cmbxTipoDoc;

    @FXML
    private TextField txtNroDoc;

    @FXML
    private DatePicker fechaNacimiento;

    @FXML
    private ComboBox<Vinculo> cmbxVinculo;

    //datos titular
    @FXML
    private TextField txtNroSocioTit;

    @FXML
    private TextField txtNombreTit;

    @FXML
    private TextField txtCategoriaTit;

    //botones
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    public void initData(SocioTitular socioTit) {
        this.sociotit = socioTit;
        //setear datos socio titular
        txtNroSocioTit.setText(Long.toString(sociotit.getNroSocio()));
        txtNombreTit.setText(sociotit.getNombreCompleto());
        txtCategoriaTit.setText(sociotit.getCategoria().getIdCategoria() + " - " + sociotit.getCategoria().getNombre());

    }

    // Método para validar que solo se ingresen números y limitar el máximo a 8 caracteres
    private void validarNumeroDocumento() {
        String texto = txtNroDoc.getText();
        if (!texto.matches("\\d*")) {  // Verificar que solo contiene dígitos
            txtNroDoc.setText(texto.replaceAll("[^\\d]", ""));  // Eliminar caracteres no numéricos
        }

        // Limitar la longitud a 8 caracteres
        if (texto.length() > 8) {
            txtNroDoc.setText(texto.substring(0, 8));
        }
    }

    // Método para validar que solo se ingresen letras y limitar el máximo a 60 caracteres
    private void validarNombre() {
        String texto = txtNombre.getText();
        if (!texto.matches("[a-zA-Z\\s]*")) {  // Verificar que solo contiene letras y/o espacios
            txtNombre.setText(texto.replaceAll("[^a-zA-Z\\s]", ""));  // Eliminar caracteres no alfabéticos
        }

        // Limitar la longitud a 60 caracteres
        if (texto.length() > 60) {
            txtNombre.setText(texto.substring(0, 60));
        }
    }

    // Crear un StringConverter personalizado para cambiar el formato de la fecha
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

    private final StringConverter<TipoDocumento> converterTipoDoc = new StringConverter<TipoDocumento>() {
        @Override
        public String toString(TipoDocumento tipodocumento) {
            return (tipodocumento != null) ? tipodocumento.getNombre() : "";
        }

        @Override
        public TipoDocumento fromString(String string) {
            return tipodocser.findByNombre(string);
        }
    };

    private final StringConverter<Vinculo> converterVinculo = new StringConverter<Vinculo>() {
        @Override
        public String toString(Vinculo vinculo) {
            return (vinculo != null) ? vinculo.getNombre() : "";
        }

        @Override
        public Vinculo fromString(String string) {
            return vinculoser.findByNombre(string);
        }
    };

    private boolean validarCamposCompletos() {
        // Verificar que los campos de texto no estén vacíos
        boolean nombreCompletoCompleto = !txtNombre.getText().isEmpty();
        boolean nroDocumentoCompleto = !txtNroDoc.getText().isEmpty();
        // Verificar que los ComboBox tengan elementos seleccionados
        boolean vinculoSeleccioando = cmbxVinculo.getSelectionModel().getSelectedItem() != null;
        boolean tipoDocumentoSeleccionado = cmbxTipoDoc.getSelectionModel().getSelectedItem() != null;
        // Comprobar el formato correcto de las fechas
        boolean formatoFechaNacimientoCorrecto = fechaNacimiento.getEditor().getText().matches("\\d{2}/\\d{2}/\\d{4}");
        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return nombreCompletoCompleto && nroDocumentoCompleto && vinculoSeleccioando && tipoDocumentoSeleccionado && formatoFechaNacimientoCorrecto;
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

    @FXML
    private void guardarSocio() {
        SocioAdherente socio = new SocioAdherente();
        socio.setNombreCompleto(txtNombre.getText());
        socio.setTipoDoc(cmbxTipoDoc.getValue());
        socio.setNroDocumento(Long.parseLong(txtNroDoc.getText()));
        System.out.println("Fecha nacimiento: "+fechaNacimiento.getValue());
        socio.setFechaNacimiento(fechaNacimiento.getValue());
        socio.setVinculo(cmbxVinculo.getValue());
        //setear el socio titular al adherente
        socio.setSocioTitular(sociotit);
        //guardar en la base de datos
        socioadhser.saveSocioAdherente(socio);
        //volver a vista de socios adherentes
        index.vistaSociosAdherentes(sociotit);
    }
    
    @FXML
    private void cancelarCarga(){
        index.vistaSociosAdherentes(sociotit);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGuardar.setDisable(true);
        txtNroDoc.textProperty().addListener((observable, oldValue, newValue) -> {
            validarNumeroDocumento();
            validarBtnGuardar();
        });

        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            validarNombre();
            validarBtnGuardar();
        });
        //Escuchador para cuando se ingresa sin date picker
        fechaNacimiento.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        // Agregar escuchadores de cambios para las fechas seleccionadas en los DatePickers
        fechaNacimiento.valueProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        //setear converter a fecha
        fechaNacimiento.setConverter(converter);

        //setear listeners de validacion a combo box
        cmbxTipoDoc.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxVinculo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());

        //llenar cmbx tipo documento
        //traer lista
        List<TipoDocumento> tiposdocumentos = tipodocser.findAllTipos();
        // Convertir la lista a una ObservableList
        ObservableList<TipoDocumento> tiposdocumentosObservableList = FXCollections.observableArrayList(tiposdocumentos);
        // Asignar la lista al ComboBox
        cmbxTipoDoc.setConverter(converterTipoDoc);
        cmbxTipoDoc.setItems(tiposdocumentosObservableList);

        //llenar cmbx vinculo
        //traer lista
        List<Vinculo> vinculos = vinculoser.findAllVinculos();
        // Convertir la lista a una ObservableList
        ObservableList<Vinculo> vinculosObservableList = FXCollections.observableArrayList(vinculos);
        // Asignar la lista al ComboBox
        cmbxVinculo.setConverter(converterVinculo);
        cmbxVinculo.setItems(vinculosObservableList);
    }

}
