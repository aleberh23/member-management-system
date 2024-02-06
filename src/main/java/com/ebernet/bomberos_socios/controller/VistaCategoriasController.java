package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.ui.CategoriaWrapper;
import com.ebernet.bomberos_socios.ui.SocioTitularWrapper;
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
import static org.hibernate.query.Page.page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class VistaCategoriasController implements Initializable {
    
    private Categoria catselec;
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private ICategoriaService catser;
    
    @FXML
    private TableView<CategoriaWrapper> tableCategorias;
    
    @FXML
    private TableColumn<CategoriaWrapper, String> colId, colNombre, colImporte;
    
    @FXML
    private Label lblIdCat, lblNombreCat, lblImporte, lblCatSelec;
    
    @FXML
    private Button btnEditarImp, btnNueva; 
    
    @FXML
    private void editarImporte(){
         //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editarCategoria.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        EditarCategoriaController controller = context.getBean(EditarCategoriaController.class);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            controller.initData(catselec);
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla();
    }
    
    @FXML
    private void nuevaCategoria(){
         //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cargaCategoria.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla();
    }
    
    public void actualizarTabla(){
        List<Categoria> categorias = catser.findAllCategorias();
        // Convertir a wrappers
        List<CategoriaWrapper> wrappers = categorias.stream()
                .map(CategoriaWrapper::new)
                .collect(Collectors.toList());
        //mapeo de columnas:
        colId.setCellValueFactory(cell -> cell.getValue().idCategoriaProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colImporte.setCellValueFactory(cell -> cell.getValue().importeProperty());
        // Setear items
        tableCategorias.setItems(FXCollections.observableArrayList(wrappers));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditarImp.setDisable(true);
        lblCatSelec.setDisable(true);
        lblIdCat.setDisable(true);
        lblImporte.setDisable(true);
        lblNombreCat.setDisable(true);
        List<Categoria> categorias = catser.findAllCategorias();
        // Convertir a wrappers
        List<CategoriaWrapper> wrappers = categorias.stream()
                .map(CategoriaWrapper::new)
                .collect(Collectors.toList());
        //mapeo de columnas:
        colId.setCellValueFactory(cell -> cell.getValue().idCategoriaProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colImporte.setCellValueFactory(cell -> cell.getValue().importeProperty());
        // Setear items
        tableCategorias.setItems(FXCollections.observableArrayList(wrappers));
        //listener para seleccion de categoria en tbl
        tableCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnEditarImp.setDisable(false);
                lblCatSelec.setDisable(false);
                lblIdCat.setDisable(false);
                lblImporte.setDisable(false);
                lblNombreCat.setDisable(false);
                //setear textos a labels
                CategoriaWrapper wrapper = tableCategorias.getSelectionModel().getSelectedItem();
                catselec = catser.findById(Long.parseLong(wrapper.idCategoriaProperty().get()));
                lblIdCat.setText("Id: "+catselec.getIdCategoria());
                lblNombreCat.setText("Nombre: "+catselec.getNombre());
                lblImporte.setText("Importe: $"+catselec.getImporte());
            } else {
                lblIdCat.setText("Id: ");
                lblNombreCat.setText("Nombre: ");
                lblImporte.setText("Importe: ");
                btnEditarImp.setDisable(true);
                lblCatSelec.setDisable(true);
                lblIdCat.setDisable(true);
                lblImporte.setDisable(true);
                lblNombreCat.setDisable(true);
            }
        });
    }    
    
}
