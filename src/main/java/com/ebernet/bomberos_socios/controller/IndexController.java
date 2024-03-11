package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.Deuda;
import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.model.SocioTitular;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class IndexController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @FXML
    private Pane mainContent;
    
    @FXML
    private HBox top;
    
    @FXML
    private void info(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/infoEbernet.fxml"));
            HBox page = loader.load();
            // Crea un nuevo escenario (ventana)
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Hace que la nueva ventana sea modal
            dialogStage.setResizable(false); // Hace que la ventana no sea redimensionable
            dialogStage.setTitle("Informacion");
            dialogStage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Bloquea la ventana principal hasta que se cierre la nueva ventana
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void notificaciones(){
        loadView("/fxml/vistaNotificaciones.fxml");
        VistaNotificacionesController controller = context.getBean(VistaNotificacionesController.class);
        controller.initData();
    }
    
    @FXML
    private void categorias(){
        loadView("/fxml/vistaCategorias.fxml");
    }
    
    @FXML
    private void cobradores(){
        loadView("/fxml/vistaCobradores.fxml");
    }
    
    @FXML
    public void cargarSocio() {
        loadView("/fxml/cargaSocio.fxml");
        CargaSocioController controller = context.getBean(CargaSocioController.class);
        controller.permitirExpansion();
    }

    @FXML
    public void verSocio() {
        loadView("/fxml/vistaSocios.fxml");
    }

    public void editarSocio(SocioTitular socioToEdit) {
        //se carga la vista
        loadView("/fxml/editarSocio.fxml");
        //se obtiene el objeto EditarSocioController gracias a el application context (Gracias SPRING!!!)
        EditarSocioController controller = context.getBean(EditarSocioController.class);
        //se le setea al controlador el objeto socio el cual tiene que editar
        controller.setSocioEditar(socioToEdit);
    }

    public void detalleSocio(SocioTitular socioDetalle) {
        //se carga la vista
        loadView("/fxml/detalleSocio.fxml");
        //se obtiene el objeto DetalleSocioController gracias a el application context (Gracias SPRING!!!)
        DetalleSocioController controller = context.getBean(DetalleSocioController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(socioDetalle);
    }

    public void vistaSociosAdherentes(SocioTitular socio) {        
        //se carga la vista y el controller!
        loadView("/fxml/vistaAdherentesSocio.fxml");    
        //se obtiene el objeto DetalleSocioController gracias a el application context (Gracias SPRING!!!)        
        VistaAdherentesSocioController controller = context.getBean(VistaAdherentesSocioController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        if (controller != null) {
            System.out.println("CONTROLLER: "+controller);
            // Imprime todas las instancias gestionadas por Spring de VistaAdherenteSocioController
            String[] beanNames = context.getBeanNamesForType(VistaAdherentesSocioController.class);
            System.out.println("Instancias gestionadas por Spring de VistaAdherentesSocioController: " + Arrays.toString(beanNames));
            controller.initData(socio);
            
        } else {
            System.err.println("El controlador es nulo después de cargar la vista.");
        }
    }
    
    public void vistaDeudas(SocioTitular socio) {
        //se carga la vista
        loadView("/fxml/vistaDeudasSocio.fxml");
        //se obtiene el objeto VistaDeudasSocioController gracias a el application context (Gracias SPRING!!!)
        VistaDeudasSocioController controller = context.getBean(VistaDeudasSocioController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(socio);
    }
    
    public void cargaSocioAdherente(SocioTitular socio) {
        //se carga la vista
        loadView("/fxml/cargaSocioAdherente.fxml");
        //se obtiene el objeto CargaSocioAdherenteController gracias a el application context (Gracias SPRING!!!)
        CargaSocioAdherenteController controller = context.getBean(CargaSocioAdherenteController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(socio);
    }
    
    public void editarSocioAdherente(SocioAdherente socio) {
        //se carga la vista
        loadView("/fxml/editarSocioAdherente.fxml");
        //se obtiene el objeto EditarSocioAdherenteController gracias a el application context (Gracias SPRING!!!)
        EditarSocioAdherenteController controller = context.getBean(EditarSocioAdherenteController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(socio);
    }
    
    public void detalleSocioAdherente(SocioAdherente socio) {
        //se carga la vista
        loadView("/fxml/detalleSocioAdherente.fxml");
        //se obtiene el objeto DetalleSocioAdherenteController gracias a el application context (Gracias SPRING!!!)
       DetalleSocioAdherenteController controller = context.getBean(DetalleSocioAdherenteController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(socio);
    }
    
    public void cargaDeuda(SocioTitular sociotit) {
        //se carga la vista
        loadView("/fxml/cargaDeuda.fxml");
        //se obtiene el objeto CargaDeudaController gracias a el application context (Gracias SPRING!!!)
       CargaDeudaController controller = context.getBean(CargaDeudaController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(sociotit);
    }
    
    public void editarDeuda(Deuda deuda) {
        //se carga la vista
        loadView("/fxml/editarDeuda.fxml");
        //se obtiene el objeto EditarDeudaControler gracias a el application context (Gracias SPRING!!!)
        EditarDeudaController controller = context.getBean(EditarDeudaController.class);
        //se le setea al controlador el objeto socio el cual se quiere ver detalle
        controller.initData(deuda);
    }
    
    @FXML
    private void libroJuridico(){
        loadView("/fxml/libroJuridico.fxml");
    }

    private void loadView(String view) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view));   
            loader.setControllerFactory(context::getBean);
            Pane newLoadedPane = loader.load();        
            
            
            mainContent.getChildren().clear();           
            mainContent.getChildren().add(newLoadedPane);
            
            //BorderPane.setMargin(mainContent, new Insets(10,10,10,10)); // Puedes ajustar el margen según tus necesidades
            // Configuramos el tamaño preferido de la vista cargada para que se ajuste al mainContent
            VBox.setVgrow(newLoadedPane, Priority.ALWAYS);
            HBox.setHgrow(newLoadedPane, Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HBox.setHgrow(top, Priority.ALWAYS);
    }

}
