package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.SocioTitularService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AltaSocioController implements Initializable {
    
    private SocioTitular socio;
    
    @Autowired
    private SocioTitularService sociotitser;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private Button btnAlta;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private void altaSocio(){
        socio.setFechaBaja(null);
        socio.setTipoBaja(null);
        socio.setBaja(false);
        
        sociotitser.createSocioTitular(socio);
        
        //obtener el stage
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Cierra el Stage
        stage.close();
    }
    
    public void initData(SocioTitular sociotit){
        this.socio = sociotit;
        lblTitulo.setText("Alta de socio: "+socio.getNroSocio()+" - "+socio.getNombreCompleto());
        
        // Obtén el Stage desde la escena
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        // Configura la propiedad de resizable a false
        stage.setResizable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
