package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.dto.ReciboDTO;
import com.ebernet.bomberos_socios.model.Deuda;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.IDeudaService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.ui.DeudaWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class VistaDeudasSocioController implements Initializable {

    private SocioTitular socio;

    private Deuda deudaselec;

    private HashMap<Long, Deuda> deudas;

    @Autowired
    private ISocioTitularService sociotitser;

    @Autowired
    private IDeudaService deudaser;

    @Autowired
    private IndexController index;

    @FXML
    private Label lblTitulo, lblDeudaSelec;

    @FXML
    private TableView<DeudaWrapper> tableDeudas;

    @FXML
    private TableColumn<DeudaWrapper, String> colAnio, colCuota, colImporte, colFechaGen, colPaga;

    @FXML
    private Button btnPaga, btnEditar, btnRecibo, btnNueva, btnVolver;

    @FXML
    private void pagarDeuda() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pago de Deuda");
        alert.setHeaderText("Pago de Deuda: " + deudaselec.getAnio() + " - " + deudaselec.getCuota());
        alert.setContentText("¿Desea dar de baja esta deuda?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // El usuario seleccionó "Sí", se realiza el pago de la deuda
            deudaselec.setPaga(true);
            deudaser.saveDeuda(deudaselec);
        }
        recargarTablaDeudas();
    }

    @FXML
    private void editarDeuda() {
        index.editarDeuda(deudaselec);
    }

    @FXML
    private void reciboDeuda() {
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
        recibo.setNroSocio(Long.toString(deudaselec.getSocioDeudor().getNroSocio()));
        recibo.setNombreTitular(deudaselec.getSocioDeudor().getNombreCompleto());
        recibo.setDireccion(deudaselec.getSocioDeudor().getDomicilio().getCalle() + " " + deudaselec.getSocioDeudor().getDomicilio().getNro());
        recibo.setCategoria(deudaselec.getSocioDeudor().getCategoria().getIdCategoria() + " - " + deudaselec.getSocioDeudor().getCategoria().getNombre());
        recibo.setPeriodo(deudaselec.getCuota() + "-" + deudaselec.getAnio());
        recibo.setImporte("$" + deudaselec.getImporte());
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
    }

    @FXML
    private void nuevaDeuda() {
        index.cargaDeuda(socio);
    }

    @FXML
    private void volverDetalleSocio() {
        index.detalleSocio(socio);
    }

    public void initData(SocioTitular sociotit) {
        this.socio = sociotit;

        // Setear titulo 
        lblTitulo.setText("Deudas de: " + socio.getNroSocio() + " - " + socio.getNombreCompleto());

        // Cargar deudas del socio
        List<Deuda> deudas = deudaser.findByAllBySocioTitular(socio.getNroSocio());

       List<DeudaWrapper> wrappers = deudas.stream()
        .map(DeudaWrapper::new)
        .sorted((w1, w2) -> {
            boolean pagaNo1 = w1.pagaProperty().equals("No");
            boolean pagaNo2 = w2.pagaProperty().equals("No");

            // Orden descendente por "pagaProperty" igual a "No"
            return Boolean.compare(pagaNo2, pagaNo1);
        })
        .collect(Collectors.toList());


        //convertir a hashmap
        this.deudas = (HashMap<Long, Deuda>) deudas.stream()
                .collect(Collectors.toMap(Deuda::getIdDeuda, deuda -> deuda));

        //Mapeo de columnas :)
        colAnio.setCellValueFactory(cell -> cell.getValue().anioProperty());
        colCuota.setCellValueFactory(cell -> cell.getValue().cuotaProperty());
        colFechaGen.setCellValueFactory(cell -> cell.getValue().fechaGeneracionProperty());
        colImporte.setCellValueFactory(cell -> cell.getValue().importeProperty());
        colPaga.setCellValueFactory(cell -> cell.getValue().pagaProperty());

        // Asignar a la tabla
        tableDeudas.setItems(FXCollections.observableArrayList(wrappers));

        tableDeudas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // habilitar botones
                lblDeudaSelec.setDisable(false);
                btnEditar.setDisable(false);
                btnRecibo.setDisable(false);
                btnPaga.setDisable(false);

                //setear texto el lblDeudaSelec
                DeudaWrapper wrapper = tableDeudas.getSelectionModel().getSelectedItem();
                lblDeudaSelec.setText("Deuda seleccionada: " + wrapper.anioProperty().get() + " - " + wrapper.cuotaProperty().get());

                //traer deuda seleccionada
                Long id = Long.parseLong(wrapper.idDeudaProperty().get());
                this.deudaselec = this.deudas.get(id);
                if (deudaselec.isPaga()) {
                    btnPaga.setDisable(true);
                } else {
                    btnPaga.setDisable(false);
                }
            } else {
                //deshabilitar cuando no hay seleccion
                lblDeudaSelec.setText("Deuda seleccionada: ");
                lblDeudaSelec.setDisable(true);
                btnEditar.setDisable(true);
                btnRecibo.setDisable(true);
                btnPaga.setDisable(true);
            }
        });

    }

    public void recargarTablaDeudas() {
        // Vuelve a cargar los datos de las deudas desde la base de datos
        List<Deuda> deudas = deudaser.findByAllBySocioTitular(socio.getNroSocio());

        List<DeudaWrapper> wrappers = deudas.stream()
        .map(DeudaWrapper::new)
        .sorted((w1, w2) -> {
            boolean pagaNo1 = w1.pagaProperty().equals("No");
            boolean pagaNo2 = w2.pagaProperty().equals("No");

            // Orden descendente por "pagaProperty" igual a "No"
            return Boolean.compare(pagaNo2, pagaNo1);
        })
        .collect(Collectors.toList());


        // Mapeo de columnas
        colAnio.setCellValueFactory(cell -> cell.getValue().anioProperty());
        colCuota.setCellValueFactory(cell -> cell.getValue().cuotaProperty());
        colImporte.setCellValueFactory(cell -> cell.getValue().importeProperty());
        colFechaGen.setCellValueFactory(cell -> cell.getValue().fechaGeneracionProperty());
        colPaga.setCellValueFactory(cell -> cell.getValue().pagaProperty());

        // Convierte a HashMap
        this.deudas = new HashMap<>();
        deudas.forEach(deuda -> this.deudas.put(deuda.getIdDeuda(), deuda));

        // Actualiza los items en la tabla
        tableDeudas.setItems(FXCollections.observableArrayList(wrappers));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Deshabilitar botones
        lblDeudaSelec.setDisable(true);
        btnPaga.setDisable(true);
        btnEditar.setDisable(true);
        btnRecibo.setDisable(true);
    }

}
