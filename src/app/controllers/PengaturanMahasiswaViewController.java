package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;

import app.session.MahasiswaSession;
import app.dao.MahasiswaProfilDAO;
import app.database.DBConnect;
import app.models.MahasiswaProfil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;

public class PengaturanMahasiswaViewController {

    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private AnchorPane navbarContainer;

    // Informasi Akun
    @FXML private TextField tfNama;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNim;
    @FXML private TextField tfNoTelp;

    private boolean isEdit = false;
    
    // Gani Kata Sandi
    @FXML private TextField tfSandi;
    
    
    // Kembali
    @FXML private Hyperlink kembaliLink;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/Navbar.fxml"));
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
    }

    private void setupBackground() {
        bgImage.fitWidthProperty().bind(root.widthProperty());
        bgImage.fitHeightProperty().bind(root.heightProperty());
    }


    // Logout
    @FXML
    private void handleLogout() {
        MahasiswaSession.clear();

        try {
            Stage stage = (Stage) root.getScene().getWindow();
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/app/fxml/LoginView.fxml"))
            );
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Buka Halaman Dasboard 
    private void openDashboardPage() {
        loadScene("/app/fxml/DashboardMahasiswaView.fxml", kembaliLink);
    }

    // Metode loadScene
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
    
    // Informasi Akun
    @FXML
    private void SimpanProfil() {

        String nama = tfNama.getText().trim();
        String nim = tfNim.getText().trim();
        String noTelp = tfNoTelp.getText().trim();
        String email = tfEmail.getText().trim();

        // Minimal satu field harus diisi
        if (nama.isEmpty() && nim.isEmpty() && noTelp.isEmpty() && email.isEmpty()) {
            showAlert(
                Alert.AlertType.WARNING,
                "Validasi",
                "Minimal satu field profil harus diisi"
            );
            return;
        }

        String sqlGetUserId =
            "SELECT id_user FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection()) {

            // ================= UPDATE MAHASISWA (DINAMIS) =================
            StringBuilder sqlMahasiswa = new StringBuilder(
                "UPDATE mahasiswa SET "
            );

            boolean first = true;

            if (!nama.isEmpty()) {
                sqlMahasiswa.append("nama = ?");
                first = false;
            }

            if (!nim.isEmpty()) {
                if (!first) sqlMahasiswa.append(", ");
                sqlMahasiswa.append("nim = ?");
                first = false;
            }

            if (!noTelp.isEmpty()) {
                if (!first) sqlMahasiswa.append(", ");
                sqlMahasiswa.append("no_telepon = ?");
            }

            sqlMahasiswa.append(" WHERE id_mahasiswa = ?");

            try (PreparedStatement ps =
                         conn.prepareStatement(sqlMahasiswa.toString())) {

                int index = 1;

                if (!nama.isEmpty()) ps.setString(index++, nama);
                if (!nim.isEmpty()) ps.setString(index++, nim);
                if (!noTelp.isEmpty()) ps.setString(index++, noTelp);

                ps.setInt(index, MahasiswaSession.getIdMahasiswa());
                ps.executeUpdate();
            }

            // ================= UPDATE EMAIL (JIKA DIISI) =================
            if (!email.isEmpty()) {

                int idUser = -1;

                try (PreparedStatement psGet =
                             conn.prepareStatement(sqlGetUserId)) {

                    psGet.setInt(1, MahasiswaSession.getIdMahasiswa());
                    ResultSet rs = psGet.executeQuery();

                    if (rs.next()) {
                        idUser = rs.getInt("id_user");
                    }
                }

                if (idUser != -1) {
                    try (PreparedStatement psUser =
                                 conn.prepareStatement(
                                     "UPDATE users SET email = ? WHERE id_user = ?"
                                 )) {

                        psUser.setString(1, email);
                        psUser.setInt(2, idUser);
                        psUser.executeUpdate();
                    }
                }
            }

            showAlert(
                Alert.AlertType.INFORMATION,
                "Berhasil",
                "Anda berhasil mengubah informasi akun"
            );

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(
                Alert.AlertType.ERROR,
                "Error",
                "Terjadi kesalahan saat menyimpan profil"
            );
        }
    }


    
    // Toggle Button Aktifkan
    @FXML
    private void AktifkanFitur(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();

        if (toggle.isSelected()) {
            toggle.setStyle(
                "-fx-background-color: #5170ff; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 8;"
            );
        } else {
            toggle.setStyle(
                "-fx-background-color: #e0e0e0; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 8;"
            );
        }
    }
    
    // Button Fitur Notifikasi
    @FXML
    private void FiturBelumTersedia(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText("Fitur belum tersedia");

        alert.showAndWait();
    }
    
    // Ganti Kata Sandi
    @FXML
    private void GantiSandi() {
        String sandiBaru = tfSandi.getText();

        if (sandiBaru == null || sandiBaru.trim().isEmpty()) {
            showAlert(
                Alert.AlertType.WARNING,
                "Validasi",
                "Sandi tidak boleh kosong"
            );
            return;
        }

        String sqlGetUserId =
            "SELECT id_user FROM mahasiswa WHERE id_mahasiswa = ?";

        String sqlUpdatePassword =
            "UPDATE users SET password = ? WHERE id_user = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement psGet = conn.prepareStatement(sqlGetUserId)) {

            psGet.setInt(1, MahasiswaSession.getIdMahasiswa());
            ResultSet rs = psGet.executeQuery();

            if (rs.next()) {
                int idUser = rs.getInt("id_user");

                try (PreparedStatement psUpdate =
                             conn.prepareStatement(sqlUpdatePassword)) {

                    psUpdate.setString(1, sandiBaru);
                    psUpdate.setInt(2, idUser);
                    psUpdate.executeUpdate();

                    tfSandi.clear();

                    showAlert(
                        Alert.AlertType.INFORMATION,
                        "Berhasil",
                        "Anda berhasil mengubah sandi"
                    );
                }
            } else {
                showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Data user tidak ditemukan"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(
                Alert.AlertType.ERROR,
                "Error",
                "Terjadi kesalahan saat mengubah sandi"
            );
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    
}
