package com.ebernet.bomberos_socios.controller;

import static com.ebernet.bomberos_socios.controller.ImprimirLibroJuridicoAnioController.imprimirPDF;
import com.ebernet.bomberos_socios.dto.SocioTitularDTO;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.ui.SocioTitularWrapper;
import java.awt.Desktop;
import java.awt.print.PrinterJob;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class LibroJuridicoController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ISocioTitularService sociotitser;

    @FXML
    private TableView<SocioTitularWrapper> tblSocios;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colNombre, colDoc, colNroCuil, colFDIng, colLocalidad;

    @FXML
    private Pagination pagination;

    @FXML
    private Button btnGenerarPdfAnio, btnGenerarPdf;

    @FXML
    private void generarPdfAnio() {
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/imprimirLibroJuridicoAnio.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void generarPdf() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro que desea generar el PDF del libro jurídico completo?");

        // Configurar los botones del cuadro de diálogo
        ButtonType buttonTypeYes = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Mostrar el cuadro de diálogo y esperar a que el usuario elija una opción
        alert.showAndWait().ifPresent(result -> {
            if (result == buttonTypeYes) {

                String appData = System.getenv("APPDATA");
                Path appFolderPath = Paths.get(appData, "bomberos_socios");
                Path libroJuridicosBomberosPath = appFolderPath.resolve("Libro Juridico BOMBEROS");

                if (!Files.exists(appFolderPath)) {
                    try {
                        Files.createDirectory(appFolderPath);
                        Files.createDirectory(libroJuridicosBomberosPath);
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
                } else if (!Files.exists(libroJuridicosBomberosPath)) {
                    try {
                        Files.createDirectory(libroJuridicosBomberosPath);
                    } catch (IOException ex) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Error");
                        alerta.setHeaderText("¡Error!");
                        alerta.setContentText("Hubo un error al generar la carpeta " + libroJuridicosBomberosPath.toString() + " " + ex.getCause() + " " + ex.getMessage());
                        alerta.showAndWait();
                        System.err.println("Error al crear la carpeta 'Libro juridico BOMBEROS': " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("La carpeta de la aplicación ya existe en: " + libroJuridicosBomberosPath);
                }
                // Ruta del archivo jrxml en el paquete "reports"
                String jrxmlFilePath = "/reports/libroJuridico.jrxml";
                // Directorio donde se guardarán los reportes
                String outputDirectory = libroJuridicosBomberosPath.toString() + "/";
                // Crea una lista de recibosDTO a partir de la lista de socios objeto
                List<SocioTitular> sociostit = sociotitser.findAllSociosTitulares();
                List<SocioTitularDTO> listaSociosDTO = new ArrayList<SocioTitularDTO>();
                DateTimeFormatter formatoDto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (SocioTitular socio : sociostit) {
                    SocioTitularDTO dto = new SocioTitularDTO();
                    dto.setNombreCompleto(socio.getNombreCompleto());
                    dto.setNroDocumento(Long.toString(socio.getNroDocumento()));
                    dto.setFechaIngreso(formatoDto.format(socio.getFechaIngreso()));
                    dto.setLocalidad(socio.getDomicilio().getLocalidad().getNombre());
                    dto.setNroCuil((socio.getNroCuil() != null) ? socio.getNroCuil().toString() : "No especifica.");
                    listaSociosDTO.add(dto);
                }

                try {
                    InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlFilePath);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
                    // Convertir la lista en un array de objetos
                    SocioTitularDTO[] arraySociosDTO = listaSociosDTO.toArray(new SocioTitularDTO[listaSociosDTO.size()]);
                    System.out.println("array size: " + arraySociosDTO.length);
                    // Crear el origen de datos JRBeanArrayDataSource
                    JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(arraySociosDTO);
                    System.out.println(dataSource.getData().length);
                    //crea y formatea la fecha de emision
                    // Obtener la fecha y hora actual
                    LocalDateTime fechaEmision = LocalDateTime.now();
                    // Definir el formato deseado
                    DateTimeFormatter formatoReporte = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    DateTimeFormatter formatoNombreArchivo = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
                    // Formatear la fecha actual según el formato
                    String fechaFormateada = fechaEmision.format(formatoReporte);
                    String fechaFormateadaNombreArchivo = fechaEmision.format(formatoNombreArchivo);
                    // Parámetros del informe
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("dataSource", dataSource);
                    parametros.put("fechaEmision", fechaFormateada);
                    parametros.put("cantidadSocios", String.valueOf(listaSociosDTO.size()));
                    parametros.put("imgDir", "img/");
                    // Llena el informe con los datos
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
                    // Guarda el informe en un archivo PDF en la carpeta especificada
                    System.out.println("report directory: " + jrxmlFilePath);
                    System.out.println("output directory: " + outputDirectory);
                    File outputFile = new File(outputDirectory + "libroJuridico_" + fechaFormateadaNombreArchivo + "hs.pdf");
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
                    alerta.setContentText("Hubo un error al generar los recibos." + e.getCause());
                    alerta.showAndWait();
                }
            }

        });
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
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Exito");
            alerta.setHeaderText(null);
            alerta.setContentText("Exito en la impresion!");
            alerta.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
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
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Error al abrir. " + ex.getCause());
            alerta.showAndWait();
        }
    }

    public void cambiarPagina(int pagina) {
        Page<SocioTitular> page = sociotitser.findAllSociosTitulares(pagina, 50);

        //Convertir a wrappers
        List<SocioTitularWrapper> wrappers = page.getContent().stream()
                .map(SocioTitularWrapper::new)
                .collect(Collectors.toList());
        System.out.println("Número de elementos en wrappers: " + wrappers.size());

        // Mapeo de columnas 
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colLocalidad.setCellValueFactory(cell -> cell.getValue().localidadProperty());
        colFDIng.setCellValueFactory(cell -> cell.getValue().fechaIngresoProperty());
        colNroCuil.setCellValueFactory(cell -> cell.getValue().nroCuilProperty());

        // Setear items
        tblSocios.setItems(FXCollections.observableArrayList(wrappers));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        long totalRegistros = sociotitser.count();
        double totalPaginasD = Math.ceil(totalRegistros / 50d);
        int totalPaginas = (int) totalPaginasD;
        System.out.println("Total registros: " + totalRegistros);
        System.out.println("Total páginas: " + totalPaginas);
        pagination.setPageCount(totalPaginas);

        // Obtener page  
        Page<SocioTitular> page = sociotitser.findAllSociosTitulares(0, 50);
        System.out.println("Contenido de pagina: " + page.getContent().size());

        // Convertir a wrappers
        List<SocioTitularWrapper> wrappers = page.getContent().stream()
                .map(SocioTitularWrapper::new)
                .collect(Collectors.toList());
        System.out.println("Número de elementos en wrappers: " + wrappers.size());

        // Mapeo de columnas 
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colLocalidad.setCellValueFactory(cell -> cell.getValue().localidadProperty());
        colFDIng.setCellValueFactory(cell -> cell.getValue().fechaIngresoProperty());
        colNroCuil.setCellValueFactory(cell -> cell.getValue().nroCuilProperty());

        // Setear items
        tblSocios.setItems(FXCollections.observableArrayList(wrappers));

        // Agrega un listener al índice de la página actual
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cambiarPagina(newValue.intValue());
            }
        });
    }

}
