package app.controllers;

import app.database.DBConnect;
import app.session.DosenSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ProfilDosenViewController {

    // ROOT container dan background
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    
    // Navbar
    @FXML private AnchorPane navbarContainer;
    
    // Kembali
    @FXML private Hyperlink kembaliLink;

    // Data Dosen
    @FXML private Text txtNamaDosen;
    @FXML private Text txtNipDosen;
    @FXML private Text txtFakultasDosen;
    @FXML private Text txtEmailDosen;
    @FXML private Text txtNoTelpDosen;
    @FXML private Text txtRuanganDosen;
    @FXML private Text txtJabatanDosen;
    @FXML private Text txtProdiDosen;
    @FXML private ImageView ivDosen;
    @FXML private Text txtKuotaDosen;
    @FXML private Text txtJumlahBimbinganDosen;
    
    // Pendidikan Dosem
    @FXML private Text txtS1Fakultas;
    @FXML private Text txtS2Fakultas;
    @FXML private Text txtS3Fakultas;

    @FXML private Text txtS1Universitas;
    @FXML private Text txtS2Universitas;
    @FXML private Text txtS3Universitas;
    
    @FXML private Text txtS1Tahun;
    @FXML private Text txtS2Tahun;
    @FXML private Text txtS3Tahun;

    // Edit Profil 
    @FXML private Button btnEditProfil;





    public void initialize() {
        setupBackground();
        // set action untuk kembali
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event -> openDashboardPage());
        }
        loadProfilDosen();
        loadPendidikanDosen();

    }

    // Background Responsive
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
        
        // Setup Navbar
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarDosen.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
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
    
    // =============================== PROFIL DOSEN ===============================
    private void loadProfilDosen() {

        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT nama, nip, jabatan, prodi, fakultas, email, no_telp, ruangan, kuota, jumlah_bimbingan, foto " +
            "FROM dosen WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaDosen.setText(rs.getString("nama"));
                txtNipDosen.setText(rs.getString("nip"));
                txtJabatanDosen.setText(rs.getString("jabatan"));
                txtProdiDosen.setText(rs.getString("prodi"));
                txtFakultasDosen.setText(rs.getString("fakultas"));
                txtEmailDosen.setText(rs.getString("email"));
                txtNoTelpDosen.setText(rs.getString("no_telp"));
                txtRuanganDosen.setText(rs.getString("ruangan"));
                txtKuotaDosen.setText(String.valueOf(rs.getInt("kuota")));
                txtJumlahBimbinganDosen.setText(String.valueOf(rs.getInt("jumlah_bimbingan")));
                
                byte[] fotoBytes = rs.getBytes("foto");

                if (fotoBytes != null && fotoBytes.length > 0) {
                    Image img = new Image(new ByteArrayInputStream(fotoBytes));
                    ivDosen.setImage(img);
                    makeCircular(ivDosen, 55);
                } else {
                    ivDosen.setImage(null); // atau default image
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();

            // fallback aman
            txtNamaDosen.setText("-");
            txtNipDosen.setText("-");
            txtJabatanDosen.setText("-");
            txtProdiDosen.setText("-");
            txtFakultasDosen.setText("-");
            txtEmailDosen.setText("-");
            txtNoTelpDosen.setText("-");
            txtRuanganDosen.setText("-");
            txtKuotaDosen.setText("0");
            txtJumlahBimbinganDosen.setText("0");

        }
    }
    
    private void makeCircular(ImageView iv, double r) {
        Circle clip = new Circle(r, r, r);
        iv.setClip(clip);
    }

    private void loadPendidikanDosen() {

        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT jenjang, fakultas, universitas, tahun " +
            "FROM pendidikan_dosen " +
            "WHERE id_dosen = ?";

        boolean hasS3 = false; // penanda apakah ada S3

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            // ===== DEFAULT =====
            txtS1Fakultas.setText("-");
            txtS1Universitas.setText("-");
            txtS1Tahun.setText("-");

            txtS2Fakultas.setText("-");
            txtS2Universitas.setText("-");
            txtS2Tahun.setText("-");

            txtS3Fakultas.setText("-");
            txtS3Universitas.setText("-");
            txtS3Tahun.setText("-");

            while (rs.next()) {
                String jenjang = rs.getString("jenjang");
                String fakultas = rs.getString("fakultas");
                String universitas = rs.getString("universitas");
                Integer tahun = rs.getObject("tahun", Integer.class);

                switch (jenjang) {
                    case "S1":
                        txtS1Fakultas.setText(fakultas != null ? fakultas : "-");
                        txtS1Universitas.setText(universitas != null ? universitas : "-");
                        txtS1Tahun.setText(tahun != null ? tahun.toString() : "-");
                        break;

                    case "S2":
                        txtS2Fakultas.setText(fakultas != null ? fakultas : "-");
                        txtS2Universitas.setText(universitas != null ? universitas : "-");
                        txtS2Tahun.setText(tahun != null ? tahun.toString() : "-");
                        break;

                    case "S3":
                        hasS3 = true;
                        txtS3Fakultas.setText(fakultas != null ? fakultas : "-");
                        txtS3Universitas.setText(universitas != null ? universitas : "-");
                        txtS3Tahun.setText(tahun != null ? tahun.toString() : "-");
                        break;
                }
            }

            // ===== ATUR VISIBILITAS S3 =====
            setS3Visibility(hasS3);

        } catch (SQLException e) {
            e.printStackTrace();
            setS3Visibility(false);
        }
    }
    
    private void setS3Visibility(boolean visible) {
        txtS3Fakultas.setVisible(visible);
        txtS3Universitas.setVisible(visible);
        txtS3Tahun.setVisible(visible);
    }

    // ===== EDIT PROFIL =====
    @FXML 
    private void showEditProfilDosenModal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/EditProfilDosenModal.fxml")
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
