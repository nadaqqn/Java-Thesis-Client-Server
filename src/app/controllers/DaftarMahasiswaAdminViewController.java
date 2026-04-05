package app.controllers;

import app.database.DBConnect;
import java.io.ByteArrayInputStream;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DaftarMahasiswaAdminViewController {

    // CARD
    @FXML private VBox CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
                      CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
                      CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12;
    
    // IMAGE
    @FXML private ImageView ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
                          ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
                          ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12;


    // TEXT
    @FXML private Text txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4,
                      txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8,
                      txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12;

    @FXML private Text txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4,
                      txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8,
                      txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12;

    @FXML private Text txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4,
                      txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8,
                      txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12;

    // BUTTON
    @FXML private Button btnSemua, btnProses, btnSelesai;
    @FXML private Button btnKelola1, btnKelola2, btnKelola3, btnKelola4,
                    btnKelola5, btnKelola6, btnKelola7, btnKelola8,
                    btnKelola9, btnKelola10, btnKelola11, btnKelola12;
    @FXML private Button btnLihat1;
    @FXML private Button btnLihat2;
    @FXML private Button btnLihat3;
    @FXML private Button btnLihat4;
    @FXML private Button btnLihat5;
    @FXML private Button btnLihat6;
    @FXML private Button btnLihat7;
    @FXML private Button btnLihat8;
    @FXML private Button btnLihat9;
    @FXML private Button btnLihat10;
    @FXML private Button btnLihat11;
    @FXML private Button btnLihat12;

    // simpan id mahasiswa per card
    private int[] idMahasiswaList = new int[12];

    // PENANDA STATUS (ambil dari DB)
    private int[] maxPertemuan = new int[12];
    
    //PENANDA KELOLA MAHASISWA
    private int[] idMahasiswa = new int[12];
    private Button[] btnKelola;
    private Button[] btnLihat;
    
    // Image
    private ImageView[] ivMahasiswa;
    
    // Root
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;

    // Navbar
    @FXML private AnchorPane navbarContainer;
             
    // Kembali
    @FXML private Hyperlink kembaliLink;

    public void initialize() {
        // Setup Navbar
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarAdmin.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupBackground();
        // set action untuk kembali
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event -> openDashboardPage());
        }
        setButtonActive(btnSemua);
        loadMahasiswa("SEMUA");
        
        btnKelola = new Button[]{
            btnKelola1, btnKelola2, btnKelola3, btnKelola4,
            btnKelola5, btnKelola6, btnKelola7, btnKelola8,
            btnKelola9, btnKelola10, btnKelola11, btnKelola12
        };
        btnLihat = new Button[]{
            btnLihat1, btnLihat2, btnLihat3, btnLihat4,
            btnLihat5, btnLihat6, btnLihat7, btnLihat8,
            btnLihat9, btnLihat10, btnLihat11, btnLihat12
        };
        
        ivMahasiswa = new ImageView[]{
            ivMahasiswa1, ivMahasiswa2, ivMahasiswa3, ivMahasiswa4,
            ivMahasiswa5, ivMahasiswa6, ivMahasiswa7, ivMahasiswa8,
            ivMahasiswa9, ivMahasiswa10, ivMahasiswa11, ivMahasiswa12
        };

        
    }
    
    /* ================= BACKGROUND ================= */
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    } 
    
    /* ================= DASHBOARD ADMIN ================= */
    private void openDashboardPage() {
        loadScene("/app/fxml/DashboardAdminView.fxml", kembaliLink);
    }

    /* ================= LOAD SCENE ================= */
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

    /* ================= LOAD DATA ================= */
    private void loadMahasiswa(String mode) {

        VBox[] cards = {
            CardMahasiswa1, CardMahasiswa2, CardMahasiswa3, CardMahasiswa4,
            CardMahasiswa5, CardMahasiswa6, CardMahasiswa7, CardMahasiswa8,
            CardMahasiswa9, CardMahasiswa10, CardMahasiswa11, CardMahasiswa12
        };
        
        

        Text[] nama = {
            txtNamaMahasiswa1, txtNamaMahasiswa2, txtNamaMahasiswa3, txtNamaMahasiswa4,
            txtNamaMahasiswa5, txtNamaMahasiswa6, txtNamaMahasiswa7, txtNamaMahasiswa8,
            txtNamaMahasiswa9, txtNamaMahasiswa10, txtNamaMahasiswa11, txtNamaMahasiswa12
        };

        Text[] nim = {
            txtNimMahasiswa1, txtNimMahasiswa2, txtNimMahasiswa3, txtNimMahasiswa4,
            txtNimMahasiswa5, txtNimMahasiswa6, txtNimMahasiswa7, txtNimMahasiswa8,
            txtNimMahasiswa9, txtNimMahasiswa10, txtNimMahasiswa11, txtNimMahasiswa12
        };

        Text[] jurusan = {
            txtJurusanMahasiswa1, txtJurusanMahasiswa2, txtJurusanMahasiswa3, txtJurusanMahasiswa4,
            txtJurusanMahasiswa5, txtJurusanMahasiswa6, txtJurusanMahasiswa7, txtJurusanMahasiswa8,
            txtJurusanMahasiswa9, txtJurusanMahasiswa10, txtJurusanMahasiswa11, txtJurusanMahasiswa12
        };

        // reset
        for (VBox c : cards) {
            c.setVisible(false);
            c.setManaged(false);
        }

        String sql =
            "SELECT m.id_mahasiswa, m.nama, m.nim, m.jurusan, m.foto, " +
            "COALESCE(MAX(b.pertemuan_ke), 0) AS max_pertemuan " +
            "FROM mahasiswa m " +
            "LEFT JOIN bimbingan b ON b.id_mahasiswa = m.id_mahasiswa " +
            "GROUP BY m.id_mahasiswa, m.nama, m.nim, m.jurusan, m.foto " +
            "ORDER BY m.nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int i = 0;
            while (rs.next() && i < 12) {

                int pertemuan = rs.getInt("max_pertemuan");
                boolean tampil = false;

                switch (mode) {
                    case "SELESAI":
                        tampil = (pertemuan == 12);
                        break;
                    case "PROSES":
                        tampil = (pertemuan >= 1 && pertemuan < 12);
                        break;
                    default:
                        tampil = true;
                }

                if (tampil) {
                    cards[i].setVisible(true);
                    cards[i].setManaged(true);

                    nama[i].setText(rs.getString("nama"));
                    nim[i].setText(rs.getString("nim"));
                    jurusan[i].setText(rs.getString("jurusan"));
                    
                    idMahasiswa[i] = rs.getInt("id_mahasiswa");
                              
                    byte[] fotoBytes = rs.getBytes("foto");

                    Image defaultImage = new Image("/app/images/fotoMahasiswa/mahasiswa4.png");
                    if (fotoBytes != null && fotoBytes.length > 0) {
                        ivMahasiswa[i].setImage(new Image(new ByteArrayInputStream(fotoBytes)));
                    } else {
                        ivMahasiswa[i].setImage(defaultImage);
                    }


                    maxPertemuan[i] = pertemuan;
                    i++;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FILTER ================= */
    @FXML
    private void SemuaCard(ActionEvent event) {
        setButtonActive(btnSemua);
        setButtonInactive(btnProses);
        setButtonInactive(btnSelesai);
        loadMahasiswa("SEMUA");
    }

    @FXML
    private void ProsesBimbingan(ActionEvent event) {
        setButtonActive(btnProses);
        setButtonInactive(btnSemua);
        setButtonInactive(btnSelesai);
        loadMahasiswa("PROSES");
    }

    @FXML
    private void SelesaiBimbingan(ActionEvent event) {
        setButtonActive(btnSelesai);
        setButtonInactive(btnSemua);
        setButtonInactive(btnProses);
        loadMahasiswa("SELESAI");
    }

    /* ================= BUTTON STYLE ================= */
    private void setButtonActive(Button btn) {
        btn.setStyle("-fx-background-color:#e53935;-fx-text-fill:white;-fx-background-radius:8;");
    }

    private void setButtonInactive(Button btn) {
        btn.setStyle("-fx-background-color:#ebebeb;-fx-text-fill:#a8a8a8;-fx-background-radius:8;");
    }
    
    /* ================= KELOLA MAHASISWA ================= */
    @FXML
    private void KelolaMahasiswa(ActionEvent event) {
        Button btn = (Button) event.getSource();

        for (int i = 0; i < btnKelola.length; i++) {
            if (btnKelola[i] == btn) {
                bukaKelolaMahasiswa(i);
                break;
            }
        }
    }
    private void bukaKelolaMahasiswa(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalKelolaProfilMahasiswaAdminView.fxml")
            );
            Parent root = loader.load();

            ModalKelolaProfilMahasiswaAdminViewController controller =
                loader.getController();

            controller.setIdMahasiswa(idMahasiswa[index]);

            // ===== BUAT STAGE BARU (MODAL) =====
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnKelola[index].getScene().getWindow());

            Scene scene = new Scene(root, 423, 350);
            modalStage.setScene(scene);
            modalStage.setResizable(false);
            modalStage.setTitle("Kelola Profil Mahasiswa");

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= LIHAT MAHASISWA ================= */
    @FXML
    private void LihatMahasiswa(ActionEvent event) {
        Button btn = (Button) event.getSource();

        for (int i = 0; i < btnLihat.length; i++) {
            if (btnLihat[i] == btn) {
                bukaLihatMahasiswa(i);
                break;
            }
        }
    }

    private void bukaLihatMahasiswa(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalLihatMahasiswaAdminView.fxml")
            );
            Parent root = loader.load();

            ModalLihatMahasiswaAdminViewController controller =
                loader.getController();

            controller.setIdMahasiswa(idMahasiswa[index]);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnLihat[index].getScene().getWindow());

            Scene scene = new Scene(root);
            modalStage.setScene(scene);
            modalStage.setResizable(false);
            modalStage.setTitle("Detail Mahasiswa");

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
