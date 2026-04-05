package app.controllers;

import app.dao.BimbinganContext;
import app.database.DBConnect;
import app.models.MahasiswaItem;
import app.session.DosenSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class JadwalDosenViewController {

    // ROOT container dan background
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Rectangle headerShape;
    
    
    // Navbar
    @FXML private AnchorPane navbarContainer;
    
    // Kembali
    @FXML private Hyperlink kembaliLink;
    
    // Nama Dosen dari DosenSession
    @FXML private Text txtNamaDosen;
    
    // Button
    @FXML private Button btnLihatJadwal;
    @FXML private Button btnBuatJadwal;
    
    // Card Bimbingan
    @FXML private HBox cardBimbingan1;
    @FXML private HBox cardBimbingan2;
    @FXML private HBox cardBimbingan3;
    @FXML private HBox cardBimbingan4;
    @FXML private HBox cardBimbingan5;
    @FXML private HBox cardBimbingan6;
    @FXML private HBox cardBimbingan7;
    @FXML private HBox cardBimbingan8;
    @FXML private HBox cardBimbingan9;
    @FXML private HBox cardBimbingan10;
    @FXML private HBox cardBimbingan11;
    @FXML private HBox cardBimbingan12;

    private HBox[] cards;
    
    // Card Bimbingan
    @FXML private ImageView ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
                      ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
                      ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12;

    @FXML private Text txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4,
                      txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8,
                      txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12;

    @FXML private Text txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4,
                      txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8,
                      txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12;

    @FXML private Text txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4,
                      txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8,
                      txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12;

    @FXML private Text txtJudulSkripsiMahasiswa1, txtJudulSkripsiMahasiswa2, txtJudulSkripsiMahasiswa3,
                      txtJudulSkripsiMahasiswa4, txtJudulSkripsiMahasiswa5, txtJudulSkripsiMahasiswa6,
                      txtJudulSkripsiMahasiswa7, txtJudulSkripsiMahasiswa8, txtJudulSkripsiMahasiswa9,
                      txtJudulSkripsiMahasiswa10, txtJudulSkripsiMahasiswa11, txtJudulSkripsiMahasiswa12;

    @FXML private Text txtRuanganPertemuan1Mahasiswa1, txtRuanganPertemuan1Mahasiswa2,
                      txtRuanganPertemuan1Mahasiswa3, txtRuanganPertemuan1Mahasiswa4,
                      txtRuanganPertemuan1Mahasiswa5, txtRuanganPertemuan1Mahasiswa6,
                      txtRuanganPertemuan1Mahasiswa7, txtRuanganPertemuan1Mahasiswa8,
                      txtRuanganPertemuan1Mahasiswa9, txtRuanganPertemuan1Mahasiswa10,
                      txtRuanganPertemuan1Mahasiswa11, txtRuanganPertemuan1Mahasiswa12;

    @FXML private Text txtPertemuan1Mahasiswa1, txtPertemuan1Mahasiswa2, txtPertemuan1Mahasiswa3,
                      txtPertemuan1Mahasiswa4, txtPertemuan1Mahasiswa5, txtPertemuan1Mahasiswa6,
                      txtPertemuan1Mahasiswa7, txtPertemuan1Mahasiswa8, txtPertemuan1Mahasiswa9,
                      txtPertemuan1Mahasiswa10, txtPertemuan1Mahasiswa11, txtPertemuan1Mahasiswa12;

    @FXML private Text txtJamBimbinganPertemuan1Mahasiswa1, txtJamBimbinganPertemuan1Mahasiswa2,
                      txtJamBimbinganPertemuan1Mahasiswa3, txtJamBimbinganPertemuan1Mahasiswa4,
                      txtJamBimbinganPertemuan1Mahasiswa5, txtJamBimbinganPertemuan1Mahasiswa6,
                      txtJamBimbinganPertemuan1Mahasiswa7, txtJamBimbinganPertemuan1Mahasiswa8,
                      txtJamBimbinganPertemuan1Mahasiswa9, txtJamBimbinganPertemuan1Mahasiswa10,
                      txtJamBimbinganPertemuan1Mahasiswa11, txtJamBimbinganPertemuan1Mahasiswa12;

    // Array View
    private ImageView[] ivMahasiswa;
    private Text[] txtNama;
    private Text[] txtNim;
    private Text[] txtJurusan;
    private Text[] txtJudul;
    private Text[] txtRuangan;
    private Text[] txtPertemuan;
    private Text[] txtJam;

    // Button Catat
    @FXML private Button btnCatatPertemuan1Mahasiswa1, btnCatatPertemuan1Mahasiswa2,
                    btnCatatPertemuan1Mahasiswa3, btnCatatPertemuan1Mahasiswa4,
                    btnCatatPertemuan1Mahasiswa5, btnCatatPertemuan1Mahasiswa6,
                    btnCatatPertemuan1Mahasiswa7, btnCatatPertemuan1Mahasiswa8,
                    btnCatatPertemuan1Mahasiswa9, btnCatatPertemuan1Mahasiswa10,
                    btnCatatPertemuan1Mahasiswa11, btnCatatPertemuan1Mahasiswa12;

    private Button[] btnCatat;
    
    // Combo Box Revisi Mahasiswa Bimbingan 
    @FXML private ComboBox<MahasiswaItem> cbMahasiswaBimbingan;


    // Card Revisi Bimbingan
    @FXML private VBox cardRevisi1;
    @FXML private VBox cardRevisi2;
    @FXML private VBox cardRevisi3;
    @FXML private VBox cardRevisi4;
    @FXML private VBox cardRevisi5;
    @FXML private VBox cardRevisi6;
    
    // Isi Card Revisi Bimbingan
    @FXML private Text txtRevisiNamaMahasiswaPertemuan1, txtRevisiNamaMahasiswaPertemuan2, txtRevisiNamaMahasiswaPertemuan3, txtRevisiNamaMahasiswaPertemuan4, txtRevisiNamaMahasiswaPertemuan5, txtRevisiNamaMahasiswaPertemuan6;
    @FXML private Text txtRevisiBimbinganMahasiswaPertemuan1, txtRevisiBimbinganMahasiswaPertemuan2, txtRevisiBimbinganMahasiswaPertemuan3, txtRevisiBimbinganMahasiswaPertemuan4, txtRevisiBimbinganMahasiswaPertemuan5, txtRevisiBimbinganMahasiswaPertemuan6;
    @FXML private Text txtRevisiMahasiswaPertemuan1, txtRevisiMahasiswaPertemuan2, txtRevisiMahasiswaPertemuan3, txtRevisiMahasiswaPertemuan4, txtRevisiMahasiswaPertemuan5, txtRevisiMahasiswaPertemuan6;
    @FXML private Text txtDeadlineRevisiMahasiswaPertemuan1, txtDeadlineRevisiMahasiswaPertemuan2, txtDeadlineRevisiMahasiswaPertemuan3, txtDeadlineRevisiMahasiswaPertemuan4, txtDeadlineRevisiMahasiswaPertemuan5, txtDeadlineRevisiMahasiswaPertemuan6;
    
    // Button Edit Revisi Bimbingan
    @FXML private Button btnEditRevisiMahasiswaPertemuan1;
    @FXML private Button btnEditRevisiMahasiswaPertemuan2;
    @FXML private Button btnEditRevisiMahasiswaPertemuan3;
    @FXML private Button btnEditRevisiMahasiswaPertemuan4;
    @FXML private Button btnEditRevisiMahasiswaPertemuan5;
    @FXML private Button btnEditRevisiMahasiswaPertemuan6;


    public void initialize() {
        setupBackground(); 
        
        // Setup Navbar
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarDosen.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // set action untuk kembali
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event -> openDashboardPage());
        }
        
        // Ambil nama dari session
        String nama = DosenSession.getNama();

        if (nama != null && !nama.isEmpty()) {
            txtNamaDosen.setText(nama + "!");
        } else {
            txtNamaDosen.setText("Dosen");
        }
        
        // Bimbingan
         cards = new HBox[]{
            cardBimbingan1, cardBimbingan2, cardBimbingan3, cardBimbingan4,
            cardBimbingan5, cardBimbingan6, cardBimbingan7, cardBimbingan8,
            cardBimbingan9, cardBimbingan10, cardBimbingan11, cardBimbingan12
        };

        ivMahasiswa = new ImageView[]{ ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
                                       ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
                                       ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12 };

        txtNama = new Text[]{ txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4, txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8, txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12 };
        txtNim = new Text[]{ txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4, txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8, txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12 };
        txtJurusan = new Text[]{ txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4, txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8, txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12 };
        txtJudul = new Text[]{ txtJudulSkripsiMahasiswa1, txtJudulSkripsiMahasiswa2, txtJudulSkripsiMahasiswa3, txtJudulSkripsiMahasiswa4, txtJudulSkripsiMahasiswa5, txtJudulSkripsiMahasiswa6, txtJudulSkripsiMahasiswa7, txtJudulSkripsiMahasiswa8, txtJudulSkripsiMahasiswa9, txtJudulSkripsiMahasiswa10, txtJudulSkripsiMahasiswa11, txtJudulSkripsiMahasiswa12 };
        txtRuangan = new Text[]{ txtRuanganPertemuan1Mahasiswa1, txtRuanganPertemuan1Mahasiswa2, txtRuanganPertemuan1Mahasiswa3, txtRuanganPertemuan1Mahasiswa4, txtRuanganPertemuan1Mahasiswa5, txtRuanganPertemuan1Mahasiswa6, txtRuanganPertemuan1Mahasiswa7, txtRuanganPertemuan1Mahasiswa8, txtRuanganPertemuan1Mahasiswa9, txtRuanganPertemuan1Mahasiswa10, txtRuanganPertemuan1Mahasiswa11, txtRuanganPertemuan1Mahasiswa12 };
        txtPertemuan = new Text[]{ txtPertemuan1Mahasiswa1, txtPertemuan1Mahasiswa2, txtPertemuan1Mahasiswa3, txtPertemuan1Mahasiswa4, txtPertemuan1Mahasiswa5, txtPertemuan1Mahasiswa6, txtPertemuan1Mahasiswa7, txtPertemuan1Mahasiswa8, txtPertemuan1Mahasiswa9, txtPertemuan1Mahasiswa10, txtPertemuan1Mahasiswa11, txtPertemuan1Mahasiswa12 };
        txtJam = new Text[]{ txtJamBimbinganPertemuan1Mahasiswa1, txtJamBimbinganPertemuan1Mahasiswa2, txtJamBimbinganPertemuan1Mahasiswa3, txtJamBimbinganPertemuan1Mahasiswa4, txtJamBimbinganPertemuan1Mahasiswa5, txtJamBimbinganPertemuan1Mahasiswa6, txtJamBimbinganPertemuan1Mahasiswa7, txtJamBimbinganPertemuan1Mahasiswa8, txtJamBimbinganPertemuan1Mahasiswa9, txtJamBimbinganPertemuan1Mahasiswa10, txtJamBimbinganPertemuan1Mahasiswa11, txtJamBimbinganPertemuan1Mahasiswa12 };

        
        // Button
        btnCatat = new Button[]{
            btnCatatPertemuan1Mahasiswa1, btnCatatPertemuan1Mahasiswa2,
            btnCatatPertemuan1Mahasiswa3, btnCatatPertemuan1Mahasiswa4,
            btnCatatPertemuan1Mahasiswa5, btnCatatPertemuan1Mahasiswa6,
            btnCatatPertemuan1Mahasiswa7, btnCatatPertemuan1Mahasiswa8,
            btnCatatPertemuan1Mahasiswa9, btnCatatPertemuan1Mahasiswa10,
            btnCatatPertemuan1Mahasiswa11, btnCatatPertemuan1Mahasiswa12
        };
        
        hideAllCards();
        loadBimbinganHariIni();
        loadMahasiswaBimbingan();
        clearRevisiView();
        resetRevisiCards();
        
        

        
    }
    
    // ================= CARD REVISI BIMBINGAN =================

    private VBox getCardRevisi(int i) {
        switch (i) {
            case 1: return cardRevisi1;
            case 2: return cardRevisi2;
            case 3: return cardRevisi3;
            case 4: return cardRevisi4;
            case 5: return cardRevisi5;
            case 6: return cardRevisi6;
            default: return null;
        }
    }
    
    private void setRevisiCard(
            int idx,
            String nama,
            int pertemuanKe,
            String revisi,
            Date deadline
    ) {
        VBox card = getCardRevisi(idx);
        if (card == null) return;

        ((Text) card.lookup("#txtRevisiNamaMahasiswaPertemuan" + idx))
            .setText(nama);

        ((Text) card.lookup("#txtRevisiBimbinganMahasiswaPertemuan" + idx))
            .setText("Pertemuan " + pertemuanKe);

        ((Text) card.lookup("#txtRevisiMahasiswaPertemuan" + idx))
            .setText(revisi);

        ((Text) card.lookup("#txtDeadlineRevisiMahasiswaPertemuan" + idx))
            .setText(deadline != null ? deadline.toString() : "-");

        card.setVisible(true);
        card.setManaged(true);
    }
    
    private void resetRevisiCards() {
        for (int i = 1; i <= 6; i++) {
            VBox card = getCardRevisi(i);
            if (card != null) {
                card.setVisible(false);
                card.setManaged(false);
            }
        }
    }



    
    // ================= MAHASISWA BIMBINGAN =================
    private void loadMahasiswaBimbingan() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT m.id_mahasiswa, m.nama " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON m.id_mahasiswa = dp.id_mahasiswa " +
            "WHERE dp.id_dosen = ?";

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
    
    @FXML
    private void onMahasiswaDipilih() {
        MahasiswaItem selected = cbMahasiswaBimbingan.getValue();
        if (selected == null) return;

        int idMahasiswa = selected.getIdMahasiswa();
        loadRevisi(idMahasiswa);
    }

    
    // ================= REVISI BIMBINGAN =================
    private void loadRevisi(int idMahasiswa) {
        resetRevisiCards();

        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT b.id_bimbingan, b.revisi, b.pertemuan_ke, b.deadline_revisi, m.nama " +
            "FROM bimbingan b " +
            "JOIN mahasiswa m ON m.id_mahasiswa = b.id_mahasiswa " +
            "WHERE b.id_mahasiswa = ? " +
            "AND b.id_dosen = ? " +
            "AND b.revisi IS NOT NULL " +
            "ORDER BY b.pertemuan_ke ASC " +
            "LIMIT 6";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);

            ResultSet rs = ps.executeQuery();

            int idx = 1;
            while (rs.next() && idx <= 6) {

                int idBimbingan = rs.getInt("id_bimbingan");

                // ===== SET DATA CARD =====
                setRevisiCard(
                    idx,
                    rs.getString("nama"),
                    rs.getInt("pertemuan_ke"),
                    rs.getString("revisi"),
                    rs.getDate("deadline_revisi")
                );

                // ===== PARSING ID BIMBINGAN KE BUTTON EDIT =====
                Button btnEdit = getBtnEditRevisi(idx);
                if (btnEdit != null) {
                    btnEdit.setUserData(idBimbingan);
                }

                idx++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ================= CLEAR CARD REVISI =================
    private void clearRevisiView() {
        txtRevisiNamaMahasiswaPertemuan1.setText("-");
        txtRevisiBimbinganMahasiswaPertemuan1.setText("-");
        txtRevisiMahasiswaPertemuan1.setText("Belum ada revisi");
        txtDeadlineRevisiMahasiswaPertemuan1.setText("-");
    }
    
    // ================= BUTTON EDIT REVISI BIMBINGAN =================
    private Button getBtnEditRevisi(int index) {
        switch (index) {
            case 1: return btnEditRevisiMahasiswaPertemuan1;
            case 2: return btnEditRevisiMahasiswaPertemuan2;
            case 3: return btnEditRevisiMahasiswaPertemuan3;
            case 4: return btnEditRevisiMahasiswaPertemuan4;
            case 5: return btnEditRevisiMahasiswaPertemuan5;
            case 6: return btnEditRevisiMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    // ================= BUTTON EDIT REVISI =================
    @FXML
    private void EditRevisi(ActionEvent event) {
        try {
            Button source = (Button) event.getSource();
            Integer idBimbingan = (Integer) source.getUserData();

            if (idBimbingan == null) {
                showAlert("Error", "ID Bimbingan tidak ditemukan.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalEditRevisiDosen.fxml")
            );

            Parent root = loader.load();

            ModalEditRevisiDosenController controller =
                    loader.getController();

            controller.setBimbinganData(
                idBimbingan,
                0, // idMahasiswa (tidak wajib di modal edit)
                DosenSession.getIdDosen(),
                0  // pertemuanKe (opsional)
            );

            Stage stage = new Stage();
            stage.setTitle("Edit Revisi Bimbingan");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    
    
    // ================= CARD JADWAL BIMBINGAN =================

    private void hideAllCards() {
        for (HBox card : cards) {
            card.setVisible(false);
            card.setManaged(false);
        }
    }
    
    private void loadBimbinganHariIni() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT b.id_bimbingan, b.id_dosen, b.id_mahasiswa, m.nama, m.nim, m.jurusan, m.foto, " +
            "s.judul_skripsi, b.pertemuan_ke, b.ruangan, b.jam " +
            "FROM bimbingan b " +
            "JOIN mahasiswa m ON b.id_mahasiswa = m.id_mahasiswa " +
            "JOIN dosenpembimbing dp ON dp.id_mahasiswa = b.id_mahasiswa AND dp.id_dosen = b.id_dosen " +
            "JOIN skripsi s ON s.id_mahasiswa = b.id_mahasiswa AND s.id_dosen = b.id_dosen " +
            "WHERE b.id_dosen = ? " +
            "AND dp.id_dosen = ? " +
            "AND b.tanggal = CURDATE() " +
            "ORDER BY b.jam ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ps.setInt(2, idDosen);

            ResultSet rs = ps.executeQuery();
            int idx = 0;

            while (rs.next() && idx < 12) {
                // ================= TAMPILKAN CARD =================
                cards[idx].setVisible(true);
                cards[idx].setManaged(true);

                // ================= SET DATA UI =================
                txtNama[idx].setText(rs.getString("nama"));
                txtNim[idx].setText(rs.getString("nim"));
                txtJurusan[idx].setText(rs.getString("jurusan"));
                txtJudul[idx].setText(rs.getString("judul_skripsi"));
                txtRuangan[idx].setText("Ruangan " + rs.getString("ruangan"));
                txtPertemuan[idx].setText("Pertemuan " + rs.getInt("pertemuan_ke"));
                txtJam[idx].setText(rs.getTime("jam").toString());
              
                loadFotoMahasiswa(ivMahasiswa[idx], rs.getBytes("foto"));

                // ================= AMBIL DATA PENTING =================
                int idBimbingan = rs.getInt("id_bimbingan");
                int idMahasiswa = rs.getInt("id_mahasiswa");
                int idDosenDb   = rs.getInt("id_dosen");
                int pertemuanKe = rs.getInt("pertemuan_ke");
                
                // ================= SIMPAN KE BUTTON CATAT =================
                btnCatat[idx].setUserData(
                    new BimbinganContext(
                        idBimbingan,
                        idMahasiswa,
                        idDosenDb,
                        pertemuanKe
                    )
                );

                // SET ACTION 
                btnCatat[idx].setOnAction(e -> {
                    if (e.getSource() instanceof Button) {
                        CatatRevisi(e);
                    }
                });


                idx++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void CatatRevisi(ActionEvent event) {
        Button btn = (Button) event.getSource();
        BimbinganContext ctx = (BimbinganContext) btn.getUserData();

        if (ctx == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalCatatRevisiDosen.fxml")
            );
            Parent root = loader.load();

            ModalCatatRevisiDosenController controller = loader.getController();
            controller.setBimbinganData(
                ctx.idBimbingan,
                ctx.idMahasiswa,
                ctx.idDosen,
                ctx.pertemuanKe
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Catat Revisi Bimbingan");
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void loadFotoMahasiswa(ImageView imageView, byte[] fotoBytes) {
        try {
            if (fotoBytes != null && fotoBytes.length > 0) {
                Image image = new Image(new ByteArrayInputStream(fotoBytes));
                imageView.setImage(image);
            } else {
                // optional: foto default
                imageView.setImage(
                    new Image(getClass().getResourceAsStream("/app/images/default-user.png"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void LihatJadwalBimbingan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalJadwalBimbinganDosen.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Jadwal Bimbingan Mahasiswa");
            stage.setScene(new Scene(root));

            // ================= MODAL SETTING =================
            stage.initModality(Modality.APPLICATION_MODAL);

            // ambil window induk (halaman jadwal dosen)
            Stage owner = (Stage) btnLihatJadwal.getScene().getWindow();
            stage.initOwner(owner);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void BuatJadwalBimbingan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalBuatJadwalBimbinganDosen.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Buat Jadwal Bimbingan Mahasiswa");
            stage.setScene(new Scene(root));

            // ================= MODAL SETTING =================
            stage.initModality(Modality.APPLICATION_MODAL);

            // ambil window induk (halaman jadwal dosen)
            Stage owner = (Stage) btnBuatJadwal.getScene().getWindow();
            stage.initOwner(owner);

            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }




    // Background Responsive
    private void setupBackground() {
        if (bgImage != null && root != null && headerShape != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());

            headerShape.widthProperty().bind(root.widthProperty());
            headerShape.heightProperty().bind(root.heightProperty().multiply(0.2)); 
        }
    }
    
    /** Buka Halaman Dasboard **/
    private void openDashboardPage() {
        loadScene("/app/fxml/DashboardDosenView.fxml", kembaliLink);
    }

    /** Metode loadScene **/
    private void loadScene(String fxmlPath, Hyperlink link) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent parent = loader.load();
            // Ganti root scene
            link.getScene().setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
