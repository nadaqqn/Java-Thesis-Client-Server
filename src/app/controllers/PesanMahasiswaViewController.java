package app.controllers;

import app.database.DBConnect;
import app.session.MahasiswaSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


import java.io.*;
import java.net.Socket;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.nio.file.Files;

public class PesanMahasiswaViewController {

    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private AnchorPane navbarContainer;
    
    @FXML private VBox vboxChatRoomList;
    @FXML private VBox vboxChatContainer;
    @FXML private ScrollPane scrollChat;
    @FXML private TextField txtInputPesan;
    @FXML private ImageView btnUploadFile;
    @FXML private ImageView ivSend;
    
    // Card Chat
    @FXML private HBox cardIdentitasPenerima;
    @FXML private VBox cardChat;
    @FXML private VBox cardButton;
    @FXML private Text txtNamaPenerima;
    @FXML private ImageView ivPenerima;



    private Socket socket;
    private DataOutputStream dataOut;

    private int activeMahasiswaId;
    private Integer activeDosenId = null;

    private final String username = "MAHASISWA";

    public void initialize() {
        setupBackground();
        setupNavbar();
        activeMahasiswaId = MahasiswaSession.getIdMahasiswa();

        // Chat
        txtNamaPenerima.setText("");
        ivPenerima.setImage(null);
        setupChatClient();
        loadChatRoomsMahasiswa();
        setChatSectionVisible(false);
        
        // Kirim
        ivSend.setOnMouseClicked(e -> sendMessage());
        txtInputPesan.setOnAction(e -> sendMessage());
        btnUploadFile.setOnMouseClicked(e -> sendFile());


    }
    
    // ================= BACKGROUND=================

    private void setupBackground() {
        if (bgImage != null && root != null) {
            bgImage.fitWidthProperty().bind(root.widthProperty());
            bgImage.fitHeightProperty().bind(root.heightProperty());
        }
    }

