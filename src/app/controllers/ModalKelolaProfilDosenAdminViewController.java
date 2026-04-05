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

public class ModalKelolaProfilDosenAdminViewController {

    @FXML private Button btnSimpanProfil;
    @FXML private Text txtFormKosong;

    @FXML private TextField tfNama;
    @FXML private TextField tfNip;
    @FXML private TextField tfProdi;
    @FXML private TextField tfFakultas;
    @FXML private TextField tfUmur;
    @FXML private TextField tfNoTelp;

    private int idDosen; // diparsing dari DaftarDosenAdminView

    /* ================= INITIALIZE ================= */
    @FXML
    public void initialize() {
        txtFormKosong.setVisible(false);
    }

    /* ================= TERIMA ID DOSEN ================= */
    public void setIdDosen(int idDosen) {
        this.idDosen = idDosen;
        loadProfilDosen();
    }

    /* ================= LOAD DATA DOSEN ================= */
    private void loadProfilDosen() {

        String sql =
            "SELECT nama, nip, prodi, fakultas, umur, no_telp " +
            "FROM dosen WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tfNama.setText(rs.getString("nama"));
                tfNip.setText(rs.getString("nip"));
                tfProdi.setText(rs.getString("prodi"));
                tfFakultas.setText(rs.getString("fakultas"));
                tfUmur.setText(
                        rs.getObject("umur") != null
                                ? rs.getString("umur")
                                : ""
                );
                tfNoTelp.setText(rs.getString("no_telp"));
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

        if (!tfNip.getText().trim().isEmpty()) {
            fields.add("nip = ?");
            values.add(tfNip.getText().trim());
        }

        if (!tfProdi.getText().trim().isEmpty()) {
            fields.add("prodi = ?");
            values.add(tfProdi.getText().trim());
        }

        if (!tfFakultas.getText().trim().isEmpty()) {
            fields.add("fakultas = ?");
            values.add(tfFakultas.getText().trim());
        }

        if (!tfUmur.getText().trim().isEmpty()) {
            try {
                fields.add("umur = ?");
                values.add(Integer.parseInt(tfUmur.getText().trim()));
            } catch (NumberFormatException e) {
                txtFormKosong.setText("Umur harus berupa angka");
                txtFormKosong.setVisible(true);
                return;
            }
        }

        if (!tfNoTelp.getText().trim().isEmpty()) {
            fields.add("no_telp = ?");
            values.add(tfNoTelp.getText().trim());
        }

        // ❌ tidak ada perubahan
        if (fields.isEmpty()) {
            txtFormKosong.setText("Minimal satu field harus diisi");
            txtFormKosong.setVisible(true);
            return;
        }

        txtFormKosong.setVisible(false);

        String sql =
            "UPDATE dosen SET " +
            String.join(", ", fields) +
            " WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            ps.setInt(values.size() + 1, idDosen);
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
