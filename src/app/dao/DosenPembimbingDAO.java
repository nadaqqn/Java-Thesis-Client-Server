package app.dao;

import app.database.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DosenPembimbingDAO {

    public String[] getDosenPembimbing(int idMahasiswa, String jenisDosen) {
    String[] data = null;

    String sql =
        "SELECT d.nama, d.prodi, d.foto " +
        "FROM DosenPembimbing dp " +
        "JOIN dosen d ON dp.id_dosen = d.id " +
        "WHERE dp.id_mahasiswa = ? " +
        "AND dp.jenis_dosen = ?";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idMahasiswa);
        ps.setString(2, jenisDosen);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            data = new String[3];
            data[0] = rs.getString("nama");
            data[1] = rs.getString("prodi");
            data[2] = rs.getString("foto"); // path foto
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return data;
}

}
