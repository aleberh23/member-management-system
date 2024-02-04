package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.service.ISocioAdherenteService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.ui.SocioAdherenteWrapper;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class VistaAdherentesSocioController implements Initializable {

    //@Autowired
    //private ISocioTitularService sociotitser;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private IndexController index;

    @Autowired
    private ISocioAdherenteService socioadhser;

    @Autowired
    private ISocioTitularService sociotitser;

    @Autowired
    private ICategoriaService categoriaser;

    private SocioTitular socio;
    private SocioAdherente socioSeleccionado;
    private HashMap<Long, SocioAdherente> socios;

    @FXML
    private Label lblTitulo;

    @FXML
    private TableView<SocioAdherenteWrapper> tableSocios;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colNombre;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colTipoDoc;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colNroDoc;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colFechaNac;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colVinculo;

    @FXML
    private TableColumn<SocioAdherenteWrapper, String> colBaja;

    @FXML
    private Label lblSocioSelec;

    @FXML
    private Button btnBaja;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnDetalles;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnVolver;

    @FXML
    private void volverDetalleSocio() {
        index.detalleSocio(socio);
    }

    @FXML
    private void nuevoAdherente() {
        index.cargaSocioAdherente(socio);
    }

    public void recargarTablaSociosAdherentes() {
        // Vuelve a cargar los datos de los socios adherentes desde la base de datos
        List<SocioAdherente> sociosAdherentes = socioadhser.findAllByIdTitular(socio.getNroSocio());

        // Convierte a wrappers
        List<SocioAdherenteWrapper> wrappers = sociosAdherentes.stream()
                .map(SocioAdherenteWrapper::new)
                .collect(Collectors.toList());
        
        //Mapeo de columnas :)
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colTipoDoc.setCellValueFactory(cell -> cell.getValue().tipoDocProperty());
        colNroDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colVinculo.setCellValueFactory(cell -> cell.getValue().vinculoProperty());
        colBaja.setCellValueFactory(cell -> cell.getValue().bajaProperty());
        colFechaNac.setCellValueFactory(cell -> cell.getValue().fechaNacimientoProperty());

        // Convierte a HashMap
        this.socios = (HashMap<Long, SocioAdherente>) sociosAdherentes.stream()
                .collect(Collectors.toMap(SocioAdherente::getNroSocio, socio -> socio));

        // Actualiza los items en la tabla
        tableSocios.setItems(FXCollections.observableArrayList(wrappers));
    }

    public void altaSocio() {
        SocioTitular sociotit = this.socio;
        SocioAdherente socioadh = this.socioSeleccionado;
        Alert modalAlta = new Alert(AlertType.CONFIRMATION);
            modalAlta.setTitle("Alta de socio");
            modalAlta.setHeaderText("Alta de socio: "+socioadh.getNombreCompleto());
            modalAlta.setContentText("¿Desea dar de alta a este socio nuevamente?");
            Optional<ButtonType> resultado = modalAlta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", se realiza el alta logica
                socioSeleccionado.setBaja(false);
                socioSeleccionado.setFechaBaja(null);
                socioSeleccionado.setTipoBaja(null);
            }
        
        //actualizar la tabla
        recargarTablaSociosAdherentes();
        //alertas de sugerencia de cambio de categoria.
        // Obtén la fecha actual
        LocalDate fechaActual = LocalDate.now();
        System.out.println(socioadh);
        // Calcula la diferencia en años
        int diferenciaEnAnios = Period.between(socioadh.getFechaNacimiento(), fechaActual).getYears();
        
        System.out.println("Diferencia en años: "+diferenciaEnAnios
                +"\nVinculo: "+socioadh.getVinculo().getNombre()
                +"\nSocios mayores: "+socioadhser.tieneSociosMayores(sociotit.getNroSocio()));
        System.out.println("Socios: "+socioadhser.tieneSocios(sociotit.getNroSocio()));
        
        if (diferenciaEnAnios >= 18 && socioadh.getVinculo().getNombre().equals("Hijo/a") && !socioadhser.tieneSociosMayores(sociotit.getNroSocio())) {
            // El socio adherente es de vinculo "Hijo/a", es mayor a 18 años y el socio titular no tiene otros socios adherentes mayores.
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio adherente es mayor a 18 años.");
            alert.setContentText("La categoría actual del socio es " + sociotit.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (mayores)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                sociotit.setCategoria(categoriaser.findByNombre("Grupo Familiar (mayores)"));
                sociotitser.createSocioTitular(sociotit);
            }
        } else if (!socio.getCategoria().getNombre().equals("Grupo Familiar (menores)") && !socioadhser.tieneSocios(sociotit.getNroSocio())) {
            // El socio titular no está en la categoría "Grupo Familiar (menores)" y no tiene otros socios adherentes
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular tiene un nuevo socio adherente");
            alert.setContentText("La categoría actual del socio es " + sociotit.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (menores)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                sociotit.setCategoria(categoriaser.findByNombre("Grupo Familiar (menores)"));
                sociotitser.createSocioTitular(sociotit);
            }
        }
        socioadhser.saveSocioAdherente(socioadh);
    }

    public void bajaSocio() {
        try {
            //cargar vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bajaSocioAdherente.fxml"));
            //pasarle el application context
            loader.setControllerFactory(context::getBean);
            //Obtener controller
            BajaSocioAdherenteController controller = context.getBean(BajaSocioAdherenteController.class);

            Parent root;

            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Establecer la modality para bloquear la ventana principal
            stage.initModality(Modality.APPLICATION_MODAL); // O Modality.WINDOW_MODAL
            //iniciar data dandole el socio a dar de baja.
            controller.initData(socioSeleccionado);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(VistaSocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        recargarTablaSociosAdherentes();
        //alertas de sugerencia de cambio de categoria.
        if (!socioadhser.tieneSocios(socio.getNroSocio())&&!socio.getCategoria().getNombre().equals("Individual")) {
            // No tiene otros socios adherentes, sugiere cambio de categoría a "Individual"
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular ya no tiene socios adherentes.");
            alert.setContentText("La categoría actual del socio titular es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Individual\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findByNombre("Individual"));
                sociotitser.createSocioTitular(socio);
            }
        } else if (socio.getCategoria().getNombre().equals("Grupo Familiar (mayores)")
                && !socioadhser.tieneSociosMayores(socio.getNroSocio())) {
            // El socio titular está en la categoría "Grupo Familiar (mayores)" y no tiene otros socios adherentes mayores
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular ya no tiene socios adherentes mayores.");
            alert.setContentText("La categoría actual del socio titular es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (menores)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findByNombre("Grupo Familiar (menores)"));
                sociotitser.createSocioTitular(socio);
            }
        }
        recargarTablaSociosAdherentes();
    }

    public void initData(SocioTitular socioTit) {
        //this.socio = sociotitser.findById(socioTit.getNroSocio());
        this.socio = socioTit;
        //traer socios adherentes de socio.
        List<SocioAdherente> sociosAdherentes = socioadhser.findAllByIdTitular(socio.getNroSocio());
        //setear titulo
        lblTitulo.setText("Socio seleccionado: " + socio.getNroSocio() + " - " + socio.getNombreCompleto());
        // Convertir a wrappers
        List<SocioAdherenteWrapper> wrappers = sociosAdherentes.stream()
                .map(SocioAdherenteWrapper::new)
                .collect(Collectors.toList());
        //convertir a hashmap
        this.socios = (HashMap<Long, SocioAdherente>) sociosAdherentes.stream()
                .collect(Collectors.toMap(SocioAdherente::getNroSocio, socio -> socio));

        //Mapeo de columnas :)
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreCompletoProperty());
        colTipoDoc.setCellValueFactory(cell -> cell.getValue().tipoDocProperty());
        colNroDoc.setCellValueFactory(cell -> cell.getValue().nroDocumentoProperty());
        colVinculo.setCellValueFactory(cell -> cell.getValue().vinculoProperty());
        colBaja.setCellValueFactory(cell -> cell.getValue().bajaProperty());
        colFechaNac.setCellValueFactory(cell -> cell.getValue().fechaNacimientoProperty());

        //Setear items
        tableSocios.setItems(FXCollections.observableArrayList(wrappers));

        //listener para seleccion de socio en tbl
        tableSocios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //habilitar label
                lblSocioSelec.setDisable(false);
                //setear texto label
                SocioAdherenteWrapper wrapper = tableSocios.getSelectionModel().getSelectedItem();
                lblSocioSelec.setText("Socio seleccionado: " + wrapper.nroSocioProperty().get() + " - " + wrapper.nombreCompletoProperty().get());
                //traer socio seleccionado
                Long nroSocio = Long.parseLong(wrapper.nroSocioProperty().get());
                this.socioSeleccionado = socios.get(nroSocio);
                //habilitar botones                           
                btnEditar.setDisable(false);
                btnDetalles.setDisable(false);
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
                lblSocioSelec.setDisable(true);
                btnBaja.setDisable(true);
                btnEditar.setDisable(true);
                btnDetalles.setDisable(true);
                this.socioSeleccionado = null;
            }

        });

    }
    
    //metodo para editar socio seleccionado
    @FXML
    public void editarAdherente(){
        index.editarSocioAdherente(socioSeleccionado);
    }
    
    //metodo detalle socio seleccionado
    @FXML
    public void detalleAdherente(){
        index.detalleSocioAdherente(socioSeleccionado);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //btn y label de acciones disabled por defecto
        lblSocioSelec.setDisable(true);
        btnBaja.setDisable(true);
        btnEditar.setDisable(true);
        btnDetalles.setDisable(true);
    }

}
