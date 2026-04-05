package app.controllers;

import app.database.DBConnect;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DashboardAdminViewController {

    // ROOT container dan background
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Rectangle headerShape;
    
    // Navbar
     @FXML private AnchorPane navbarContainer;

     // Log
    @FXML private Text log1, log2, log3, log4, log5, log6, log7, log8, log9, log10, log11, log12;
    @FXML private Text jenis_log1, jenis_log2, jenis_log3, jenis_log4, jenis_log5, jenis_log6, jenis_log7, jenis_log8, jenis_log9, jenis_log10, jenis_log11, jenis_log12;

    
    // Statistik
    @FXML private Text txtTotalMahasiswa;
    @FXML private Text txtTotalDosen;
    @FXML private Text txtMahasiswaLulus;
    
    // Kuota Mahasiswa Bimbingan Dosen
    @FXML private VBox CardDosen1, CardDosen2, CardDosen3, CardDosen4, CardDosen5, CardDosen6,
                            CardDosen7, CardDosen8, CardDosen9, CardDosen10, CardDosen11, CardDosen12;

    @FXML private Text txtNamaDosen1, txtNamaDosen2, txtNamaDosen3, txtNamaDosen4, txtNamaDosen5, txtNamaDosen6,
                      txtNamaDosen7, txtNamaDosen8, txtNamaDosen9, txtNamaDosen10, txtNamaDosen11, txtNamaDosen12;

    @FXML private Text txtJumlahMahasiswaBimbinganDosen1, txtJumlahMahasiswaBimbinganDosen2,
                      txtJumlahMahasiswaBimbinganDosen3, txtJumlahMahasiswaBimbinganDosen4,
                      txtJumlahMahasiswaBimbinganDosen5, txtJumlahMahasiswaBimbinganDosen6,
                      txtJumlahMahasiswaBimbinganDosen7, txtJumlahMahasiswaBimbinganDosen8,
                      txtJumlahMahasiswaBimbinganDosen9, txtJumlahMahasiswaBimbinganDosen10,
                      txtJumlahMahasiswaBimbinganDosen11, txtJumlahMahasiswaBimbinganDosen12;

    @FXML private javafx.scene.control.ProgressBar pbDosen1, pbDosen2, pbDosen3, pbDosen4, pbDosen5, pbDosen6,
                                                    pbDosen7, pbDosen8, pbDosen9, pbDosen10, pbDosen11, pbDosen12;




    
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
        loadActivityLog();
        loadDashboardStats();
        loadDosenCards();

        
    }

    // Background
    private void setupBackground() {
        if (bgImage != null && root != null && headerShape != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());

            headerShape.widthProperty().bind(root.widthProperty());
            headerShape.heightProperty().bind(root.heightProperty().multiply(0.2)); 
        }
    }
    
     
    // DIPANGGIL DARI BUTTON (FXML)
    @FXML
    public void showModal(ActionEvent event) {
        openModal();
    }

    // LOGIC MODAL (INTI)
    private void openModal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalDosen.fxml")
            );

            Parent rootModal = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("Jadwal Dosen");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(root.getScene().getWindow());
            modalStage.setScene(new Scene(rootModal));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Log Activity
    private void loadActivityLog() {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT jenis_activity, activity " +
                 "FROM activity_log " +
                 "ORDER BY created_at DESC " +
                 "LIMIT 12"
             );
             ResultSet rs = ps.executeQuery()) {

            int index = 1;

            while (rs.next()) {
                String jenis = rs.getString("jenis_activity");
                String activity = rs.getString("activity");

                switch (index) {
                    case 1:
                        log1.setText(activity);
                        jenis_log1.setText(jenis);
                        break;
                    case 2:
                        log2.setText(activity);
                        jenis_log2.setText(jenis);
                        break;
                    case 3:
                        log3.setText(activity);
                        jenis_log3.setText(jenis);
                        break;
                    case 4:
                        log4.setText(activity);
                        jenis_log4.setText(jenis);
                        break;
                    case 5:
                        log5.setText(activity);
                        jenis_log5.setText(jenis);
                        break;
                    case 6:
                        log6.setText(activity);
                        jenis_log6.setText(jenis);
                        break;
                    case 7:
                        log7.setText(activity);
                        jenis_log7.setText(jenis);
                        break;
                    case 8:
                        log8.setText(activity);
                        jenis_log8.setText(jenis);
                        break;
                    case 9:
                        log9.setText(activity);
                        jenis_log9.setText(jenis);
                        break;
                    case 10:
                        log10.setText(activity);
                        jenis_log10.setText(jenis);
                        break;
                    case 11:
                        log11.setText(activity);
                        jenis_log11.setText(jenis);
                        break;
                    case 12:
                        log12.setText(activity);
                        jenis_log12.setText(jenis);
                        break;
                }
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // STATISTIK
    private void loadDashboardStats() {
        String sqlTotalMahasiswa = "SELECT COUNT(*) FROM mahasiswa";
        String sqlTotalDosen = "SELECT COUNT(*) FROM dosen";
        String sqlMahasiswaLulus =
            "SELECT COUNT(DISTINCT id_mahasiswa) " +
            "FROM bimbingan " +
            "WHERE status = 'SELESAI'";

        try (Connection conn = DBConnect.getConnection()) {

            // ================= TOTAL MAHASISWA =================
            try (PreparedStatement ps = conn.prepareStatement(sqlTotalMahasiswa);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    txtTotalMahasiswa.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // ================= TOTAL DOSEN =================
            try (PreparedStatement ps = conn.prepareStatement(sqlTotalDosen);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    txtTotalDosen.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // ================= MAHASISWA LULUS =================
            try (PreparedStatement ps = conn.prepareStatement(sqlMahasiswaLulus);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    txtMahasiswaLulus.setText(String.valueOf(rs.getInt(1)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kuota Mahasiswa Bimbingan Dosen
    private void loadDosenCards() {

        VBox[] cards = {
            CardDosen1, CardDosen2, CardDosen3, CardDosen4, CardDosen5, CardDosen6,
            CardDosen7, CardDosen8, CardDosen9, CardDosen10, CardDosen11, CardDosen12
        };

        Text[] namaDosen = {
            txtNamaDosen1, txtNamaDosen2, txtNamaDosen3, txtNamaDosen4, txtNamaDosen5, txtNamaDosen6,
            txtNamaDosen7, txtNamaDosen8, txtNamaDosen9, txtNamaDosen10, txtNamaDosen11, txtNamaDosen12
        };

        Text[] jumlahBimbingan = {
            txtJumlahMahasiswaBimbinganDosen1, txtJumlahMahasiswaBimbinganDosen2,
            txtJumlahMahasiswaBimbinganDosen3, txtJumlahMahasiswaBimbinganDosen4,
            txtJumlahMahasiswaBimbinganDosen5, txtJumlahMahasiswaBimbinganDosen6,
            txtJumlahMahasiswaBimbinganDosen7, txtJumlahMahasiswaBimbinganDosen8,
            txtJumlahMahasiswaBimbinganDosen9, txtJumlahMahasiswaBimbinganDosen10,
            txtJumlahMahasiswaBimbinganDosen11, txtJumlahMahasiswaBimbinganDosen12
        };

        javafx.scene.control.ProgressBar[] progressBars = {
            pbDosen1, pbDosen2, pbDosen3, pbDosen4, pbDosen5, pbDosen6,
            pbDosen7, pbDosen8, pbDosen9, pbDosen10, pbDosen11, pbDosen12
        };

        // hide semua card dulu
        for (VBox card : cards) {
            card.setVisible(false);
            card.setManaged(false);
        }

        String sqlDosen =
            "SELECT id, nama " +
            "FROM dosen " +
            "ORDER BY nama ASC " +
            "LIMIT 12";

        String sqlJumlahBimbingan =
            "SELECT COUNT(*) FROM dosenpembimbing WHERE id_dosen = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement psDosen = conn.prepareStatement(sqlDosen);
             ResultSet rs = psDosen.executeQuery()) {

            int index = 0;

            while (rs.next() && index < 12) {
                int idDosen = rs.getInt("id");
                String nama = rs.getString("nama");

                int jumlah = 0;
                try (PreparedStatement psCount = conn.prepareStatement(sqlJumlahBimbingan)) {
                    psCount.setInt(1, idDosen);
                    ResultSet rsCount = psCount.executeQuery();
                    if (rsCount.next()) {
                        jumlah = rsCount.getInt(1);
                    }
                }

                // set visible
                cards[index].setVisible(true);
                cards[index].setManaged(true);

                namaDosen[index].setText(nama);
                jumlahBimbingan[index].setText(jumlah + " Mahasiswa");

                double progress = Math.min(jumlah / 12.0, 1.0);
                progressBars[index].setProgress(progress);

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
