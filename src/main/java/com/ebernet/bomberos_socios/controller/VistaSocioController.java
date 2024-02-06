package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.ui.SocioTitularWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Pagination;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class VistaSocioController implements Initializable {

    //ATRIBUTO SOCIO TITULAR SELECCIONADO
    private SocioTitular socioSeleccionado;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private IndexController index;

    @Autowired
    private ISocioTitularService sociotitser;

    @FXML
    private TextField txtFiltro;

    @FXML
    private CheckBox chkbxDeBaja;

    @FXML
    private TableView<SocioTitularWrapper> tblSocios;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colNroSocio;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colNombre;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colTipoDoc;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colDoc;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colCategoria;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colFDNacim;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colFDIng;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colDomicilio;

    @FXML
    private TableColumn<SocioTitularWrapper, String> colLocalidad;

    @FXML
    private Pagination pagination;

    @FXML
    private Label lblSocioSelec;

    @FXML
    private Button btnBaja;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnDetalles;
    
    @FXML
    private Button btnRecibo;
    
    @FXML
    private Button btnTodosLosRecibos;

    
    @FXML
    private void reciboSocio(){
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/imprimirReciboSocio.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        //Obtener controller
        ImprimirReciboSocioController controller = context.getBean(ImprimirReciboSocioController.class);

        Parent root;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            controller.initData(socioSeleccionado);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    @FXML
    private void imprimirTodosLosRecibos() {
        //cargar vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/imprimirRecibos.fxml"));
        //pasarle el application context
        loader.setControllerFactory(context::getBean);
        //Obtener controller
        ImprimirRecibosController controller = context.getBean(ImprimirRecibosController.class);

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
    }

    @FXML
    private void editarSocio() {
        index.editarSocio(socioSeleccionado);
    }
    
    @FXML 
    private void detalleSocio() {
        System.out.println("Entra a detalleSocio()");
        index.detalleSocio(socioSeleccionado);
    }

    private void altaSocio() {
        try {
            //cargar vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/altaSocio.fxml"));
            //pasarle el application context
            loader.setControllerFactory(context::getBean);
            //Obtener controller
            AltaSocioController controller = context.getBean(AltaSocioController.class);

            Parent root;

            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            //iniciar data dandole el socio a dar de baja.
            controller.initData(socioSeleccionado);
            stage.showAndWait();
            handleFilterChange();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bajaSocio() {
        try {
            //cargar vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bajaSocio.fxml"));
            //pasarle el application context
            loader.setControllerFactory(context::getBean);
            //Obtener controller
            BajaSocioController controller = context.getBean(BajaSocioController.class);

            Parent root;

            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            //iniciar data dandole el socio a dar de baja.
            stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
            controller.initData(socioSeleccionado);
            stage.showAndWait();
            handleFilterChange();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cambiarPagina(int pagina) {
        // Obtener page  
        Page<SocioTitular> page;
        System.out.println("pagina: "+pagina);

        if (txtFiltro.getText().isEmpty() && !chkbxDeBaja.isSelected()) {
            // Sin filtro
            page = sociotitser.findAllSociosTitulares(pagina, 50);

        } else {
            // Con filtro
            page = sociotitser.findByFilter(txtFiltro.getText().toLowerCase(), chkbxDeBaja.isSelected(), pagina, 50);
        }

        // Convertir a wrappers
        List<SocioTitularWrapper> wrappers = page.getContent().stream()
                .map(SocioTitularWrapper::new)
                .collect(Collectors.toList());

        // Mapeo de columnas 
        colNroSocio.setCellValueFactory(cell -> cell.getValue().nroSocioProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colCategoria.setCellValueFactory(cell -> cell.getValue().categoriaProperty());
        colTipoDoc.setCellValueFactory(cell -> cell.getValue().tipoDocProperty());
        colDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colDomicilio.setCellValueFactory(cell -> cell.getValue().domicilioProperty());
        colLocalidad.setCellValueFactory(cell -> cell.getValue().localidadProperty());
        colFDIng.setCellValueFactory(cell -> cell.getValue().fechaIngresoProperty());
        colFDNacim.setCellValueFactory(cell -> cell.getValue().fechaNacimientoProperty());

        // Setear items
        tblSocios.setItems(FXCollections.observableArrayList(wrappers));

    }

    @FXML
    private void handleFilterChange() {
        String filterText = txtFiltro.getText().toLowerCase();
        System.out.println(filterText);
        boolean baja = chkbxDeBaja.isSelected();
        System.out.println(baja);
        
        //setear cantidad de paginas
        long totalRegistros = sociotitser.countByFilter(filterText, baja);
        double totalPaginasD = Math.ceil(totalRegistros / 50d);
        int totalPaginas = (int) totalPaginasD;
        System.out.println("Total registros: " + totalRegistros);
        System.out.println("Total páginas: " + totalPaginas);
        pagination.setPageCount(totalPaginas);
        

        // Obtener page  
        Page<SocioTitular> page = sociotitser.findByFilter(filterText, baja, 0, 50);

        // Convertir a wrappers
        List<SocioTitularWrapper> wrappers = page.getContent().stream()
                .map(SocioTitularWrapper::new)
                .collect(Collectors.toList());

        // Mapeo de columnas 
        colNroSocio.setCellValueFactory(cell -> cell.getValue().nroSocioProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colCategoria.setCellValueFactory(cell -> cell.getValue().categoriaProperty());
        colTipoDoc.setCellValueFactory(cell -> cell.getValue().tipoDocProperty());
        colDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colDomicilio.setCellValueFactory(cell -> cell.getValue().domicilioProperty());
        colLocalidad.setCellValueFactory(cell -> cell.getValue().localidadProperty());
        colFDIng.setCellValueFactory(cell -> cell.getValue().fechaIngresoProperty());
        colFDNacim.setCellValueFactory(cell -> cell.getValue().fechaNacimientoProperty());

        // Setear items
        tblSocios.setItems(FXCollections.observableArrayList(wrappers));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        long totalRegistros = sociotitser.countByFilter("", false);
        double totalPaginasD = Math.ceil(totalRegistros / 50d);
        int totalPaginas = (int) totalPaginasD;
        System.out.println("Total registros: " + totalRegistros);
        System.out.println("Total páginas: " + totalPaginas);
        pagination.setPageCount(totalPaginas);

        // Obtener page  
        Page<SocioTitular> page = sociotitser.findByFilter("", false, 0, 50);
        //Page<SocioTitular> page= sociotitser.findAllSociosTitulares(0, 50);
        System.out.println("Contenido de pagina: "+page.getContent().size());

        // Convertir a wrappers
        List<SocioTitularWrapper> wrappers = page.getContent().stream()
                .map(SocioTitularWrapper::new)
                .collect(Collectors.toList());
        System.out.println("Número de elementos en wrappers: " + wrappers.size());

        // Mapeo de columnas 
        colNroSocio.setCellValueFactory(cell -> cell.getValue().nroSocioProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colCategoria.setCellValueFactory(cell -> cell.getValue().categoriaProperty());
        colTipoDoc.setCellValueFactory(cell -> cell.getValue().tipoDocProperty());
        colDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colDomicilio.setCellValueFactory(cell -> cell.getValue().domicilioProperty());
        colLocalidad.setCellValueFactory(cell -> cell.getValue().localidadProperty());
        colFDIng.setCellValueFactory(cell -> cell.getValue().fechaIngresoProperty());
        colFDNacim.setCellValueFactory(cell -> cell.getValue().fechaNacimientoProperty());

        // Setear items
        tblSocios.setItems(FXCollections.observableArrayList(wrappers));

        //Setearle un event listener al txtFiltro
        txtFiltro.textProperty().addListener((obs, oldText, newText) -> {
            handleFilterChange();
        });

        //Setearle un event listener a chkboxbaja
        chkbxDeBaja.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                handleFilterChange();
            }
        });

        //btn y label de acciones disabled por defecto
        lblSocioSelec.setDisable(true);
        btnBaja.setDisable(true);
        btnEditar.setDisable(true);
        btnDetalles.setDisable(true);
        btnRecibo.setDisable(true);

        //listener para seleccion de socio en tbl
        tblSocios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //habilitar label
                lblSocioSelec.setDisable(false);
                lblSocioSelec.setPrefWidth(350);
                //setear texto label
                SocioTitularWrapper wrapper = tblSocios.getSelectionModel().getSelectedItem();
                lblSocioSelec.setText("Socio seleccionado: " + wrapper.nroSocioProperty().get() + " - " + wrapper.nombreCompletoProperty().get());
                //traer socio seleccionado
                Long nroSocio = Long.parseLong(wrapper.nroSocioProperty().get());
                this.socioSeleccionado = sociotitser.findById(nroSocio);
                //habilitar botones                           
                btnEditar.setDisable(false);
                btnDetalles.setDisable(false);
                btnRecibo.setDisable(false);
                if (socioSeleccionado.isBaja()) {
                    btnBaja.setDisable(false);
                    btnBaja.setText("Alta");
                    btnBaja.setOnAction(event -> altaSocio());
                } else {
                    btnBaja.setDisable(false);
                    btnBaja.setText("Baja");
                    btnBaja.setOnAction(event -> bajaSocio());
                }
            } else {
                lblSocioSelec.setText("Socio seleccionado: ");
                lblSocioSelec.setPrefWidth(137);
                lblSocioSelec.setDisable(true);
                btnBaja.setDisable(true);
                btnEditar.setDisable(true);
                btnDetalles.setDisable(true);
                btnRecibo.setDisable(true);
                this.socioSeleccionado = null;
            }

        });
        
        // Agrega un listener al índice de la página actual
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cambiarPagina(newValue.intValue());
            }
        });

    }
}
