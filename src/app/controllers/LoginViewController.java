package app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import app.database.DBConnect;
import app.session.DosenSession;
import app.session.MahasiswaSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginViewController {

    @FXML private AnchorPane root;
    @FXML private ImageView imgBackground;
    @FXML private Hyperlink linkRegister;
    @FXML private TextField txtNama;

    @FXML private PasswordField txtsanditidaktampak;
    @FXML private TextField txtsanditampak;
    @FXML private ImageView eyesanditerbuka, eyesanditertutup;

    @FXML private Button btnLogin;
    @FXML private RadioButton rbLupakan, rbIngat;
    @FXML private Label txtlogingagal;

    @FXML
    public void initialize() {
        if (imgBackground != null && root != null) {
            imgBackground.fitWidthProperty().bind(root.widthProperty());
            imgBackground.fitHeightProperty().bind(root.heightProperty());
        }

        txtsanditidaktampak.setVisible(true);
        txtsanditampak.setVisible(false);
        eyesanditerbuka.setVisible(true);
        eyesanditertutup.setVisible(false);
        txtlogingagal.setVisible(false);

        eyesanditerbuka.setOnMouseClicked(e -> showPassword());
        eyesanditertutup.setOnMouseClicked(e -> hidePassword());

        linkRegister.setOnAction(e -> openRegisterPage());
        btnLogin.setOnAction(e -> handleLogin());

        rbLupakan.setOnAction(e -> handleRadio(rbLupakan, rbIngat));
        rbIngat.setOnAction(e -> handleRadio(rbIngat, rbLupakan));
    }

    private void showPassword() {
        txtsanditampak.setText(txtsanditidaktampak.getText());
        txtsanditidaktampak.setVisible(false);
        txtsanditampak.setVisible(true);
        eyesanditerbuka.setVisible(false);
        eyesanditertutup.setVisible(true);
    }

    private void hidePassword() {
        txtsanditidaktampak.setText(txtsanditampak.getText());
        txtsanditampak.setVisible(false);
        txtsanditidaktampak.setVisible(true);
        eyesanditerbuka.setVisible(true);
        eyesanditertutup.setVisible(false);
    }

    /** LOGIN MAHASISWA & DOSEN **/
    private void handleLogin() {
        String input = txtNama.getText();
        String password = txtsanditidaktampak.isVisible()
                ? txtsanditidaktampak.getText()
                : txtsanditampak.getText();

        if (input.isEmpty() || password.isEmpty()) {
            showError("Nama / Email dan kata sandi wajib diisi!");
            return;
        }

        String sql =
            "SELECT u.id_user, u.role, " +
            "m.id_mahasiswa, m.nama AS nama_mahasiswa, " +
            "d.id AS id_dosen, d.nama AS nama_dosen " +
            "FROM users u " +
            "LEFT JOIN mahasiswa m ON u.id_user = m.id_user " +
            "LEFT JOIN dosen d ON u.id_user = d.id_user " +
            "WHERE (u.email = ? OR m.nama = ? OR d.nama = ?) " +
            "AND u.password = ?";

        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, input);
            stmt.setString(2, input);
            stmt.setString(3, input);
            stmt.setString(4, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int idUser = rs.getInt("id_user");
                String role = rs.getString("role");

                if ("MAHASISWA".equalsIgnoreCase(role)) {

                    MahasiswaSession.setSession(
                        idUser,
                        rs.getInt("id_mahasiswa"),
                        rs.getString("nama_mahasiswa"),
                        role
                    );

                } else if ("DOSEN".equalsIgnoreCase(role)) {

                    DosenSession.setSession(
                        idUser,
                        rs.getInt("id_dosen"),
                        rs.getString("nama_dosen"),
                        role
                    );
                }

                openDashboard(role);

            } else {
                showError("Nama/Email atau kata sandi salah!");
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Terjadi kesalahan sistem!");
        }
    }

    private void showError(String message) {
        txtlogingagal.setText(message);
        txtlogingagal.setVisible(true);
    }

    private void handleRadio(RadioButton clicked, RadioButton other) {
        if (clicked.isSelected()) {
            other.setSelected(false);
        }
    }

    private void openRegisterPage() {
        loadScene("/app/fxml/RegisterView.fxml", linkRegister);
    }

    private void openDashboard(String role) {
        String path;

        switch (role.toLowerCase()) {
            case "mahasiswa":
                path = "/app/fxml/DashboardMahasiswaView.fxml";
                break;
            case "dosen":
                path = "/app/fxml/DashboardDosenView.fxml";
                break;
            case "admin":
                path = "/app/fxml/DashboardAdminView.fxml";
                break;
            default:
                showError("Role tidak dikenal!");
                return;
        }

        loadScene(path, root);
    }

    private void loadScene(String fxmlPath, javafx.scene.Node referenceNode) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) referenceNode.getScene().getWindow();
            stage.setScene(new Scene(view));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal membuka halaman!");
        }
    }
}
