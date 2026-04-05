package app.controllers;

import app.session.MahasiswaSession;
import app.database.DBConnect;
import java.io.ByteArrayInputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ModalJadwalBimbinganMahasiswaController {
    // Mahasiswa
    @FXML private Text txtNamaMahasiswa;

    // Card Bimbingan
    @FXML private HBox cardBimbingan1;
    @FXML private HBox cardBimbingan2;
    @FXML private HBox cardBimbingan3;
    @FXML private HBox cardBimbingan4;
    @FXML private HBox cardBimbingan5;
    @FXML private HBox cardBimbingan6;
    @FXML private HBox cardBimbingan7;
    @FXML private HBox cardBimbingan8;
    @FXML private HBox cardBimbingan9;
    @FXML private HBox cardBimbingan10;
    @FXML private HBox cardBimbingan11;
    @FXML private HBox cardBimbingan12;
    
    // Card Bimbingan
    @FXML private ImageView ivDosenPertemuan1, ivDosenPertemuan2, ivDosenPertemuan3, ivDosenPertemuan4, ivDosenPertemuan5, ivDosenPertemuan6, ivDosenPertemuan7, ivDosenPertemuan8, ivDosenPertemuan9, ivDosenPertemuan10, ivDosenPertemuan11, ivDosenPertemuan12;
    @FXML private Text txtNamaDosenPertemuan1, txtNamaDosenPertemuan2, txtNamaDosenPertemuan3, txtNamaDosenPertemuan4, txtNamaDosenPertemuan5, txtNamaDosenPertemuan6, txtNamaDosenPertemuan7, txtNamaDosenPertemuan8, txtNamaDosenPertemuan9, txtNamaDosenPertemuan10, txtNamaDosenPertemuan11, txtNamaDosenPertemuan12;
    @FXML private Text txtProdiDosenPertemuan1, txtProdiDosenPertemuan2, txtProdiDosenPertemuan3, txtProdiDosenPertemuan4, txtProdiDosenPertemuan5, txtProdiDosenPertemuan6, txtProdiDosenPertemuan7, txtProdiDosenPertemuan8, txtProdiDosenPertemuan9, txtProdiDosenPertemuan10, txtProdiDosenPertemuan11, txtProdiDosenPertemuan12;
    @FXML private Text txtJudulBimbinganPertemuan1, txtJudulBimbinganPertemuan2, txtJudulBimbinganPertemuan3, txtJudulBimbinganPertemuan4, txtJudulBimbinganPertemuan5, txtJudulBimbinganPertemuan6, txtJudulBimbinganPertemuan7, txtJudulBimbinganPertemuan8, txtJudulBimbinganPertemuan9, txtJudulBimbinganPertemuan10, txtJudulBimbinganPertemuan11, txtJudulBimbinganPertemuan12;
    @FXML private Text txtRuanganBimbinganPertemuan1, txtRuanganBimbinganPertemuan2, txtRuanganBimbinganPertemuan3, txtRuanganBimbinganPertemuan4, txtRuanganBimbinganPertemuan5, txtRuanganBimbinganPertemuan6, txtRuanganBimbinganPertemuan7, txtRuanganBimbinganPertemuan8, txtRuanganBimbinganPertemuan9, txtRuanganBimbinganPertemuan10, txtRuanganBimbinganPertemuan11, txtRuanganBimbinganPertemuan12;
    @FXML private Text txtPertemuan1, txtPertemuan2, txtPertemuan3, txtPertemuan4, txtPertemuan5, txtPertemuan6, txtPertemuan7, txtPertemuan8, txtPertemuan9, txtPertemuan10, txtPertemuan11, txtPertemuan12;
    @FXML private Text txtTanggalBimbinganPertemuan1, txtTanggalBimbinganPertemuan2, txtTanggalBimbinganPertemuan3, txtTanggalBimbinganPertemuan4, txtTanggalBimbinganPertemuan5, txtTanggalBimbinganPertemuan6, txtTanggalBimbinganPertemuan7, txtTanggalBimbinganPertemuan8, txtTanggalBimbinganPertemuan9, txtTanggalBimbinganPertemuan10, txtTanggalBimbinganPertemuan11, txtTanggalBimbinganPertemuan12;
    @FXML private Text txtJamBimbinganPertemuan1, txtJamBimbinganPertemuan2, txtJamBimbinganPertemuan3, txtJamBimbinganPertemuan4, txtJamBimbinganPertemuan5, txtJamBimbinganPertemuan6, txtJamBimbinganPertemuan7, txtJamBimbinganPertemuan8, txtJamBimbinganPertemuan9, txtJamBimbinganPertemuan10, txtJamBimbinganPertemuan11, txtJamBimbinganPertemuan12;



    @FXML
    public void initialize() {
        loadNamaMahasiswa();
        loadJadwalBimbingan();
    }

    // tampilkan nama di header
    private void loadNamaMahasiswa() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();
        String sql = "SELECT nama FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaMahasiswa.setText(rs.getString("nama"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJadwalBimbingan() {
        int idMahasiswa = MahasiswaSession.getIdMahasiswa();

        String sql =
            "SELECT b.pertemuan_ke, b.judul, b.ruangan, b.tanggal, b.jam, " +
            "       d.nama, d.prodi, d.foto " +
            "FROM bimbingan b " +
            "JOIN dosen d ON d.id = b.id_dosen " +
            "WHERE b.id_mahasiswa = ?";

        // ================= ARRAY CARD =================
        HBox[] cards = {
            cardBimbingan1, cardBimbingan2, cardBimbingan3, cardBimbingan4,
            cardBimbingan5, cardBimbingan6, cardBimbingan7, cardBimbingan8,
            cardBimbingan9, cardBimbingan10, cardBimbingan11, cardBimbingan12
        };

        ImageView[] ivDosen = {
            ivDosenPertemuan1, ivDosenPertemuan2, ivDosenPertemuan3, ivDosenPertemuan4,
            ivDosenPertemuan5, ivDosenPertemuan6, ivDosenPertemuan7, ivDosenPertemuan8,
            ivDosenPertemuan9, ivDosenPertemuan10, ivDosenPertemuan11, ivDosenPertemuan12
        };

        Text[] txtNamaDosen = {
            txtNamaDosenPertemuan1, txtNamaDosenPertemuan2, txtNamaDosenPertemuan3,
            txtNamaDosenPertemuan4, txtNamaDosenPertemuan5, txtNamaDosenPertemuan6,
            txtNamaDosenPertemuan7, txtNamaDosenPertemuan8, txtNamaDosenPertemuan9,
            txtNamaDosenPertemuan10, txtNamaDosenPertemuan11, txtNamaDosenPertemuan12
        };

        Text[] txtProdiDosen = {
            txtProdiDosenPertemuan1, txtProdiDosenPertemuan2, txtProdiDosenPertemuan3,
            txtProdiDosenPertemuan4, txtProdiDosenPertemuan5, txtProdiDosenPertemuan6,
            txtProdiDosenPertemuan7, txtProdiDosenPertemuan8, txtProdiDosenPertemuan9,
            txtProdiDosenPertemuan10, txtProdiDosenPertemuan11, txtProdiDosenPertemuan12
        };

        Text[] txtJudul = {
            txtJudulBimbinganPertemuan1, txtJudulBimbinganPertemuan2,
            txtJudulBimbinganPertemuan3, txtJudulBimbinganPertemuan4,
            txtJudulBimbinganPertemuan5, txtJudulBimbinganPertemuan6,
            txtJudulBimbinganPertemuan7, txtJudulBimbinganPertemuan8,
            txtJudulBimbinganPertemuan9, txtJudulBimbinganPertemuan10,
            txtJudulBimbinganPertemuan11, txtJudulBimbinganPertemuan12
        };

        Text[] txtRuangan = {
            txtRuanganBimbinganPertemuan1, txtRuanganBimbinganPertemuan2,
            txtRuanganBimbinganPertemuan3, txtRuanganBimbinganPertemuan4,
            txtRuanganBimbinganPertemuan5, txtRuanganBimbinganPertemuan6,
            txtRuanganBimbinganPertemuan7, txtRuanganBimbinganPertemuan8,
            txtRuanganBimbinganPertemuan9, txtRuanganBimbinganPertemuan10,
            txtRuanganBimbinganPertemuan11, txtRuanganBimbinganPertemuan12
        };
        
        Text[] txtPertemuan = {
            txtPertemuan1, txtPertemuan2, txtPertemuan3, txtPertemuan4,
            txtPertemuan5, txtPertemuan6, txtPertemuan7, txtPertemuan8,
            txtPertemuan9, txtPertemuan10, txtPertemuan11, txtPertemuan12
        };


        Text[] txtTanggal = {
            txtTanggalBimbinganPertemuan1, txtTanggalBimbinganPertemuan2,
            txtTanggalBimbinganPertemuan3, txtTanggalBimbinganPertemuan4,
            txtTanggalBimbinganPertemuan5, txtTanggalBimbinganPertemuan6,
            txtTanggalBimbinganPertemuan7, txtTanggalBimbinganPertemuan8,
            txtTanggalBimbinganPertemuan9, txtTanggalBimbinganPertemuan10,
            txtTanggalBimbinganPertemuan11, txtTanggalBimbinganPertemuan12
        };

        Text[] txtJam = {
            txtJamBimbinganPertemuan1, txtJamBimbinganPertemuan2,
            txtJamBimbinganPertemuan3, txtJamBimbinganPertemuan4,
            txtJamBimbinganPertemuan5, txtJamBimbinganPertemuan6,
            txtJamBimbinganPertemuan7, txtJamBimbinganPertemuan8,
            txtJamBimbinganPertemuan9, txtJamBimbinganPertemuan10,
            txtJamBimbinganPertemuan11, txtJamBimbinganPertemuan12
        };

        // 🔒 sembunyikan semua dulu
        for (HBox c : cards) {
            c.setVisible(false);
            c.setManaged(false);
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int pertemuan = rs.getInt("pertemuan_ke");

                if (pertemuan >= 1 && pertemuan <= 12) {
                    int idx = pertemuan - 1;

                    cards[idx].setVisible(true);
                    cards[idx].setManaged(true);

                    txtNamaDosen[idx].setText(rs.getString("nama"));
                    txtProdiDosen[idx].setText(rs.getString("prodi"));
                    txtJudul[idx].setText(rs.getString("judul"));
                    txtRuangan[idx].setText("Ruangan " + rs.getString("ruangan"));
                    txtTanggal[idx].setText(rs.getString("tanggal"));
                    txtJam[idx].setText(rs.getString("jam"));

                    txtPertemuan[idx].setText("Pertemuan " + pertemuan);

                    // FOTO
                    byte[] foto = rs.getBytes("foto");
                    if (foto != null) {
                        Image img = new Image(new ByteArrayInputStream(foto));
                        ivDosen[idx].setImage(img);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    // ================= CLOSE MODAL =================
    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
