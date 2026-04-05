package app.controllers;

import app.database.DBConnect;
import app.session.DosenSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MahasiswaBimbinganDosenViewController {

    // ROOT container dan background
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    
    // Navbar
    @FXML private AnchorPane navbarContainer;
    
    // Card
    @FXML private VBox CardMahasiswa1;
    @FXML private VBox CardMahasiswa2;
    @FXML private VBox CardMahasiswa3;
    @FXML private VBox CardMahasiswa4;
    @FXML private VBox CardMahasiswa5;
    @FXML private VBox CardMahasiswa6;
    @FXML private VBox CardMahasiswa7;
    @FXML private VBox CardMahasiswa8;
    @FXML private VBox CardMahasiswa9;
    @FXML private VBox CardMahasiswa10;
    @FXML private VBox CardMahasiswa11;
    @FXML private VBox CardMahasiswa12;

    @FXML private Text txtNamaMahasiswa1;
    @FXML private Text txtNamaMahasiswa2;
    @FXML private Text txtNamaMahasiswa3;
    @FXML private Text txtNamaMahasiswa4;
    @FXML private Text txtNamaMahasiswa5;
    @FXML private Text txtNamaMahasiswa6;
    @FXML private Text txtNamaMahasiswa7;
    @FXML private Text txtNamaMahasiswa8;
    @FXML private Text txtNamaMahasiswa9;
    @FXML private Text txtNamaMahasiswa10;
    @FXML private Text txtNamaMahasiswa11;
    @FXML private Text txtNamaMahasiswa12;

    @FXML private Text txtNimMahasiswa1;
    @FXML private Text txtNimMahasiswa2;
    @FXML private Text txtNimMahasiswa3;
    @FXML private Text txtNimMahasiswa4;
    @FXML private Text txtNimMahasiswa5;
    @FXML private Text txtNimMahasiswa6;
    @FXML private Text txtNimMahasiswa7;
    @FXML private Text txtNimMahasiswa8;
    @FXML private Text txtNimMahasiswa9;
    @FXML private Text txtNimMahasiswa10;
    @FXML private Text txtNimMahasiswa11;
    @FXML private Text txtNimMahasiswa12;

    @FXML private Text txtJurusanMahasiswa1;
    @FXML private Text txtJurusanMahasiswa2;
    @FXML private Text txtJurusanMahasiswa3;
    @FXML private Text txtJurusanMahasiswa4;
    @FXML private Text txtJurusanMahasiswa5;
    @FXML private Text txtJurusanMahasiswa6;
    @FXML private Text txtJurusanMahasiswa7;
    @FXML private Text txtJurusanMahasiswa8;
    @FXML private Text txtJurusanMahasiswa9;
    @FXML private Text txtJurusanMahasiswa10;
    @FXML private Text txtJurusanMahasiswa11;
    @FXML private Text txtJurusanMahasiswa12;

    @FXML private Text txtTanggalBimbinganMahasiswa1;
    @FXML private Text txtTanggalBimbinganMahasiswa2;
    @FXML private Text txtTanggalBimbinganMahasiswa3;
    @FXML private Text txtTanggalBimbinganMahasiswa4;
    @FXML private Text txtTanggalBimbinganMahasiswa5;
    @FXML private Text txtTanggalBimbinganMahasiswa6;
    @FXML private Text txtTanggalBimbinganMahasiswa7;
    @FXML private Text txtTanggalBimbinganMahasiswa8;
    @FXML private Text txtTanggalBimbinganMahasiswa9;
    @FXML private Text txtTanggalBimbinganMahasiswa10;
    @FXML private Text txtTanggalBimbinganMahasiswa11;
    @FXML private Text txtTanggalBimbinganMahasiswa12;

    @FXML private Text txtJamBimbinganMahasiswa1;
    @FXML private Text txtJamBimbinganMahasiswa2;
    @FXML private Text txtJamBimbinganMahasiswa3;
    @FXML private Text txtJamBimbinganMahasiswa4;
    @FXML private Text txtJamBimbinganMahasiswa5;
    @FXML private Text txtJamBimbinganMahasiswa6;
    @FXML private Text txtJamBimbinganMahasiswa7;
    @FXML private Text txtJamBimbinganMahasiswa8;
    @FXML private Text txtJamBimbinganMahasiswa9;
    @FXML private Text txtJamBimbinganMahasiswa10;
    @FXML private Text txtJamBimbinganMahasiswa11;
    @FXML private Text txtJamBimbinganMahasiswa12;

    @FXML private Text txtJumlahPertemuanMahasiswa1;
    @FXML private Text txtJumlahPertemuanMahasiswa2;
    @FXML private Text txtJumlahPertemuanMahasiswa3;
    @FXML private Text txtJumlahPertemuanMahasiswa4;
    @FXML private Text txtJumlahPertemuanMahasiswa5;
    @FXML private Text txtJumlahPertemuanMahasiswa6;
    @FXML private Text txtJumlahPertemuanMahasiswa7;
    @FXML private Text txtJumlahPertemuanMahasiswa8;
    @FXML private Text txtJumlahPertemuanMahasiswa9;
    @FXML private Text txtJumlahPertemuanMahasiswa10;
    @FXML private Text txtJumlahPertemuanMahasiswa11;
    @FXML private Text txtJumlahPertemuanMahasiswa12;
    
    @FXML private ImageView ivMahasiswa1;
    @FXML private ImageView ivMahasiswa2;
    @FXML private ImageView ivMahasiswa3;
    @FXML private ImageView ivMahasiswa4;
    @FXML private ImageView ivMahasiswa5;
    @FXML private ImageView ivMahasiswa6;
    @FXML private ImageView ivMahasiswa7;
    @FXML private ImageView ivMahasiswa8;
    @FXML private ImageView ivMahasiswa9;
    @FXML private ImageView ivMahasiswa10;
    @FXML private ImageView ivMahasiswa11;
    @FXML private ImageView ivMahasiswa12;


    @FXML private ProgressBar pbBimbinganMahasiswa1;
    @FXML private ProgressBar pbBimbinganMahasiswa2;
    @FXML private ProgressBar pbBimbinganMahasiswa3;
    @FXML private ProgressBar pbBimbinganMahasiswa4;
    @FXML private ProgressBar pbBimbinganMahasiswa5;
    @FXML private ProgressBar pbBimbinganMahasiswa6;
    @FXML private ProgressBar pbBimbinganMahasiswa7;
    @FXML private ProgressBar pbBimbinganMahasiswa8;
    @FXML private ProgressBar pbBimbinganMahasiswa9;
    @FXML private ProgressBar pbBimbinganMahasiswa10;
    @FXML private ProgressBar pbBimbinganMahasiswa11;
    @FXML private ProgressBar pbBimbinganMahasiswa12;

    // Button
    @FXML private Button btnSemua;
    @FXML private Button btnProses;
    @FXML private Button btnSelesai;
    private int[] totalPertemuan = new int[12];




    public void initialize() {
     /* ================= NAVBAR ================= */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarDosen.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupBackground();
        loadMahasiswaBimbingan();
        setButtonActive(btnSemua);

        
    }

     /* ================= NACKGROUND ================= */
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    }
    
     /* ================= PESAN ================= */
    @FXML
    private void loadPesan(ActionEvent event) {
        loadPage(
            event,
            "/app/fxml/PesanDosenView.fxml",
            "Pesan Mahasiswa"
        );
    }

    /* ================= JADWAL ================= */
    @FXML
    private void loadJadwal(ActionEvent event) {
        loadPage(
            event,
            "/app/fxml/JadwalDosenView.fxml",
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
    
    /* ================= MAHASISWA BIMBINGAN ================= */
    private void loadMahasiswaBimbingan() {
        int idDosen = DosenSession.getIdDosen();
        for (int i = 0; i < totalPertemuan.length; i++) {
            totalPertemuan[i] = -1; // penanda tidak ada data
        }


        VBox[] cards = {
            CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
            CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
            CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12
        };
        
        ImageView[] foto = {
            ivMahasiswa1, ivMahasiswa2, ivMahasiswa3,
            ivMahasiswa4, ivMahasiswa5, ivMahasiswa6,
            ivMahasiswa7, ivMahasiswa8, ivMahasiswa9,
            ivMahasiswa10, ivMahasiswa11, ivMahasiswa12
        };


        Text[] nama = {
            txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3,
            txtNamaMahasiswa4, txtNamaMahasiswa5, txtNamaMahasiswa6,
            txtNamaMahasiswa7, txtNamaMahasiswa8, txtNamaMahasiswa9,
            txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12
        };

        Text[] nim = {
            txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3,
            txtNimMahasiswa4, txtNimMahasiswa5, txtNimMahasiswa6,
            txtNimMahasiswa7, txtNimMahasiswa8, txtNimMahasiswa9,
            txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12
        };

        Text[] jurusan = {
            txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3,
            txtJurusanMahasiswa4, txtJurusanMahasiswa5, txtJurusanMahasiswa6,
            txtJurusanMahasiswa7, txtJurusanMahasiswa8, txtJurusanMahasiswa9,
            txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12
        };

        Text[] tanggal = {
            txtTanggalBimbinganMahasiswa1, txtTanggalBimbinganMahasiswa2,
            txtTanggalBimbinganMahasiswa3, txtTanggalBimbinganMahasiswa4,
            txtTanggalBimbinganMahasiswa5, txtTanggalBimbinganMahasiswa6,
            txtTanggalBimbinganMahasiswa7, txtTanggalBimbinganMahasiswa8,
            txtTanggalBimbinganMahasiswa9, txtTanggalBimbinganMahasiswa10,
            txtTanggalBimbinganMahasiswa11, txtTanggalBimbinganMahasiswa12
        };

        Text[] jam = {
            txtJamBimbinganMahasiswa1, txtJamBimbinganMahasiswa2,
            txtJamBimbinganMahasiswa3, txtJamBimbinganMahasiswa4,
            txtJamBimbinganMahasiswa5, txtJamBimbinganMahasiswa6,
            txtJamBimbinganMahasiswa7, txtJamBimbinganMahasiswa8,
            txtJamBimbinganMahasiswa9, txtJamBimbinganMahasiswa10,
            txtJamBimbinganMahasiswa11, txtJamBimbinganMahasiswa12
        };

        Text[] jumlah = {
            txtJumlahPertemuanMahasiswa1, txtJumlahPertemuanMahasiswa2,
            txtJumlahPertemuanMahasiswa3, txtJumlahPertemuanMahasiswa4,
            txtJumlahPertemuanMahasiswa5, txtJumlahPertemuanMahasiswa6,
            txtJumlahPertemuanMahasiswa7, txtJumlahPertemuanMahasiswa8,
            txtJumlahPertemuanMahasiswa9, txtJumlahPertemuanMahasiswa10,
            txtJumlahPertemuanMahasiswa11, txtJumlahPertemuanMahasiswa12
        };

        ProgressBar[] pb = {
            pbBimbinganMahasiswa1, pbBimbinganMahasiswa2,
            pbBimbinganMahasiswa3, pbBimbinganMahasiswa4,
            pbBimbinganMahasiswa5, pbBimbinganMahasiswa6,
            pbBimbinganMahasiswa7, pbBimbinganMahasiswa8,
            pbBimbinganMahasiswa9, pbBimbinganMahasiswa10,
            pbBimbinganMahasiswa11, pbBimbinganMahasiswa12
        };

        // hide semua card
        for (VBox c : cards) c.setVisible(false);

        String sql =
            "SELECT m.id_mahasiswa, m.nama, m.nim, m.jurusan, m.foto, " +
            "       MAX(b.tanggal) AS tanggal, " +
            "       MAX(b.jam) AS jam, " +
            "       COUNT(b.id_bimbingan) AS total " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON m.id_mahasiswa = dp.id_mahasiswa " +
            "LEFT JOIN bimbingan b " +
            "  ON b.id_mahasiswa = m.id_mahasiswa AND b.id_dosen = dp.id_dosen " +
            "WHERE dp.id_dosen = ? " +
            "GROUP BY m.id_mahasiswa " +
            "ORDER BY m.nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            int index = 0;

            while (rs.next() && index < 12) {
                cards[index].setVisible(true);
                cards[index].setManaged(true);

                nama[index].setText(rs.getString("nama"));
                nim[index].setText(rs.getString("nim"));
                jurusan[index].setText(rs.getString("jurusan"));

                tanggal[index].setText(
                    rs.getDate("tanggal") != null
                        ? rs.getDate("tanggal").toString()
                        : "-"
                );

                jam[index].setText(
                    rs.getTime("jam") != null
                        ? rs.getTime("jam").toString()
                        : "-"
                );
                
                // ===== FOTO MAHASISWA (BLOB) =====
                byte[] fotoBlob = rs.getBytes("foto");

                if (fotoBlob != null && fotoBlob.length > 0) {
                    Image image = new Image(new java.io.ByteArrayInputStream(fotoBlob));
                    foto[index].setImage(image);
                } else {
                    // fallback default
                    foto[index].setImage(
                        new Image(getClass().getResource(
                            "/app/images/default-user.png"
                        ).toExternalForm())
                    );
                }


                int total = rs.getInt("total");
                jumlah[index].setText(String.valueOf(total));

                pb[index].setProgress(Math.min(total / 6.0, 1.0));
                totalPertemuan[index] = total;

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FILTER ================= */
    private int parseJumlah(Text txt) {
        if (txt == null || txt.getText() == null) return 0;

        try {
            return Integer.parseInt(txt.getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @FXML
    private void SemuaCard(ActionEvent event) {

        setButtonActive(btnSemua);
        setButtonInactive(btnProses);
        setButtonInactive(btnSelesai);

        VBox[] cards = {
            CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
            CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
            CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12
        };

        for (int i = 0; i < cards.length; i++) {
            if (totalPertemuan[i] >= 0) {
                cards[i].setVisible(true);
                cards[i].setManaged(true);
            } else {
                cards[i].setVisible(false);
                cards[i].setManaged(false);
            }
        }
    }



    @FXML
    private void SelesaiBimbingan(ActionEvent event) {

        setButtonActive(btnSelesai);
        setButtonInactive(btnSemua);
        setButtonInactive(btnProses);

        VBox[] cards = {
            CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
            CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
            CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12
        };

        for (int i = 0; i < cards.length; i++) {
            if (totalPertemuan[i] == 6) {
                cards[i].setVisible(true);
                cards[i].setManaged(true);
            } else {
                cards[i].setVisible(false);
                cards[i].setManaged(false);
            }
        }
    }

    @FXML
    private void ProsesBimbingan(ActionEvent event) {

        setButtonActive(btnProses);
        setButtonInactive(btnSemua);
        setButtonInactive(btnSelesai);

        VBox[] cards = {
            CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
            CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
            CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12
        };

        for (int i = 0; i < cards.length; i++) {
            if (totalPertemuan[i] > 0 && totalPertemuan[i] < 6) {
                cards[i].setVisible(true);
                cards[i].setManaged(true);
            } else {
                cards[i].setVisible(false);
                cards[i].setManaged(false);
            }
        }
    }



    private void setButtonActive(Button btn) {
        btn.setStyle(
            "-fx-background-color: #e53935;" +   // merah
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;"
        );
    }

    private void setButtonInactive(Button btn) {
        btn.setStyle(
            "-fx-background-color: #ebebeb;" +
            "-fx-text-fill: #a8a8a8;" +
            "-fx-background-radius: 8;"
        );
    }

    private void resetAllCards(VBox[] cards) {
      for (VBox c : cards) {
          c.setVisible(false);
          c.setManaged(false);
      }
  }



    

}
