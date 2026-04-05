package app.controllers;

import app.dao.DosenDAO;
import app.models.Dosen;
import app.session.MahasiswaSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;


public class DashboardMahasiswaViewController {

    // ================= STATE DATA =================
    private List<Dosen> dosenList;

    // ================= ROOT =================
    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private Rectangle headerShape;

    // ================= NAVBAR =================
    @FXML private Button btnProfil;
    @FXML private Button btnPesan;
    @FXML private Button btnDosen;
    @FXML private Button btnPengaturan;
    
    // ================= Navbar =================
    @FXML private AnchorPane navbarContainer;
    
    // ================= IMAGE =================
    @FXML private ImageView ivDosen1, ivDosen2, ivDosen3, ivDosen4,
                            ivDosen5, ivDosen6, ivDosen7, ivDosen8;

    // ================= RECT STATUS =================
    @FXML private Rectangle rect1, rect2, rect3, rect4,
                            rect5, rect6, rect7, rect8;

    // ================= TEXT =================
    @FXML private Text txtNamaDosen1, txtProdiDosen1, txtUmurDosen1, txtNoTelpDosen1, txtEmailDosen1;
    @FXML private Text txtNamaDosen2, txtProdiDosen2, txtUmurDosen2, txtNoTelpDosen2, txtEmailDosen2;
    @FXML private Text txtNamaDosen3, txtProdiDosen3, txtUmurDosen3, txtNoTelpDosen3, txtEmailDosen3;
    @FXML private Text txtNamaDosen4, txtProdiDosen4, txtUmurDosen4, txtNoTelpDosen4, txtEmailDosen4;
    @FXML private Text txtNamaDosen5, txtProdiDosen5, txtUmurDosen5, txtNoTelpDosen5, txtEmailDosen5;
    @FXML private Text txtNamaDosen6, txtProdiDosen6, txtUmurDosen6, txtNoTelpDosen6, txtEmailDosen6;
    @FXML private Text txtNamaDosen7, txtProdiDosen7, txtUmurDosen7, txtNoTelpDosen7, txtEmailDosen7;
    @FXML private Text txtNamaDosen8, txtProdiDosen8, txtUmurDosen8, txtNoTelpDosen8, txtEmailDosen8;

    @FXML private Text txtJmlBimbDosen1, txtJmlBimbDosen2, txtJmlBimbDosen3, txtJmlBimbDosen4,
                       txtJmlBimbDosen5, txtJmlBimbDosen6, txtJmlBimbDosen7, txtJmlBimbDosen8;

    @FXML private Text txtKuotaDosen1, txtKuotaDosen2, txtKuotaDosen3, txtKuotaDosen4,
                       txtKuotaDosen5, txtKuotaDosen6, txtKuotaDosen7, txtKuotaDosen8;

    // ================= FILTER =================
    @FXML private Button btnFilterSemua, btnFilterFull, btnFilterTersedia;

    // ================= CARD =================
    @FXML private Node card1, card2, card3, card4,
                       card5, card6, card7, card8;
    
    // ================= Mahasiswa session =================
    @FXML private Text txtNamaMahasiswa;

    // ================= INIT =================
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
        // Setup Halaman
        setupBackground();
        
        // Ambil nama dari session
        String nama = MahasiswaSession.getNama();

