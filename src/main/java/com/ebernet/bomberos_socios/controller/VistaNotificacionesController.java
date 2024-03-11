package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.dto.NotificacionDTO;
import com.ebernet.bomberos_socios.model.Categoria;
import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.service.ISocioAdherenteService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaNotificacionesController implements Initializable {
    
    private HashMap<Integer, NotificacionDTO> notificacionesHashMap;
    
    private NotificacionDTO notSeleccionada;
    
    @Autowired
    private IndexController index;
    
    @Autowired
    private ISocioAdherenteService socioadhser;
    
    @Autowired
    private ISocioTitularService sociotitser;
    
    @Autowired
    private ICategoriaService catser;
    
    @FXML
    private TableView<NotificacionDTO> tableNotificaciones;
    
    @FXML
    private TableColumn<NotificacionDTO, String> colNroSocio, colSocioTit, colSocioAdh, colFechaCumpl, colCatActual, colCatSugerida; 
    
    @FXML
    private Label lblSelecc;
    
    @FXML
    private Button btnVer, btnCambiar;
    
    @FXML
    private void cambiarCat(){
        // Crear alerta
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambio de categoría");
        alert.setHeaderText("Cambio de categoría"); // Desactivar encabezado predeterminado

        // Configurar contenido de la alerta
        alert.setContentText("¿Está seguro que desea cambiar la categoría de " + notSeleccionada.getSocioTitular().getNombreCompleto() +
                " de " + notSeleccionada.getCatActual().getNombre() + " a " + notSeleccionada.getCatSugerida().getNombre() + "?");

        // Configurar botones
        ButtonType buttonTypeSi = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);

        // Mostrar la alerta y esperar a que el usuario seleccione una opción
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeSi) {
                SocioTitular s = notSeleccionada.getSocioTitular();
                s.setCategoria(notSeleccionada.getCatSugerida());
                sociotitser.createSocioTitular(s);
            } 
        });
        actualizarTabla();
    }    
    @FXML
    private void verSociosAdh(){
        index.detalleSocioAdherente(notSeleccionada.getSocioAdherente());
    }
    
    public void actualizarTabla(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<NotificacionDTO> notificaciones = armarNotificacionesDTO();        
        for(NotificacionDTO n : notificaciones){
            this.notificacionesHashMap.put(n.getId(), n);
        }
        ObservableList<NotificacionDTO> notificacionesObservable = FXCollections.observableArrayList(notificaciones);
         // Configurar las celdas de las columnas
        colNroSocio.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNroSocio())));
        colSocioTit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSocioTitular().getNombreCompleto()));
        colSocioAdh.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSocioAdherente().getNombreCompleto()));
        colFechaCumpl.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaCumpleaños().format(dateFormatter)));
        colCatActual.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCatActual().getNombre()));
        colCatSugerida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCatSugerida().getNombre()));
        
        tableNotificaciones.setItems(notificacionesObservable);  
    }
    
    private List<NotificacionDTO> armarNotificacionesDTO(){
        int idIncremental = 1;
        List<SocioAdherente> sociosQueCumplen = socioadhser.findSociosCumplen18();
        System.out.println("Socios que cumplen 18: "+sociosQueCumplen.size());
        List<NotificacionDTO> notificaciones = new ArrayList<NotificacionDTO>();
        for(SocioAdherente socio : sociosQueCumplen){
            System.out.println(checkearCambioCategoria(socio)==null);
           if(checkearCambioCategoria(socio)!=null){
               NotificacionDTO n = new NotificacionDTO();
               n.setSocioAdherente(socio);
               n.setSocioTitular(socio.getSocioTitular());
               n.setNroSocio(socio.getSocioTitular().getNroSocio());
               n.setCatActual(socio.getSocioTitular().getCategoria());
               n.setCatSugerida(checkearCambioCategoria(socio));
               n.setFechaCumpleaños(socio.getFechaNacimiento().plusYears(18));
               n.setId(idIncremental);
               notificaciones.add(n);
               idIncremental++;
           }
        }
        System.out.println("NOTIFICACIONES: "+notificaciones.size());
        return notificaciones;
    }
    
    private Categoria checkearCambioCategoria(SocioAdherente socioAdh){
        int cantidadHijosMayores = 0;
        //traemos los "Hijos" por socio titular
        List<SocioAdherente> socios = socioadhser.findAllHijosActivosByIdTitular(socioAdh.getSocioTitular().getNroSocio());
        Iterator<SocioAdherente> iterator = socios.iterator();        
        //recorremos la lista de socios adherentes hijos
        while (iterator.hasNext()) {
            SocioAdherente socio = iterator.next();

            if (socio.getNroSocio() == socioAdh.getNroSocio()) {
                System.out.println("SE ENCONTRO SOCIO EN CUESTION EN LISTA DE ADHERENTES, SE ELIMINA");
                //eliminamos el que recibe el metodo por parametro.
                iterator.remove();
            } else {
                Period periodo = Period.between(socio.getFechaNacimiento(), LocalDate.now());

                int diferenciaEnAnios = periodo.getYears();
                if (diferenciaEnAnios >= 18) {
                    cantidadHijosMayores++;
                }
            }
        }
        Period periodo = Period.between(socioAdh.getFechaNacimiento(), LocalDate.now());
        LocalDate fechaLimiteSup = socioAdh.getFechaNacimiento().plusYears(18).plusMonths(1);
        LocalDate fechaLimiteInf = socioAdh.getFechaNacimiento().plusYears(18).minusMonths(1);
        LocalDate fechaCumpleAños = socioAdh.getFechaNacimiento().plusYears(18);
        int diferenciaEnAniosSocioAdh = periodo.getYears();
        if(diferenciaEnAniosSocioAdh>18 || fechaCumpleAños.isAfter(fechaLimiteInf) && fechaCumpleAños.isBefore(fechaLimiteSup)){
            cantidadHijosMayores++;
            if(socioAdh.getSocioTitular().getCategoria().getIdCategoria()!=6 && cantidadHijosMayores>=2){
                return catser.findById(6L);
            }else if (socioAdh.getSocioTitular().getCategoria().getIdCategoria()!=3 && cantidadHijosMayores<=1){
                return catser.findById(3L);
            }
        }
        return null;
    }
    
    public void initData(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<NotificacionDTO> notificaciones = armarNotificacionesDTO();        
        for(NotificacionDTO n : notificaciones){
            this.notificacionesHashMap.put(n.getId(), n);
        }
        ObservableList<NotificacionDTO> notificacionesObservable = FXCollections.observableArrayList(notificaciones);
         // Configurar las celdas de las columnas
        colNroSocio.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNroSocio())));
        colSocioTit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSocioTitular().getNombreCompleto()));
        colSocioAdh.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSocioAdherente().getNombreCompleto()));
        colFechaCumpl.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaCumpleaños().format(dateFormatter)));
        colCatActual.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCatActual().getNombre()));
        colCatSugerida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCatSugerida().getNombre()));
        
        tableNotificaciones.setItems(notificacionesObservable);  
        
        tableNotificaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                notSeleccionada = notificacionesHashMap.get(newSelection.getId());
                lblSelecc.setText(notSeleccionada.getSocioAdherente().getNombreCompleto()+" - "+notSeleccionada.getCatActual().getNombre()+" A "+notSeleccionada.getCatSugerida().getNombre());
                btnCambiar.setDisable(false);
                btnVer.setDisable(false);
            }else{
                lblSelecc.setText("");
                btnCambiar.setDisable(true);
                btnVer.setDisable(true);
            }
        });
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.notificacionesHashMap = new HashMap<>();
        btnCambiar.setDisable(true);
        btnVer.setDisable(true);
        lblSelecc.setText("");
    }    
    
}
