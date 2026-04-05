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

public class ModalLihatMahasiswaAdminViewController {

    private int idMahasiswa; // id dari halaman sebelumnya

    // ===== FXML =====
    @FXML private ImageView ivMahasiswa;

    @FXML private Text txtNama;
    @FXML private Text txtNim;
    @FXML private Text txtJurusan;
    @FXML private Text txtAngkatan;
    @FXML private Text txtNoTelp;

    @FXML private Button btnClose;

    /* ================= SET ID MAHASISWA ================= */
    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
        loadDataMahasiswa();
    }

    /* ================= LOAD DATA ================= */
    private void loadDataMahasiswa() {

        String sql =
            "SELECT foto, nama, nim, jurusan, angkatan, no_telepon " +
            "FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // ===== FOTO (LONGBLOB) =====
                byte[] fotoBytes = rs.getBytes("foto");
                if (fotoBytes != null && fotoBytes.length > 0) {
                    Image image = new Image(new ByteArrayInputStream(fotoBytes));
                    ivMahasiswa.setImage(image);
                } else {
                    ivMahasiswa.setImage(null); // atau gambar default
                }

                // ===== TEXT =====
                txtNama.setText(rs.getString("nama"));
                txtNim.setText(rs.getString("nim"));
                txtJurusan.setText(rs.getString("jurusan"));

                txtAngkatan.setText(
                    rs.getObject("angkatan") != null
                        ? rs.getString("angkatan")
                        : "-"
                );

                txtNoTelp.setText(
                    rs.getString("no_telepon") != null
                        ? rs.getString("no_telepon")
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
