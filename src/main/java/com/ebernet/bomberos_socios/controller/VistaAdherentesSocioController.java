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
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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
        

        
       List<SocioAdherenteWrapper> wrappers = sociosAdherentes.stream()
        .map(SocioAdherenteWrapper::new)
        .sorted((w1, w2) -> {
            boolean baja1 = w1.bajaProperty().get().equals("Si");
            boolean baja2 = w2.bajaProperty().get().equals("Si");
            if (baja1 && !baja2) {
                return 1; // w1 viene después de w2
            } else if (!baja1 && baja2) {
                return -1; // w1 viene antes de w2
            } else {
                return 0; // mantener el orden original
            }
        })
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
        socioadhser.saveSocioAdherente(socioadh);
        checkearCambioCategoria();
        recargarTablaSociosAdherentes();
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
        checkearCambioCategoria();
        recargarTablaSociosAdherentes();
    }

    public void initData(SocioTitular socioTit) {
        //this.socio = sociotitser.findById(socioTit.getNroSocio());
        this.socio = socioTit;
        //traer socios adherentes de socio.
        List<SocioAdherente> sociosAdherentes = socioadhser.findAllByIdTitular(socio.getNroSocio());
        //setear titulo
        lblTitulo.setText("Socio seleccionado: " + socio.getNroSocio() + " - " + socio.getNombreCompleto());
        //crear y ordenar wrappers
        List<SocioAdherenteWrapper> wrappers = sociosAdherentes.stream()
        .map(SocioAdherenteWrapper::new)
        .sorted((w1, w2) -> {
            boolean baja1 = w1.bajaProperty().get().equals("Si");
            boolean baja2 = w2.bajaProperty().get().equals("Si");
            if (baja1 && !baja2) {
                return 1; // w1 viene después de w2
            } else if (!baja1 && baja2) {
                return -1; // w1 viene antes de w2
            } else {
                return 0; // mantener el orden original
            }
        })
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
        
        // Configurar RowFactory personalizado para cambiar el color de la fuente de la fila
        tableSocios.setRowFactory(tv -> {
            TableRow<SocioAdherenteWrapper> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (newValue.bajaProperty().get().equals("Si")) {
                        System.out.println("Si");
                        row.setTextFill(javafx.scene.paint.Color.RED); // Cambia el color de la fuente a rojo
                    } else {
                        System.out.println("No");
                        row.setTextFill(javafx.scene.paint.Color.BLACK);
                    }
                }
            });
            return row;
        });
               
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
        checkearCambioCategoria();
        recargarTablaSociosAdherentes();
        

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
    
    private void checkearCambioCategoria(){
        List<SocioAdherente> socios = socioadhser.findAllActivosByIdTitular(socio.getNroSocio());
        int cantAdherentes = socios.size();
        System.out.println("Cantidad adherentes: "+cantAdherentes);
        int cantidadHijosMayores = 0;       
        //recorremos lista de socios y identificamos la cantidad de hijos mayores
        if(!socios.isEmpty()){
            for (SocioAdherente socio : socios){
                if(socio.getVinculo().getNombre().equals("Hijo/a")){
                    Period periodo = Period.between(socio.getFechaNacimiento(), LocalDate.now());
                    int diferenciaEnAnios = periodo.getYears();
                    if(diferenciaEnAnios>=18){
                        cantidadHijosMayores++;
                    }
                }
            }
        }
        System.out.println("Cantidad hijos mayores: "+cantidadHijosMayores);
        if(socio.getCategoria().getIdCategoria() != 1 && cantAdherentes==0){
            // No tiene otros socios adherentes, sugiere cambio de categoría a "Individual"
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular ya no tiene socios adherentes.");
            alert.setContentText("La categoría actual del socio titular es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Individual\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findById(1L));
                sociotitser.createSocioTitular(socio);
            }
        }else if (socio.getCategoria().getIdCategoria() != 2 && cantidadHijosMayores < 1 && cantAdherentes>0){
            // El socio titular no está en la categoría "Grupo Familiar (menores)" y no tiene otros socios adherentes
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular tiene socios adherentes a su nombre.");
            alert.setContentText("La categoría actual del socio es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (menores)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findById(2L));
                sociotitser.createSocioTitular(socio);
            }
        }else if (socio.getCategoria().getIdCategoria() != 3 && cantidadHijosMayores==1 && cantAdherentes>0){
            // El socio titular no está en la categoría "Grupo Familiar (mayores)" y tiene un hijo mayor
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular tiene un socio adherente 'Hijo/a' mayor.");
            alert.setContentText("La categoría actual del socio es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (mayores)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findById(3L));
                sociotitser.createSocioTitular(socio);
            }
        }else if (socio.getCategoria().getIdCategoria() != 6 && cantidadHijosMayores>1 && cantAdherentes>0){
            // El socio titular no está en la categoría "Grupo Familiar (mas de un mayor)" y tiene mas de un hijo mayor
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cambio de Categoría");
            alert.setHeaderText("El socio titular tiene mas de un socio adherente 'Hijo/a' mayor.");
            alert.setContentText("La categoría actual del socio es " + socio.getCategoria().getNombre()
                    + "¿Desea cambiar la categoría del socio titular a \"Grupo Familiar (mas de un mayor)\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario seleccionó "Sí", realiza el cambio de categoría aquí
                socio.setCategoria(categoriaser.findById(6L));
                sociotitser.createSocioTitular(socio);
            }
        }
        
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lblSocioSelec.setDisable(true);
        btnBaja.setDisable(true);
        btnEditar.setDisable(true);
        btnDetalles.setDisable(true);
        
         // Configurar RowFactory personalizado para cambiar el color de la fuente de la fila
        /*tableSocios.setRowFactory(tv -> new TableRow<SocioAdherenteWrapper>() {
            @Override
            protected void updateItem(SocioAdherenteWrapper item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.bajaProperty().get().equals("Si")) {
                        System.out.println("item propert 'Si'");
                        setTextFill(javafx.scene.paint.Color.RED); // Establece el color de fuente a rojo
                    } else {
                        System.out.println("item propert 'No'");
                        setTextFill(javafx.scene.paint.Color.BLACK); // Establece el color de fuente a negro
                    }
                }
            }
        });*/
    }

}
