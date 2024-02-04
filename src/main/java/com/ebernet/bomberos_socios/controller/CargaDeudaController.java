package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.dto.CuotaDTO;
import com.ebernet.bomberos_socios.model.Deuda;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.IDeudaService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CargaDeudaController implements Initializable {
    
    private SocioTitular socio;
    
    private HashMap<Integer, CuotaDTO>cuotas;
    
    @Autowired
    private IDeudaService deudaser;
    
    @Autowired
    private ISocioTitularService sociotitser;
    
    @Autowired
    private IndexController index;
    
    @FXML
    private ComboBox<Integer> cmbxAnio;
    
    @FXML
    private ComboBox<CuotaDTO> cmbxCuota;
    
    @FXML
    private DatePicker fechaGeneracion;
    
    @FXML
    private TextField txtImporte, txtNroSocioTit, txtNombreTit, txtCategoriaTit;
    
    @FXML
    private Button btnCancelar, btnGuardar;
    
    @FXML
    private void guardarDeuda(){
        Deuda deuda = new Deuda();
        deuda.setAnio(cmbxAnio.getValue());
        deuda.setCuota(cmbxCuota.getValue().getNroCuota());
        deuda.setFechaGeneracion(fechaGeneracion.getValue());
        deuda.setImporte(Integer.parseInt(txtImporte.getText()));
        deuda.setSocioDeudor(socio);
        deuda.setPaga(false);
        
        deudaser.saveDeuda(deuda);
        index.vistaDeudas(socio);
    }
    
    @FXML
    private void cancelarCarga(){
        index.vistaDeudas(socio);
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
    
    //converter para CuotaDTO
    private final StringConverter<CuotaDTO> converterCuotaDTO = new StringConverter<>() {
         @Override
        public String toString(CuotaDTO cuota) {
            if (cuota == null) {
                return null;
            }
            return cuota.getNroCuota() + " - " + cuota.getMesCuota();
        }

        @Override
        public CuotaDTO fromString(String text) {
            return cuotas.get(Integer.parseInt(text));
        }
    };
    
    // Método para validar que solo se ingresen números y limitar el máximo a 6 caracteres
    private void validarImporte() {
        String texto = txtImporte.getText();
        if (!texto.matches("\\d*")) {  // Verificar que solo contiene dígitos
            txtImporte.setText(texto.replaceAll("[^\\d]", ""));  // Eliminar caracteres no numéricos
        }

        // Limitar la longitud a 8 caracteres
        if (texto.length() > 8) {
            txtImporte.setText(texto.substring(0, 6));
        }
    }
    
    private boolean validarCamposCompletos() {
        // Verificar que los campos de texto no estén vacíos
        boolean importeCompleto = !txtImporte.getText().isEmpty();
        // Verificar que los ComboBox tengan elementos seleccionados
        boolean anioSeleccioando = cmbxAnio.getSelectionModel().getSelectedItem() != null;
        boolean cuotaSeleccionada = cmbxCuota.getSelectionModel().getSelectedItem() != null;
        // Comprobar el formato correcto de las fechas
        boolean formatoFechaGeneracionCorrecto = fechaGeneracion.getEditor().getText().matches("\\d{2}/\\d{2}/\\d{4}");
        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return importeCompleto && anioSeleccioando && cuotaSeleccionada && formatoFechaGeneracionCorrecto;
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
    
    public void initData(SocioTitular sociotit){
        this.socio = sociotit;
        LocalDate fechaActual = LocalDate.now();
        
        //setear datos del titular:
        txtNroSocioTit.setText(Long.toString(socio.getNroSocio()));
        txtNombreTit.setText(socio.getNombreCompleto());
        txtCategoriaTit.setText(socio.getCategoria().getIdCategoria()+" - "+socio.getCategoria().getNombre());
        
        //setear importe y fecha de generacion
        txtImporte.setText(Integer.toString(socio.getCategoria().getImporte()));
        fechaGeneracion.setValue(fechaActual);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGuardar.setDisable(true);
        if (this.cuotas == null) {
            this.cuotas = new HashMap<>(); 
        }
        
        
        txtImporte.textProperty().addListener((observable, oldValue, newValue) -> {
            validarImporte();
            validarBtnGuardar();
        });
        
        //Escuchador para cuando se ingresa sin date picker
        fechaGeneracion.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        // Agregar escuchadores de cambios para las fechas seleccionadas en los DatePickers
        fechaGeneracion.valueProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        //setear converter
        fechaGeneracion.setConverter(converter);
        
        //setear listeners de validacion a combo box
        cmbxAnio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxCuota.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        
        //setear converter a cmbxcuota
        cmbxCuota.setConverter(converterCuotaDTO);
        
        //llenar cmbxAnio
        // Obtener el año actual
        int anioActual = Year.now().getValue();
        // Crear una lista con los 3 años anteriores y los 3 siguientes
        List<Integer> listaAnios = new ArrayList<>();
        for (int i = anioActual - 3; i <= anioActual + 3; i++) {
            listaAnios.add(i);
        }
        // Convertir la lista a ObservableList
        ObservableList<Integer> opcionesAnios = FXCollections.observableArrayList(listaAnios);
        // Setear la lista al ComboBox
        cmbxAnio.setItems(opcionesAnios);
        
        //llenar cmbxcuota
        List<CuotaDTO> cuotas = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String nombreMes = Month.of(i).name();
            CuotaDTO cuotaDTO = new CuotaDTO(i, nombreMes);
            cuotas.add(cuotaDTO); 
        }
        // Convertir la lista a ObservableList
        ObservableList<CuotaDTO> opcionesCuotas = FXCollections.observableArrayList(cuotas);
        // Setear la lista al ComboBox
        cmbxCuota.setItems(opcionesCuotas);
        //convertir cuotas a hashmap
         for (CuotaDTO cuota : cuotas) {
            this.cuotas.put(cuota.getNroCuota(), cuota);
        }
    }    
    
}
