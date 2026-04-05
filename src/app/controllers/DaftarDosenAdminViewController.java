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

public class DaftarDosenAdminViewController {

    /* ================= CARD ================= */
    @FXML private VBox CardDosen1, CardDosen2, CardDosen3, CardDosen4,
                      CardDosen5, CardDosen6, CardDosen7, CardDosen8,
                      CardDosen9, CardDosen10, CardDosen11, CardDosen12;

    /* ================= IMAGE ================= */
    @FXML private ImageView ivDosen1, ivDosen2, ivDosen3, ivDosen4,
                            ivDosen5, ivDosen6, ivDosen7, ivDosen8,
                            ivDosen9, ivDosen10, ivDosen11, ivDosen12;

    /* ================= TEXT ================= */
    @FXML private Text txtNamaDosen1, txtNamaDosen2, txtNamaDosen3, txtNamaDosen4,
                      txtNamaDosen5, txtNamaDosen6, txtNamaDosen7, txtNamaDosen8,
                      txtNamaDosen9, txtNamaDosen10, txtNamaDosen11, txtNamaDosen12;

    @FXML private Text txtNipDosen1, txtNipDosen2, txtNipDosen3, txtNipDosen4,
                      txtNipDosen5, txtNipDosen6, txtNipDosen7, txtNipDosen8,
                      txtNipDosen9, txtNipDosen10, txtNipDosen11, txtNipDosen12;

    @FXML private Text txtProdiDosen1, txtProdiDosen2, txtProdiDosen3, txtProdiDosen4,
                      txtProdiDosen5, txtProdiDosen6, txtProdiDosen7, txtProdiDosen8,
                      txtProdiDosen9, txtProdiDosen10, txtProdiDosen11, txtProdiDosen12;
    
    /* ================= BUTTON ================= */
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
    private Button[] btnLihat;

    /* ================= SIMPAN ID DOSEN PER CARD ================= */
    private int[] idDosenList = new int[12];



    /* ================= ROOT ================= */
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;

    /* ================= NAVBAR ================= */
    @FXML private AnchorPane navbarContainer;

    /* ================= KEMBALI ================= */
    @FXML private Hyperlink kembaliLink;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarAdmin.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupBackground();

        if (kembaliLink != null) {
            kembaliLink.setOnAction(e -> openDashboardPage());
        }

       
        Button[] btnKelola = {
            btnKelola1, btnKelola2, btnKelola3, btnKelola4,
            btnKelola5, btnKelola6, btnKelola7, btnKelola8,
            btnKelola9, btnKelola10, btnKelola11, btnKelola12
        };
        
        btnLihat = new Button[]{
            btnLihat1, btnLihat2, btnLihat3, btnLihat4,
            btnLihat5, btnLihat6, btnLihat7, btnLihat8,
            btnLihat9, btnLihat10, btnLihat11, btnLihat12
        };
        
         loadDosen();
        

    }

    /* ================= BACKGROUND ================= */
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    }

    /* ================= DASHBOARD ================= */
    private void openDashboardPage() {
        loadScene("/app/fxml/DashboardAdminView.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
            kembaliLink.getScene().setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= LOAD DOSEN ================= */
    private void loadDosen() {

        VBox[] cards = {
            CardDosen1, CardDosen2, CardDosen3, CardDosen4,
            CardDosen5, CardDosen6, CardDosen7, CardDosen8,
            CardDosen9, CardDosen10, CardDosen11, CardDosen12
        };
        
        Button[] btnKelola = {
            btnKelola1, btnKelola2, btnKelola3, btnKelola4,
            btnKelola5, btnKelola6, btnKelola7, btnKelola8,
            btnKelola9, btnKelola10, btnKelola11, btnKelola12
        };

        ImageView[] foto = {
            ivDosen1, ivDosen2, ivDosen3, ivDosen4,
            ivDosen5, ivDosen6, ivDosen7, ivDosen8,
            ivDosen9, ivDosen10, ivDosen11, ivDosen12
        };

        Text[] nama = {
            txtNamaDosen1, txtNamaDosen2, txtNamaDosen3, txtNamaDosen4,
            txtNamaDosen5, txtNamaDosen6, txtNamaDosen7, txtNamaDosen8,
            txtNamaDosen9, txtNamaDosen10, txtNamaDosen11, txtNamaDosen12
        };

        Text[] nip = {
            txtNipDosen1, txtNipDosen2, txtNipDosen3, txtNipDosen4,
            txtNipDosen5, txtNipDosen6, txtNipDosen7, txtNipDosen8,
            txtNipDosen9, txtNipDosen10, txtNipDosen11, txtNipDosen12
        };

        Text[] prodi = {
            txtProdiDosen1, txtProdiDosen2, txtProdiDosen3, txtProdiDosen4,
            txtProdiDosen5, txtProdiDosen6, txtProdiDosen7, txtProdiDosen8,
            txtProdiDosen9, txtProdiDosen10, txtProdiDosen11, txtProdiDosen12
        };

        // hide semua card
        for (VBox c : cards) {
            c.setVisible(false);
            c.setManaged(false);
        }

        String sql =
            "SELECT id, nama, nip, prodi, foto " +
            "FROM dosen " +
            "ORDER BY nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int i = 0;
            while (rs.next() && i < 12) {
                int idDosen = rs.getInt("id");
                idDosenList[i] = idDosen;

                cards[i].setVisible(true);
                cards[i].setManaged(true);

                nama[i].setText(rs.getString("nama"));
                nip[i].setText(rs.getString("nip"));
                prodi[i].setText(rs.getString("prodi"));

                byte[] fotoBlob = rs.getBytes("foto");
                if (fotoBlob != null && fotoBlob.length > 0) {
                    foto[i].setImage(new Image(new ByteArrayInputStream(fotoBlob)));
                }
                
                // ===== BUTTON KELOLA =====
                final int index = i;
                btnKelola[i].setOnAction(e -> openKelolaDosenModal(idDosenList[index]));

                btnLihat[i].setOnAction(e -> bukaLihatDosen(index));
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void KelolaDosen(ActionEvent event) {
        Object source = event.getSource();

        Button[] btnKelola = {
            btnKelola1, btnKelola2, btnKelola3, btnKelola4,
            btnKelola5, btnKelola6, btnKelola7, btnKelola8,
            btnKelola9, btnKelola10, btnKelola11, btnKelola12
        };

        for (int i = 0; i < btnKelola.length; i++) {
            if (source == btnKelola[i]) {
                int idDosen = idDosenList[i];
                openKelolaDosenModal(idDosen);
                break;
            }
        }
    }

    private void openKelolaDosenModal(int idDosen) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/app/fxml/ModalKelolaProfilDosenAdminView.fxml"
                )
            );

            Parent root = loader.load();

            ModalKelolaProfilDosenAdminViewController controller =
                    loader.getController();

            controller.setIdDosen(idDosen); // parsing ID

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Kelola Profil Dosen");
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void bukaLihatDosen(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalLihatDosenAdminView.fxml")
            );
            Parent root = loader.load();

            ModalLihatDosenAdminViewController controller =
                loader.getController();

            controller.setIdDosen (idDosenList[index]);

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
