package app.controllers;

import app.database.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

public class ModalKelolaProfilMahasiswaAdminViewController {

    @FXML private Button btnSimpanProfil;

    @FXML private Text txtFormKosong;

    @FXML private TextField tfNama;
    @FXML private TextField tfNim;
    @FXML private TextField tfJurusan;
    @FXML private TextField tfAngkatan;
    @FXML private TextField tfNoTelp;

    private int idMahasiswa; // id dari halaman sebelumnya

    @FXML
    public void initialize() {
        txtFormKosong.setVisible(false);
    }

    /* ================= SET ID MAHASISWA ================= */
    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
        loadProfilMahasiswa();
    }

    /* ================= LOAD DATA AWAL ================= */
    private void loadProfilMahasiswa() {

        String sql = "SELECT nama, nim, jurusan, angkatan, no_telepon " +
                     "FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tfNama.setText(rs.getString("nama"));
                tfNim.setText(rs.getString("nim"));
                tfJurusan.setText(rs.getString("jurusan"));
                tfAngkatan.setText(
                        rs.getObject("angkatan") != null 
                        ? rs.getString("angkatan") 
                        : ""
                );
                tfNoTelp.setText(rs.getString("no_telepon"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= SIMPAN PROFIL ================= */
    @FXML
    private void simpanProfil(ActionEvent event) {

        ArrayList<String> fields = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        if (!tfNama.getText().trim().isEmpty()) {
            fields.add("nama = ?");
            values.add(tfNama.getText().trim());
        }

        if (!tfNim.getText().trim().isEmpty()) {
            fields.add("nim = ?");
            values.add(tfNim.getText().trim());
        }

        if (!tfJurusan.getText().trim().isEmpty()) {
            fields.add("jurusan = ?");
            values.add(tfJurusan.getText().trim());
        }

        if (!tfAngkatan.getText().trim().isEmpty()) {
            try {
                fields.add("angkatan = ?");
                values.add(Integer.parseInt(tfAngkatan.getText().trim()));
            } catch (NumberFormatException e) {
                txtFormKosong.setText("Angkatan harus angka");
                txtFormKosong.setVisible(true);
                return;
            }
        }

        if (!tfNoTelp.getText().trim().isEmpty()) {
            fields.add("no_telepon = ?");
            values.add(tfNoTelp.getText().trim());
        }

        // tidak ada perubahan
        if (fields.isEmpty()) {
            txtFormKosong.setText("Minimal satu field harus diisi");
            txtFormKosong.setVisible(true);
            return;
        }

        txtFormKosong.setVisible(false);

        String sql = "UPDATE mahasiswa SET " +
                     String.join(", ", fields) +
                     " WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            ps.setInt(values.size() + 1, idMahasiswa);
            ps.executeUpdate();

            closeModal(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= CLOSE MODAL ================= */
    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
