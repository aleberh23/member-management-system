package com.ebernet.bomberos_socios;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
@Component
public class StageListener implements ApplicationListener<StageReadyEvent>{

    private final String appTitle;
    private final Resource fxml;
    private final ApplicationContext ac;

    StageListener(@Value("${spring.application.ui.title}")String appTitle,
                  @Value("${classpath:/fxml/index.fxml}") Resource resource, ApplicationContext ac) {
        this.appTitle = appTitle;
        this.fxml = resource;
        this.ac = ac;
    }
    
    
    
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(ac::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1129, 914);
            stage.setScene(scene);
            stage.setTitle(appTitle);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(StageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
