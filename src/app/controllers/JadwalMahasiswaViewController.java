package app.controllers;

import app.dao.DosenPembimbingDAO;
import app.dao.BimbinganDAO;
import app.database.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import app.session.MahasiswaSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JadwalMahasiswaViewController {

    // ROOT container dan background
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Rectangle headerShape;

    // Navbar
    @FXML private AnchorPane navbarContainer;
    
    // Card
    @FXML private HBox cardBimbingan1, cardBimbingan2;
    
    // CARD 1
    @FXML private ImageView ivDosen1;
    @FXML private Text txtNamaDosen1, txtProdiDosen1;
    @FXML private Text txtJudulBimbinganPertemuan1Dosen1;
    @FXML private Text txtPertemuan1Dosen1, txtJamBimbinganPertemuan1Dosen1;

    // CARD 2
    @FXML private ImageView ivDosen2;
    @FXML private Text txtNamaDosen2, txtProdiDosen2;
    @FXML private Text txtJudulBimbinganPertemuan1Dosen2;
    @FXML private Text txtPertemuan1Dosen2, txtJamBimbinganPertemuan1Dosen2; 
    
    // Button
    @FXML private Button btnLihatJadwal;
    @FXML private Button btnRequestJadwal;
    
    // Kembali
    @FXML private Hyperlink kembaliLink;
    
    //Nama Mahasiswa dari Session Mahasiswa
    @FXML private Text txtNamaMahasiswa;
    
    // Card Revisi Bimbingan
    @FXML private VBox cardRevisi1, cardRevisi2, cardRevisi3, cardRevisi4,
                      cardRevisi5, cardRevisi6, cardRevisi7, cardRevisi8,
                      cardRevisi9, cardRevisi10, cardRevisi11, cardRevisi12;

    @FXML private Text txtRevisiPertemuan1, txtRevisiPertemuan2, txtRevisiPertemuan3,
                      txtRevisiPertemuan4, txtRevisiPertemuan5, txtRevisiPertemuan6,
                      txtRevisiPertemuan7, txtRevisiPertemuan8, txtRevisiPertemuan9,
                      txtRevisiPertemuan10, txtRevisiPertemuan11, txtRevisiPertemuan12;

    @FXML private Text txtRevisiBimbinganPertemuan1, txtRevisiBimbinganPertemuan2,
                      txtRevisiBimbinganPertemuan3, txtRevisiBimbinganPertemuan4,
                      txtRevisiBimbinganPertemuan5, txtRevisiBimbinganPertemuan6,
                      txtRevisiBimbinganPertemuan7, txtRevisiBimbinganPertemuan8,
                      txtRevisiBimbinganPertemuan9, txtRevisiBimbinganPertemuan10,
                      txtRevisiBimbinganPertemuan11, txtRevisiBimbinganPertemuan12;

    @FXML private Text txtDeadlineRevisiPertemuan1, txtDeadlineRevisiPertemuan2,
                      txtDeadlineRevisiPertemuan3, txtDeadlineRevisiPertemuan4,
                      txtDeadlineRevisiPertemuan5, txtDeadlineRevisiPertemuan6,
                      txtDeadlineRevisiPertemuan7, txtDeadlineRevisiPertemuan8,
                      txtDeadlineRevisiPertemuan9, txtDeadlineRevisiPertemuan10,
                      txtDeadlineRevisiPertemuan11, txtDeadlineRevisiPertemuan12;

    @FXML private Text txtNamaDosenPertemuan1, txtNamaDosenPertemuan2,
                      txtNamaDosenPertemuan3, txtNamaDosenPertemuan4,
                      txtNamaDosenPertemuan5, txtNamaDosenPertemuan6,
                      txtNamaDosenPertemuan7, txtNamaDosenPertemuan8,
                      txtNamaDosenPertemuan9, txtNamaDosenPertemuan10,
                      txtNamaDosenPertemuan11, txtNamaDosenPertemuan12;
    
    // Button Edit Revisi Bimbingan
    @FXML private Button btnEdit1, btnEdit2, btnEdit3, btnEdit4,
                    btnEdit5, btnEdit6, btnEdit7, btnEdit8,
                    btnEdit9, btnEdit10, btnEdit11, btnEdit12;
    
    // Button Catat Revisi Bimbingan
    @FXML private Button btnCatat;




    public void initialize() {
        // Setup Navbar
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/Navbar.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupBackground();
        
        // Ambil nama dari session
        String nama = MahasiswaSession.getNama();

        if (nama != null && !nama.isEmpty()) {
            txtNamaMahasiswa.setText(nama + "!");
        } else {
            txtNamaMahasiswa.setText("Mahasiswa");
        }
        
        loadJadwalBimbinganHariIni();
        loadRevisiMahasiswa();

        // set action untuk kembali
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event -> openDashboardPage());
        }
        
        
        
    }

    // BACKGROUND RESPONSIVE
    private void setupBackground() {
        if (bgImage != null && root != null && headerShape != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());

            headerShape.widthProperty().bind(root.widthProperty());
            headerShape.heightProperty().bind(root.heightProperty().multiply(0.2)); 
        }
    }
    
   private void loadJadwalBimbinganHariIni() {
        loadCardBimbinganPerDosen("1", cardBimbingan1,
                ivDosen1, txtNamaDosen1, txtProdiDosen1,
                txtJudulBimbinganPertemuan1Dosen1,
                txtPertemuan1Dosen1, txtJamBimbinganPertemuan1Dosen1);

        loadCardBimbinganPerDosen("2", cardBimbingan2,
                ivDosen2, txtNamaDosen2, txtProdiDosen2,
                txtJudulBimbinganPertemuan1Dosen2,
                txtPertemuan1Dosen2, txtJamBimbinganPertemuan1Dosen2);
    }

    private void loadCardBimbinganPerDosen(
            String jenisDosen,
            HBox card,
            ImageView ivDosen,
            Text txtNama,
            Text txtProdi,
            Text txtJudul,
            Text txtPertemuan,
            Text txtJam
    ) {
        card.setVisible(false); // default sembunyi

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        LocalDate today = LocalDate.now();

        String sql =
            "SELECT b.pertemuan_ke, b.judul, b.jam, " +
            "       d.nama, d.prodi, d.foto " +
            "FROM bimbingan b " +
            "JOIN DosenPembimbing dp ON dp.id_dosen = b.id_dosen " +
            "JOIN dosen d ON d.id = b.id_dosen " +
            "WHERE dp.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = ? " +
            "AND b.id_mahasiswa = ? " +
            "AND DATE(b.tanggal) = ? " +
            "LIMIT 1";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setString(2, jenisDosen);
            ps.setInt(3, idMahasiswa);
            ps.setDate(4, java.sql.Date.valueOf(today));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                card.setVisible(true);

                txtNama.setText(rs.getString("nama"));
                txtProdi.setText(rs.getString("prodi"));
                txtJudul.setText(rs.getString("judul"));
                txtPertemuan.setText("Pertemuan ke-" + rs.getInt("pertemuan_ke"));
                txtJam.setText(rs.getString("jam"));

                byte[] foto = rs.getBytes("foto");
                if (foto != null && foto.length > 0) {
                    ivDosen.setImage(new Image(new ByteArrayInputStream(foto)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLihatJadwal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalJadwalBimbinganMahasiswa.fxml")
            );

            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("Jadwal Bimbingan Mahasiswa");
            modalStage.setScene(new Scene(root));

            // 🔒 modal (tidak bisa klik halaman belakang)
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // opsional: owner (kalau mau)
            modalStage.initOwner(btnLihatJadwal.getScene().getWindow());

            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void RequestJadwalBimbingan() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalRequestJadwalBimbinganMahasiswa.fxml")
            );

            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Request Jadwal Bimbingan");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnRequestJadwal.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Card Revisi Bimbingan
    private VBox getCardRevisi(int i) {
        switch (i) {
            case 1: return cardRevisi1;
            case 2: return cardRevisi2;
            case 3: return cardRevisi3;
            case 4: return cardRevisi4;
            case 5: return cardRevisi5;
            case 6: return cardRevisi6;
            case 7: return cardRevisi7;
            case 8: return cardRevisi8;
            case 9: return cardRevisi9;
            case 10: return cardRevisi10;
            case 11: return cardRevisi11;
            case 12: return cardRevisi12;
            default: return null;
        }
    }

    private Text getTxtRevisi(int i) {
        switch (i) {
            case 1: return txtRevisiPertemuan1;
            case 2: return txtRevisiPertemuan2;
            case 3: return txtRevisiPertemuan3;
            case 4: return txtRevisiPertemuan4;
            case 5: return txtRevisiPertemuan5;
            case 6: return txtRevisiPertemuan6;
            case 7: return txtRevisiPertemuan7;
            case 8: return txtRevisiPertemuan8;
            case 9: return txtRevisiPertemuan9;
            case 10: return txtRevisiPertemuan10;
            case 11: return txtRevisiPertemuan11;
            case 12: return txtRevisiPertemuan12;
            default: return null;
        }
    }

    private Text getTxtRevisiBimbingan(int i) {
        switch (i) {
            case 1: return txtRevisiBimbinganPertemuan1;
            case 2: return txtRevisiBimbinganPertemuan2;
            case 3: return txtRevisiBimbinganPertemuan3;
            case 4: return txtRevisiBimbinganPertemuan4;
            case 5: return txtRevisiBimbinganPertemuan5;
            case 6: return txtRevisiBimbinganPertemuan6;
            case 7: return txtRevisiBimbinganPertemuan7;
            case 8: return txtRevisiBimbinganPertemuan8;
            case 9: return txtRevisiBimbinganPertemuan9;
            case 10: return txtRevisiBimbinganPertemuan10;
            case 11: return txtRevisiBimbinganPertemuan11;
            case 12: return txtRevisiBimbinganPertemuan12;
            default: return null;
        }
    }
    
    private Text getTxtDeadline(int i) {
        switch (i) {
            case 1: return txtDeadlineRevisiPertemuan1;
            case 2: return txtDeadlineRevisiPertemuan2;
            case 3: return txtDeadlineRevisiPertemuan3;
            case 4: return txtDeadlineRevisiPertemuan4;
            case 5: return txtDeadlineRevisiPertemuan5;
            case 6: return txtDeadlineRevisiPertemuan6;
            case 7: return txtDeadlineRevisiPertemuan7;
            case 8: return txtDeadlineRevisiPertemuan8;
            case 9: return txtDeadlineRevisiPertemuan9;
            case 10: return txtDeadlineRevisiPertemuan10;
            case 11: return txtDeadlineRevisiPertemuan11;
            case 12: return txtDeadlineRevisiPertemuan12;
            default: return null;
        }
    }
    
    private Text getTxtNamaDosen(int i) {
        switch (i) {
            case 1: return txtNamaDosenPertemuan1;
            case 2: return txtNamaDosenPertemuan2;
            case 3: return txtNamaDosenPertemuan3;
            case 4: return txtNamaDosenPertemuan4;
            case 5: return txtNamaDosenPertemuan5;
            case 6: return txtNamaDosenPertemuan6;
            case 7: return txtNamaDosenPertemuan7;
            case 8: return txtNamaDosenPertemuan8;
            case 9: return txtNamaDosenPertemuan9;
            case 10: return txtNamaDosenPertemuan10;
            case 11: return txtNamaDosenPertemuan11;
            case 12: return txtNamaDosenPertemuan12;
            default: return null;
        }
    }

    private void clearRevisiView() {
        for (int i = 1; i <= 12; i++) {
            VBox card = getCardRevisi(i);
            if (card != null) card.setVisible(false);
        }
    }
    
    private void loadRevisiMahasiswa() {

        clearRevisiView();

        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT b.id_bimbingan, b.revisi, b.pertemuan_ke, b.deadline_revisi, d.nama AS nama_dosen " +
            "FROM bimbingan b " +
            "JOIN dosen d ON d.id = b.id_dosen " +
            "WHERE b.id_mahasiswa = ? " +
            "AND b.revisi IS NOT NULL " +
            "ORDER BY b.pertemuan_ke ASC " +
            "LIMIT 12";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int pertemuan = rs.getInt("pertemuan_ke");
                if (pertemuan < 1 || pertemuan > 12) continue;
                
                int idBimbingan = rs.getInt("id_bimbingan");

                VBox card = getCardRevisi(pertemuan);
                if (card == null) continue;

                getTxtRevisiBimbingan(pertemuan).setText(rs.getString("revisi"));
                getTxtRevisi(pertemuan).setText("Pertemuan " + pertemuan);
                getTxtNamaDosen(pertemuan).setText(rs.getString("nama_dosen"));

                Date deadline = rs.getDate("deadline_revisi");
                getTxtDeadline(pertemuan)
                    .setText(deadline != null ? deadline.toString() : "-");
                
                Button btnEdit = getBtnEdit(pertemuan);
                if (btnEdit != null) {
                    btnEdit.setUserData(idBimbingan);
                    btnEdit.setVisible(true);
                    btnEdit.setManaged(true);
                }

                card.setVisible(true);
                card.setManaged(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Button Edit Revisi Bimbingan
    private Button getBtnEdit(int i) {
        switch (i) {
            case 1: return btnEdit1;
            case 2: return btnEdit2;
            case 3: return btnEdit3;
            case 4: return btnEdit4;
            case 5: return btnEdit5;
            case 6: return btnEdit6;
            case 7: return btnEdit7;
            case 8: return btnEdit8;
            case 9: return btnEdit9;
            case 10: return btnEdit10;
            case 11: return btnEdit11;
            case 12: return btnEdit12;
            default: return null;
        }
    }
    
    @FXML
    private void EditRevisi(javafx.event.ActionEvent event) {

        Button btn = (Button) event.getSource();
        Object data = btn.getUserData();

        if (data == null) {
            System.out.println("id_bimbingan belum ter-set");
            return;
        }

        int idBimbingan = (int) data;

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalEditRevisiMahasiswa.fxml")
            );

            Parent root = loader.load();

            // ambil controller modal
            ModalEditRevisiMahasiswaController controller =
                loader.getController();

            // kirim id_bimbingan
            controller.setIdBimbingan(idBimbingan);

            Stage stage = new Stage();
            stage.setTitle("Edit Revisi Bimbingan");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btn.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            // reload setelah edit
            loadRevisiMahasiswa();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button Catat Revisi Bimbingan
    @FXML
    private void CatatRevisi(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalCatatRevisiMahasiswa.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Catat Revisi Bimbingan");
            stage.setScene(new Scene(root));

            // modal → halaman belakang terkunci
            stage.initModality(Modality.APPLICATION_MODAL);

            // owner biar fokus ke halaman ini
            stage.initOwner(btnCatat.getScene().getWindow());

            stage.setResizable(false);
            stage.showAndWait();

            // OPTIONAL: reload revisi setelah modal ditutup
            loadRevisiMahasiswa();

        } catch (Exception e) {
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

     
     
     
     
}
