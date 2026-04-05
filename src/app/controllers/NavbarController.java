package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavbarController {
    
    @FXML
    private void goToProfil(ActionEvent event) {
        openPage(event, "/app/fxml/ProfilMahasiswaView.fxml");
    }

    @FXML
    private void goToPesan(ActionEvent event) {
        openPage(event, "/app/fxml/PesanMahasiswaView.fxml");
    }

    @FXML
    private void goToDosen(ActionEvent event) {
        openPage(event, "/app/fxml/DosenPembimbingMahasiswaView.fxml");
    }

    @FXML
    private void goToPengaturan(ActionEvent event) {
        openPage(event, "/app/fxml/PengaturanMahasiswaView.fxml");
    }

    private void openPage(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