    // ================= NAVBAR =================
    private void setupNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/Navbar.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ================= IDENTITAS PENERIMA =================
    private void loadIdentitasDosen() {

        String sql = "SELECT nama, foto FROM dosen WHERE id = ?";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeDosenId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaPenerima.setText(rs.getString("nama"));

                Blob fotoBlob = rs.getBlob("foto");
                if (fotoBlob != null) {
                    ivPenerima.setImage(
                        new Image(fotoBlob.getBinaryStream())
                    );
                } else {
                    ivPenerima.setImage(null); // atau default avatar
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // ================= Mengatur Visible Chat =================
    private void setChatSectionVisible(boolean visible) {
        cardIdentitasPenerima.setVisible(visible);
        cardIdentitasPenerima.setManaged(visible);

        cardChat.setVisible(visible);
        cardChat.setManaged(visible);

        cardButton.setVisible(visible);
        cardButton.setManaged(visible);
    }
    
    private void showChatUI() {
        cardButton.setVisible(true);
        cardChat.setVisible(true);
        cardIdentitasPenerima.setVisible(true);

        cardButton.setManaged(true);
        cardChat.setManaged(true);
        cardIdentitasPenerima.setManaged(true);
    }

    private HBox buildDosenCard(int dosenId, String nama, Blob fotoBlob)
            throws SQLException, IOException {

        ImageView iv = new ImageView();
        iv.setFitWidth(60);
        iv.setFitHeight(60);
        iv.setPreserveRatio(true);

        if (fotoBlob != null) {
            iv.setImage(new Image(fotoBlob.getBinaryStream()));
        }

        Text txt = new Text(nama);
        txt.setStyle("-fx-font-weight: bold;");

        HBox card = new HBox(iv, txt);
        card.setSpacing(15);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color:#ffffff;" +
            "-fx-background-radius:10;" +
            "-fx-cursor:hand;"
        );

        card.setOnMouseClicked(e -> {
            activeDosenId = dosenId;
            highlightActiveCard(card);
            showChatUI(); 
            loadIdentitasDosen();
            loadChatHistory();
        });

        return card;
    }


    // ================= CHAT CLIENT =================
    private void setupChatClient() {
        try {
            socket = new Socket("localhost", 12345);
            dataOut = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try (BufferedReader in =
                         new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        String finalMsg = msg;
                        Platform.runLater(() ->
                            addBubble(finalMsg, false)
                        );
                    }
                } catch (IOException ignored) {}
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= SEND MESSAGE =================
    private void sendMessage() {
        if (activeDosenId == null) {
            System.out.println("Pilih dosen pembimbing terlebih dahulu");
            return;
        }

        String pesan = txtInputPesan.getText().trim();
        if (pesan.isEmpty()) return;

        try {
            dataOut.writeUTF(
                activeMahasiswaId + "|" +
                activeDosenId + "|" +
                username + "|" +
                pesan
            );
            dataOut.flush();

            saveChatToDB(pesan);
            addBubble(pesan, true);
            txtInputPesan.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= CHATROOM LIST =================
    private void loadChatRoomsMahasiswa() {
        vboxChatRoomList.getChildren().clear();

        String sql =
            "SELECT d.id, d.nama, d.foto " +
            "FROM dosenpembimbing dp " +
            "JOIN dosen d ON d.id = dp.id_dosen " +
            "WHERE dp.id_mahasiswa=?";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeMahasiswaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int dosenId = rs.getInt("id");
                String nama = rs.getString("nama");
                Blob foto = rs.getBlob("foto");

                HBox card = buildDosenCard(dosenId, nama, foto);
                vboxChatRoomList.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void highlightActiveCard(HBox active) {
        for (Node n : vboxChatRoomList.getChildren()) {
            n.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;");
        }
        active.setStyle(
            "-fx-background-color:#dbe7ff;-fx-background-radius:10;"
        );
    }

    // ================= CHAT HISTORY =================
    private void loadChatHistory() {
        vboxChatContainer.getChildren().clear();
        if (activeDosenId == null) return;

        String sql =
            "SELECT * FROM (" +

            "SELECT 'TEXT' AS tipe, pesan AS konten, NULL AS size, pengirim, waktu AS created_at " +
            "FROM chat WHERE id_mahasiswa=? AND id_dosen=? " +

            "UNION ALL " +

            "SELECT 'FILE' AS tipe, file_name AS konten, LENGTH(file_data) AS size, pengirim, uploaded_at AS created_at " +
            "FROM files WHERE id_mahasiswa=? AND id_dosen=? " +

            ") t ORDER BY created_at ASC";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeMahasiswaId);
            ps.setInt(2, activeDosenId);
            ps.setInt(3, activeMahasiswaId);
            ps.setInt(4, activeDosenId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boolean isSelf =
                    rs.getString("pengirim").equals("MAHASISWA");

                if (rs.getString("tipe").equals("TEXT")) {
                    addBubble(rs.getString("konten"), isSelf);
                } else {
                    addFileBubble(
                        rs.getString("konten"),
                        rs.getLong("size"),
                        isSelf,
                        rs.getTimestamp("created_at")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveChatToDB(String pesan) {
        String sql =
            "INSERT INTO chat(id_mahasiswa,id_dosen,pengirim,pesan) " +
            "VALUES(?,?,?,?)";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeMahasiswaId);
            ps.setInt(2, activeDosenId);
            ps.setString(3, "MAHASISWA");
            ps.setString(4, pesan);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CHAT BUBBLE =================
    private void addBubble(String pesan, boolean isSelf) {
        HBox wrap = new HBox();
        wrap.setAlignment(isSelf ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        wrap.setPadding(new Insets(5));

        Text txt = new Text(pesan);
        txt.setWrappingWidth(260);
        txt.setFill(javafx.scene.paint.Color.WHITE);

        VBox bubble = new VBox(txt);
        bubble.setPadding(new Insets(8));
        bubble.setStyle(
            "-fx-background-color:" +
            (isSelf ? "#2979FF" : "#616161") +
            ";-fx-background-radius:12;"
        );

        wrap.getChildren().add(bubble);
        vboxChatContainer.getChildren().add(wrap);
        scrollChat.setVvalue(1.0);
    }
    
    // ================= KIRIM FILE =================
    private void sendFile() {
        if (activeDosenId == null) return;

        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(root.getScene().getWindow());
        if (file == null) return;

        try {
            byte[] data = Files.readAllBytes(file.toPath());

            String sql =
                "INSERT INTO files(file_name, file_data, id_mahasiswa, id_dosen, pengirim) " +
                "VALUES(?,?,?,?,?)";

            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, file.getName());
                ps.setBytes(2, data);
                ps.setInt(3, activeMahasiswaId);
                ps.setInt(4, activeDosenId);
                ps.setString(5, "MAHASISWA");
                ps.executeUpdate();
            }

            // tampilkan langsung
            addFileBubble(
                file.getName(),
                file.length(),
                true,
                new Timestamp(System.currentTimeMillis())
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ================= DESAIN FORMAT FILE =================
    private void addFileBubble(
        String fileName,
        long size,
        boolean isSelf,
        Timestamp uploadedAt
    ) {

        HBox wrapper = new HBox();
        wrapper.setAlignment(isSelf ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        wrapper.setPadding(new Insets(5, 10, 5, 10));

        VBox bubble = new VBox(8);
        bubble.setPadding(new Insets(10));
        bubble.setPrefWidth(200);
        bubble.setStyle(
            "-fx-background-color:" +
            (isSelf ? "#dce2ff" : "#eeeeee") +
            "; -fx-background-radius:20;"
        );

        Text title = new Text("File");
        title.setStyle("-fx-font-weight:bold;");

        Text name = new Text(fileName);
        Text sizeTxt = new Text((size / 1024) + " KB");
        Text time = new Text(uploadedAt.toString());

        ImageView btnSave = new ImageView(
            new Image(getClass().getResourceAsStream("/app/images/download.png"))
        );
        btnSave.setFitWidth(28);
        btnSave.setFitHeight(28);
        btnSave.setOnMouseClicked(e -> downloadFile(fileName));

        HBox row = new HBox(10, name, btnSave);
        row.setAlignment(Pos.CENTER_LEFT);

        bubble.getChildren().addAll(title, row, sizeTxt, time);
        wrapper.getChildren().add(bubble);

        vboxChatContainer.getChildren().add(wrapper);
        scrollChat.setVvalue(1.0);
    }
    
    // ================= DOWNLOAD FILE =================
    private void downloadFile(String fileName) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(fileName);
        File save = fc.showSaveDialog(root.getScene().getWindow());
        if (save == null) return;

        String sql =
            "SELECT file_data FROM files " +
            "WHERE file_name=? AND id_mahasiswa=? AND id_dosen=?";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, fileName);
            ps.setInt(2, activeMahasiswaId);
            ps.setInt(3, activeDosenId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Files.write(save.toPath(), rs.getBytes("file_data"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    
}
