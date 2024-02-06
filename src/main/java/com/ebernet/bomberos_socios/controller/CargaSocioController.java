package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.model.Cobrador;
import com.ebernet.bomberos_socios.model.Domicilio;
import com.ebernet.bomberos_socios.model.Localidad;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.model.TipoDocumento;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.service.ICobradorService;
import com.ebernet.bomberos_socios.service.IDomicilioService;
import com.ebernet.bomberos_socios.service.ILocalidadService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.service.ITipoDocumentoService;
import jakarta.annotation.PostConstruct;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CargaSocioController implements Initializable {

    @Autowired
    private ISocioTitularService soctitser;
    @Autowired
    private ICategoriaService catser;
    @Autowired
    private ILocalidadService locser;
    @Autowired
    private ITipoDocumentoService tipodocser;
    @Autowired
    private ICobradorService cobradorser;
    @Autowired
    private IDomicilioService domser;

    @FXML
    private DatePicker fechaNacimiento;

    @FXML
    private DatePicker fechaIngreso;

    @FXML
    private BorderPane rootPane;

    @FXML
    private HBox hBoxContenido;
    
    @FXML
    private VBox leftVbox;
    
    @FXML
    private VBox vboxFormSocio;
    
    @FXML
    private VBox rightVbox;
    
    @FXML
    private VBox vboxFormDomicilio;
    
    @FXML
    private HBox bottomHbox;
    

    // Atributos FXML para la primera VBox
    @FXML
    private Label lblTitulo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Categoria> cmbxCategoria;

    @FXML
    private ComboBox<TipoDocumento> cmbxTipoDocumento;

    @FXML
    private TextField txtNroDocumento;

    @FXML
    private ComboBox<Cobrador> cmbxCobrador;

    // Atributos FXML para la segunda VBox
    @FXML
    private ImageView imageView;

    @FXML
    private Label domicilioLabel;

    @FXML
    private TextField txtCalle;

    @FXML
    private TextField txtNro;

    @FXML
    private ComboBox<Localidad> cmbxLocalidad;

    // Atributos FXML para la bottom HBox
    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnGuardar;

    // Método para validar que solo se ingresen números y limitar el máximo a 8 caracteres
    private void validarNumeroDocumento() {
        String texto = txtNroDocumento.getText();
        if (!texto.matches("\\d*")) {  // Verificar que solo contiene dígitos
            txtNroDocumento.setText(texto.replaceAll("[^\\d]", ""));  // Eliminar caracteres no numéricos
        }

        // Limitar la longitud a 8 caracteres
        if (texto.length() > 8) {
            txtNroDocumento.setText(texto.substring(0, 8));
        }
    }

    // Método para validar que solo se ingresen números y limitar el máximo a 5 caracteres
    private void validarNumero() {
        String texto = txtNro.getText();
        if (!texto.matches("\\d*")) {  // Verificar que solo contiene dígitos
            txtNro.setText(texto.replaceAll("[^\\d]", ""));  // Eliminar caracteres no numéricos
        }

        // Limitar la longitud a 5 caracteres
        if (texto.length() > 5) {
            txtNro.setText(texto.substring(0, 5));
        }
    }

    // Método para validar que solo se ingresen letras y limitar el máximo a 60 caracteres
    private void validarNombre() {
        String texto = txtNombre.getText();
        if (!texto.matches("[a-zA-Z\\s]*")) {  // Verificar que solo contiene letras y/o espacios
            txtNombre.setText(texto.replaceAll("[^a-zA-Z]", ""));  // Eliminar caracteres no alfabéticos
        }

        // Limitar la longitud a 60 caracteres
        if (texto.length() > 60) {
            txtNombre.setText(texto.substring(0, 60));
        }
    }

    // Método para validar que solo se ingresen letras y limitar el máximo a 25 caracteres
    private void validarCalle() {
        String texto = txtCalle.getText();
        // Limitar la longitud a 25 caracteres
        if (texto.length() > 25) {
            txtCalle.setText(texto.substring(0, 25));
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

    private final StringConverter<Cobrador> converterCobrador = new StringConverter<Cobrador>() {
        @Override
        public String toString(Cobrador cobrador) {
            return (cobrador != null) ? cobrador.getNombre() : "";
        }

        @Override
        public Cobrador fromString(String string) {
            return cobradorser.findByNombre(string);
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

    private final StringConverter<Categoria> converterCategoria = new StringConverter<Categoria>() {
        @Override
        public String toString(Categoria categoria) {
            return (categoria != null) ? categoria.getIdCategoria() + " - " + categoria.getNombre() : "";
        }

        @Override
        public Categoria fromString(String string) {
            return catser.findByNombre(string);
        }
    };

    private final StringConverter<Localidad> converterLocalidad = new StringConverter<Localidad>() {
        @Override
        public String toString(Localidad localidad) {
            return (localidad != null) ? localidad.getNombre() : "";
        }

        @Override
        public Localidad fromString(String string) {
            return locser.findByNombre(string);
        }
    };

    private boolean validarCamposCompletos() {
        // Verificar que los campos de texto no estén vacíos
        boolean nombreCompletoCompleto = !txtNombre.getText().isEmpty();
        boolean nroDocumentoCompleto = !txtNroDocumento.getText().isEmpty();
        boolean nroCompleto = !txtNro.getText().isEmpty();
        boolean calleCompleta = !txtCalle.getText().isEmpty();

        // Verificar que los ComboBox tengan elementos seleccionados
        boolean categoriaSeleccionada = cmbxCategoria.getSelectionModel().getSelectedItem() != null;
        boolean tipoDocumentoSeleccionado = cmbxTipoDocumento.getSelectionModel().getSelectedItem() != null;
        boolean cobradorSeleccionado = cmbxCobrador.getSelectionModel().getSelectedItem() != null;
        boolean localidadSeleccionada = cmbxLocalidad.getSelectionModel().getSelectedItem() != null;

        // Comprobar el formato correcto de las fechas
        boolean formatoFechaNacimientoCorrecto = fechaNacimiento.getEditor().getText().matches("\\d{2}/\\d{2}/\\d{4}");
        boolean formatoFechaIngresoCorrecto = fechaIngreso.getEditor().getText().matches("\\d{2}/\\d{2}/\\d{4}");

        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return nombreCompletoCompleto && nroDocumentoCompleto && nroCompleto && calleCompleta
                && categoriaSeleccionada && tipoDocumentoSeleccionado && cobradorSeleccionado && localidadSeleccionada
                && formatoFechaNacimientoCorrecto && formatoFechaIngresoCorrecto;
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
    public void guardarSocio(){
         try {
        // Crear y guardar Domicilio
        Domicilio dom = new Domicilio();
        dom.setCalle(txtCalle.getText());
        dom.setNro(Integer.parseInt(txtNro.getText()));
        dom.setLocalidad(cmbxLocalidad.getValue());

        // Crear y guardar SocioTitular
        SocioTitular socio = new SocioTitular();
        socio.setNombreCompleto(txtNombre.getText());
        socio.setCategoria(cmbxCategoria.getValue());
        socio.setTipoDoc(cmbxTipoDocumento.getValue());
        socio.setNroDocumento(Long.parseLong(txtNroDocumento.getText()));
        socio.setCobrador(cmbxCobrador.getValue());
        socio.setFechaNacimiento(fechaNacimiento.getValue());
        socio.setFechaIngreso(fechaIngreso.getValue());
        socio.setDomicilio(dom); // Asociar el Domicilio al SocioTitular
        socio.setBaja(false);

        soctitser.createSocioTitular(socio);

        limpiarCampos();
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error al guardar");
        alert.setContentText("Ocurrió un error al intentar guardar el socio.");
        alert.showAndWait();
    }
    }

    @FXML
    public void limpiarCampos() {
        // Limpiar campos de texto
        txtNombre.clear();
        txtNroDocumento.clear();
        txtNro.clear();
        txtCalle.clear();

        // Desseleccionar elementos en ComboBox

       cmbxCategoria.setPromptText("Seleccione una..");
       cmbxCategoria.setValue(null);

       cmbxTipoDocumento.setPromptText("Seleccione uno..");
       cmbxTipoDocumento.setValue(null);

       cmbxCobrador.setPromptText("Seleccione uno..");
       cmbxCobrador.setValue(null);

       cmbxLocalidad.setPromptText("Seleccione una..");
       cmbxLocalidad.setValue(null);

        // Limpiar fechas
        fechaNacimiento.getEditor().clear();
        fechaIngreso.getEditor().clear();
    }
    
    public void permitirExpansion(){
        Scene scene = rootPane.getScene();
        // Permitir expansión horizontal y vertical del BorderPane
        // Permitir expansión horizontal y vertical del BorderPane
        // rootPane.prefWidthProperty().bind(scene.widthProperty());
        //rootPane.prefHeightProperty().bind(scene.heightProperty());

        // Permitir expansión de las VBox y HBox
        //VBox.setVgrow(leftVbox, Priority.ALWAYS);
        //VBox.setVgrow(rightVbox, Priority.ALWAYS);
        //VBox.setVgrow(vboxFormDomicilio, Priority.ALWAYS);
        //VBox.setVgrow(vboxFormSocio, Priority.ALWAYS);

        //HBox.setHgrow(hBoxContenido, Priority.ALWAYS);
        //HBox.setHgrow(bottomHbox, Priority.ALWAYS);
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTitulo.setText("Alta de Socio N° "+(soctitser.getLastNroSocio()+1));
        txtNroDocumento.textProperty().addListener((observable, oldValue, newValue) -> {
            validarNumeroDocumento();
            validarBtnGuardar();
        });

        txtNro.textProperty().addListener((observable, oldValue, newValue) -> {
            validarNumero();
            validarBtnGuardar();
        });

        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            validarNombre();
            validarBtnGuardar();
        });

        txtCalle.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCalle();
            validarBtnGuardar();
        });

        fechaNacimiento.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        fechaIngreso.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());

        // Agregar escuchadores de cambios para las fechas seleccionadas en los DatePickers
        fechaNacimiento.valueProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        fechaIngreso.valueProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        
        fechaNacimiento.setConverter(converter);
        fechaIngreso.setConverter(converter);

        cmbxCobrador.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxTipoDocumento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxCategoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxLocalidad.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());


        //llenar cmbx cobradores
        //traer lista
        List<Cobrador> cobradores = cobradorser.findAllCobradores();
        // Convertir la lista a una ObservableList
        ObservableList<Cobrador> cobradoresObservableList = FXCollections.observableArrayList(cobradores);
        // Asignar la lista al ComboBox
        cmbxCobrador.setConverter(converterCobrador);
        cmbxCobrador.setItems(cobradoresObservableList);

        //llenar cmbx tipo documento
        //traer lista
        List<TipoDocumento> tiposdocumentos = tipodocser.findAllTipos();
        // Convertir la lista a una ObservableList
        ObservableList<TipoDocumento> tiposdocumentosObservableList = FXCollections.observableArrayList(tiposdocumentos);
        // Asignar la lista al ComboBox
        cmbxTipoDocumento.setConverter(converterTipoDoc);
        cmbxTipoDocumento.setItems(tiposdocumentosObservableList);

        //llenar cmbx categoria
        //traer lista
        List<Categoria> categorias = catser.findAllCategorias();
        // Convertir la lista a una ObservableList
        ObservableList<Categoria> categoriasObservableList = FXCollections.observableArrayList(categorias);
        // Asignar la lista al ComboBox
        cmbxCategoria.setConverter(converterCategoria);
        cmbxCategoria.setItems(categoriasObservableList);

        //llenar cmbx localidad
        //traer lista
        List<Localidad> localidades = locser.findAllLocalidades();
        // Convertir la lista a una ObservableList
        ObservableList<Localidad> localidadesObservableList = FXCollections.observableArrayList(localidades);
        // Asignar la lista al ComboBox
        cmbxLocalidad.setConverter(converterLocalidad);
        cmbxLocalidad.setItems(localidadesObservableList);
        validarBtnGuardar();
    }

}
