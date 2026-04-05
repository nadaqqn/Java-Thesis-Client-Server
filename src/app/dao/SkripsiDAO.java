package app.dao;

import app.database.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static javax.swing.text.html.HTML.Tag.SELECT;

public class SkripsiDAO {

    public void insertSkripsi(
            int idMahasiswa,
            int idDosen,
            String judul,
            String metode,
            String deskripsi,
            String tujuan,
            byte[] fileSkripsi
    ) throws Exception {

        String sql = "INSERT INTO skripsi "
                   + "(id_mahasiswa, id_dosen, judul_skripsi, metode_penelitian, "
                   + "deskripsi_singkat, tujuan_penelitian, file_skripsi, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 'DIAJUKAN')";

        Connection conn = DBConnect.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, idMahasiswa);
        stmt.setInt(2, idDosen);
        stmt.setString(3, judul);
        stmt.setString(4, metode);
        stmt.setString(5, deskripsi);
        stmt.setString(6, tujuan);
        stmt.setBytes(7, fileSkripsi);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    public int countByStatus(int idMahasiswa, String status) {
        String sql =
            "SELECT COUNT(*) " +
            "FROM skripsi " +
            "WHERE id_mahasiswa = ? " +
            "AND status = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
