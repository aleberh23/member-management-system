package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.model.SocioTitular;
import com.ebernet.bomberos_socios.service.ICategoriaService;
import com.ebernet.bomberos_socios.service.ICobradorService;
import com.ebernet.bomberos_socios.service.IDeudaService;
import com.ebernet.bomberos_socios.service.IDomicilioService;
import com.ebernet.bomberos_socios.service.ILocalidadService;
import com.ebernet.bomberos_socios.service.ISocioTitularService;
import com.ebernet.bomberos_socios.service.ITipoBajaService;
import com.ebernet.bomberos_socios.service.ITipoDocumentoService;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetalleSocioController implements Initializable {

    private SocioTitular socio;

    //dependencias
    @Autowired
    private IndexController index;
    @Autowired
    private ISocioTitularService soctitser;
    @Autowired
    private ICategoriaService catser;
    @Autowired
    private ILocalidadService locser;
    @Autowired
    private ITipoDocumentoService tipodocser;
    @Autowired
    private ICobradorService cobradorser;
    @Autowired
    private IDomicilioService domser;
    @Autowired
    private ITipoBajaService tipobajaser;
    @Autowired
    private IDeudaService deudaser;

    //nodos 
    @FXML
    private Label lblSocio;

    @FXML
    private Label lblDeBaja, lblDeudor;

    @FXML
    private Label lblFechaBaja;

    @FXML
    private TextField txtFechaNacimiento;

    @FXML
    private TextField txtFechaIngreso;

    @FXML
    private TextField txtFechaBaja;

    @FXML
    private BorderPane rootPane;

    @FXML
    private HBox centerHBox;

    // Atributos FXML para la primera VBox
    @FXML
    private Label lblTitulo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtTipoDoc;

    @FXML
    private TextField txtNroDocumento;

    @FXML
    private TextField txtCobrador;

    // Atributos FXML para la segunda VBox
    @FXML
    private ImageView imageView;

    @FXML
    private Label domicilioLabel;

    @FXML
    private TextField txtCalle;

    @FXML
    private TextField txtNro;

    @FXML
    private TextField txtLocalidad;

    //VBOX de datos de baja
    @FXML
    private VBox vboxBaja;
    //cmbx de tipo de baja
    @FXML
    private TextField txtTipoBaja;

    //BOTONES
    @FXML
    private Button btnDeudas;

    @FXML
    private Button btnAdherentes;
    
    @FXML
    private void sociosAdherentes(){
        index.vistaSociosAdherentes(socio);
    }
    
    @FXML
    private void deudasSocio(){
        index.vistaDeudas(socio);
    }

    public void initData(SocioTitular sociotit) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.socio = sociotit;
        
        lblTitulo.setText("Socio N° "+socio.getNroSocio());
        
        txtNombre.setText(socio.getNombreCompleto());
        txtCategoria.setText(socio.getCategoria().getIdCategoria()+" - "+socio.getCategoria().getNombre());
        txtTipoDoc.setText(socio.getTipoDoc().getNombre());
        txtNroDocumento.setText(Long.toString(socio.getNroDocumento()));
        txtCobrador.setText(socio.getCobrador().getNombre());
        txtFechaNacimiento.setText(socio.getFechaNacimiento().format(dateFormatter));
        txtFechaIngreso.setText(socio.getFechaIngreso().format(dateFormatter));
        
        //domicilio
        txtCalle.setText(socio.getDomicilio().getCalle());
        txtNro.setText(Integer.toString(socio.getDomicilio().getNro()));
        txtLocalidad.setText(socio.getDomicilio().getLocalidad().getNombre());
        
        //baja
        if(socio.isBaja()){
            vboxBaja.setVisible(true);
            lblFechaBaja.setVisible(true);
            txtFechaBaja.setVisible(true);
            lblDeBaja.setDisable(false);
            lblDeBaja.setVisible(true);
            
            txtFechaBaja.setText(socio.getFechaBaja().format(dateFormatter));
            txtTipoBaja.setText(socio.getTipoBaja().getNombre());
            lblDeBaja.setText("¡DADO DE BAJA!");
        }else{
            vboxBaja.setVisible(false);
            lblFechaBaja.setVisible(false);
            txtFechaBaja.setVisible(false);
            lblDeBaja.setDisable(true);
            lblDeBaja.setVisible(false);
            
            txtFechaBaja.setText("");
            txtTipoBaja.setText("");
            lblDeBaja.setText("");
            
        }
        
        //deudor
        if(deudaser.countDeudasBySocioTitular(sociotit)>=1){
            lblDeudor.setDisable(false);
            lblDeudor.setText("¡SOCIO CON DEUDAS!");
        }else{
            lblDeudor.setDisable(true);
            lblDeudor.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}
