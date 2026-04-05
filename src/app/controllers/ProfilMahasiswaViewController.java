package app.controllers; 

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import app.database.DBConnect;
import app.session.MahasiswaSession;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ProfilMahasiswaViewController {

    // ================= ROOT =================
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;

    // ================= NAVBAR =================
    @FXML private AnchorPane navbarContainer;

    // ================= IMAGE =================
    @FXML private ImageView ivMahasiswa, ivDosen1, ivDosen2;

    // ================= Mahasiswa =================
    @FXML private Text txtNamaMahasiswa, txtNIMMahasiswa, txtNoTelpMahasiswa, txtEmailMahasiswa;
    
    // ================= Bimbingan =================
    @FXML private Text txtStatusBimbinganPertemuan1, txtStatusBimbinganPertemuan2, txtStatusBimbinganPertemuan3, txtStatusBimbinganPertemuan4, txtStatusBimbinganPertemuan5, txtStatusBimbinganPertemuan6, txtStatusBimbinganPertemuan7, txtStatusBimbinganPertemuan8, txtStatusBimbinganPertemuan9, txtStatusBimbinganPertemuan10, txtStatusBimbinganPertemuan11, txtStatusBimbinganPertemuan12,
                       txtJudulBimbinganPertemuan1, txtJudulBimbinganPertemuan2, txtJudulBimbinganPertemuan3, txtJudulBimbinganPertemuan4, txtJudulBimbinganPertemuan5, txtJudulBimbinganPertemuan6, txtJudulBimbinganPertemuan7, txtJudulBimbinganPertemuan8, txtJudulBimbinganPertemuan9, txtJudulBimbinganPertemuan10, txtJudulBimbinganPertemuan11, txtJudulBimbinganPertemuan12,
                       txtTanggalBimbinganPertemuan1, txtTanggalBimbinganPertemuan2, txtTanggalBimbinganPertemuan3, txtTanggalBimbinganPertemuan4, txtTanggalBimbinganPertemuan5, txtTanggalBimbinganPertemuan6, txtTanggalBimbinganPertemuan7, txtTanggalBimbinganPertemuan8, txtTanggalBimbinganPertemuan9, txtTanggalBimbinganPertemuan10, txtTanggalBimbinganPertemuan11, txtTanggalBimbinganPertemuan12,
                       txtBimbingan, txtTotalJumlahPertemuan;
    @FXML private Circle circleDot1, circleDot2, circleDot3, circleDot4, circleDot5, circleDot6, circleDot7, circleDot8, circleDot9, circleDot10, circleDot11, circleDot12;
    @FXML private ProgressBar pbPertemuanBimbingan;
   


   
    // ================= DOSEN PEMBIMBING =================
    @FXML private Text txtDosenPembimbing1, txtDosenPembimbing2;
    @FXML private Text txtNamaDosenPembimbing1, txtNamaDosenPembimbing2;
    @FXML private Text txtProdiDosenPembimbing1, txtProdiDosenPembimbing2;
    @FXML private ProgressBar progressPembimbing1;
    @FXML private ProgressBar progressPembimbing2;


    // ================= SKRIPSI =================
    @FXML private Text txtJudulSkripsi;

    // ================= Bimbingan Progress =================
    @FXML private VBox vbProgressBimbingan1, vbProgressBimbingan2;
    @FXML private Text txtJumlahPertemuanDosen1, txtJumlahPertemuanDosen2;
    
    // ================= Edit Profil =================
    @FXML private Button btnEditProfil;

    
    // ================= Kembali =================
    @FXML private Hyperlink kembaliLink;
    

    public void initialize() {
        loadNavbar();
        setupBackground();
        loadMahasiswaData(); 
        loadDosenPembimbing();
        loadJudulSkripsi();   
        loadBimbinganMahasiswa();
        loadTotalPertemuan();
        loadJumlahPertemuanDosen1();
        loadJumlahPertemuanDosen2();
        
        // set action untuk kembali
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event -> openDashboardPage());
        }
    }

    // ================= NAVBAR =================
    private void loadNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/Navbar.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ================= BACKGROUND =================
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    }


    // ================= DATA MAHASISWA =================
    private void loadMahasiswaData() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        String sql =
            "SELECT m.nama, m.nim, m.no_telepon, u.email, m.foto " +
            "FROM mahasiswa m JOIN users u ON m.id_user = u.id_user " +
            "WHERE m.id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaMahasiswa.setText(rs.getString("nama"));
                txtNIMMahasiswa.setText(rs.getString("nim"));
                txtNoTelpMahasiswa.setText(rs.getString("no_telepon"));
                txtEmailMahasiswa.setText(rs.getString("email"));

                byte[] foto = rs.getBytes("foto");

                if (foto != null && foto.length > 0) {
                    Image img = new Image(new ByteArrayInputStream(foto));
                    ivMahasiswa.setImage(img);
                    makeCircular(ivMahasiswa);
                } else {
                    Image defaultImg = new Image(
                        getClass().getResourceAsStream("/images/fotoMahasiswa/default.png")
                    );
                    ivMahasiswa.setImage(defaultImg);
                    makeCircular(ivMahasiswa);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ================= Gambar Bulat =================
    private void makeCircular(ImageView iv) {
        iv.setPreserveRatio(true);

        Circle clip = new Circle();

        clip.centerXProperty().bind(iv.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(iv.fitHeightProperty().divide(2));

        clip.radiusProperty().bind(
            Bindings.when(iv.fitWidthProperty().lessThan(iv.fitHeightProperty()))
                .then(iv.fitWidthProperty().divide(2))
                .otherwise(iv.fitHeightProperty().divide(2))
        );

        iv.setClip(clip);
    }



    
    // ================= DOSEN PEMBIMBING =================
    private void loadDosenPembimbing() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        // default: sembunyikan semua
        txtDosenPembimbing1.setVisible(false);
        txtNamaDosenPembimbing1.setVisible(false);
        txtProdiDosenPembimbing1.setVisible(false);
        ivDosen1.setVisible(false);

        txtDosenPembimbing2.setVisible(false);
        txtNamaDosenPembimbing2.setVisible(false);
        txtProdiDosenPembimbing2.setVisible(false);
        ivDosen2.setVisible(false);

        String sql =
            "SELECT dp.jenis_dosen, d.nama, d.prodi, d.foto " +
            "FROM DosenPembimbing dp " +
            "JOIN dosen d ON dp.id_dosen = d.id " +
            "WHERE dp.id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String jenis = rs.getString("jenis_dosen");
                String nama = rs.getString("nama");
                String prodi = rs.getString("prodi");
                byte[] foto = rs.getBytes("foto");

                if ("1".equals(jenis)) {
                    txtNamaDosenPembimbing1.setText(nama);
                    txtProdiDosenPembimbing1.setText(prodi);

                    txtDosenPembimbing1.setVisible(true);
                    txtNamaDosenPembimbing1.setVisible(true);
                    txtProdiDosenPembimbing1.setVisible(true);
                    ivDosen1.setVisible(true);

                    if (foto != null && foto.length > 0) {
                        Image img = new Image(new ByteArrayInputStream(foto));
                        ivDosen1.setImage(img);
                        makeCircular(ivDosen1);
                    }
                }

                if ("2".equals(jenis)) {
                    txtNamaDosenPembimbing2.setText(nama);
                    txtProdiDosenPembimbing2.setText(prodi);

                    txtDosenPembimbing2.setVisible(true);
                    txtNamaDosenPembimbing2.setVisible(true);
                    txtProdiDosenPembimbing2.setVisible(true);
                    ivDosen2.setVisible(true);

                    if (foto != null && foto.length > 0) {
                        Image img = new Image(new ByteArrayInputStream(foto));
                        ivDosen2.setImage(img);
                        makeCircular(ivDosen2);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    // ================= JUDUL SKRIPSI =================
    private void loadJudulSkripsi() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT judul_skripsi " +
            "FROM skripsi " +
            "WHERE id_mahasiswa = ? " +
            "ORDER BY created_at DESC " +
            "LIMIT 1";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String judul = rs.getString("judul_skripsi");

                if (judul != null && !judul.isEmpty()) {
                    txtJudulSkripsi.setText(judul);
                } else {
                    txtJudulSkripsi.setText("Judul skripsi belum ditentukan");
                }
            } else {
                txtJudulSkripsi.setText("Belum memiliki skripsi");
            }

        } catch (Exception e) {
            e.printStackTrace();
            txtJudulSkripsi.setText("-");
        }
    }

    // ================= KALENDER AKADEMIK =================
    private void loadBimbinganMahasiswa() {
        boolean adaBimbingan = false;
        int maxPertemuan = 0; // 🔹 untuk progress bar

        Text[] statusTexts = {
            txtStatusBimbinganPertemuan1, txtStatusBimbinganPertemuan2,
            txtStatusBimbinganPertemuan3, txtStatusBimbinganPertemuan4,
            txtStatusBimbinganPertemuan5, txtStatusBimbinganPertemuan6,
            txtStatusBimbinganPertemuan7, txtStatusBimbinganPertemuan8,
            txtStatusBimbinganPertemuan9, txtStatusBimbinganPertemuan10,
            txtStatusBimbinganPertemuan11, txtStatusBimbinganPertemuan12
        };

        Text[] judulTexts = {
            txtJudulBimbinganPertemuan1, txtJudulBimbinganPertemuan2,
            txtJudulBimbinganPertemuan3, txtJudulBimbinganPertemuan4,
            txtJudulBimbinganPertemuan5, txtJudulBimbinganPertemuan6,
            txtJudulBimbinganPertemuan7, txtJudulBimbinganPertemuan8,
            txtJudulBimbinganPertemuan9, txtJudulBimbinganPertemuan10,
            txtJudulBimbinganPertemuan11, txtJudulBimbinganPertemuan12
        };

        Text[] tanggalTexts = {
            txtTanggalBimbinganPertemuan1, txtTanggalBimbinganPertemuan2,
            txtTanggalBimbinganPertemuan3, txtTanggalBimbinganPertemuan4,
            txtTanggalBimbinganPertemuan5, txtTanggalBimbinganPertemuan6,
            txtTanggalBimbinganPertemuan7, txtTanggalBimbinganPertemuan8,
            txtTanggalBimbinganPertemuan9, txtTanggalBimbinganPertemuan10,
            txtTanggalBimbinganPertemuan11, txtTanggalBimbinganPertemuan12
        };

        Circle[] circleDots = {
            circleDot1, circleDot2, circleDot3, circleDot4,
            circleDot5, circleDot6, circleDot7, circleDot8,
            circleDot9, circleDot10, circleDot11, circleDot12
        };

        for (int i = 0; i < 12; i++) {
            statusTexts[i].setVisible(false);
            judulTexts[i].setVisible(false);
            tanggalTexts[i].setVisible(false);
        }

        for (Circle c : circleDots) {
            c.setVisible(false);
        }

        pbPertemuanBimbingan.setProgress(0); // default jika belum ada data

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT pertemuan_ke, status, judul, tanggal " +
            "FROM bimbingan " +
            "WHERE id_mahasiswa = ? AND pertemuan_ke BETWEEN 1 AND 12 " +
            "ORDER BY pertemuan_ke ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                adaBimbingan = true;

                int pertemuan = rs.getInt("pertemuan_ke");
                String status = rs.getString("status");

                if (pertemuan >= 1 && pertemuan <= 12) {
                    int idx = pertemuan - 1;

                    statusTexts[idx].setText(status);
                    judulTexts[idx].setText(rs.getString("judul"));
                    tanggalTexts[idx].setText(rs.getString("tanggal"));

                    statusTexts[idx].setVisible(true);
                    judulTexts[idx].setVisible(true);
                    tanggalTexts[idx].setVisible(true);

                    Circle dot = circleDots[idx];
                    dot.setVisible(true);

                    switch (status.toUpperCase()) {
                        case "MENDATANG":
                            dot.setFill(Color.RED);
                            break;
                        case "PROSES":
                            dot.setFill(Color.GOLD);
                            break;
                        case "SELESAI":
                            dot.setFill(Color.GREEN);
                            break;
                        default:
                            dot.setFill(Color.GRAY);
                    }

                    if (pertemuan > maxPertemuan) {
                        maxPertemuan = pertemuan;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pbPertemuanBimbingan.setProgress(maxPertemuan / 12.0);

        txtBimbingan.setVisible(!adaBimbingan);
    }
    
    // ================= TOTAL PERTEMUAN =================
    private void loadTotalPertemuan() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        String sql = "SELECT MAX(pertemuan_ke) AS total FROM bimbingan WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total"); // kalau null → otomatis 0
                txtTotalJumlahPertemuan.setText(String.valueOf(total));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ================= JUMLAH BIMBINGAN DOSEN PEMBIMBING 1 =================
    private void loadJumlahPertemuanDosen1() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT COUNT(*) AS total " +
            "FROM bimbingan b " +
            "JOIN DosenPembimbing dp " +
            "  ON dp.id_dosen = b.id_dosen " +
            " AND dp.id_mahasiswa = b.id_mahasiswa " +
            "WHERE b.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = '1'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            int total = 0;

            if (rs.next()) {
                total = rs.getInt("total");
            }

            txtJumlahPertemuanDosen1.setText(String.valueOf(total));

            // PROGRESS BAR (maks 6)
            double progress = Math.min(total / 6.0, 1.0);
            progressPembimbing1.setProgress(progress);

        } catch (Exception e) {
            e.printStackTrace();
            txtJumlahPertemuanDosen1.setText("0");
            progressPembimbing1.setProgress(0);
        }
    }


    // ================= JUMLAH BIMBINGAN DOSEN PEMBIMBING 2 =================
    private void loadJumlahPertemuanDosen2() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT COUNT(*) AS total " +
            "FROM bimbingan b " +
            "JOIN DosenPembimbing dp " +
            "  ON dp.id_dosen = b.id_dosen " +
            " AND dp.id_mahasiswa = b.id_mahasiswa " +
            "WHERE b.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = '2'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            int total = 0;

            if (rs.next()) {
                total = rs.getInt("total");
            }

            txtJumlahPertemuanDosen2.setText(String.valueOf(total));

            // 🔥 PROGRESS BAR
            double progress = Math.min(total / 6.0, 1.0);
            progressPembimbing2.setProgress(progress);

        } catch (Exception e) {
            e.printStackTrace();
            txtJumlahPertemuanDosen2.setText("0");
            progressPembimbing2.setProgress(0);
        }
    }








    
    /** Buka Halaman Dasboard **/
    private void openDashboardPage() {
        loadScene("/app/fxml/DashboardMahasiswaView.fxml", kembaliLink);
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
    
    @FXML 
    private void showEditProfilMahasiswaModal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/EditProfilMahasiswaModal.fxml")
            );
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UNDECORATED); // optional
            modalStage.setResizable(false);

            // ambil stage parent dari button
            Stage parentStage = (Stage) btnEditProfil.getScene().getWindow();
            modalStage.initOwner(parentStage);

            modalStage.showAndWait(); // MODAL (blok halaman belakang)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
