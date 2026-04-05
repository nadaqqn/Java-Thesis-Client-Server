package app.controllers;

import app.database.DBConnect;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;

import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import app.dao.ActivityLogDAO;


public class RegisterViewController implements Initializable {

    @FXML private AnchorPane root;
    @FXML private ImageView imgBackground;

    @FXML private TextField txtnama, txtnim, txtjurusan, txtemail, txtno, txtangkatan;

    // Password Fields
    @FXML private PasswordField txtsanditidaktampak;
    @FXML private TextField txtsanditampak;
    @FXML private ImageView eyesanditerbuka, eyesanditertutup;

    // Konfirmasi Password
    @FXML private PasswordField txtkonfirmasisanditidaktampak;
    @FXML private TextField txtkonfirmasisanditampak;
    @FXML private ImageView eyekonfirmasisanditerbuka, eyekonfirmasisanditertutup;

    // Error messages
    @FXML private Text fmemail, fmnomor, fmsandi, fmkonfirmasisandi;

    // ComboBox Role
    @FXML private ComboBox<String> comboRole;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Tambahkan pilihan role
        comboRole.getItems().addAll("MAHASISWA", "DOSEN");

        // Background menyesuaikan ukuran layar
        if (imgBackground != null && root != null) {
            imgBackground.fitWidthProperty().bind(root.widthProperty());
            imgBackground.fitHeightProperty().bind(root.heightProperty());
        }

        // Hide error messages
        fmemail.setVisible(false);
        fmnomor.setVisible(false);
        fmsandi.setVisible(false);
        fmkonfirmasisandi.setVisible(false);

        // Default: hide visible-text password fields
        txtsanditampak.setVisible(false);
        txtkonfirmasisanditampak.setVisible(false);

        // Hide icon “tertutup” awal
        eyesanditertutup.setVisible(false);
        eyekonfirmasisanditertutup.setVisible(false);

        // Sync password textfields
        txtsanditidaktampak.textProperty().addListener((obs, oldV, newV) -> txtsanditampak.setText(newV));
        txtsanditampak.textProperty().addListener((obs, oldV, newV) -> txtsanditidaktampak.setText(newV));

        txtkonfirmasisanditidaktampak.textProperty().addListener((obs, oldV, newV) -> txtkonfirmasisanditampak.setText(newV));
        txtkonfirmasisanditampak.textProperty().addListener((obs, oldV, newV) -> txtkonfirmasisanditidaktampak.setText(newV));

        // Eye icon actions
        eyesanditerbuka.setOnMouseClicked(e -> showPassword());
        eyesanditertutup.setOnMouseClicked(e -> hidePassword());

        eyekonfirmasisanditerbuka.setOnMouseClicked(e -> showKonfirmasi());
        eyekonfirmasisanditertutup.setOnMouseClicked(e -> hideKonfirmasi());
    }

    // -------------------------
    // Password Toggle Functions
    // -------------------------
    private void showPassword() {
        txtsanditampak.setVisible(true);
        txtsanditidaktampak.setVisible(false);
        eyesanditerbuka.setVisible(false);
        eyesanditertutup.setVisible(true);
    }

    private void hidePassword() {
        txtsanditampak.setVisible(false);
        txtsanditidaktampak.setVisible(true);
        eyesanditerbuka.setVisible(true);
        eyesanditertutup.setVisible(false);
    }

    private void showKonfirmasi() {
        txtkonfirmasisanditampak.setVisible(true);
        txtkonfirmasisanditidaktampak.setVisible(false);
        eyekonfirmasisanditerbuka.setVisible(false);
        eyekonfirmasisanditertutup.setVisible(true);
    }

    private void hideKonfirmasi() {
        txtkonfirmasisanditampak.setVisible(false);
        txtkonfirmasisanditidaktampak.setVisible(true);
        eyekonfirmasisanditerbuka.setVisible(true);
        eyekonfirmasisanditertutup.setVisible(false);
    }

    // -------------------------
    // Handle Register
    // -------------------------
    @FXML
private void handleRegister() {

    // reset error message
    fmemail.setVisible(false);
    fmnomor.setVisible(false);
    fmsandi.setVisible(false);
    fmkonfirmasisandi.setVisible(false);

    String nama = txtnama.getText();
    String nim = txtnim.getText();
    String jurusan = txtjurusan.getText();
    String email = txtemail.getText();
    String no = txtno.getText();
    String angkatan = txtangkatan.getText();

    String sandi = txtsanditampak.isVisible() ? txtsanditampak.getText() : txtsanditidaktampak.getText();
    String ulang = txtkonfirmasisanditampak.isVisible() ? txtkonfirmasisanditampak.getText() : txtkonfirmasisanditidaktampak.getText();

    String role = comboRole.getValue();
    String username = nama; // username untuk log

    boolean valid = true;

    if (nama.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || email.isEmpty() ||
        no.isEmpty() || sandi.isEmpty() || ulang.isEmpty()) valid = false;

    if (role == null) valid = false;
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) valid = false;
    if (!no.matches("^\\+62\\s?\\d{2,3}-?\\d{3,4}-?\\d{3,4}$")) valid = false;
    if (sandi.length() < 6) valid = false;
    if (!sandi.equals(ulang)) valid = false;

    if (!valid) return;

    try (Connection conn = DBConnect.getConnection()) {

        conn.setAutoCommit(false);

        // =========================
        // 1. INSERT USERS
        // =========================
        String sqlUser = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement psUser = conn.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS);

        psUser.setString(1, username);
        psUser.setString(2, email);
        psUser.setString(3, sandi);
        psUser.setString(4, role);

        psUser.executeUpdate();

        int idUser = 0;
        try (ResultSet rs = psUser.getGeneratedKeys()) {
            if (rs.next()) idUser = rs.getInt(1);
        }
        psUser.close();

        // =========================
        // 2. INSERT ROLE DATA
        // =========================
        String logActivity = "";

        if ("MAHASISWA".equalsIgnoreCase(role)) {

            String sqlMhs = "INSERT INTO mahasiswa (id_user, nama, nim, jurusan, angkatan, no_telepon) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psMhs = conn.prepareStatement(sqlMhs);

            psMhs.setInt(1, idUser);
            psMhs.setString(2, nama);
            psMhs.setString(3, nim);
            psMhs.setString(4, jurusan);
            psMhs.setInt(5, Integer.parseInt(angkatan));
            psMhs.setString(6, no);

            psMhs.executeUpdate();
            psMhs.close();

            logActivity = username + " bergabung sebagai Mahasiswa";

        } else if ("DOSEN".equalsIgnoreCase(role)) {

            String sqlDosen = "INSERT INTO dosen (id_user, nama, nip, prodi, no_telp, umur, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psDosen = conn.prepareStatement(sqlDosen);

            psDosen.setInt(1, idUser);
            psDosen.setString(2, nama);
            psDosen.setString(3, nim);
            psDosen.setString(4, jurusan);
            psDosen.setString(5, no);
            psDosen.setInt(6, 0);
            psDosen.setString(7, email);

            psDosen.executeUpdate();
            psDosen.close();

            logActivity = username + " bergabung sebagai Dosen";
        }

        // =========================
        // 3. SAVE ACTIVITY LOG
        // =========================
        ActivityLogDAO.saveLog(
            idUser,
            username,
            role,
            logActivity
        );

        conn.commit();
        System.out.println("Registrasi berhasil + Activity Log tercatat!");
        openLoginPage();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // -------------------------
    // Open Login Page
    // -------------------------
    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/LoginView.fxml"));
            Parent loginRoot = loader.load();
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
