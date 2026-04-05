package app.dao;

import app.database.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BimbinganDAO {

    public String[] getBimbinganTerakhir(int idMahasiswa, String jenisDosen) {
        String[] data = null;

        String sql =
            "SELECT b.judul, b.pertemuan_ke, b.jam, b.catatan_revisi, b.deadline_revisi " +
            "FROM DosenPembimbing dp " +
            "JOIN Bimbingan b ON dp.id_dosen = b.id_dosenpembimbing " +
            "WHERE dp.id_mahasiswa = ? " +
            "AND dp.jenis_dosen = ? " +
            "ORDER BY b.id_bimbingan DESC LIMIT 1"; // ambil record terakhir

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ps.setString(2, jenisDosen);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                data = new String[5]; // 5 kolom
                data[0] = rs.getString("judul");
                data[1] = String.valueOf(rs.getInt("pertemuan_ke"));
                data[2] = rs.getString("jam"); // bisa pakai getTime() juga
                data[3] = rs.getString("catatan_revisi");
                data[4] = rs.getString("deadline_revisi");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data; // jangan null
    }
}
