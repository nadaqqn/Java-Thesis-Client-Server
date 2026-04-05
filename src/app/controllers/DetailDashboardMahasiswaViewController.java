package app.controllers;


import app.dao.DosenDAO;
import app.models.Dosen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import app.session.MahasiswaSession;
import app.dao.SkripsiDAO;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DetailDashboardMahasiswaViewController {

    // ================= ROOT & BACKGROUND =================
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Hyperlink kembaliLink;
    @FXML private Button btnBatal; 
    
    // ================= Navbar =================
    @FXML private AnchorPane navbarContainer;
    
    // ================= BUTTON CEK PROFIL =================
    @FXML private Button btnCekProfil;
    
    // ================= DATA SKRIPSI =================
    @FXML private TextField txtJudulSkripsi;
    @FXML private TextField txtMetodePenelitian;
    @FXML private TextField txtDeskripsi;
    @FXML private TextField txtTujuan;
    @FXML private Button btnAjukan;


    // ================= DATA DOSEN =================
    @FXML private Text txtNamaDosen;
    @FXML private Text txtProdiDosen;
    @FXML private Text txtNoTelpDosen;
    @FXML private Text txtEmailDosen;
    @FXML private ImageView txtFotoDosen;

    // ================= UPLOAD FILE =================
    @FXML private Button btnUpload;
    @FXML private Text txtFileName;

    private File selectedFile;

    // ================= DATA & DAO =================
    private int idDosen;
    private final DosenDAO dosenDAO = new DosenDAO();

    // ================= ERROR TEXT =================
    @FXML private Text txtFormBelumLengkap;
    @FXML private Text txtFileBelumDiupload;
    @FXML private Text txtSudahMemilikiDosenPembimbing;
    @FXML private Text txtSudahMengajukanSkripsi;
    @FXML private Text txtJudulSkripsiHarusSama;



    
    // ================= INITIALIZE =================
    @FXML
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
        setupBackNavigation();
        setupFileChooser();
        resetErrorText();
        btnAjukan.setOnAction(event -> handleAjukanSkripsi());
        // tombol Batal
        if (btnBatal != null) {
            btnBatal.setOnAction(event -> handleBatal());
        }
        // tombol Cek Profil
        if (btnCekProfil != null) {
            btnCekProfil.setOnAction(event -> showDosenModal());
        }
        
    }

    // ================= TERIMA PARAMETER DARI DASHBOARD =================
    public void setIdDosen(int idDosen) {
        this.idDosen = idDosen;
        loadDataDosen();
    }

    // ================= LOAD DATA DOSEN =================
    private void loadDataDosen() {
        System.out.println("Load data dosen dengan ID: " + idDosen);

        Dosen d = dosenDAO.getDosenById(idDosen);

        if (d != null) {
            txtNamaDosen.setText(d.getNama());
            txtProdiDosen.setText(d.getProdi());
            txtNoTelpDosen.setText(d.getNoTelp());
            txtEmailDosen.setText(d.getEmail());

            if (d.getFoto() != null) {
                Image img = new Image(new ByteArrayInputStream(d.getFoto()));
                txtFotoDosen.setImage(img);
            } else {
                txtFotoDosen.setImage(null);
            }
        } else {
            System.out.println("Data dosen tidak ditemukan!");
        }
    }

    // ================= BACKGROUND RESPONSIVE =================
    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    }

    // ================= NAVIGASI KEMBALI =================
    private void setupBackNavigation() {
        if (kembaliLink != null) {
            kembaliLink.setOnAction(event ->
                loadScene("/app/fxml/DashboardMahasiswaView.fxml")
            );
        }
    }
    
    
    // ================= MODAL DOSEN 
    @FXML
    private void showDosenModal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalDetailDosen.fxml")
            );

            Parent rootModal = loader.load();

            // ambil controller modal untuk kirim data dosen
            ModalDetailDosenController modalController = loader.getController();
            modalController.setDosenData(dosenDAO.getDosenById(idDosen));

            Stage modalStage = new Stage();
            modalStage.setTitle("Profil Dosen");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(root.getScene().getWindow());
            modalStage.setScene(new Scene(rootModal));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GENERIC LOAD SCENE =================
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent parent = loader.load();
            root.getScene().setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= FILE CHOOSER =================
    private void setupFileChooser() {
        if (btnUpload != null) {
            btnUpload.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Pilih Dokumen Proposal");

                FileChooser.ExtensionFilter pdfFilter =
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
                FileChooser.ExtensionFilter docFilter =
                        new FileChooser.ExtensionFilter("Word Files", "*.doc", "*.docx");
                FileChooser.ExtensionFilter txtFilter =
                        new FileChooser.ExtensionFilter("Text Files", "*.txt");

                fileChooser.getExtensionFilters().addAll(
                        pdfFilter, docFilter, txtFilter
                );

                File file = fileChooser.showOpenDialog(
                        root.getScene().getWindow()
                );

                if (file != null) {
                    selectedFile = file;
                    txtFileName.setText(file.getName());
                }
            });
        }
    }

    // ================= OPTIONAL GETTER =================
    public File getSelectedFile() {
        return selectedFile;
    }
    
    // ================= FORM SKRIPSI =================
    private void handleAjukanSkripsi() {
        resetErrorText();
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String judul = txtJudulSkripsi.getText();
        String metode = txtMetodePenelitian.getText();
        String deskripsi = txtDeskripsi.getText();
        String tujuan = txtTujuan.getText();

        // ================= FORM KOSONG =================
        if (judul.isEmpty() || metode.isEmpty() || deskripsi.isEmpty() || tujuan.isEmpty()) {
            txtFormBelumLengkap.setVisible(true);
            return;
        }

        // ================= FILE BELUM UPLOAD =================
        if (selectedFile == null) {
            txtFileBelumDiupload.setVisible(true);
            return;
        }

        try {
            SkripsiDAO skripsiDAO = new SkripsiDAO();

            // ================= CEK JUMLAH PENGAJUAN AKTIF =================
            int jumlahDiajukan = skripsiDAO.countByStatus(
                idMahasiswa,
                "DIAJUKAN"
            );

            if (jumlahDiajukan >= 2) {
                txtSudahMengajukanSkripsi.setText(
                    "Anda telah mengajukan skripsi kepada 2 dosen pembimbing.\n" +
                    "Silakan menunggu persetujuan salah satu dosen."
                );
                txtSudahMengajukanSkripsi.setVisible(true);
                btnAjukan.setDisable(true);
                return;
            }
            // ================= INSERT SKRIPSI =================
            byte[] fileBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());

            skripsiDAO.insertSkripsi(
                idMahasiswa,
                idDosen,
                judul,
                metode,
                deskripsi,
                tujuan,
                fileBytes
            );

            System.out.println("Pengajuan skripsi berhasil!");

            loadScene("/app/fxml/ProfilMahasiswaView.fxml");

        } catch (java.sql.SQLException e) {

            String msg = e.getMessage();
            if (msg.contains("ERR_2_DISETUJUI")) {
                txtSudahMemilikiDosenPembimbing.setVisible(true);
                btnAjukan.setDisable(true);

            } else if (msg.contains("ERR_2_AKTIF")) {
                txtSudahMengajukanSkripsi.setVisible(true);
                btnAjukan.setDisable(true);

            } else if (msg.contains("ERR_JUDUL_BEDA")) {
                txtJudulSkripsiHarusSama.setVisible(true);

            } else {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ================= BUTTON BATAL =================
    @FXML
    private void handleBatal() {
        loadScene("/app/fxml/DashboardMahasiswaView.fxml");
    }
    
    // ================= ERROR TEXT =================
    private void resetErrorText() {
        txtFormBelumLengkap.setVisible(false);
        txtFileBelumDiupload.setVisible(false);
        txtSudahMemilikiDosenPembimbing.setVisible(false);
        txtSudahMengajukanSkripsi.setVisible(false);
        txtJudulSkripsiHarusSama.setVisible(false);

    }

    
}
