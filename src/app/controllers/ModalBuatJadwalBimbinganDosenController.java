package app.controllers;

import app.database.DBConnect;
import app.models.MahasiswaItem;
import app.session.DosenSession;
import app.session.MahasiswaSession;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModalBuatJadwalBimbinganDosenController {

    // ================= HEADER =================
    @FXML private Text txtNamaDosen;

    // ================= COMBO BOX =================
    @FXML private ComboBox<MahasiswaItem> cbMahasiswaBimbingan;

    // ================= CARD BIMBINGAN =================
    @FXML private HBox cardBimbingan1, cardBimbingan2, cardBimbingan3,
                       cardBimbingan4, cardBimbingan5, cardBimbingan6;

    @FXML private ImageView ivMahasiswaPertemuan1, ivMahasiswaPertemuan2,
                           ivMahasiswaPertemuan3, ivMahasiswaPertemuan4,
                           ivMahasiswaPertemuan5, ivMahasiswaPertemuan6;

    @FXML private Text txtNamaMahasiswaPertemuan1, txtNamaMahasiswaPertemuan2,
                      txtNamaMahasiswaPertemuan3, txtNamaMahasiswaPertemuan4,
                      txtNamaMahasiswaPertemuan5, txtNamaMahasiswaPertemuan6;
    
    @FXML private Text txtTanggalMahasiswaPertemuan1, txtTanggalMahasiswaPertemuan2,
                  txtTanggalMahasiswaPertemuan3, txtTanggalMahasiswaPertemuan4,
                  txtTanggalMahasiswaPertemuan5, txtTanggalMahasiswaPertemuan6;
    
    @FXML private Text txtNimMahasiswaPertemuan1, txtNimMahasiswaPertemuan2,
                  txtNimMahasiswaPertemuan3, txtNimMahasiswaPertemuan4,
                  txtNimMahasiswaPertemuan5, txtNimMahasiswaPertemuan6;

    @FXML private Text txtJurusanMahasiswaPertemuan1, txtJurusanMahasiswaPertemuan2,
                      txtJurusanMahasiswaPertemuan3, txtJurusanMahasiswaPertemuan4,
                      txtJurusanMahasiswaPertemuan5, txtJurusanMahasiswaPertemuan6;

    @FXML private Text txtJudulSkripsiMahasiswaPertemuan1, txtJudulSkripsiMahasiswaPertemuan2,
                  txtJudulSkripsiMahasiswaPertemuan3, txtJudulSkripsiMahasiswaPertemuan4,
                  txtJudulSkripsiMahasiswaPertemuan5, txtJudulSkripsiMahasiswaPertemuan6;

    @FXML private Text txtRuanganMahasiswaPertemuan1, txtRuanganMahasiswaPertemuan2,
                  txtRuanganMahasiswaPertemuan3, txtRuanganMahasiswaPertemuan4,
                  txtRuanganMahasiswaPertemuan5, txtRuanganMahasiswaPertemuan6;

    @FXML private Text txtMahasiswaPertemuan1, txtMahasiswaPertemuan2,
                      txtMahasiswaPertemuan3, txtMahasiswaPertemuan4,
                      txtMahasiswaPertemuan5, txtMahasiswaPertemuan6;

    @FXML private Text txtJamMahasiswaPertemuan1, txtJamMahasiswaPertemuan2,
                      txtJamMahasiswaPertemuan3, txtJamMahasiswaPertemuan4,
                      txtJamMahasiswaPertemuan5, txtJamMahasiswaPertemuan6;

    @FXML private Text txtJudulPertemuan1, txtJudulPertemuan2,
                      txtJudulPertemuan3, txtJudulPertemuan4,
                      txtJudulPertemuan5, txtJudulPertemuan6;
    
    @FXML private Button btnTerimaCard1, btnTerimaCard2, btnTerimaCard3,
                    btnTerimaCard4, btnTerimaCard5, btnTerimaCard6;

    @FXML private Button btnTolakCard1, btnTolakCard2, btnTolakCard3,
                        btnTolakCard4, btnTolakCard5, btnTolakCard6;

    // ================== ID HOLDER PER CARD ==================
    private Integer[] requestIds = new Integer[6];
    private Integer[] mahasiswaIds = new Integer[6];


    // ================= FORM =================
    @FXML private ComboBox<MahasiswaItem> cbFormMahasiswaBimbingan;
    @FXML private ComboBox<String> cbJam;
    @FXML private TextField tfTopik;
    @FXML private TextField tfRuangan;
    @FXML private DatePicker dpTanggal;
    @FXML private Button btnSimpanJadwal;
    


    @FXML
    public void initialize() {
        String nama = DosenSession.getNama();
        txtNamaDosen.setText(
            (nama != null && !nama.isEmpty()) ? nama + "!" : "Dosen!"
        );

        loadMahasiswaBimbingan();

        cbMahasiswaBimbingan.setOnAction(e -> {
            MahasiswaItem selected = cbMahasiswaBimbingan.getValue();
            if (selected != null) {
                loadRequestBimbingan(selected.getIdMahasiswa());
            } else {
                resetCard();
            }
        });

        resetCard();
        cbMahasiswaBimbingan();
        loadJamBimbingan();
    }
    
    // ================= Form =================
    private void cbMahasiswaBimbingan() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT m.id_mahasiswa, m.nama " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON m.id_mahasiswa = dp.id_mahasiswa " +
            "WHERE dp.id_dosen = ? " +
            "ORDER BY m.nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            cbFormMahasiswaBimbingan.getItems().clear();

            while (rs.next()) {
                cbFormMahasiswaBimbingan.getItems().add(
                    new MahasiswaItem(
                        rs.getInt("id_mahasiswa"),
                        rs.getString("nama")
                    )
                );
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
    private void SimpanJadwal() {

        // ===== VALIDASI FORM =====
        if (cbFormMahasiswaBimbingan.getValue() == null ||
            tfTopik.getText().isEmpty() ||
            tfRuangan.getText().isEmpty() ||
            dpTanggal.getValue() == null ||
            cbJam.getValue() == null) {

            showAlert(Alert.AlertType.ERROR, "Validasi", "Semua field wajib diisi");
            return;
        }

        int idDosen = DosenSession.getIdDosen();
        MahasiswaItem selected = cbFormMahasiswaBimbingan.getValue();
        int idMahasiswa = selected.getIdMahasiswa();
        LocalDate tanggal = dpTanggal.getValue();

        // ===== CEK BENTROK TANGGAL =====
        if (isTanggalBentrok(idMahasiswa, idDosen, tanggal)) {
            showAlert(
                Alert.AlertType.ERROR,
                "Request Ditolak",
                "Pengajuan gagal. Mahasiswa telah memiliki jadwal bimbingan dengan dosen yang sama pada tanggal ini.\n" +
"Silakan pilih tanggal lain."
            );
            return;
        }

        // ===== INSERT REQUEST =====
        String sql =
            "INSERT INTO bimbingan " +
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

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal bimbingan telah berhasil dibuat");

        } catch (SQLException e) {
            // 🔥 pesan dari trigger database
            showAlert(Alert.AlertType.ERROR, "Gagal", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan sistem");
        }
    }
    
    private boolean isTanggalBentrok(int idMahasiswa, int idDosen, LocalDate tanggal) {
        String sql =
            "SELECT COUNT(*) FROM (" +
            "   SELECT tanggal FROM bimbingan " +
            "   WHERE id_mahasiswa = ? AND id_dosen = ? AND tanggal = ? " +
            "   AND status IN ('DIAJUKAN', 'DITERIMA')" +
            ") t";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);
            ps.setDate(3, Date.valueOf(tanggal));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // ================= REQUEST JADWAL BIMBINGAN DARI MAHASISWA BIMBINGAN =================

    private void loadMahasiswaBimbingan() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT m.id_mahasiswa, m.nama " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON m.id_mahasiswa = dp.id_mahasiswa " +
            "WHERE dp.id_dosen = ? " +
            "ORDER BY m.nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            cbMahasiswaBimbingan.getItems().clear();

            while (rs.next()) {
                cbMahasiswaBimbingan.getItems().add(
                    new MahasiswaItem(
                        rs.getInt("id_mahasiswa"),
                        rs.getString("nama")
                    )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD BIMBINGAN =================

    private void loadRequestBimbingan(int idMahasiswa) {
        int idDosen = DosenSession.getIdDosen();
        resetCard();

        String sql =
            "SELECT b.id_requestbimbingan, m.id_mahasiswa, " +
            "       m.nama, m.nim, m.jurusan, m.foto, " +
            "       s.judul_skripsi, " +
            "       b.judul, b.pertemuan_ke, b.tanggal, b.jam, b.ruangan " +
            "FROM request_bimbingan b " +
            "JOIN mahasiswa m ON m.id_mahasiswa = b.id_mahasiswa " +
            "LEFT JOIN skripsi s ON s.id_mahasiswa = b.id_mahasiswa AND s.id_dosen = b.id_dosen " +
            "WHERE b.id_mahasiswa = ? " +
            "  AND b.id_dosen = ? " +
            "  AND b.status = 'DIAJUKAN' " +
            "ORDER BY b.tanggal ASC, b.jam ASC " +
            "LIMIT 6";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);

            ResultSet rs = ps.executeQuery();
            int index = 0;

            while (rs.next() && index < 6) {

                // SIMPAN ID UNTUK BUTTON
                requestIds[index] = rs.getInt("id_requestbimbingan");
                mahasiswaIds[index] = rs.getInt("id_mahasiswa");

                setCardData(
                    index + 1,
                    rs.getString("nama"),
                    rs.getString("nim"),
                    rs.getString("jurusan"),
                    rs.getString("judul_skripsi"),
                    rs.getString("judul"),
                    rs.getBytes("foto"),
                    rs.getDate("tanggal"),
                    rs.getInt("pertemuan_ke"),
                    rs.getTime("jam"),
                    rs.getString("ruangan")
                );

                showActionButtons(index + 1, true);
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= SET CARD DATA =================

    private void setCardData(int index, String nama, String nim, String jurusan, String judulSkripsi, String judulPertemuan, byte[] foto, java.sql.Date tanggal, int pertemuanKe, java.sql.Time jam, String ruangan) {
        HBox card = getCard(index);
        Text txtNama = getNamaText(index);
        Text txtNim = getNimText(index);
        Text txtJurusan = getJurusanText(index);
        Text txtJudul = getJudulSkripsiText(index);
        Text txtJudulPertemuan = getJudulPertemuanText(index);
        Text txtTanggal = getTanggalText(index);
        Text txtPertemuan = getPertemuanText(index);
        Text txtJam = getJamText(index);
        Text txtRuangan = getRuanganText(index);
        ImageView iv = getImageView(index);

        if (card == null) return;

        card.setVisible(true);
        card.setManaged(true);
        txtNama.setText(nama);
        txtNim.setText(nim);
        txtJurusan.setText(jurusan);
        
        if (tanggal != null && txtTanggal != null) {
            txtTanggal.setText(tanggal.toString()); 
        }

        if (foto != null) {
            iv.setImage(new Image(new java.io.ByteArrayInputStream(foto)));
        } else {
            iv.setImage(new Image(
                getClass().getResourceAsStream("/assets/img/default-user.png")
            ));
        }
        
        if (txtJudul != null) {
            txtJudul.setText(
                (judulSkripsi != null && !judulSkripsi.isEmpty())
                    ? judulSkripsi
                    : "Judul skripsi belum ditentukan"
            );
        }
        
        if (txtJudulPertemuan != null) {
            txtJudulPertemuan.setText(
                (judulPertemuan != null && !judulPertemuan.isEmpty())
                    ? judulPertemuan
                    : "Bimbingan"
            );
        }
        
        if (txtPertemuan != null) {
            txtPertemuan.setText("Pertemuan " + pertemuanKe);
        }

        if (txtJam != null && jam != null) {
            txtJam.setText(jam.toString());
        }

        if (txtRuangan != null) {
            txtRuangan.setText(
                (ruangan != null && !ruangan.isEmpty())
                    ? "Ruangan " + ruangan
                    : "Ruangan -"
            );
        }
    }

    // ================= RESET CARD =================
    private void resetCard() {
        for (int i = 0; i < 6; i++) {

            // RESET ID CARD
            requestIds[i] = null;
            mahasiswaIds[i] = null;

            HBox card = getCard(i + 1);
            if (card != null) {
                card.setVisible(false);
                card.setManaged(false);

                if (getNamaText(i + 1) != null) getNamaText(i + 1).setText("");
                if (getNimText(i + 1) != null) getNimText(i + 1).setText("");
                if (getJurusanText(i + 1) != null) getJurusanText(i + 1).setText("");
                if (getTanggalText(i + 1) != null) getTanggalText(i + 1).setText("");
                if (getJudulSkripsiText(i + 1) != null) getJudulSkripsiText(i + 1).setText("");
                if (getJudulPertemuanText(i + 1) != null) getJudulPertemuanText(i + 1).setText("");
                if (getPertemuanText(i + 1) != null) getPertemuanText(i + 1).setText("");
                if (getJamText(i + 1) != null) getJamText(i + 1).setText("");
                if (getRuanganText(i + 1) != null) getRuanganText(i + 1).setText("");
            }
        }
    }


    // ================= HELPER =================

    private HBox getCard(int i) {
        switch (i) {
            case 1: return cardBimbingan1;
            case 2: return cardBimbingan2;
            case 3: return cardBimbingan3;
            case 4: return cardBimbingan4;
            case 5: return cardBimbingan5;
            case 6: return cardBimbingan6;
            default: return null;
        }
    }


    private Text getNamaText(int i) {
        switch (i) {
            case 1: return txtNamaMahasiswaPertemuan1;
            case 2: return txtNamaMahasiswaPertemuan2;
            case 3: return txtNamaMahasiswaPertemuan3;
            case 4: return txtNamaMahasiswaPertemuan4;
            case 5: return txtNamaMahasiswaPertemuan5;
            case 6: return txtNamaMahasiswaPertemuan6;
            default: return null;
        }
    }


    private ImageView getImageView(int i) {
        switch (i) {
            case 1: return ivMahasiswaPertemuan1;
            case 2: return ivMahasiswaPertemuan2;
            case 3: return ivMahasiswaPertemuan3;
            case 4: return ivMahasiswaPertemuan4;
            case 5: return ivMahasiswaPertemuan5;
            case 6: return ivMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getTanggalText(int i) {
        switch (i) {
            case 1: return txtTanggalMahasiswaPertemuan1;
            case 2: return txtTanggalMahasiswaPertemuan2;
            case 3: return txtTanggalMahasiswaPertemuan3;
            case 4: return txtTanggalMahasiswaPertemuan4;
            case 5: return txtTanggalMahasiswaPertemuan5;
            case 6: return txtTanggalMahasiswaPertemuan6;
            default: return null;
        }
    }

    private Text getNimText(int i) {
        switch (i) {
            case 1: return txtNimMahasiswaPertemuan1;
            case 2: return txtNimMahasiswaPertemuan2;
            case 3: return txtNimMahasiswaPertemuan3;
            case 4: return txtNimMahasiswaPertemuan4;
            case 5: return txtNimMahasiswaPertemuan5;
            case 6: return txtNimMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJurusanText(int i) {
        switch (i) {
            case 1: return txtJurusanMahasiswaPertemuan1;
            case 2: return txtJurusanMahasiswaPertemuan2;
            case 3: return txtJurusanMahasiswaPertemuan3;
            case 4: return txtJurusanMahasiswaPertemuan4;
            case 5: return txtJurusanMahasiswaPertemuan5;
            case 6: return txtJurusanMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJudulSkripsiText(int i) {
        switch (i) {
            case 1: return txtJudulSkripsiMahasiswaPertemuan1;
            case 2: return txtJudulSkripsiMahasiswaPertemuan2;
            case 3: return txtJudulSkripsiMahasiswaPertemuan3;
            case 4: return txtJudulSkripsiMahasiswaPertemuan4;
            case 5: return txtJudulSkripsiMahasiswaPertemuan5;
            case 6: return txtJudulSkripsiMahasiswaPertemuan6;
            default: return null;
        }
    }

    private Text getPertemuanText(int i) {
        switch (i) {
            case 1: return txtMahasiswaPertemuan1;
            case 2: return txtMahasiswaPertemuan2;
            case 3: return txtMahasiswaPertemuan3;
            case 4: return txtMahasiswaPertemuan4;
            case 5: return txtMahasiswaPertemuan5;
            case 6: return txtMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getRuanganText(int i) {
        switch (i) {
            case 1: return txtRuanganMahasiswaPertemuan1;
            case 2: return txtRuanganMahasiswaPertemuan2;
            case 3: return txtRuanganMahasiswaPertemuan3;
            case 4: return txtRuanganMahasiswaPertemuan4;
            case 5: return txtRuanganMahasiswaPertemuan5;
            case 6: return txtRuanganMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJamText(int i) {
        switch (i) {
            case 1: return txtJamMahasiswaPertemuan1;
            case 2: return txtJamMahasiswaPertemuan2;
            case 3: return txtJamMahasiswaPertemuan3;
            case 4: return txtJamMahasiswaPertemuan4;
            case 5: return txtJamMahasiswaPertemuan5;
            case 6: return txtJamMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJudulPertemuanText(int i) {
        switch (i) {
            case 1: return txtJudulPertemuan1;
            case 2: return txtJudulPertemuan2;
            case 3: return txtJudulPertemuan3;
            case 4: return txtJudulPertemuan4;
            case 5: return txtJudulPertemuan5;
            case 6: return txtJudulPertemuan6;
            default: return null;
        }
    }
    
    private void showActionButtons(int i, boolean show) {
        getTerimaButton(i).setVisible(show);
        getTolakButton(i).setVisible(show);
    }
    
    private Button getTerimaButton(int i) {
        switch (i) {
            case 1: return btnTerimaCard1;
            case 2: return btnTerimaCard2;
            case 3: return btnTerimaCard3;
            case 4: return btnTerimaCard4;
            case 5: return btnTerimaCard5;
            case 6: return btnTerimaCard6;
            default: return null;
        }
    }

    private Button getTolakButton(int i) {
        switch (i) {
            case 1: return btnTolakCard1;
            case 2: return btnTolakCard2;
            case 3: return btnTolakCard3;
            case 4: return btnTolakCard4;
            case 5: return btnTolakCard5;
            case 6: return btnTolakCard6;
            default: return null;
        }
    }
    
   @FXML
    private void TerimaBimbingan(ActionEvent event) {
        int index = getCardIndex(event);
        if (index == -1) return;

        Integer requestId = requestIds[index];

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            // INSERT ke bimbingan
            String insert =
                "INSERT INTO bimbingan (id_mahasiswa, id_dosen, judul, ruangan, tanggal, jam) " +
                "SELECT id_mahasiswa, id_dosen, judul, ruangan, tanggal, jam " +
                "FROM request_bimbingan WHERE id_requestbimbingan = ?";

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setInt(1, requestId);
                ps.executeUpdate();
            }

            // UPDATE status request
            String update =
                "UPDATE request_bimbingan SET status = 'DITERIMA' " +
                "WHERE id_requestbimbingan = ?";

            try (PreparedStatement ps = conn.prepareStatement(update)) {
                ps.setInt(1, requestId);
                ps.executeUpdate();
            }

            conn.commit();

            showAlert(
                Alert.AlertType.INFORMATION,
                "Berhasil",
                "Jadwal bimbingan berhasil diterima."
            );

            resetCard();
            MahasiswaItem selected = cbMahasiswaBimbingan.getValue();
            if (selected != null) {
                loadRequestBimbingan(selected.getIdMahasiswa());
            }

        } catch (SQLException e) {
            try {
                DBConnect.getConnection().rollback();
            } catch (Exception ignored) {}

            showAlert(
                Alert.AlertType.ERROR,
                "Gagal Menerima Jadwal",
                e.getMessage() // 🔥 pesan dari trigger MySQL
            );

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(
                Alert.AlertType.ERROR,
                "Error Sistem",
                "Terjadi kesalahan tak terduga."
            );
        }
    }

    
    @FXML
    private void TolakJadwal(ActionEvent event) {
        int index = getCardIndex(event);
        if (index == -1) return;

        Integer requestId = requestIds[index];

        String sql =
            "UPDATE request_bimbingan SET status = 'DITOLAK' " +
            "WHERE id_requestbimbingan = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.executeUpdate();

            resetCard();
            MahasiswaItem selected = cbMahasiswaBimbingan.getValue();
            if (selected != null) {
                loadRequestBimbingan(selected.getIdMahasiswa());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int getCardIndex(ActionEvent event) {
        Button btn = (Button) event.getSource();

        if (btn == btnTerimaCard1 || btn == btnTolakCard1) return 0;
        if (btn == btnTerimaCard2 || btn == btnTolakCard2) return 1;
        if (btn == btnTerimaCard3 || btn == btnTolakCard3) return 2;
        if (btn == btnTerimaCard4 || btn == btnTolakCard4) return 3;
        if (btn == btnTerimaCard5 || btn == btnTolakCard5) return 4;
        if (btn == btnTerimaCard6 || btn == btnTolakCard6) return 5;

        return -1;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    // ================= CLOSE MODAL =================

    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
