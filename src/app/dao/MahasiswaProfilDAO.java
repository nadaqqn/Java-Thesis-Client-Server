package app.dao;

import app.database.DBConnect;
import app.models.MahasiswaProfil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MahasiswaProfilDAO {

    public static MahasiswaProfil getByIdMahasiswa(int idMahasiswa) {

        String sql =
            "SELECT m.id_mahasiswa, m.nama, m.nim, m.no_telepon, u.email " +
            "FROM mahasiswa m " +
            "JOIN users u ON m.id_user = u.id_user " +
            "WHERE m.id_mahasiswa = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MahasiswaProfil(
                        rs.getInt("id_mahasiswa"),
                        rs.getString("nama"),
                        rs.getString("nim"),
                        rs.getString("email"),
                        rs.getString("no_telepon")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateProfil(MahasiswaProfil m) {

        String sqlMahasiswa =
            "UPDATE mahasiswa " +
            "SET nama = ?, no_telepon = ? " +
            "WHERE id_mahasiswa = ?";

        String sqlUser =
            "UPDATE users " +
            "SET email = ? " +
            "WHERE id_user = (" +
            "   SELECT id_user FROM mahasiswa WHERE id_mahasiswa = ?" +
            ")";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlMahasiswa);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUser)) {

                ps1.setString(1, m.getNama());
                ps1.setString(2, m.getNoHp());
                ps1.setInt(3, m.getIdMahasiswa());
                ps1.executeUpdate();

                ps2.setString(1, m.getEmail());
                ps2.setInt(2, m.getIdMahasiswa());
                ps2.executeUpdate();

                conn.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
