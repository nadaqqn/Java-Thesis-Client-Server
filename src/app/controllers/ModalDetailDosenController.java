package app.controllers;

import app.models.Dosen;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ModalDetailDosenController {

    @FXML private Text txtNama;
    @FXML private Text txtProdi;
    @FXML private Text txtNoTelp;
    @FXML private Text txtEmail;
    @FXML private ImageView imgFoto;
    
    // tombol close di FXML
    @FXML private Button btnClose;

    public void setDosenData(Dosen d) {
        if (d != null) {
            txtNama.setText(d.getNama());
            txtProdi.setText(d.getProdi());
            txtNoTelp.setText(d.getNoTelp());
            txtEmail.setText(d.getEmail());
            if (d.getFoto() != null) {
                imgFoto.setImage(new Image(new ByteArrayInputStream(d.getFoto())));
            } else {
                imgFoto.setImage(null);
            }
        }
    }

    @FXML
    private void closeModal(ActionEvent event) {
        // ambil stage dari button yang dipencet
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
