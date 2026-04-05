package app.controllers;

import app.database.DBConnect;
import app.session.DosenSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModalDosenController {

    @FXML private Text txtNamaMahasiswa;
    @FXML private Text txtNimMahasiswa;
    @FXML private Text txtJurusanMahasiswa;
    @FXML private Text txtJudulSkripsi;
    @FXML private Text txtTanggalPengajuan;
    @FXML private Text txtFileSkripsi;
    @FXML private ImageView ivMahasiswa;
    @FXML private ImageView ivDownloadFileSkripsi; 

    @FXML private Button btnSetujuiPengajuan;
    @FXML private Button btnTolakPengajuan;

    private int idMahasiswa;
    private int idDosen = DosenSession.getIdDosen();

    // =========================
    // DIPANGGIL DARI Dashboard
    // =========================
    public void setDataMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;

        String sql = "SELECT m.nama, m.nim, m.jurusan, m.foto, " +
                     "s.judul_skripsi, s.created_at, s.file_skripsi " +
                     "FROM skripsi s " +
                     "JOIN mahasiswa m ON s.id_mahasiswa = m.id_mahasiswa " +
                     "WHERE s.id_mahasiswa = ? AND s.id_dosen = ? AND s.status = 'DIAJUKAN'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);
            ResultSet rs = ps.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            byte[] fileSkripsiBytes = null;
            String namaFile = null;

            if (rs.next()) {
                txtNamaMahasiswa.setText(rs.getString("nama"));
                txtNimMahasiswa.setText(rs.getString("nim"));
                txtJurusanMahasiswa.setText(rs.getString("jurusan"));

                txtJudulSkripsi.setText(rs.getString("judul_skripsi"));
                txtTanggalPengajuan.setText(
                        rs.getTimestamp("created_at").toLocalDateTime().format(formatter)
                );
                txtFileSkripsi.setText("File Skripsi.pdf");

                // Foto mahasiswa
                byte[] fotoBytes = rs.getBytes("foto");
                if (fotoBytes != null && fotoBytes.length > 0) {
                    ivMahasiswa.setImage(new Image(new ByteArrayInputStream(fotoBytes)));
                } else {
                    ivMahasiswa.setImage(null);
                }

                // Ambil file skripsi sebagai byte[]
                if (rs.getBlob("file_skripsi") != null) {
                    fileSkripsiBytes = rs.getBlob("file_skripsi").getBytes(1, (int) rs.getBlob("file_skripsi").length());
                }
                namaFile = rs.getString("judul_skripsi") + ".pdf";
            } else {
                System.out.println("Data mahasiswa dengan id " + idMahasiswa + " tidak ditemukan atau belum diajukan.");
            }

            // =========================
            // Pasang listener download
            // =========================
            byte[] finalFileSkripsiBytes = fileSkripsiBytes;
            String finalNamaFile = namaFile;
            ivDownloadFileSkripsi.setOnMouseClicked(event -> {
                if (finalFileSkripsiBytes != null) {
                    try {
                        downloadFileSkripsi(finalFileSkripsiBytes, finalNamaFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal download file: " + e.getMessage());
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "File skripsi tidak tersedia!");
                    alert.showAndWait();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Download file skripsi dari byte[]
    // =========================
    private void downloadFileSkripsi(byte[] fileBytes, String fileName) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        File targetFile = fileChooser.showSaveDialog(ivDownloadFileSkripsi.getScene().getWindow());

        if (targetFile != null) {
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(fileBytes);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "File berhasil di-download!");
            alert.showAndWait();
        }
    }

    // =========================
    // Tombol Setujui
    // =========================
    @FXML
    private void setujuiPengajuan(ActionEvent event) {
        try (Connection conn = DBConnect.getConnection()) {

            // 1. Ambil nama mahasiswa dari DB
            String namaMahasiswa = "";
            try (PreparedStatement psMhs = conn.prepareStatement("SELECT nama FROM mahasiswa WHERE id_mahasiswa = ?")) {
                psMhs.setInt(1, idMahasiswa);
                ResultSet rsMhs = psMhs.executeQuery();
                if (rsMhs.next()) {
                    namaMahasiswa = rsMhs.getString("nama");
                }
            }

            // 2. Ambil nama dosen dari DB
            String namaDosen = "";
            try (PreparedStatement psDosen = conn.prepareStatement("SELECT nama FROM dosen WHERE id = ?")) {
                psDosen.setInt(1, idDosen);
                ResultSet rsDosen = psDosen.executeQuery();
                if (rsDosen.next()) {
                    namaDosen = rsDosen.getString("nama");
                }
            }
            
            // 3. Cek sudah ada dosen pembimbing
            String cekSudahAda =
                "SELECT COUNT(*) FROM DosenPembimbing WHERE id_mahasiswa = ? AND id_dosen = ?";

            try (PreparedStatement ps = conn.prepareStatement(cekSudahAda)) {
                ps.setInt(1, idMahasiswa);
                ps.setInt(2, idDosen);
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) {
                    new Alert(Alert.AlertType.WARNING,
                        "Dosen ini sudah terdaftar sebagai pembimbing mahasiswa tersebut.")
                        .showAndWait();
                    return;
                }
            }
            
            // 4. Tentukan jenis dosen
            String cekJumlah =
                "SELECT COUNT(*) FROM DosenPembimbing WHERE id_mahasiswa = ?";

            int jumlahPembimbing = 0;
            try (PreparedStatement ps = conn.prepareStatement(cekJumlah)) {
                ps.setInt(1, idMahasiswa);
                ResultSet rs = ps.executeQuery();
                rs.next();
                jumlahPembimbing = rs.getInt(1);
            }

            String jenisDosen = (jumlahPembimbing == 0) ? "1" : "2";

            // 5. Insert dosen pembimbing
            String insertPembimbing =
                "INSERT INTO DosenPembimbing (id_mahasiswa, id_dosen, jenis_dosen) " +
                "VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertPembimbing)) {
                ps.setInt(1, idMahasiswa);
                ps.setInt(2, idDosen);
                ps.setString(3, jenisDosen);
                ps.executeUpdate();
            }



            // 3. Update status skripsi menjadi DISETUJUI
            String updateSkripsi = "UPDATE skripsi SET status = ? WHERE id_mahasiswa = ? AND id_dosen = ?";
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSkripsi)) {
                psUpdate.setString(1, "DISETUJUI");
                psUpdate.setInt(2, idMahasiswa);
                psUpdate.setInt(3, idDosen);
                psUpdate.executeUpdate();
            }

            // 4. Log activity
            String insertLog = "INSERT INTO activity_log (username, role, activity) VALUES (?, 'DOSEN', ?)";
            try (PreparedStatement psLog = conn.prepareStatement(insertLog)) {
                psLog.setString(1, namaDosen);
                String aktivitas = "Dosen [" + namaDosen + "] menyetujui pengajuan skripsi Mahasiswa [" + namaMahasiswa + "]";
                psLog.setString(2, aktivitas);
                psLog.executeUpdate();
            }

            // 5. Tutup modal
            closeModal(event);

            // 6. Alert sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pengajuan skripsi telah disetujui.");
            alert.showAndWait();

       } catch (SQLException e) {
            if (e.getMessage().contains("Mahasiswa sudah memiliki 2 dosen pembimbing")) {
                new Alert(Alert.AlertType.ERROR,
                    "Mahasiswa sudah memiliki 2 dosen pembimbing.")
                    .showAndWait();
            } else {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                    "Gagal menyetujui pengajuan: " + e.getMessage())
                    .showAndWait();
            }
        }

    }


    // =========================
    // Tombol Tolak
    // =========================
    @FXML
    private void tolakPengajuan(ActionEvent event) {
        try {
            // 1. Update status skripsi menjadi DITOLAK
            String sqlUpdate = "UPDATE skripsi SET status = 'DITOLAK' WHERE id_mahasiswa = ? AND id_dosen = ?";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, idMahasiswa);
                ps.setInt(2, idDosen);
                ps.executeUpdate();
            }

            // 2. Log activity (opsional tapi disarankan)
            String log = "Dosen [" + idDosen + "] menolak pengajuan skripsi Mahasiswa [" + idMahasiswa + "]";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement psLog = conn.prepareStatement(
                         "INSERT INTO activity_log (username, role, activity) VALUES (?, 'DOSEN', ?)"
                 )) {
                psLog.setString(1, String.valueOf(idMahasiswa));
                psLog.setString(2, log);
                psLog.executeUpdate();
            }

            // 3. Tutup modal
            closeModal(event);

            // 4. Opsional: beri alert sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pengajuan skripsi telah ditolak.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menolak pengajuan: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void updateStatusSkripsi(String status) {
        String updateSkripsi = "UPDATE skripsi SET status = ? WHERE id_mahasiswa = ? AND id_dosen = ?";
        String insertLog = "INSERT INTO activity_log (username, role, aktivitas) VALUES (?, 'DOSEN', ?)";

        // Ambil nama mahasiswa dari DB
        String namaMahasiswa = "";
        String namaDosen = "";

        try (Connection conn = DBConnect.getConnection()) {

            // Ambil nama mahasiswa
            try (PreparedStatement psMhs = conn.prepareStatement("SELECT nama FROM mahasiswa WHERE id_mahasiswa = ?")) {
                psMhs.setInt(1, idMahasiswa);
                ResultSet rsMhs = psMhs.executeQuery();
                if (rsMhs.next()) {
                    namaMahasiswa = rsMhs.getString("nama");
                }
            }

            // Ambil nama dosen
            try (PreparedStatement psDosen = conn.prepareStatement("SELECT nama FROM dosen WHERE id_dosen = ?")) {
                psDosen.setInt(1, idDosen);
                ResultSet rsDosen = psDosen.executeQuery();
                if (rsDosen.next()) {
                    namaDosen = rsDosen.getString("nama");
                }
            }

            // update status skripsi
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSkripsi)) {
                psUpdate.setString(1, status);
                psUpdate.setInt(2, idMahasiswa);
                psUpdate.setInt(3, idDosen);
                psUpdate.executeUpdate();
            }

            // log activity
            try (PreparedStatement psLog = conn.prepareStatement(insertLog)) {
                psLog.setString(1, namaDosen);
                String aktivitas = "Dosen [" + namaDosen + "] " + status + " pengajuan skripsi Mahasiswa [" + namaMahasiswa + "]";
                psLog.setString(2, aktivitas);
                psLog.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // =========================
    // Close modal
    // =========================
    public void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
