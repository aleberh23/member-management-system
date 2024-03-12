package com.ebernet.bomberos_socios.controller;

import static com.ebernet.bomberos_socios.controller.ImprimirReciboSocioController.imprimirPDF;
import com.ebernet.bomberos_socios.dto.SocioTitularDTO;
import com.ebernet.bomberos_socios.model.Cobrador;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ICobradorService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javax.print.attribute.standard.Chromaticity;

@Component
public class ImprimirControlCobranzaController implements Initializable {

    private Cobrador cobrador;

    @Autowired
    private ICobradorService cobradorser;

    @Autowired
    private ISocioTitularService sociotitser;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField txtCobrador;

    @FXML
    private ComboBox<Integer> cmbxAnio;

    @FXML
    private Button btnCancelar, btnGenerar;

    @FXML
    private void generar() {
        String appData = System.getenv("APPDATA");
        Path appFolderPath = Paths.get(appData, "bomberos_socios");
        Path controlDeCobranzasBomberosPath = appFolderPath.resolve("Control de cobranzas BOMBEROS");

        if (!Files.exists(appFolderPath)) {
            try {
                Files.createDirectory(appFolderPath);
                Files.createDirectory(controlDeCobranzasBomberosPath);
                System.out.println("Carpeta de la aplicación creada en: " + appFolderPath);
            } catch (Exception e) {
                // Mostrar la alerta de error
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("¡Error!");
                alerta.setContentText("Hubo un error al generar la carpeta " + appFolderPath.toString() + " " + e.getCause() + " " + e.getMessage());
                alerta.showAndWait();
                System.err.println("Error al crear la carpeta 'Libro Juridico BOMBEROS': " + e.getMessage());
                e.printStackTrace();
            }
        } else if (!Files.exists(controlDeCobranzasBomberosPath)) {
            try {
                Files.createDirectory(controlDeCobranzasBomberosPath);
            } catch (IOException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("¡Error!");
                alerta.setContentText("Hubo un error al generar la carpeta " + controlDeCobranzasBomberosPath.toString() + " " + ex.getCause() + " " + ex.getMessage());
                alerta.showAndWait();
                System.err.println("Error al crear la carpeta 'Libro juridico BOMBEROS': " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            System.out.println("La carpeta de la aplicación ya existe en: " + controlDeCobranzasBomberosPath);
        }
        // Ruta del archivo jrxml en el paquete "reports"
        String jrxmlFilePath = "/reports/controlCobranza.jrxml";
        // Directorio donde se guardarán los reportes
        String outputDirectory = controlDeCobranzasBomberosPath.toString() + "/";
        // Crea una lista de sociosDTO a partir de la lista de socios objeto
        List<SocioTitular> sociostit = sociotitser.findAllByCobradorAndAnio(cobrador, cmbxAnio.getValue());
        List<SocioTitularDTO> listaSociosDTO = new ArrayList<SocioTitularDTO>();
        for (SocioTitular socio : sociostit) {
            SocioTitularDTO dto = new SocioTitularDTO();
            dto.setNombreCompleto(socio.getNombreCompleto());
            dto.setNroSocio(socio.getNroSocio().toString());
            listaSociosDTO.add(dto);
        }

        try {
            InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            // Convertir la lista en un array de objetos
            SocioTitularDTO[] arraySociosDTO = listaSociosDTO.toArray(new SocioTitularDTO[listaSociosDTO.size()]);
            System.out.println("array size: " + arraySociosDTO.length);
            // Crear el origen de datos JRBeanArrayDataSource
            JRBeanArrayDataSource sociosDataSource = new JRBeanArrayDataSource(arraySociosDTO);
            System.out.println(sociosDataSource.getData().length);
            //crea y formatea la fecha de emision
            // Obtener la fecha y hora actual
            LocalDateTime fechaEmision = LocalDateTime.now();
            // Definir el formato deseado
            DateTimeFormatter formatoNombreArchivo = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
            // Formatear la fecha actual según el formato
            String fechaFormateadaNombreArchivo = fechaEmision.format(formatoNombreArchivo);
            // Parámetros del informe
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("sociosDataSource", sociosDataSource);
            parametros.put("anio", Integer.toString(cmbxAnio.getValue()));
            parametros.put("cobrador", cobrador.getNombre());
            // Llena el informe con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, sociosDataSource);
            // Guarda el informe en un archivo PDF en la carpeta especificada
            System.out.println("report directory: " + jrxmlFilePath);
            System.out.println("output directory: " + outputDirectory);
            File outputFile = new File(outputDirectory + "controldecobranza_" + cobrador.getNombre() + "_" + cmbxAnio.getValue() + "_" + fechaFormateadaNombreArchivo + "hs.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(outputFile));

            // Mostrar la alerta de éxito
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Éxito");
            alerta.setHeaderText("¡Éxito!");
            alerta.setContentText("El pdf se genero correctamente!"
                    + "\n¿Como desea proceder?");
            // Crear botones personalizados
            ButtonType buttonImprimir = new ButtonType("Imprimir", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonAbrir = new ButtonType("Abrir", ButtonBar.ButtonData.CANCEL_CLOSE);

            // Agregar los botones a la alerta
            alerta.getButtonTypes().setAll(buttonImprimir, buttonAbrir);

            // Mostrar la alerta y obtener la respuesta
            Optional<ButtonType> resultado = alerta.showAndWait();

            // Verificar qué botón se presionó
            if (resultado.isPresent()) {
                if (resultado.get() == buttonImprimir) {
                    imprimirPDF(outputFile);
                } else if (resultado.get() == buttonAbrir) {
                    abrirPDF(outputFile);
                }
            }
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            // Mostrar la alerta de error
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("¡Error!");
            alerta.setContentText("Hubo un error al generar el control de cobranza." + e.getCause());
            alerta.showAndWait();
        }

        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }

    public static void imprimirPDF(File pdfDoc) {
        try {
            PDDocument document = PDDocument.load(pdfDoc);

            // Obtener la impresora predeterminada
            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();

            // Crear un trabajo de impresión
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(defaultPrinter);

            // Configurar opciones de impresión
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(Chromaticity.MONOCHROME);

            // Imprimir el documento PDF
            job.setPageable(new PDFPageable(document));
            job.print(attributes);

            // Cerrar el documento PDF
            document.close();
            Alert alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("Exito");
            alerta.setHeaderText(null);
            alerta.setContentText("Exito en la impresion!");
            alerta.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Error en la impresion: " + e.getCause());
            alerta.showAndWait();
        }
    }

    public void abrirPDF(File archivoPDF) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivoPDF);
        } catch (IOException ex) {
            Logger.getLogger(ImprimirControlCobranzaController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Error al abrir. " + ex.getCause());
            alerta.showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }

    public void initData(Cobrador cob) {
        this.cobrador = cob;
        txtCobrador.setEditable(false);
        txtCobrador.setText(cobrador.getNombre());
    }

    private boolean validarCamposCompletos() {
        // Verificar que el comboboc tenga elementos seleccionados
        boolean anioSeleccioando = cmbxAnio.getSelectionModel().getSelectedItem() != null;
        // Devolver true si hay item seleccionado.
        return anioSeleccioando;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGenerar.setDisable(true);
        //llenar cmbxAnio
        // Obtener el año actual
        int anioActual = Year.now().getValue();
        // Crear una lista con los 3 años anteriores y los 3 siguientes
        List<Integer> listaAnios = new ArrayList<>();
        for (int i = anioActual - 5; i <= anioActual; i++) {
            listaAnios.add(i);
        }
        // Convertir la lista a ObservableList
        ObservableList<Integer> opcionesAnios = FXCollections.observableArrayList(listaAnios);
        // Setear la lista al ComboBox
        cmbxAnio.setItems(opcionesAnios);

        cmbxAnio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
    }

}
