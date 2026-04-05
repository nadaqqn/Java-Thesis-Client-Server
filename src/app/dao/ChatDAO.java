package app.dao;

import app.database.DBConnect;
import app.models.Chat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    // Ambil chat per mahasiswa-dosen
    public List<Chat> getChatByMahasiswaDosen(int idMahasiswa, int idDosen) {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM chat ORDER BY waktu ASC"; // Ambil semua dulu, bisa filter di Java
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int mhsId = rs.getInt("id_mahasiswa");
                int dosenId = rs.getInt("id_dosen");
                if (mhsId == idMahasiswa && dosenId == idDosen) {
                    Chat chat = new Chat();
                    chat.setIdChat(rs.getInt("id_chat"));
                    chat.setIdMahasiswa(mhsId);
                    chat.setIdDosen(dosenId);
                    chat.setPengirim(rs.getString("pengirim"));
                    chat.setPesan(rs.getString("pesan"));
                    chat.setWaktu(rs.getTimestamp("waktu"));
                    chat.setStatus(rs.getString("status"));
                    chat.setNamaFile(rs.getString("nama_file"));
                    Blob blob = rs.getBlob("file_data");
                    if (blob != null) {
                        chat.setFileData(blob.getBytes(1, (int) blob.length()));
                    }
                    chats.add(chat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }

    // Kirim chat tanpa file
    public void sendChat(Chat chat) {
        String sql = "INSERT INTO chat (id_mahasiswa, id_dosen, pengirim, pesan) VALUES (?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chat.getIdMahasiswa());
            ps.setInt(2, chat.getIdDosen());
            ps.setString(3, chat.getPengirim());
            ps.setString(4, chat.getPesan());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kirim chat dengan file
    public void sendChatWithFile(Chat chat) {
        String sql = "INSERT INTO chat (id_mahasiswa, id_dosen, pengirim, pesan, nama_file, file_data) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chat.getIdMahasiswa());
            ps.setInt(2, chat.getIdDosen());
            ps.setString(3, chat.getPengirim());
            ps.setString(4, chat.getPesan());
            ps.setString(5, chat.getNamaFile());
            ps.setBytes(6, chat.getFileData());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
