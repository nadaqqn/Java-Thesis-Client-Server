package app.controllers;

import app.session.MahasiswaSession;
import app.database.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class EditProfilMahasiswaModalController {

    @FXML private Button btnSimpanProfil;
    @FXML private Text txtNamaMahasiswa;
    @FXML private Text txtFormKosong;

    @FXML private TextField tfNama;
    @FXML private TextField tfNim;
    @FXML private TextField tfJurusan;
    @FXML private TextField tfAngkatan;
    @FXML private TextField tfNoTelp;

    @FXML
    public void initialize() {
        txtFormKosong.setVisible(false);
        loadNamaMahasiswa();
    }

    // tampilkan nama di header
    private void loadNamaMahasiswa() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        String sql = "SELECT nama FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaMahasiswa.setText(rs.getString("nama"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SIMPAN PROFIL =================
    @FXML
    private void simpanProfil(ActionEvent event) {

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

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
            fields.add("angkatan = ?");
            values.add(Integer.parseInt(tfAngkatan.getText().trim()));
        }

        if (!tfNoTelp.getText().trim().isEmpty()) {
            fields.add("no_telepon = ?");
            values.add(tfNoTelp.getText().trim());
        }


        // ❌ semua kosong
        if (fields.isEmpty()) {
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

            // tutup modal
            closeModal(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CLOSE MODAL =================
    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
