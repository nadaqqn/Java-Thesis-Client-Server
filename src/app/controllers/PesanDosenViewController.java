package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Hyperlink;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import app.session.DosenSession;
import javafx.scene.image.Image;
import javafx.scene.Node;
import java.io.ByteArrayInputStream;


import java.io.*;
import java.net.Socket;
import java.sql.*;

import app.database.DBConnect;
import java.nio.file.Files;

public class PesanDosenViewController {

    @FXML private AnchorPane root;
    @FXML private ImageView bgImage;
    @FXML private AnchorPane navbarContainer;

    @FXML private TextField txtInputPesan;
    @FXML private ImageView btnUploadFile;
    @FXML private ImageView ivSend;
    @FXML private VBox vboxChatContainer;
    @FXML private ScrollPane scrollChat;
    @FXML private VBox vboxChatRoomList;
    
    // Card Chat
    @FXML private HBox cardIdentitasPenerima;
    @FXML private VBox cardChat;
    @FXML private VBox cardButton;
    @FXML private Text txtNamaPenerima;
    @FXML private ImageView ivPenerima;


    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    

    // Chatroom
    private int activeDosenId;
    private Integer activeMahasiswaId = null;

    private String username = "Dosen1";

    public void initialize() {
        setupBackground();
        setupNavbar();
        
        // Chat
        txtNamaPenerima.setText("");
        ivPenerima.setImage(null);
        setupChatClient();
        clearChat();
        
        
        activeDosenId = DosenSession.getIdDosen();
        activeMahasiswaId = null;
        loadChatRoomsDosen();
        setChatSectionVisible(false);

        ivSend.setOnMouseClicked(e -> sendMessage());
        txtInputPesan.setOnAction(e -> sendMessage());
        btnUploadFile.setOnMouseClicked(e -> sendFile());

        root.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.setOnCloseRequest(event -> closeConnection());
                    }
                });
            }
        });
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/fxml/NavbarDosen.fxml"));
            Node navbar = loader.load();
            navbarContainer.getChildren().add(navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ================= IDENTITAS PENERIMA =================
    private void loadIdentitasMahasiswa() {

        String sql = "SELECT nama, foto FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeMahasiswaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNamaPenerima.setText(rs.getString("nama"));

                Blob fotoBlob = rs.getBlob("foto");
                if (fotoBlob != null) {
                    ivPenerima.setImage(
                        new Image(fotoBlob.getBinaryStream())
                    );
                } else {
                    ivPenerima.setImage(null); 
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
    
    private HBox createMahasiswaCard(
        int mahasiswaId,
        String nama,
        byte[] fotoBytes
    ) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color:#ffffff;" +
            "-fx-background-radius:10;" +
            "-fx-cursor:hand;"
        );

        ImageView iv = new ImageView();
        iv.setFitWidth(50);
        iv.setFitHeight(50);
        iv.setPreserveRatio(true);

        if (fotoBytes != null) {
            Image img = new Image(
                new ByteArrayInputStream(fotoBytes)
            );
            iv.setImage(img);
        }

        Text txt = new Text(nama);
        txt.setStyle("-fx-font-weight:bold;");

        card.getChildren().addAll(iv, txt);

        card.setOnMouseClicked(e -> {
            openChatRoom(mahasiswaId);
            showChatUI(); 
            loadIdentitasMahasiswa();
            highlightActiveCard(card);
        });

        return card;
    }

    // SETUP CHAT
    private void setupChatClient() {
        try {
            socket = new Socket("localhost", 12345);

            out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),
                true
            );

            in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8")
            );

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        final String finalLine = line;

                        Platform.runLater(() -> {
                            // Socket hanya handle CHAT TEXT
                            addChatMessage(finalLine);
                        });
                    }
                } catch (IOException e) {
                    System.out.println("Socket closed");
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage() {
        if (activeMahasiswaId == null) {
            System.out.println("Pilih mahasiswa terlebih dahulu");
            return;
        }

        String pesan = txtInputPesan.getText().trim();
        if (pesan.isEmpty()) return;

        try {
            DataOutputStream dataOut =
                new DataOutputStream(socket.getOutputStream());

            // FORMAT: idMahasiswa|idDosen|ROLE|pesan
            dataOut.writeUTF(
                activeMahasiswaId + "|" +
                activeDosenId + "|" +
                "DOSEN" + "|" +
                pesan
            );
            dataOut.flush();

            // SIMPAN KE DB
            saveChatToDB(pesan);

            // TAMPILKAN LANGSUNG
            addBubble(pesan, true, "DOSEN");

            txtInputPesan.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addChatMessage(String raw) {
        // FORMAT: idMahasiswa|idDosen|ROLE|pesan
        String[] parts = raw.split("\\|", 4);
        if (parts.length < 4) return;

        int msgMahasiswaId = Integer.parseInt(parts[0]);
        int msgDosenId = Integer.parseInt(parts[1]);
        String role = parts[2];
        String pesan = parts[3];

        // ❗ FILTER CHATROOM AKTIF
        if (activeMahasiswaId == null) return;
        if (msgMahasiswaId != activeMahasiswaId) return;
        if (msgDosenId != activeDosenId) return;

        boolean isSelf = role.equals("DOSEN");

        addBubble(pesan, isSelf, "DOSEN");
    }


    private void loadChatRoomsDosen() {
        vboxChatRoomList.getChildren().clear();

        String sql =
            "SELECT m.id_mahasiswa, m.nama, m.foto " +
            "FROM dosenpembimbing dp " +
            "JOIN mahasiswa m ON dp.id_mahasiswa = m.id_mahasiswa " +
            "WHERE dp.id_dosen = ? " +
            "ORDER BY dp.jenis_dosen";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeDosenId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int mahasiswaId = rs.getInt("id_mahasiswa");
                String nama = rs.getString("nama");
                byte[] fotoBytes = rs.getBytes("foto");

                HBox card = createMahasiswaCard(
                    mahasiswaId, nama, fotoBytes
                );

                vboxChatRoomList.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void highlightActiveCard(HBox active) {
        for (Node n : vboxChatRoomList.getChildren()) {
            n.setStyle(
                "-fx-background-color:#ffffff;" +
                "-fx-background-radius:10;"
            );
        }

        active.setStyle(
            "-fx-background-color:#edf7ed;" +
            "-fx-background-radius:10;"
        );
    }
    
    private void openChatRoom(int mahasiswaId) {
        activeMahasiswaId = mahasiswaId;
        vboxChatContainer.getChildren().clear();
        loadChatHistory();

        System.out.println(
            "Chat aktif: Dosen=" + activeDosenId +
            " Mahasiswa=" + activeMahasiswaId
        );
    }
    
    // MENAMPILKAN CHAT DAN FILE
    private void loadChatHistory() {
        if (activeMahasiswaId == null) return;

        String sql =
            "SELECT * FROM (" +

            "SELECT 'TEXT' AS tipe, pesan AS konten, NULL AS file_size, pengirim, waktu AS created_at " +
            "FROM chat WHERE id_dosen=? AND id_mahasiswa=? " +

            "UNION ALL " +

            "SELECT 'FILE' AS tipe, file_name AS konten, LENGTH(file_data) AS file_size, pengirim, uploaded_at AS created_at " +
            "FROM files WHERE id_dosen=? AND id_mahasiswa=? " +

            ") t ORDER BY created_at ASC";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeDosenId);
            ps.setInt(2, activeMahasiswaId);
            ps.setInt(3, activeDosenId);
            ps.setInt(4, activeMahasiswaId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                boolean isSelf = rs.getString("pengirim").equals("DOSEN");

                if (rs.getString("tipe").equals("TEXT")) {
                    addBubble(
                        rs.getString("konten"),
                        isSelf,
                        "DOSEN"
                    );
                } else {
                    addFileBubble(
                        rs.getString("konten"),
                        rs.getLong("file_size"),
                        isSelf,
                        rs.getTimestamp("created_at")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // MENGIRIM FILE
    private void sendFile() {
        if (activeMahasiswaId == null) return;

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
                ps.setString(5, "DOSEN");
                ps.executeUpdate();
            }

            // tampilkan langsung
            addFileBubble(
                file.getName(),
                Files.size(file.toPath()),
                true,
                new Timestamp(System.currentTimeMillis())
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // FORMAT KIRIM FILE
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
        bubble.setStyle(
            "-fx-background-color:" +
            (isSelf ? "#bcf0b1" : "#eeeeee") +
            "; -fx-background-radius:20;"
        );

        Text title = new Text("File");
        title.setStyle("-fx-font-weight:bold;");

        Text name = new Text(fileName);
        Text sizeTxt = new Text(size / 1024 + " KB");
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

    private void downloadFile(String fileName) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(fileName);
        File save = fc.showSaveDialog(root.getScene().getWindow());
        if (save == null) return;

        String sql =
            "SELECT file_data FROM files " +
            "WHERE file_name=? AND id_dosen=? AND id_mahasiswa=?";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, fileName);
            ps.setInt(2, activeDosenId);
            ps.setInt(3, activeMahasiswaId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Files.write(save.toPath(), rs.getBytes("file_data"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menyimpan pesan 
    private void saveChatToDB(String pesan) {
        if (activeMahasiswaId == null) return;

        String sql =
            "INSERT INTO chat(id_mahasiswa, id_dosen, pengirim, pesan) " +
            "VALUES(?,?,?,?)";

        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, activeMahasiswaId);
            ps.setInt(2, activeDosenId);
            ps.setString(3, "DOSEN");
            ps.setString(4, pesan);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bubble Text
    private void addBubble(String message, boolean isSelf, String role) {
        HBox wrapper = new HBox();
        wrapper.setPadding(new Insets(5, 10, 5, 10));
        wrapper.setAlignment(isSelf ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        VBox bubble = new VBox();
        bubble.setPadding(new Insets(8, 12, 8, 12));
        bubble.setMaxWidth(320);

        String bgColor;
        if (isSelf) {
            bgColor = role.equals("MAHASISWA") ? "#2979FF" : "#2E7D32";
        } else {
            bgColor = "#616161";
        }

        bubble.setStyle(
            "-fx-background-color:" + bgColor + ";" +
            "-fx-background-radius:12;"
        );

        Text txt = new Text(message);
        txt.setWrappingWidth(280);
        txt.setFill(javafx.scene.paint.Color.WHITE);

        bubble.getChildren().add(txt);
        wrapper.getChildren().add(bubble);

        vboxChatContainer.getChildren().add(wrapper);
        scrollChat.setVvalue(1.0);
    }
    
    //Kosongkan chatroom
    private void clearChat() {
        vboxChatContainer.getChildren().clear();
    }
    


    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