        if (nama != null && !nama.isEmpty()) {
            txtNamaMahasiswa.setText(nama + "!");
        } else {
            txtNamaMahasiswa.setText("Mahasiswa");
        }
        loadDosen();
        filterSemua(null);
    }

    // ================= LOAD DOSEN =================
    private void loadDosen() {
    dosenList = new DosenDAO().getAllDosen();
    int jumlah = Math.min(dosenList.size(), 8);

    ImageView[] ivArr = { ivDosen1, ivDosen2, ivDosen3, ivDosen4,
                          ivDosen5, ivDosen6, ivDosen7, ivDosen8 };

    Text[] namaArr = { txtNamaDosen1, txtNamaDosen2, txtNamaDosen3, txtNamaDosen4,
                       txtNamaDosen5, txtNamaDosen6, txtNamaDosen7, txtNamaDosen8 };

    Text[] prodiArr = { txtProdiDosen1, txtProdiDosen2, txtProdiDosen3, txtProdiDosen4,
                        txtProdiDosen5, txtProdiDosen6, txtProdiDosen7, txtProdiDosen8 };

    Text[] umurArr = { txtUmurDosen1, txtUmurDosen2, txtUmurDosen3, txtUmurDosen4,
                       txtUmurDosen5, txtUmurDosen6, txtUmurDosen7, txtUmurDosen8 };

    Text[] noTelpArr = { txtNoTelpDosen1, txtNoTelpDosen2, txtNoTelpDosen3, txtNoTelpDosen4,
                         txtNoTelpDosen5, txtNoTelpDosen6, txtNoTelpDosen7, txtNoTelpDosen8 };

    Text[] emailArr = { txtEmailDosen1, txtEmailDosen2, txtEmailDosen3, txtEmailDosen4,
                        txtEmailDosen5, txtEmailDosen6, txtEmailDosen7, txtEmailDosen8 };

    Text[] jmlArr = { txtJmlBimbDosen1, txtJmlBimbDosen2, txtJmlBimbDosen3, txtJmlBimbDosen4,
                      txtJmlBimbDosen5, txtJmlBimbDosen6, txtJmlBimbDosen7, txtJmlBimbDosen8 };

    Text[] kuotaArr = { txtKuotaDosen1, txtKuotaDosen2, txtKuotaDosen3, txtKuotaDosen4,
                        txtKuotaDosen5, txtKuotaDosen6, txtKuotaDosen7, txtKuotaDosen8 };

    Rectangle[] rectArr = getRects();

    for (int i = 0; i < 8; i++) {
        if (i >= jumlah) {
            getCards()[i].setVisible(false);
            getCards()[i].setManaged(false);
            continue;
        }

        Dosen d = dosenList.get(i);

        // ===== SET TEXT =====
        namaArr[i].setText(d.getNama());
        prodiArr[i].setText(d.getProdi());
        umurArr[i].setText(d.getUmur());
        noTelpArr[i].setText(d.getNoTelp());
        emailArr[i].setText(d.getEmail());

        jmlArr[i].setText(String.valueOf(d.getJumlahBimbingan()));
        kuotaArr[i].setText(String.valueOf(d.getKuota()));

        // ===== WARNA + TOOLTIP =====
        int jml = d.getJumlahBimbingan();
        int kuota = d.getKuota();

        Color warna;
        String status;

        if (jml >= kuota) {
            warna = Color.RED;
            status = "FULL";
        } else if (jml >= kuota * 0.7) {
            warna = Color.web("#ebb543");
            status = "Hampir Penuh";
        } else {
            warna = Color.web("#009201");
            status = "Tersedia";
        }

        rectArr[i].setFill(warna);

        Tooltip.install(rectArr[i],
            new Tooltip(
                "Status: " + status +
                "\nBimbingan: " + jml +
                "/" + kuota
            )
        );

        // ===== FOTO DOSEN =====
        if (d.getFoto() != null) {
            Image img = new Image(new ByteArrayInputStream(d.getFoto()));
            ivArr[i].setImage(img);
            makeCircular(ivArr[i], 35);
        }
    }
}


    // ================= FILTER =================
    @FXML
    private void filterSemua(ActionEvent e) {
        setActive(btnFilterSemua);
        for (Node card : getCards()) {
            card.setVisible(true);
            card.setManaged(true);
        }
    }

    @FXML
    private void filterFull(ActionEvent e) {
        setActive(btnFilterFull);
        Node[] cards = getCards();

        for (int i = 0; i < cards.length; i++) {
            if (i >= dosenList.size()) continue;
            boolean full = dosenList.get(i).getJumlahBimbingan()
                            >= dosenList.get(i).getKuota();
            cards[i].setVisible(full);
            cards[i].setManaged(full);
        }
    }

    @FXML
    private void filterTersedia(ActionEvent e) {
        setActive(btnFilterTersedia);
        Node[] cards = getCards();

        for (int i = 0; i < cards.length; i++) {
            if (i >= dosenList.size()) continue;
            boolean tersedia = dosenList.get(i).getJumlahBimbingan()
                               < dosenList.get(i).getKuota();
            cards[i].setVisible(tersedia);
            cards[i].setManaged(tersedia);
        }
    }

    // ================= HELPER =================
    private Node[] getCards() {
        return new Node[]{ card1, card2, card3, card4,
                           card5, card6, card7, card8 };
    }

    private Rectangle[] getRects() {
        return new Rectangle[]{ rect1, rect2, rect3, rect4,
                                rect5, rect6, rect7, rect8 };
    }

    private void setActive(Button active) {
        btnFilterSemua.setStyle("-fx-background-color: white;");
        btnFilterFull.setStyle("-fx-background-color: white;");
        btnFilterTersedia.setStyle("-fx-background-color: white;");
        active.setStyle("-fx-background-color: #253a80; -fx-text-fill: white;");
    }

    private void makeCircular(ImageView iv, double r) {
        iv.setClip(new Circle(r, r, r));
    }
    // ================= Ajukan Button ================= 

    @FXML private void handleAjukan(ActionEvent event) { if (!(event.getSource() instanceof Button)) return; Button btn = (Button) event.getSource(); if (btn.getUserData() == null) return; int idDosen = Integer.parseInt(btn.getUserData().toString()); openDetail(btn, idDosen); } private void openDetail(Node sourceNode, int idDosen) { try { FXMLLoader loader = new FXMLLoader( getClass().getResource("/app/fxml/DetailDashboardMahasiswaView.fxml") ); Parent root = loader.load(); DetailDashboardMahasiswaViewController controller = loader.getController(); controller.setIdDosen(idDosen); Stage stage = (Stage) sourceNode.getScene().getWindow(); stage.setScene(new Scene(root)); stage.show(); } catch (IOException e) { e.printStackTrace(); } }
    
    // ================= BG Responsif ================= 

    private void setupBackground() {
        bgImage.fitWidthProperty().bind(root.widthProperty());
        bgImage.fitHeightProperty().bind(root.heightProperty());
        headerShape.widthProperty().bind(root.widthProperty());
        headerShape.heightProperty().bind(root.heightProperty().multiply(0.2));
    }
    // ================= Jadwal Button ================= 
    @FXML 
    private void goToJadwal(MouseEvent event) {
        openPage(event, "/app/fxml/JadwalMahasiswaView.fxml");
    }
    private void openPage(Event event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


