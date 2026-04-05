package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavbarDosenController {
    
    @FXML
    private void goToProfil(ActionEvent event) {
        openPage(event, "/app/fxml/ProfilDosenView.fxml");
    }

    @FXML
    private void goToPesan(ActionEvent event) {
        openPage(event, "/app/fxml/PesanDosenView.fxml");
    }

    @FXML
    private void goToMahasiswa(ActionEvent event) {
        openPage(event, "/app/fxml/MahasiswaBimbinganDosenView.fxml");
    }

    @FXML
    private void goToPengaturan(ActionEvent event) {
        openPage(event, "/app/fxml/PengaturanDosenView.fxml");
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
