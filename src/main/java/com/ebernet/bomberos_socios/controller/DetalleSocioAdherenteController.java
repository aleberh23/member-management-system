package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioAdherente;
import com.ebernet.bomberos_socios.service.ISocioAdherenteService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetalleSocioAdherenteController implements Initializable {
    
    @Autowired
    private ISocioAdherenteService socioadhser;
    
    @Autowired
    private IndexController index;
    
    
    private SocioAdherente socio;
    
    @FXML
    private Label lblDeBaja, lblFechaBaja, lblTipoDeBaja;
    
    @FXML
    private TextField txtNombre, txtTipoDoc, txtNroDoc, txtFechaNac, txtVinculo, txtFechaBaja,txtTipoBaja, txtNroSocioTit, txtNombreTit, txtCategoriaTit;   
    
    @FXML
    private Button btnVolver;
    
    @FXML
    private void volver(){
        index.vistaSociosAdherentes(socio.getSocioTitular());
    }
    
    public void initData(SocioAdherente socioadh){
        this.socio = socioadh;
        
        txtNombre.setText(socio.getNombreCompleto());
        txtTipoDoc.setText(socio.getTipoDoc().getNombre());
        txtNroDoc.setText(Long.toString(socio.getNroDocumento()));
        txtFechaNac.setText(socio.getFechaNacimiento().toString());
        txtVinculo.setText(socio.getVinculo().getNombre());
        
        txtNroSocioTit.setText(Long.toString(socio.getSocioTitular().getNroSocio()));
        txtNombreTit.setText(socio.getSocioTitular().getNombreCompleto());
        txtCategoriaTit.setText(socio.getSocioTitular().getCategoria().getIdCategoria()+" - "+socio.getSocioTitular().getCategoria().getNombre());
        
        if (socio.isBaja()){
            lblDeBaja.setVisible(true);
            lblDeBaja.setText("¡DADO DE BAJA!");
            lblFechaBaja.setVisible(true);
            txtFechaBaja.setVisible(true);
            txtFechaBaja.setText(socio.getFechaBaja().toString());
            lblTipoDeBaja.setVisible(true);
            txtTipoBaja.setVisible(true);
            txtTipoBaja.setText(socio.getTipoBaja().getNombre());            
        }else{
            lblDeBaja.setVisible(false);
            lblFechaBaja.setVisible(false);
            txtFechaBaja.setVisible(false);
            lblTipoDeBaja.setVisible(false);
            txtTipoBaja.setVisible(false);        
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
