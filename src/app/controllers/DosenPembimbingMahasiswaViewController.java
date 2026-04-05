package app.controllers;

import app.database.DBConnect;
import app.dao.DosenPembimbingDAO;
import app.session.MahasiswaSession;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DosenPembimbingMahasiswaViewController {

    // ================= ROOT =================
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;

    // Navbar
    @FXML private AnchorPane navbarContainer;

    // ================= DOSEN =================
    @FXML private Text txtNamaDosen1;
    @FXML private Text txtProdiDosen1;
    @FXML private ImageView ivDosen1;

    @FXML private Text txtNamaDosen2;
    @FXML private Text txtProdiDosen2;
    @FXML private ImageView ivDosen2;

    // ================= BIMBINGAN INFO =================
    @FXML private Text txtTanggalBimbinganPertemuanDosen1;
    @FXML private Text txtJamBimbinganPertemuanDosen1;
    @FXML private ProgressBar pbDosen1;
    @FXML private Text txtJumlahPertemuanDosen1;

    @FXML private Text txtTanggalBimbinganPertemuanDosen2;
    @FXML private Text txtJamBimbinganPertemuanDosen2;
    @FXML private ProgressBar pbDosen2;
    @FXML private Text txtJumlahPertemuanDosen2;
    
    // ================= CARD BIMBINGAN =================
    @FXML private VBox CardBimbingan1;
    @FXML private VBox CardBimbingan2;
    @FXML private VBox CardBimbingan3;
    @FXML private VBox CardBimbingan4;
    @FXML private VBox CardBimbingan5;
    @FXML private VBox CardBimbingan6;
    @FXML private VBox CardBimbingan7;
    @FXML private VBox CardBimbingan8;
    @FXML private VBox CardBimbingan9;
    @FXML private VBox CardBimbingan10;
    @FXML private VBox CardBimbingan11;
    @FXML private VBox CardBimbingan12;

    @FXML private Text txtJudulPertemuan1;
    @FXML private Text txtJudulPertemuan2;
    @FXML private Text txtJudulPertemuan3;
    @FXML private Text txtJudulPertemuan4;
    @FXML private Text txtJudulPertemuan5;
    @FXML private Text txtJudulPertemuan6;
    @FXML private Text txtJudulPertemuan7;
    @FXML private Text txtJudulPertemuan8;
    @FXML private Text txtJudulPertemuan9;
    @FXML private Text txtJudulPertemuan10;
    @FXML private Text txtJudulPertemuan11;
    @FXML private Text txtJudulPertemuan12;

    @FXML private Text txtBimbinganNamaDosenPertemuan1;
    @FXML private Text txtBimbinganNamaDosenPertemuan2;
    @FXML private Text txtBimbinganNamaDosenPertemuan3;
    @FXML private Text txtBimbinganNamaDosenPertemuan4;
    @FXML private Text txtBimbinganNamaDosenPertemuan5;
    @FXML private Text txtBimbinganNamaDosenPertemuan6;
    @FXML private Text txtBimbinganNamaDosenPertemuan7;
    @FXML private Text txtBimbinganNamaDosenPertemuan8;
    @FXML private Text txtBimbinganNamaDosenPertemuan9;
    @FXML private Text txtBimbinganNamaDosenPertemuan10;
    @FXML private Text txtBimbinganNamaDosenPertemuan11;
    @FXML private Text txtBimbinganNamaDosenPertemuan12;

    @FXML private Text txtPertemuan1;
    @FXML private Text txtPertemuan2;
    @FXML private Text txtPertemuan3;
    @FXML private Text txtPertemuan4;
    @FXML private Text txtPertemuan5;
    @FXML private Text txtPertemuan6;
    @FXML private Text txtPertemuan7;
    @FXML private Text txtPertemuan8;
    @FXML private Text txtPertemuan9;
    @FXML private Text txtPertemuan10;
    @FXML private Text txtPertemuan11;
    @FXML private Text txtPertemuan12;

    @FXML private Text txtTanggalBimbinganPertemuan1;
    @FXML private Text txtTanggalBimbinganPertemuan2;
    @FXML private Text txtTanggalBimbinganPertemuan3;
    @FXML private Text txtTanggalBimbinganPertemuan4;
    @FXML private Text txtTanggalBimbinganPertemuan5;
    @FXML private Text txtTanggalBimbinganPertemuan6;
    @FXML private Text txtTanggalBimbinganPertemuan7;
    @FXML private Text txtTanggalBimbinganPertemuan8;
    @FXML private Text txtTanggalBimbinganPertemuan9;
    @FXML private Text txtTanggalBimbinganPertemuan10;
    @FXML private Text txtTanggalBimbinganPertemuan11;
    @FXML private Text txtTanggalBimbinganPertemuan12;

    @FXML private Text txtCatatanPertemuan1;
    @FXML private Text txtCatatanPertemuan2;
    @FXML private Text txtCatatanPertemuan3;
    @FXML private Text txtCatatanPertemuan4;
    @FXML private Text txtCatatanPertemuan5;
    @FXML private Text txtCatatanPertemuan6;
    @FXML private Text txtCatatanPertemuan7;
    @FXML private Text txtCatatanPertemuan8;
    @FXML private Text txtCatatanPertemuan9;
    @FXML private Text txtCatatanPertemuan10;
    @FXML private Text txtCatatanPertemuan11;
    @FXML private Text txtCatatanPertemuan12;

    


    // ================= INIT =================
    @FXML
    public void initialize() {
        loadNavbar();
        setupBackground();
        loadDosenPembimbing();
        loadJumlahPertemuan();
        loadProgressDanJadwal();
        loadCardBimbingan();
    }

    private void loadNavbar() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/app/fxml/Navbar.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBackground() {
        bgImage.fitWidthProperty().bind(root.widthProperty());
        bgImage.fitHeightProperty().bind(root.heightProperty());
    }

    // ================= DOSEN PEMBIMBING =================
    private void loadDosenPembimbing() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        // default: sembunyikan semua
        txtNamaDosen1.setVisible(false);
        txtProdiDosen1.setVisible(false);
        ivDosen1.setVisible(false);

        txtNamaDosen2.setVisible(false);
        txtProdiDosen2.setVisible(false);
        ivDosen2.setVisible(false);

        String sql =
            "SELECT dp.jenis_dosen, d.nama, d.prodi, d.foto " +
            "FROM dosenpembimbing dp " +
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
                    txtNamaDosen1.setText(nama);
                    txtProdiDosen1.setText(prodi);

                    txtNamaDosen1.setVisible(true);
                    txtProdiDosen1.setVisible(true);
                    ivDosen1.setVisible(true);

                    if (foto != null && foto.length > 0) {
                        Image img = new Image(new ByteArrayInputStream(foto));
                        ivDosen1.setImage(img);
                        makeCircular(ivDosen1);
                    }
                }

                if ("2".equals(jenis)) {
                    txtNamaDosen2.setText(nama);
                    txtProdiDosen2.setText(prodi);

                    txtNamaDosen2.setVisible(true);
                    txtProdiDosen2.setVisible(true);
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



    // ================= LOAD JADWAL & PROGRESS =================
    private void loadProgressDanJadwal() {
        loadPerDosen(1,
            txtTanggalBimbinganPertemuanDosen1,
            txtJamBimbinganPertemuanDosen1,
            pbDosen1
        );

        loadPerDosen(2,
            txtTanggalBimbinganPertemuanDosen2,
            txtJamBimbinganPertemuanDosen2,
            pbDosen2
        );
    }

    private void loadPerDosen(
        int jenisDosen,
        Text txtTanggal,
        Text txtJam,
        ProgressBar pb
    ) {
        txtTanggal.setText("-");
        txtJam.setText("-");
        pb.setProgress(0);

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT MAX(b.tanggal) AS tanggal, " +
            "       MAX(b.jam) AS jam, " +
            "       COUNT(*) AS total " +
            "FROM bimbingan b " +
            "JOIN dosenpembimbing dp ON dp.id_dosen = b.id_dosen " +
            "WHERE b.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, jenisDosen);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getDate("tanggal") != null) {
                    txtTanggal.setText(rs.getDate("tanggal").toString());
                }

                if (rs.getTime("jam") != null) {
                    txtJam.setText(rs.getTime("jam").toString());
                }

                int total = rs.getInt("total");
                double progress = Math.min(total / 6.0, 1.0);
                pb.setProgress(progress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

       private void loadJumlahPertemuan() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        loadJumlahPertemuanByJenis(
            idMahasiswa,
            1,
            txtJumlahPertemuanDosen1
        );

        loadJumlahPertemuanByJenis(
            idMahasiswa,
            2,
            txtJumlahPertemuanDosen2
        );
    }

    private void loadJumlahPertemuanByJenis(
        int idMahasiswa,
        int jenisDosen,
        Text txtJumlah
    ) {

        String sql =
            "SELECT COUNT(*) AS total " +
            "FROM bimbingan b " +
            "JOIN dosenpembimbing dp " +
            "  ON dp.id_dosen = b.id_dosen " +
            " AND dp.id_mahasiswa = b.id_mahasiswa " +
            "WHERE b.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = ?";

        txtJumlah.setText("0");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, jenisDosen);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtJumlah.setText(String.valueOf(rs.getInt("total")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /* ================= PESAN ================= */
    @FXML
    private void loadPesan(ActionEvent event) {
        loadPage(
            event,
            "/app/fxml/PesanMahasiswaView.fxml",
            "Pesan Mahasiswa"
        );
    }

    /* ================= JADWAL ================= */
    @FXML
    private void loadJadwal(ActionEvent event) {
        loadPage(
            event,
            "/app/fxml/JadwalMahasiswaView.fxml",
            "Jadwal Mahasiswa"
        );
    }

    /* ================= REUSABLE PAGE LOADER ================= */
    private void loadPage(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(fxmlPath)
            );

            Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* ================= CARD ================= */
    private void loadCardBimbingan() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        VBox[] cards = {
            CardBimbingan1, CardBimbingan2, CardBimbingan3, CardBimbingan4,
            CardBimbingan5, CardBimbingan6, CardBimbingan7, CardBimbingan8,
            CardBimbingan9, CardBimbingan10, CardBimbingan11, CardBimbingan12
        };

        Text[] judul = {
            txtJudulPertemuan1, txtJudulPertemuan2, txtJudulPertemuan3,
            txtJudulPertemuan4, txtJudulPertemuan5, txtJudulPertemuan6,
            txtJudulPertemuan7, txtJudulPertemuan8, txtJudulPertemuan9,
            txtJudulPertemuan10, txtJudulPertemuan11, txtJudulPertemuan12
        };

        Text[] namaDosen = {
            txtBimbinganNamaDosenPertemuan1, txtBimbinganNamaDosenPertemuan2,
            txtBimbinganNamaDosenPertemuan3, txtBimbinganNamaDosenPertemuan4,
            txtBimbinganNamaDosenPertemuan5, txtBimbinganNamaDosenPertemuan6,
            txtBimbinganNamaDosenPertemuan7, txtBimbinganNamaDosenPertemuan8,
            txtBimbinganNamaDosenPertemuan9, txtBimbinganNamaDosenPertemuan10,
            txtBimbinganNamaDosenPertemuan11, txtBimbinganNamaDosenPertemuan12
        };

        Text[] pertemuan = {
            txtPertemuan1, txtPertemuan2, txtPertemuan3, txtPertemuan4,
            txtPertemuan5, txtPertemuan6, txtPertemuan7, txtPertemuan8,
            txtPertemuan9, txtPertemuan10, txtPertemuan11, txtPertemuan12
        };

        Text[] tanggal = {
            txtTanggalBimbinganPertemuan1, txtTanggalBimbinganPertemuan2,
            txtTanggalBimbinganPertemuan3, txtTanggalBimbinganPertemuan4,
            txtTanggalBimbinganPertemuan5, txtTanggalBimbinganPertemuan6,
            txtTanggalBimbinganPertemuan7, txtTanggalBimbinganPertemuan8,
            txtTanggalBimbinganPertemuan9, txtTanggalBimbinganPertemuan10,
            txtTanggalBimbinganPertemuan11, txtTanggalBimbinganPertemuan12
        };

        Text[] catatan = {
            txtCatatanPertemuan1, txtCatatanPertemuan2, txtCatatanPertemuan3,
            txtCatatanPertemuan4, txtCatatanPertemuan5, txtCatatanPertemuan6,
            txtCatatanPertemuan7, txtCatatanPertemuan8, txtCatatanPertemuan9,
            txtCatatanPertemuan10, txtCatatanPertemuan11, txtCatatanPertemuan12
        };

        // hide semua card dulu
        for (VBox card : cards) {
            card.setVisible(false);
        }

        String sql =
            "SELECT b.judul, b.pertemuan_ke, b.tanggal, b.catatan_bimbingan, d.nama " +
            "FROM bimbingan b " +
            "JOIN dosen d ON d.id = b.id_dosen " +
            "WHERE b.id_mahasiswa = ? " +
            "ORDER BY b.pertemuan_ke ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            int index = 0;

            while (rs.next() && index < 12) {
                cards[index].setVisible(true);

                judul[index].setText(rs.getString("judul"));
                namaDosen[index].setText(rs.getString("nama"));
                pertemuan[index].setText(
                    "Pertemuan ke-" + rs.getInt("pertemuan_ke")
                );
                tanggal[index].setText(
                    rs.getDate("tanggal").toString()
                );
                catatan[index].setText(
                    rs.getString("catatan_bimbingan") != null
                        ? rs.getString("catatan_bimbingan")
                        : "-"
                );

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
}
