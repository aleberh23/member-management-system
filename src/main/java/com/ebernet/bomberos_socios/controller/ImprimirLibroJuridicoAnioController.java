package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.dto.SocioTitularDTO;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
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
import javafx.scene.control.Label;
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

@Component
public class ImprimirLibroJuridicoAnioController implements Initializable {

    @Autowired
    private ISocioTitularService sociotitser;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblTitulo;

    @FXML
    private ComboBox<Integer> cmbxAnio;

    @FXML
    private Button btnGenerar, btnCancelar;

    @FXML
    private void cancelar() {
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }

    @FXML
    private void generarLibroJuridicoAnio() {
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
        String jrxmlFilePath = "/reports/libroJuridicoAnio.jrxml";
        // Directorio donde se guardarán los reportes
        String outputDirectory = libroJuridicosBomberosPath.toString()+"/";
        // Crea una lista de recibosDTO a partir de la lista de socios objeto
        List<SocioTitular> sociostit = sociotitser.findAllSociosTitularesByAnioIngreso(cmbxAnio.getValue());
        List<SocioTitularDTO> listaSociosDTO = new ArrayList<SocioTitularDTO>();
        DateTimeFormatter formatoDto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (SocioTitular socio : sociostit) {
            SocioTitularDTO dto = new SocioTitularDTO();
            dto.setNombreCompleto(socio.getNombreCompleto());
            dto.setNroDocumento(Long.toString(socio.getNroDocumento()));
            dto.setFechaIngreso(formatoDto.format(socio.getFechaIngreso()));
            dto.setLocalidad(socio.getDomicilio().getLocalidad().getNombre());
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
            parametros.put("anio", Integer.toString(cmbxAnio.getValue()));
            // Llena el informe con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
            // Guarda el informe en un archivo PDF en la carpeta especificada
            System.out.println("report directory: " + jrxmlFilePath);
            System.out.println("output directory: " + outputDirectory);
            File outputFile = new File(outputDirectory + "libroJuridico_" + cmbxAnio.getValue() + "_" + fechaFormateadaNombreArchivo + "hs.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(outputFile));

            // Mostrar la alerta de éxito
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Éxito");
            alerta.setHeaderText("¡Éxito!");
            alerta.setContentText("El pdf se genero correctamente!.");
            alerta.showAndWait();
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            // Mostrar la alerta de error
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("¡Error!");
            alerta.setContentText("Hubo un error al generar los recibos." + e.getCause());
            alerta.showAndWait();
        }

        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
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
        for (int i = anioActual - 15; i <= anioActual; i++) {
            listaAnios.add(i);
        }
        // Convertir la lista a ObservableList
        ObservableList<Integer> opcionesAnios = FXCollections.observableArrayList(listaAnios);
        // Setear la lista al ComboBox
        cmbxAnio.setItems(opcionesAnios);

        cmbxAnio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validarBtnGuardar());
    }

}
