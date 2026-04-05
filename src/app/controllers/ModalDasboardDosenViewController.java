package app.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ModalDasboardDosenViewController {

    public void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();
        stage.close();
    }
}
