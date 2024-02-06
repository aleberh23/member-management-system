package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.dto.CuotaDTO;
import com.ebernet.bomberos_socios.dto.ReciboDTO;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImprimirReciboSocioController implements Initializable {
    
    private SocioTitular socio;
    
    private HashMap<Integer, CuotaDTO> cuotas;

    @Autowired
    private ISocioTitularService sociotitser;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<Integer> cmbxAnio;

    @FXML
    private ComboBox<CuotaDTO> cmbxCuota;

    @FXML
    private Button btnCancelar, btnGenerar;

    @FXML
    private void generarRecibos() {
        String appData = System.getenv("APPDATA");
        Path appFolderPath = Paths.get(appData, "bomberos_socios");
        Path recibosBomberosPath = appFolderPath.resolve("Recibos BOMBEROS");

         if (!Files.exists(appFolderPath)) {
            try {
                Files.createDirectory(appFolderPath);
                Files.createDirectory(recibosBomberosPath);
                System.out.println("Carpeta de la aplicación creada en: " + appFolderPath);
            } catch (Exception e) {
                // Mostrar la alerta de error
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("¡Error!");
                alerta.setContentText("Hubo un error al generar la carpeta " + appFolderPath.toString() + " " + e.getCause() + " " + e.getMessage());
                alerta.showAndWait();
                System.err.println("Error al crear la carpeta 'Recibos BOMBEROS': " + e.getMessage());
                e.printStackTrace();
            }
        } else if (!Files.exists(recibosBomberosPath)){
            try {
                Files.createDirectory(recibosBomberosPath);
            } catch (IOException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("¡Error!");
                alerta.setContentText("Hubo un error al generar la carpeta " + recibosBomberosPath.toString() + " " + ex.getCause() + " " + ex.getMessage());
                alerta.showAndWait();
                System.err.println("Error al crear la carpeta 'Recibos BOMBEROS': " + ex.getMessage());
                ex.printStackTrace();
            }
        }else{
             System.out.println("La carpeta de la aplicación ya existe en: " + recibosBomberosPath);
        }
        // Ruta del archivo jrxml en el paquete "reports"
        String jrxmlFilePath = "/reports/report.jrxml";
        // Directorio donde se guardarán los reportes
        String outputDirectory = recibosBomberosPath.toString()+"/";
        // Crea un objeto de datos
        ReciboDTO recibo = new ReciboDTO();
        recibo.setNroSocio(Long.toString(socio.getNroSocio()));
        recibo.setNombreTitular(socio.getNombreCompleto());
        recibo.setDireccion(socio.getDomicilio().getCalle() + " " + socio.getDomicilio().getNro());
        recibo.setCategoria(socio.getCategoria().getIdCategoria() + " - " + socio.getCategoria().getNombre());
        recibo.setPeriodo(cmbxCuota.getValue().getNroCuota() + "-" + cmbxAnio.getValue());
        recibo.setImporte("$" + socio.getCategoria().getImporte());
        // Parámetros del reporte
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nroSocio", recibo.getNroSocio());
        parameters.put("nombreTitular", recibo.getNombreTitular());
        parameters.put("direccion", recibo.getDireccion());
        parameters.put("periodo", recibo.getPeriodo());
        parameters.put("categoria", recibo.getCategoria());
        parameters.put("importe", recibo.getImporte());
        parameters.put("imageDir", "img/");  // Directorio de imágenes en resources

        try {
            // Compila el archivo jrxml
            InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // Convierte el objeto a una fuente de datos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(java.util.Collections.singletonList(recibo));

            // Llena el informe con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Guarda el informe en un archivo PDF en la carpeta especificada
            File outputFile = new File(outputDirectory + "recibo_" + recibo.getPeriodo() + "_" + recibo.getNombreTitular() + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(outputFile));

            System.out.println("Reporte generado y guardado en: " + outputFile.getAbsolutePath());
            // Después de generar el reporte, muestra una alerta
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Éxito");
            alerta.setHeaderText("¡Éxito!");
            alerta.setContentText("El recibo se generó correctamente.");
            alerta.showAndWait();

        } catch (JRException | IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("¡Error!");
            alerta.setContentText("Hubo un error al generar el recibo."+e.getCause());
            alerta.showAndWait();
        }
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
    @FXML
    private void cancelar() {
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
    
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

    private boolean validarCamposCompletos() {
        // Verificar que los ComboBox tengan elementos seleccionados
        boolean anioSeleccioando = cmbxAnio.getSelectionModel().getSelectedItem() != null;
        boolean cuotaSeleccionada = cmbxCuota.getSelectionModel().getSelectedItem() != null;
        // Devolver true si todos los campos están completos y el formato de las fechas es correcto
        return anioSeleccioando && cuotaSeleccionada;
    }

    //habilotar o deshabilitar btnguardar
    private void validarBtnGuardar() {
        if (validarCamposCompletos() == true) {
            System.out.println("CAMPOS COMPLETOS TRUE");
            btnGenerar.setDisable(false);
        } else {
            System.out.println("CAMPOS COMPLETOS FALSE");
            btnGenerar.setDisable(true);
        }
    }
    
    public void initData(SocioTitular sociotit){
        this.socio=sociotit;
        lblTitulo.setText("Imprimir recibo de: "+socio.getNroSocio()+" - "+socio.getNombreCompleto());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGenerar.setDisable(true);
        this.cuotas = new HashMap<Integer, CuotaDTO>();
        //setear listeners de validacion a combo box
        cmbxAnio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
        cmbxCuota.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
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
        //setear converter
        cmbxCuota.setConverter(converterCuotaDTO);
        //convertir cuotas a hashmap
        for (CuotaDTO cuota : cuotas) {
            this.cuotas.put(cuota.getNroCuota(), cuota);
        }
    } 
}
