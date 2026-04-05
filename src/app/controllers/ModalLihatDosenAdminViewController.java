package app.controllers;

import app.database.DBConnect;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ModalLihatDosenAdminViewController {

    private int idDosen; // id dari halaman sebelumnya

    // ===== FXML =====
    @FXML private ImageView ivDosen;

    @FXML private Text txtNama;
    @FXML private Text txtNip;
    @FXML private Text txtProdi;
    @FXML private Text txtFakultas;
    @FXML private Text txtUmur;
    @FXML private Text txtNoTelp;

    @FXML private Button btnClose;

    /* ================= SET ID DOSEN ================= */
    public void setIdDosen(int idDosen) {
        this.idDosen = idDosen;
        loadDataDosen();
    }

    /* ================= LOAD DATA DOSEN ================= */
    private void loadDataDosen() {

        String sql =
            "SELECT foto, nama, nip, prodi, fakultas, umur, no_telp " +
            "FROM dosen WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // ===== FOTO (BLOB) =====
                byte[] fotoBytes = rs.getBytes("foto");
                if (fotoBytes != null && fotoBytes.length > 0) {
                    ivDosen.setImage(new Image(new ByteArrayInputStream(fotoBytes)));
                } else {
                    ivDosen.setImage(
                        new Image(getClass().getResourceAsStream(
                            "/app/images/default_dosen.png"
                        ))
                    );
                }

                // ===== TEXT =====
                txtNama.setText(rs.getString("nama"));
                txtNip.setText(rs.getString("nip"));
                txtProdi.setText(rs.getString("prodi"));
                txtFakultas.setText(rs.getString("fakultas"));

                txtUmur.setText(
                    rs.getObject("umur") != null
                        ? rs.getString("umur")
                        : "-"
                );

                txtNoTelp.setText(
                    rs.getString("no_telp") != null
                        ? rs.getString("no_telp")
                        : "-"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= CLOSE MODAL ================= */
    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
