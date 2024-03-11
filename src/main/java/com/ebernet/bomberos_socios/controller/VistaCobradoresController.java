package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Cobrador;
import com.ebernet.bomberos_socios.service.ICobradorService;
import com.ebernet.bomberos_socios.ui.CategoriaWrapper;
import com.ebernet.bomberos_socios.ui.CobradorWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class VistaCobradoresController implements Initializable{
    
    private Cobrador cobselec;
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private ICobradorService cobser;
    
    @FXML
    private TableView<CobradorWrapper> tableCobradores;
    
    @FXML
    private TableColumn<CobradorWrapper, String> colNombre, colNroTelefono;
    
    @FXML
    private Label lblCobrSelec, lblNombreCob, lblNroTelefono;
    
    @FXML
    private Button btnEditar, btnNuevo, btnImprimirControl;
    
    @FXML
    private void editar(){
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editarCobrador.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        EditarCobradorController controller = context.getBean(EditarCobradorController.class);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            controller.initData(cobselec);
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla();
    }
    
    @FXML
    private void nuevoCobrador(){
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cargaCobrador.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla();
    }
    
    @FXML
    private void imprimirControl(){
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/imprimirControlCobranza.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        ImprimirControlCobranzaController controller = context.getBean(ImprimirControlCobranzaController.class);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            controller.initData(cobselec);
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla();
    }
    
    public void actualizarTabla(){
        //llenar tabla
        List<Cobrador> cobradores = cobser.findAllCobradores();
        // Convertir a wrappers
        List<CobradorWrapper> wrappers = cobradores.stream()
                .map(CobradorWrapper::new)
                .collect(Collectors.toList());
        //mapeo de columnas:
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colNroTelefono.setCellValueFactory(cell -> cell.getValue().nroTelefonoProperty());
        // Setear items
        tableCobradores.setItems(FXCollections.observableArrayList(wrappers));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCobrSelec.setDisable(true);
        lblNombreCob.setDisable(true);
        lblNroTelefono.setDisable(true);
        btnEditar.setDisable(true);
        btnImprimirControl.setDisable(true);
        
        //llenar tabla
        List<Cobrador> cobradores = cobser.findAllCobradores();
        // Convertir a wrappers
        List<CobradorWrapper> wrappers = cobradores.stream()
                .map(CobradorWrapper::new)
                .collect(Collectors.toList());
        //mapeo de columnas:
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colNroTelefono.setCellValueFactory(cell -> cell.getValue().nroTelefonoProperty());
        // Setear items
        tableCobradores.setItems(FXCollections.observableArrayList(wrappers));
        //listener para seleccion de categoria en tbl
        tableCobradores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnEditar.setDisable(false);
                lblCobrSelec.setDisable(false);
                lblNroTelefono.setDisable(false);
                lblNombreCob.setDisable(false);
                btnImprimirControl.setDisable(false);
                //setear textos a labels
                CobradorWrapper wrapper = tableCobradores.getSelectionModel().getSelectedItem();
                cobselec = cobser.findById(Long.parseLong(wrapper.idCobradorProperty().get()));
                lblNombreCob.setText("Nombre: "+cobselec.getNombre());
                lblNroTelefono.setText("Nro. Telefono: "+cobselec.getNroTelefono());
            } else {
                lblNombreCob.setText("Nombre: ");
                lblNroTelefono.setText("Nro. Telefono: ");
                btnEditar.setDisable(true);
                lblNombreCob.setDisable(true);
                lblNroTelefono.setDisable(true);
                btnImprimirControl.setDisable(true);
            }
        });
    }
    
}
