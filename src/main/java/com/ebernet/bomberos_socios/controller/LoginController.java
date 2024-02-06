package com.ebernet.bomberos_socios.controller;

import com.ebernet.bomberos_socios.service.IUsuarioService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import static org.springframework.web.server.adapter.WebHttpHandlerBuilder.applicationContext;

@Component
public class LoginController implements Initializable {
    
    @Autowired
    private IUsuarioService userser;
    
    @Autowired
    private ApplicationContext context;
    
    @FXML
    private TextField txtUser;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnIngresar;
    
    @FXML
    private void validar(){
        if(userser.validar(txtUser.getText(), txtPassword.getText())){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/index.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root;
                root = loader.load();
                // Obtener el controlador desde el ApplicationContext
                IndexController indexController = context.getBean(IndexController.class);
                // Configurar el root y mostrar la escena
                Stage stage = new Stage();
                Scene scene = new Scene(root, 1129, 914);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                // Cerrar la pestaña de login
                Stage loginStage = (Stage) txtUser.getScene().getWindow();
                //setearle el titulo
                stage.setTitle(loginStage.getTitle());
                stage.getIcons().add(new javafx.scene.image.Image("/img/logo_sistema.png"));  // Reemplaza con la ruta correcta al ícono
                loginStage.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            txtUser.setText("");
            txtUser.setPromptText("usuario123");
            txtPassword.setText("");
            txtPassword.setPromptText("***********");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("¡Error! Usuario o contraseña incorrecto.");
            alert.showAndWait();
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
