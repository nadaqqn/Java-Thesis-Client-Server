package app.controllers;

import app.database.DBConnect;
import app.models.DosenItem;
import app.session.MahasiswaSession;

import java.sql.*;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModalCatatRevisiMahasiswaController {

    // ================= HEADER =================
    @FXML private Text txtNamaMahasiswa;

    // ================= FORM =================
    @FXML private ComboBox<DosenItem> cbDosenPembimbing;
    @FXML private ComboBox<Integer> cbPertemuan;
    @FXML private TextField tfCatatan;
    @FXML private TextField tfRevisi;
    @FXML private DatePicker dpDeadline;
    @FXML private Button btnSimpan;

    // ================= INIT =================
    @FXML
    public void initialize() {
        txtNamaMahasiswa.setText(
            MahasiswaSession.getNama() != null
                ? MahasiswaSession.getNama() + "!"
                : "Mahasiswa!"
        );

        loadDosenPembimbing();
        loadPertemuan();
    }

    // ================= LOAD DOSEN =================
    private void loadDosenPembimbing() {

        ObservableList<DosenItem> list = FXCollections.observableArrayList();
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT d.id, d.nama " +
            "FROM dosenpembimbing dp " +
            "JOIN dosen d ON d.id = dp.id_dosen " +
            "WHERE dp.id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new DosenItem(
                    rs.getInt("id"),
                    rs.getString("nama")
                ));
            }

            cbDosenPembimbing.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD PERTEMUAN =================
    private void loadPertemuan() {

        ObservableList<Integer> list = FXCollections.observableArrayList();
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT DISTINCT pertemuan_ke " +
            "FROM bimbingan " +
            "WHERE id_mahasiswa = ? " +
            "ORDER BY pertemuan_ke ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("pertemuan_ke"));
            }

            cbPertemuan.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SIMPAN REVISI =================
    @FXML
    private void simpanRevisi(ActionEvent event) {

        DosenItem dosen = cbDosenPembimbing.getValue();
        Integer pertemuan = cbPertemuan.getValue();
        String catatan = tfCatatan.getText();
        String revisi = tfRevisi.getText();
        LocalDate deadline = dpDeadline.getValue();

        if (dosen == null || pertemuan == null ||
            catatan.isEmpty() || revisi.isEmpty() || deadline == null) {

            showAlert(Alert.AlertType.WARNING,
                "Validasi Gagal",
                "Semua field wajib diisi!");
            return;
        }

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        // Cari id_bimbingan
        String findSql =
            "SELECT id_bimbingan FROM bimbingan " +
            "WHERE id_mahasiswa = ? " +
            "AND id_dosen = ? " +
            "AND pertemuan_ke = ?";

        String updateSql =
            "UPDATE bimbingan SET " +
            "catatan_bimbingan = ?, " +
            "revisi = ?, " +
            "deadline_revisi = ? " +
            "WHERE id_bimbingan = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement findPs = conn.prepareStatement(findSql)) {

            findPs.setInt(1, idMahasiswa);
            findPs.setInt(2, dosen.getId());
            findPs.setInt(3, pertemuan);

            ResultSet rs = findPs.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR,
                    "Data Tidak Ditemukan",
                    "Bimbingan tidak ditemukan.");
                return;
            }

            int idBimbingan = rs.getInt("id_bimbingan");

            try (PreparedStatement updatePs =
                     conn.prepareStatement(updateSql)) {

                updatePs.setString(1, catatan);
                updatePs.setString(2, revisi);
                updatePs.setDate(3, Date.valueOf(deadline));
                updatePs.setInt(4, idBimbingan);

                updatePs.executeUpdate();
            }

            showAlert(Alert.AlertType.INFORMATION,
                "Berhasil",
                "Revisi bimbingan berhasil disimpan.");

            closeModal(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                "Error",
                "Gagal menyimpan revisi.");
        }
    }

    // ================= UTIL =================
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    private void closeModal(ActionEvent event) {
        ((Stage) ((Node) event.getSource())
            .getScene().getWindow()).close();
    }
}
