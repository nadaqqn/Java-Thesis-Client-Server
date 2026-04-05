package app.controllers;

import app.dao.BimbinganContext;
import app.database.DBConnect;
import app.models.MahasiswaItem;
import app.session.DosenSession;

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
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalJadwalBimbinganDosenController {

    // ================= HEADER =================
    @FXML private Text txtNamaDosen;

    // ================= COMBO BOX =================
    @FXML private ComboBox<MahasiswaItem> cbMahasiswaBimbingan;

    // ================= CARD BIMBINGAN =================
    @FXML private HBox cardBimbingan1, cardBimbingan2, cardBimbingan3,
                       cardBimbingan4, cardBimbingan5, cardBimbingan6;

    @FXML private ImageView ivMahasiswaPertemuan1, ivMahasiswaPertemuan2,
                           ivMahasiswaPertemuan3, ivMahasiswaPertemuan4,
                           ivMahasiswaPertemuan5, ivMahasiswaPertemuan6;

    @FXML private Text txtNamaMahasiswaPertemuan1, txtNamaMahasiswaPertemuan2,
                      txtNamaMahasiswaPertemuan3, txtNamaMahasiswaPertemuan4,
                      txtNamaMahasiswaPertemuan5, txtNamaMahasiswaPertemuan6;
    
    @FXML private Text txtTanggalMahasiswaPertemuan1, txtTanggalMahasiswaPertemuan2,
                  txtTanggalMahasiswaPertemuan3, txtTanggalMahasiswaPertemuan4,
                  txtTanggalMahasiswaPertemuan5, txtTanggalMahasiswaPertemuan6;
    
    @FXML private Text txtNimMahasiswaPertemuan1, txtNimMahasiswaPertemuan2,
                  txtNimMahasiswaPertemuan3, txtNimMahasiswaPertemuan4,
                  txtNimMahasiswaPertemuan5, txtNimMahasiswaPertemuan6;

    @FXML private Text txtJurusanMahasiswaPertemuan1, txtJurusanMahasiswaPertemuan2,
                      txtJurusanMahasiswaPertemuan3, txtJurusanMahasiswaPertemuan4,
                      txtJurusanMahasiswaPertemuan5, txtJurusanMahasiswaPertemuan6;

    @FXML private Text txtJudulSkripsiMahasiswaPertemuan1, txtJudulSkripsiMahasiswaPertemuan2,
                  txtJudulSkripsiMahasiswaPertemuan3, txtJudulSkripsiMahasiswaPertemuan4,
                  txtJudulSkripsiMahasiswaPertemuan5, txtJudulSkripsiMahasiswaPertemuan6;

    @FXML private Text txtRuanganMahasiswaPertemuan1, txtRuanganMahasiswaPertemuan2,
                  txtRuanganMahasiswaPertemuan3, txtRuanganMahasiswaPertemuan4,
                  txtRuanganMahasiswaPertemuan5, txtRuanganMahasiswaPertemuan6;

    @FXML private Text txtMahasiswaPertemuan1, txtMahasiswaPertemuan2,
                      txtMahasiswaPertemuan3, txtMahasiswaPertemuan4,
                      txtMahasiswaPertemuan5, txtMahasiswaPertemuan6;

    @FXML private Text txtJamMahasiswaPertemuan1, txtJamMahasiswaPertemuan2,
                      txtJamMahasiswaPertemuan3, txtJamMahasiswaPertemuan4,
                      txtJamMahasiswaPertemuan5, txtJamMahasiswaPertemuan6;

    @FXML private Text txtJudulPertemuan1, txtJudulPertemuan2,
                  txtJudulPertemuan3, txtJudulPertemuan4,
                  txtJudulPertemuan5, txtJudulPertemuan6;
    
    @FXML private Button btnCatat1, btnCatat2, btnCatat3,
                    btnCatat4, btnCatat5, btnCatat6;




    // ==================================================

    @FXML
    public void initialize() {
        String nama = DosenSession.getNama();
        txtNamaDosen.setText(
            (nama != null && !nama.isEmpty()) ? nama + "!" : "Dosen!"
        );

        loadMahasiswaBimbingan();

        cbMahasiswaBimbingan.setOnAction(e -> {
            MahasiswaItem selected = cbMahasiswaBimbingan.getValue();
            if (selected != null) {
                loadBimbinganMahasiswa(selected.getIdMahasiswa());
            } else {
                resetCard();
            }
        });

        resetCard();
    }

    // ================= HELPER =================
    private Button getBtnCatat(int i) {
        switch (i) {
            case 1: return btnCatat1;
            case 2: return btnCatat2;
            case 3: return btnCatat3;
            case 4: return btnCatat4;
            case 5: return btnCatat5;
            case 6: return btnCatat6;
            default: return null;
        }
    }
    
    // ================= MAHASISWA BIMBINGAN =================

    private void loadMahasiswaBimbingan() {
        int idDosen = DosenSession.getIdDosen();

        String sql =
            "SELECT m.id_mahasiswa, m.nama " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON m.id_mahasiswa = dp.id_mahasiswa " +
            "WHERE dp.id_dosen = ? " +
            "ORDER BY m.nama";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            cbMahasiswaBimbingan.getItems().clear();

            while (rs.next()) {
                cbMahasiswaBimbingan.getItems().add(
                    new MahasiswaItem(
                        rs.getInt("id_mahasiswa"),
                        rs.getString("nama")
                    )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD BIMBINGAN =================

    private void loadBimbinganMahasiswa(int idMahasiswa) {
        int idDosen = DosenSession.getIdDosen();

        resetCard();

        String sql =
            "SELECT m.nama, m.nim, m.jurusan, m.foto, " +
            "       s.judul_skripsi, " +
            "       b.id_bimbingan, b.judul, b.pertemuan_ke, b.tanggal, b.jam, b.ruangan " +
            "FROM bimbingan b " +
            "JOIN mahasiswa m ON m.id_mahasiswa = b.id_mahasiswa " +
            "LEFT JOIN skripsi s ON s.id_mahasiswa = b.id_mahasiswa AND s.id_dosen = b.id_dosen " +
            "WHERE b.id_mahasiswa = ? " +
            "AND b.id_dosen = ? " +
            "ORDER BY b.pertemuan_ke ASC " +
            "LIMIT 6";


        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idDosen);
            ResultSet rs = ps.executeQuery();

            int index = 1;

            while (rs.next() && index <= 6) {
                int idBimbingan = rs.getInt("id_bimbingan");

                setCardData(
                    index,
                    idBimbingan,
                    idMahasiswa,
                    idDosen,
                    rs.getString("nama"),
                    rs.getString("nim"),
                    rs.getString("jurusan"),
                    rs.getString("judul_skripsi"),
                    rs.getString("judul"), 
                    rs.getBytes("foto"),
                    rs.getDate("tanggal"),
                    rs.getInt("pertemuan_ke"),
                    rs.getTime("jam"),
                    rs.getString("ruangan")

                );
                
                

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SET CARD DATA =================

    private void setCardData(int index, int idBimbingan, int idMahasiswa, int idDosen, String nama, String nim, String jurusan, String judulSkripsi, String judulPertemuan, byte[] foto, java.sql.Date tanggal, int pertemuanKe, java.sql.Time jam, String ruangan) {
        HBox card = getCard(index);
        Text txtNama = getNamaText(index);
        Text txtNim = getNimText(index);
        Text txtJurusan = getJurusanText(index);
        Text txtJudul = getJudulSkripsiText(index);
        Text txtJudulPertemuan = getJudulPertemuanText(index);
        Text txtTanggal = getTanggalText(index);
        Text txtPertemuan = getPertemuanText(index);
        Text txtJam = getJamText(index);
        Text txtRuangan = getRuanganText(index);
        ImageView iv = getImageView(index);

        if (card == null) return;

        card.setVisible(true);
        card.setManaged(true);
        txtNama.setText(nama);
        txtNim.setText(nim);
        txtJurusan.setText(jurusan);
        
        if (tanggal != null && txtTanggal != null) {
            txtTanggal.setText(tanggal.toString()); 
        }

        if (foto != null) {
            iv.setImage(new Image(new java.io.ByteArrayInputStream(foto)));
        } else {
            iv.setImage(new Image(
                getClass().getResourceAsStream("/assets/img/default-user.png")
            ));
        }
        
        if (txtJudul != null) {
            txtJudul.setText(
                (judulSkripsi != null && !judulSkripsi.isEmpty())
                    ? judulSkripsi
                    : "Judul skripsi belum ditentukan"
            );
        }
        
        if (txtJudulPertemuan != null) {
            txtJudulPertemuan.setText(
                (judulPertemuan != null && !judulPertemuan.isEmpty())
                    ? judulPertemuan
                    : "Bimbingan"
            );
        }
        
        if (txtPertemuan != null) {
            txtPertemuan.setText("Pertemuan " + pertemuanKe);
        }

        if (txtJam != null && jam != null) {
            txtJam.setText(jam.toString());
        }

        if (txtRuangan != null) {
            txtRuangan.setText(
                (ruangan != null && !ruangan.isEmpty())
                    ? "Ruangan " + ruangan
                    : "Ruangan -"
            );
        }
        
        Button btnCatat = getBtnCatat(index);
        if (btnCatat != null) {
            btnCatat.setUserData(
                new BimbinganContext(
                    idBimbingan,
                    idMahasiswa,
                    idDosen,
                    pertemuanKe
                )
            );

            btnCatat.setOnAction(this::openModalCatatRevisi);
        }

    }
    
    @FXML
    private void openModalCatatRevisi(ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            BimbinganContext ctx = (BimbinganContext) btn.getUserData();

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/fxml/ModalCatatRevisiDosen.fxml")
            );

            Parent root = loader.load();

            ModalCatatRevisiDosenController controller = loader.getController();
            controller.setBimbinganData(
                ctx.idBimbingan,
                ctx.idMahasiswa,
                ctx.idDosen,
                ctx.pertemuanKe
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= RESET CARD =================

    private void resetCard() {
        for (int i = 1; i <= 6; i++) {
            HBox card = getCard(i);
            if (card != null) {
                card.setVisible(false);
                card.setManaged(false);
                
                if (getNamaText(i) != null) getNamaText(i).setText("");
                if (getNimText(i) != null) getNimText(i).setText("");
                if (getJurusanText(i) != null) getJurusanText(i).setText("");
                if (getTanggalText(i) != null) getTanggalText(i).setText("");
                if (getJudulSkripsiText(i) != null) getJudulSkripsiText(i).setText("");
                if (getJudulPertemuanText(i) != null) getJudulPertemuanText(i).setText("");

                if (getPertemuanText(i) != null) getPertemuanText(i).setText("");
                if (getJamText(i) != null) getJamText(i).setText("");
                if (getRuanganText(i) != null) getRuanganText(i).setText("");

            }
        }
    }

    // ================= HELPER =================

    private HBox getCard(int i) {
        switch (i) {
            case 1: return cardBimbingan1;
            case 2: return cardBimbingan2;
            case 3: return cardBimbingan3;
            case 4: return cardBimbingan4;
            case 5: return cardBimbingan5;
            case 6: return cardBimbingan6;
            default: return null;
        }
    }


    private Text getNamaText(int i) {
        switch (i) {
            case 1: return txtNamaMahasiswaPertemuan1;
            case 2: return txtNamaMahasiswaPertemuan2;
            case 3: return txtNamaMahasiswaPertemuan3;
            case 4: return txtNamaMahasiswaPertemuan4;
            case 5: return txtNamaMahasiswaPertemuan5;
            case 6: return txtNamaMahasiswaPertemuan6;
            default: return null;
        }
    }


    private ImageView getImageView(int i) {
        switch (i) {
            case 1: return ivMahasiswaPertemuan1;
            case 2: return ivMahasiswaPertemuan2;
            case 3: return ivMahasiswaPertemuan3;
            case 4: return ivMahasiswaPertemuan4;
            case 5: return ivMahasiswaPertemuan5;
            case 6: return ivMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getTanggalText(int i) {
        switch (i) {
            case 1: return txtTanggalMahasiswaPertemuan1;
            case 2: return txtTanggalMahasiswaPertemuan2;
            case 3: return txtTanggalMahasiswaPertemuan3;
            case 4: return txtTanggalMahasiswaPertemuan4;
            case 5: return txtTanggalMahasiswaPertemuan5;
            case 6: return txtTanggalMahasiswaPertemuan6;
            default: return null;
        }
    }

    private Text getNimText(int i) {
        switch (i) {
            case 1: return txtNimMahasiswaPertemuan1;
            case 2: return txtNimMahasiswaPertemuan2;
            case 3: return txtNimMahasiswaPertemuan3;
            case 4: return txtNimMahasiswaPertemuan4;
            case 5: return txtNimMahasiswaPertemuan5;
            case 6: return txtNimMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJurusanText(int i) {
        switch (i) {
            case 1: return txtJurusanMahasiswaPertemuan1;
            case 2: return txtJurusanMahasiswaPertemuan2;
            case 3: return txtJurusanMahasiswaPertemuan3;
            case 4: return txtJurusanMahasiswaPertemuan4;
            case 5: return txtJurusanMahasiswaPertemuan5;
            case 6: return txtJurusanMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJudulSkripsiText(int i) {
        switch (i) {
            case 1: return txtJudulSkripsiMahasiswaPertemuan1;
            case 2: return txtJudulSkripsiMahasiswaPertemuan2;
            case 3: return txtJudulSkripsiMahasiswaPertemuan3;
            case 4: return txtJudulSkripsiMahasiswaPertemuan4;
            case 5: return txtJudulSkripsiMahasiswaPertemuan5;
            case 6: return txtJudulSkripsiMahasiswaPertemuan6;
            default: return null;
        }
    }

    private Text getPertemuanText(int i) {
        switch (i) {
            case 1: return txtMahasiswaPertemuan1;
            case 2: return txtMahasiswaPertemuan2;
            case 3: return txtMahasiswaPertemuan3;
            case 4: return txtMahasiswaPertemuan4;
            case 5: return txtMahasiswaPertemuan5;
            case 6: return txtMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getRuanganText(int i) {
        switch (i) {
            case 1: return txtRuanganMahasiswaPertemuan1;
            case 2: return txtRuanganMahasiswaPertemuan2;
            case 3: return txtRuanganMahasiswaPertemuan3;
            case 4: return txtRuanganMahasiswaPertemuan4;
            case 5: return txtRuanganMahasiswaPertemuan5;
            case 6: return txtRuanganMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJamText(int i) {
        switch (i) {
            case 1: return txtJamMahasiswaPertemuan1;
            case 2: return txtJamMahasiswaPertemuan2;
            case 3: return txtJamMahasiswaPertemuan3;
            case 4: return txtJamMahasiswaPertemuan4;
            case 5: return txtJamMahasiswaPertemuan5;
            case 6: return txtJamMahasiswaPertemuan6;
            default: return null;
        }
    }
    
    private Text getJudulPertemuanText(int i) {
        switch (i) {
            case 1: return txtJudulPertemuan1;
            case 2: return txtJudulPertemuan2;
            case 3: return txtJudulPertemuan3;
            case 4: return txtJudulPertemuan4;
            case 5: return txtJudulPertemuan5;
            case 6: return txtJudulPertemuan6;
            default: return null;
        }
    }








    // ================= CLOSE MODAL =================

    @FXML
    private void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}
