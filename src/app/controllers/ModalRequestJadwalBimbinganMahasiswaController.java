package app.controllers;

import app.session.MahasiswaSession;
import app.database.DBConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class ModalRequestJadwalBimbinganMahasiswaController {

    // ================= HEADER =================
    @FXML private Text txtNamaMahasiswa;

    // ================= FORM =================
    @FXML private ComboBox<String> cbDosenPembimbing;
    @FXML private ComboBox<String> cbJam;
    @FXML private TextField tfTopik;
    @FXML private TextField tfRuangan;
    @FXML private DatePicker dpTanggal;
    @FXML private Button btnKirim;

    // Map label dosen -> id_dosen
    private Map<String, Integer> dosenMap = new HashMap<>();

    // ================= INIT =================
    @FXML
    public void initialize() {
        loadNamaMahasiswa();
        loadDosenPembimbing();
        loadJamBimbingan();
    }

    // ================= LOAD NAMA =================
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

    // ================= LOAD DOSEN PEMBIMBING =================
    private void loadDosenPembimbing() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT d.id, d.nama, dp.jenis_dosen " +
            "FROM dosenpembimbing dp " +
            "JOIN dosen d ON dp.id_dosen = d.id " +
            "WHERE dp.id_mahasiswa = ? " +
            "ORDER BY dp.jenis_dosen ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            cbDosenPembimbing.getItems().clear();
            dosenMap.clear();

            while (rs.next()) {
                int idDosen = rs.getInt("id");
                String namaDosen = rs.getString("nama");
                int jenisDosen = rs.getInt("jenis_dosen");

                // ⬇️ hanya tampilkan NAMA DOSEN
                cbDosenPembimbing.getItems().add(namaDosen);

                // simpan mapping nama -> id_dosen
                dosenMap.put(namaDosen, idDosen);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ================= LOAD JAM =================
    private void loadJamBimbingan() {
        cbJam.getItems().addAll(
            "08:00", "09:00", "10:00", "11:00",
            "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00"
        );
    }

    // ================= KIRIM REQUEST =================
    @FXML
    private void KirimRequestBimbingan() {

        // ===== VALIDASI FORM =====
        if (cbDosenPembimbing.getValue() == null ||
            tfTopik.getText().isEmpty() ||
            tfRuangan.getText().isEmpty() ||
            dpTanggal.getValue() == null ||
            cbJam.getValue() == null) {

            showAlert("Validasi", "Semua field wajib diisi");
            return;
        }

        if (!dosenMap.containsKey(cbDosenPembimbing.getValue())) {
            showAlert("Error", "Dosen tidak valid");
            return;
        }

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        int idDosen = dosenMap.get(cbDosenPembimbing.getValue());
        LocalDate tanggal = dpTanggal.getValue();

        // ===== CEK BENTROK TANGGAL =====
        if (isTanggalBentrok(idMahasiswa, idDosen, tanggal)) {
            showAlert(
                "Request Ditolak",
                "Pengajuan gagal. Mahasiswa telah memiliki jadwal bimbingan dengan dosen yang sama pada tanggal ini.\n" +
"Silakan pilih tanggal lain."
            );
            return;
        }

        // ===== INSERT REQUEST =====
        String sql =
            "INSERT INTO request_bimbingan " +
            "(id_mahasiswa, id_dosen, judul, ruangan, tanggal, jam) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);
            ps.setString(3, tfTopik.getText());
            ps.setString(4, tfRuangan.getText());
            ps.setDate(5, Date.valueOf(tanggal));
            ps.setTime(6, Time.valueOf(cbJam.getValue() + ":00"));

            ps.executeUpdate();

            showAlert("Sukses", "Request bimbingan berhasil dikirim");

        } catch (SQLException e) {
            // 🔥 pesan dari trigger database
            showAlert("Gagal", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan sistem");
        }
    }
    
    private boolean isTanggalBentrok(int idMahasiswa, int idDosen, LocalDate tanggal) {
        String sql =
            "SELECT COUNT(*) FROM (" +
            "   SELECT tanggal FROM bimbingan " +
            "   WHERE id_mahasiswa = ? AND id_dosen = ? AND tanggal = ? " +
            "   UNION ALL " +
            "   SELECT tanggal FROM request_bimbingan " +
            "   WHERE id_mahasiswa = ? AND id_dosen = ? AND tanggal = ? " +
            "   AND status IN ('DIAJUKAN', 'DITERIMA')" +
            ") t";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);
            ps.setDate(3, Date.valueOf(tanggal));

            ps.setInt(4, idMahasiswa);
            ps.setInt(5, idDosen);
            ps.setDate(6, Date.valueOf(tanggal));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    // ================= UTIL =================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void closeModal(ActionEvent event) {
        ((Stage) ((Node) event.getSource())
            .getScene().getWindow()).close();
    }



}
