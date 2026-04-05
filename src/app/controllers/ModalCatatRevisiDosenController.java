package app.controllers;

import app.database.DBConnect;
import app.session.DosenSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModalCatatRevisiDosenController {

    // ================= HEADER =================
    @FXML private Text txtNamaDosen;

    // ================= FORM =================
    @FXML private TextField tfCatatan;
    @FXML private TextField tfRevisi;
    @FXML private DatePicker dpDeadline;
    @FXML private Button btnSimpan;

    // ================= DATA PARSING =================
    private int idBimbingan;
    private int idMahasiswa;
    private int idDosen;
    private int pertemuanKe;

    // ================= INIT =================
    @FXML
    public void initialize() {
        String nama = DosenSession.getNama();
        txtNamaDosen.setText(
            (nama != null && !nama.isEmpty()) ? nama + "!" : "Dosen!"
        );
    }

    // ================= TERIMA DATA DARI CARD =================
    public void setBimbinganData(int idBimbingan, int idMahasiswa, int idDosen, int pertemuanKe) {
        this.idBimbingan = idBimbingan;
        this.idMahasiswa = idMahasiswa;
        this.idDosen = idDosen;
        this.pertemuanKe = pertemuanKe;
    }

    // ================= SIMPAN REVISI =================
    @FXML
    private void simpanRevisi(ActionEvent event) {

        String catatan = tfCatatan.getText();
        String revisi = tfRevisi.getText();
        LocalDate deadline = dpDeadline.getValue();

        // ---------- VALIDASI ----------
        if (catatan == null || catatan.trim().isEmpty()
                || revisi == null || revisi.trim().isEmpty()
                || deadline == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validasi Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Semua field wajib diisi!");
            alert.show();
            return;
        }

        String sql = "UPDATE bimbingan "
                   + "SET catatan_bimbingan = ?, "
                   + "    revisi = ?, "
                   + "    deadline_revisi = ? "
                   + "WHERE id_bimbingan = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, catatan);
            ps.setString(2, revisi);
            ps.setDate(3, Date.valueOf(deadline));
            ps.setInt(4, idBimbingan);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Catatan dan revisi berhasil disimpan.");
                alert.showAndWait();

                closeModal(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal menyimpan data.");
            alert.show();
        }
    }

    // ================= CLOSE MODAL =================
    @FXML
    private void closeModal(ActionEvent event) {
        ((Stage) ((Node) event.getSource())
                .getScene().getWindow()).close();
    }
}
