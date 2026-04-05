package app.controllers;

import app.database.DBConnect;
import app.session.DosenSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardDosenViewController {

    // ================= ROOT =================
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Rectangle headerShape;
    @FXML private AnchorPane navbarContainer;
    // Nama Dosen dari DosenSession
    @FXML private Text txtNamaDosen;

    // ================= CARD =================
    @FXML private Node card1, card2, card3, card4,card5, card6,
                       card7, card8, card9, card10, card11, card12; 
    @FXML private Text
    txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4,
    txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8,
    txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12;

    @FXML private Text
        txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4,
        txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8,
        txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12;

    @FXML private Text
        txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4,
        txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8,
        txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12;

    @FXML private Text
        txtJudulSkripsi1, txtJudulSkripsi2, txtJudulSkripsi3, txtJudulSkripsi4,
        txtJudulSkripsi5, txtJudulSkripsi6, txtJudulSkripsi7, txtJudulSkripsi8,
        txtJudulSkripsi9, txtJudulSkripsi10, txtJudulSkripsi11, txtJudulSkripsi12;

    @FXML private Text
        txtTanggalPengajuan1, txtTanggalPengajuan2, txtTanggalPengajuan3, txtTanggalPengajuan4,
        txtTanggalPengajuan5, txtTanggalPengajuan6, txtTanggalPengajuan7, txtTanggalPengajuan8,
        txtTanggalPengajuan9, txtTanggalPengajuan10, txtTanggalPengajuan11, txtTanggalPengajuan12;

    @FXML private ImageView
        ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
        ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
        ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12;
    
    // Tombol
    @FXML private Button
        btnSetujuiPengajuan1, btnSetujuiPengajuan2, btnSetujuiPengajuan3, btnSetujuiPengajuan4,
        btnSetujuiPengajuan5, btnSetujuiPengajuan6, btnSetujuiPengajuan7, btnSetujuiPengajuan8,
        btnSetujuiPengajuan9, btnSetujuiPengajuan10, btnSetujuiPengajuan11, btnSetujuiPengajuan12;

    @FXML private Button
        btnTolakPengajuan1, btnTolakPengajuan2, btnTolakPengajuan3, btnTolakPengajuan4,
        btnTolakPengajuan5, btnTolakPengajuan6, btnTolakPengajuan7, btnTolakPengajuan8,
        btnTolakPengajuan9, btnTolakPengajuan10, btnTolakPengajuan11, btnTolakPengajuan12;                      ;

    // Menyimpan idMahasiswa & idSkripsi tiap card
    private static final int MAX_CARD = 12;
    private int[] idMahasiswaCard = new int[MAX_CARD];
    private int[] idSkripsiCard   = new int[MAX_CARD];
    
    // Filter
    private String filterJurusan = null; // null = semua
    @FXML private Button btnSemua;
    @FXML private Button btnInformatika;
    @FXML private Button btnKomunikasi;
    
    // Statistik
    @FXML private Text txtKuotaDosen;
    @FXML private Text txtStatusDisetujuiSkripsi;
    @FXML private Text txtStatusDiajukanSkripsi;



    // ================= INITIALIZE =================

    public void initialize() {
        // Setup Navbar
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarDosen.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupBackground();

        // Ambil nama dari session
        String nama = DosenSession.getNama();
        txtNamaDosen.setText((nama != null && !nama.isEmpty()) ? nama + "!" : "Dosen");

        // Load pengajuan skripsi
        loadPengajuanSkripsi();
        
        // Load ringkasan status dosen & skripsi
        loadRingkasanDosen();

        // Filter
        setActiveButton(btnSemua);
        

    }

    private void setupBackground() {
        if (bgImage != null && root != null && headerShape != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
            headerShape.widthProperty().bind(root.widthProperty());
            headerShape.heightProperty().bind(root.heightProperty().multiply(0.2));
        }
    }

    // ================= Jadwal Button ================= 
    @FXML 
    private void goToJadwal(MouseEvent event) {
        openPage(event, "/app/fxml/JadwalDosenView.fxml");
    }

    private void openPage(Event event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    // =============================== CARD MODAL ===============================
    @FXML public void showModal1(ActionEvent event)  { openModal(idMahasiswaCard[0]); }
    @FXML public void showModal2(ActionEvent event)  { openModal(idMahasiswaCard[1]); }
    @FXML public void showModal3(ActionEvent event)  { openModal(idMahasiswaCard[2]); }
    @FXML public void showModal4(ActionEvent event)  { openModal(idMahasiswaCard[3]); }
    @FXML public void showModal5(ActionEvent event)  { openModal(idMahasiswaCard[4]); }
    @FXML public void showModal6(ActionEvent event)  { openModal(idMahasiswaCard[5]); }
    @FXML public void showModal7(ActionEvent event)  { openModal(idMahasiswaCard[6]); }
    @FXML public void showModal8(ActionEvent event)  { openModal(idMahasiswaCard[7]); }
    @FXML public void showModal9(ActionEvent event)  { openModal(idMahasiswaCard[8]); }
    @FXML public void showModal10(ActionEvent event) { openModal(idMahasiswaCard[9]); }
    @FXML public void showModal11(ActionEvent event) { openModal(idMahasiswaCard[10]); }
    @FXML public void showModal12(ActionEvent event) { openModal(idMahasiswaCard[11]); }


    private void openModal(int idMahasiswa) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalDosen.fxml")
            );
            Parent rootModal = loader.load();

            // Kirim idMahasiswa ke modal
            app.controllers.ModalDosenController modalController = loader.getController();
            modalController.setDataMahasiswa(idMahasiswa);

            Stage modalStage = new Stage();
            modalStage.setTitle("Detail Pengajuan Skripsi");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(root.getScene().getWindow());
            modalStage.setScene(new Scene(rootModal));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================== LOAD PENGAJUAN ===============================
    private void loadPengajuanSkripsi() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT s.id_skripsi, m.id_mahasiswa, m.nama, m.nim, m.jurusan, m.foto, " +
            "s.judul_skripsi, s.created_at " +
            "FROM skripsi s " +
            "JOIN mahasiswa m ON s.id_mahasiswa = m.id_mahasiswa " +
            "WHERE s.status = 'DIAJUKAN' " +
            "AND s.id_dosen = ? ";

        if (filterJurusan != null) {
            sql += "AND m.jurusan = ? ";
        }

        sql += "ORDER BY s.created_at DESC LIMIT 12";

        Node[] cards = {
            card1, card2, card3, card4, card5, card6,
            card7, card8, card9, card10, card11, card12
        };
        Text[] namaText = {
            txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4,
            txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8,
            txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12
        };
        Text[] nimText = {
            txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4,
            txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8,
            txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12
        };

        Text[] jurusanText = {
            txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4,
            txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8,
            txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12
        };

        Text[] judulText = {
            txtJudulSkripsi1, txtJudulSkripsi2, txtJudulSkripsi3, txtJudulSkripsi4,
            txtJudulSkripsi5, txtJudulSkripsi6, txtJudulSkripsi7, txtJudulSkripsi8,
            txtJudulSkripsi9, txtJudulSkripsi10, txtJudulSkripsi11, txtJudulSkripsi12
        };

        Text[] tanggalText = {
            txtTanggalPengajuan1, txtTanggalPengajuan2, txtTanggalPengajuan3, txtTanggalPengajuan4,
            txtTanggalPengajuan5, txtTanggalPengajuan6, txtTanggalPengajuan7, txtTanggalPengajuan8,
            txtTanggalPengajuan9, txtTanggalPengajuan10, txtTanggalPengajuan11, txtTanggalPengajuan12
        };
        ImageView[] ivMahasiswa = {
            ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
            ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
            ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12
        };

        // default semua card disembunyikan
        for (Node card : cards) {
            card.setVisible(false);
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            
            if (filterJurusan != null) {
                ps.setString(2, filterJurusan);
            }

            ResultSet rs = ps.executeQuery();

            int index = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (rs.next() && index < MAX_CARD) {
                cards[index].setVisible(true);

                // SIMPAN ID UNIK
                idMahasiswaCard[index] = rs.getInt("id_mahasiswa");
                idSkripsiCard[index]   = rs.getInt("id_skripsi");
                


                namaText[index].setText(rs.getString("nama"));
                nimText[index].setText(rs.getString("nim"));
                jurusanText[index].setText(rs.getString("jurusan"));
                judulText[index].setText(rs.getString("judul_skripsi"));
                tanggalText[index].setText(
                    rs.getTimestamp("created_at").toLocalDateTime().format(formatter)
                );

                byte[] fotoBytes = rs.getBytes("foto");
                if (fotoBytes != null && fotoBytes.length > 0) {
                    ivMahasiswa[index].setImage(
                        new javafx.scene.image.Image(new java.io.ByteArrayInputStream(fotoBytes))
                    );
                } else {
                    ivMahasiswa[index].setImage(null);
                }

                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // =============================== TOMBOL TOLAK DAN TERIMA ===============================
    @FXML private void tolakPengajuan1(ActionEvent e){ prosesTolak(idSkripsiCard[0], idMahasiswaCard[0]); }
    @FXML private void tolakPengajuan2(ActionEvent e){ prosesTolak(idSkripsiCard[1], idMahasiswaCard[1]); }
    @FXML private void tolakPengajuan3(ActionEvent e){ prosesTolak(idSkripsiCard[2], idMahasiswaCard[2]); }
    @FXML private void tolakPengajuan4(ActionEvent e){ prosesTolak(idSkripsiCard[3], idMahasiswaCard[3]); }
    @FXML private void tolakPengajuan5(ActionEvent e){ prosesTolak(idSkripsiCard[4], idMahasiswaCard[4]); }
    @FXML private void tolakPengajuan6(ActionEvent e){ prosesTolak(idSkripsiCard[5], idMahasiswaCard[5]); }
    @FXML private void tolakPengajuan7(ActionEvent e){ prosesTolak(idSkripsiCard[6], idMahasiswaCard[6]); }
    @FXML private void tolakPengajuan8(ActionEvent e){ prosesTolak(idSkripsiCard[7], idMahasiswaCard[7]); }
    @FXML private void tolakPengajuan9(ActionEvent e){ prosesTolak(idSkripsiCard[8], idMahasiswaCard[8]); }
    @FXML private void tolakPengajuan10(ActionEvent e){ prosesTolak(idSkripsiCard[9], idMahasiswaCard[9]); }
    @FXML private void tolakPengajuan11(ActionEvent e){ prosesTolak(idSkripsiCard[10], idMahasiswaCard[10]); }
    @FXML private void tolakPengajuan12(ActionEvent e){ prosesTolak(idSkripsiCard[11], idMahasiswaCard[11]); }


    @FXML private void setujuiPengajuan1(ActionEvent e){ prosesSetujui(idSkripsiCard[0], idMahasiswaCard[0]); }
    @FXML private void setujuiPengajuan2(ActionEvent e){ prosesSetujui(idSkripsiCard[1], idMahasiswaCard[1]); }
    @FXML private void setujuiPengajuan3(ActionEvent e){ prosesSetujui(idSkripsiCard[2], idMahasiswaCard[2]); }
    @FXML private void setujuiPengajuan4(ActionEvent e){ prosesSetujui(idSkripsiCard[3], idMahasiswaCard[3]); }
    @FXML private void setujuiPengajuan5(ActionEvent e){ prosesSetujui(idSkripsiCard[4], idMahasiswaCard[4]); }
    @FXML private void setujuiPengajuan6(ActionEvent e){ prosesSetujui(idSkripsiCard[5], idMahasiswaCard[5]); }
    @FXML private void setujuiPengajuan7(ActionEvent e){ prosesSetujui(idSkripsiCard[6], idMahasiswaCard[6]); }
    @FXML private void setujuiPengajuan8(ActionEvent e){ prosesSetujui(idSkripsiCard[7], idMahasiswaCard[7]); }
    @FXML private void setujuiPengajuan9(ActionEvent e){ prosesSetujui(idSkripsiCard[8], idMahasiswaCard[8]); }
    @FXML private void setujuiPengajuan10(ActionEvent e){ prosesSetujui(idSkripsiCard[9], idMahasiswaCard[9]); }
    @FXML private void setujuiPengajuan11(ActionEvent e){ prosesSetujui(idSkripsiCard[10], idMahasiswaCard[10]); }
    @FXML private void setujuiPengajuan12(ActionEvent e){ prosesSetujui(idSkripsiCard[11], idMahasiswaCard[11]); }

    private void prosesTolak(int idSkripsi, int idMahasiswa) {
        int idDosen = DosenSession.getIdDosen();

        String sql = "UPDATE skripsi SET status = 'DITOLAK' WHERE id_skripsi = ? AND id_dosen = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSkripsi);
            ps.setInt(2, idDosen);
            ps.executeUpdate();

            // Ambil nama
            String namaDosen = getNamaDosen(conn, idDosen);
            String namaMahasiswa = getNamaMahasiswa(conn, idMahasiswa);

            // Insert log
            String activity =
                "Dosen " + namaDosen +
                " menolak pengajuan skripsi mahasiswa " + namaMahasiswa;

            insertActivityLog(
                conn,
                idDosen,
                namaDosen,
                "DOSEN",
                activity
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pengajuan skripsi ditolak.");
            alert.showAndWait();

            loadPengajuanSkripsi(); // refresh card
            loadRingkasanDosen();


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal menolak pengajuan").showAndWait();
        }
    }
    
    private void prosesSetujui(int idSkripsi, int idMahasiswa) {
        int idDosen = DosenSession.getIdDosen();

        try (Connection conn = DBConnect.getConnection()) {

            // 1. Update status skripsi 
            String updateSkripsi =
                "UPDATE skripsi SET status = 'DISETUJUI' WHERE id_skripsi = ? AND id_dosen = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSkripsi)) {
                ps.setInt(1, idSkripsi);
                ps.setInt(2, idDosen);
                ps.executeUpdate();
            }

            // 2. Cek apakah dosen ini sudah jadi pembimbing mahasiswa tsb
            String cekSudahAda =
                "SELECT COUNT(*) FROM DosenPembimbing WHERE id_mahasiswa = ? AND id_dosen = ?";
            boolean sudahAda;

            try (PreparedStatement ps = conn.prepareStatement(cekSudahAda)) {
                ps.setInt(1, idMahasiswa);
                ps.setInt(2, idDosen);
                ResultSet rs = ps.executeQuery();
                rs.next();
                sudahAda = rs.getInt(1) > 0;
            }

            if (sudahAda) {
                new Alert(Alert.AlertType.WARNING,
                    "Dosen ini sudah terdaftar sebagai pembimbing mahasiswa tersebut.")
                    .showAndWait();
                return;
            }

            // 3. Tentukan jenis dosen (1 atau 2)
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

            // 4. Insert ke DosenPembimbing (AMAN)
            String insertPembimbing =
                "INSERT INTO DosenPembimbing (id_mahasiswa, id_dosen, jenis_dosen) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertPembimbing)) {
                ps.setInt(1, idMahasiswa);
                ps.setInt(2, idDosen);
                ps.setString(3, jenisDosen);
                ps.executeUpdate();
            }

            new Alert(Alert.AlertType.INFORMATION,
                "Pengajuan diterima sebagai Dosen Pembimbing " + jenisDosen)
                .showAndWait();
            
            // Ambil nama
            String namaDosen = getNamaDosen(conn, idDosen);
            String namaMahasiswa = getNamaMahasiswa(conn, idMahasiswa);

            // Insert activity log
            String activity =
                "Dosen " + namaDosen +
                " menyetujui pengajuan skripsi mahasiswa " + namaMahasiswa;

            insertActivityLog(
                conn,
                idDosen,
                namaDosen,
                "DOSEN",
                activity
            );


            loadPengajuanSkripsi();
            loadRingkasanDosen();


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Mahasiswa sudah memiliki 2 dosen pembimbing").showAndWait();
        }
    }
    
    // =============================== LOG AKTIVITAS ===============================
    private String getNamaDosen(Connection conn, int idDosen) throws SQLException {
        String sql = "SELECT nama FROM dosen WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("nama") : "Dosen";
        }
    }

    private String getNamaMahasiswa(Connection conn, int idMahasiswa) throws SQLException {
        String sql = "SELECT nama FROM mahasiswa WHERE id_mahasiswa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("nama") : "Mahasiswa";
        }
    }

    private void insertActivityLog(Connection conn,
                                int userId,
                                String username,
                                String role,
                                String activity) throws SQLException {

        String sql =
            "INSERT INTO activity_log (user_id, username, role, activity) " +
            "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, username);
            ps.setString(3, role);
            ps.setString(4, activity);
            ps.executeUpdate();
        }
    }

    // =============================== FILTER JURUSAN ===============================
    @FXML
    private void filterSemua(ActionEvent event) {
        filterJurusan = null; // tampilkan semua
        setActiveButton(btnSemua);
        loadPengajuanSkripsi();
    }

    @FXML
    private void filterInformatika(ActionEvent event) {
        filterJurusan = "Informatika";
        setActiveButton(btnInformatika);
        loadPengajuanSkripsi();
    }

    @FXML
    private void filterKomunikasi(ActionEvent event) {
        filterJurusan = "Komunikasi";
        setActiveButton(btnKomunikasi);
        loadPengajuanSkripsi();
    }

    // =============================== FILTER BUTTON UI ===============================
    private void setActiveButton(Button activeBtn) {

        Button[] allButtons = { btnSemua, btnInformatika, btnKomunikasi };

        for (Button btn : allButtons) {
            btn.setStyle("-fx-background-color: white; -fx-text-fill: #949494;");
        }

        // tombol aktif
        activeBtn.setStyle("-fx-background-color: #E53935; -fx-text-fill: #FFFFFF;");
    }

    // =============================== RINGKASAN DOSEN ===============================
    private void loadRingkasanDosen() {

        int idDosen = DosenSession.getIdDosen();

        // ================= KUOTA DOSEN =================
        String sqlKuota = "SELECT kuota FROM dosen WHERE id = ?";

        // ================= STATUS SKRIPSI =================
        String sqlStatus =
            "SELECT status, COUNT(*) AS total " +
            "FROM skripsi " +
            "WHERE id_dosen = ? " +
            "AND status IN ('DISETUJUI', 'DIAJUKAN') " +
            "GROUP BY status";

        int jumlahDisetujui = 0;
        int jumlahDiajukan = 0;

        try (Connection conn = DBConnect.getConnection()) {

            // ===== Ambil KUOTA DOSEN =====
            try (PreparedStatement ps = conn.prepareStatement(sqlKuota)) {
                ps.setInt(1, idDosen);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    txtKuotaDosen.setText(String.valueOf(rs.getInt("kuota")));
                } else {
                    txtKuotaDosen.setText("0");
                }
            }

            // ===== Hitung STATUS SKRIPSI =====
            try (PreparedStatement ps = conn.prepareStatement(sqlStatus)) {
                ps.setInt(1, idDosen);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String status = rs.getString("status");
                    int total = rs.getInt("total");

                    if ("DISETUJUI".equals(status)) {
                        jumlahDisetujui = total;
                    } else if ("DIAJUKAN".equals(status)) {
                        jumlahDiajukan = total;
                    }
                }
            }

            txtStatusDisetujuiSkripsi.setText(String.valueOf(jumlahDisetujui));
            txtStatusDiajukanSkripsi.setText(String.valueOf(jumlahDiajukan));

        } catch (SQLException e) {
            e.printStackTrace();
            txtKuotaDosen.setText("0");
            txtStatusDisetujuiSkripsi.setText("0");
            txtStatusDiajukanSkripsi.setText("0");
        }
    }


}
