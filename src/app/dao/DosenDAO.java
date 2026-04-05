package app.dao;

import app.database.DBConnect;
import app.models.Dosen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDAO {

    public List<Dosen> getAllDosen() {
        List<Dosen> list = new ArrayList<>();

        String query = "SELECT * FROM dosen LIMIT 8";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dosen d = new Dosen(
                        rs.getInt("id"),
                        rs.getString("nip"),
                        rs.getString("nama"),
                        rs.getString("prodi"),
                        rs.getString("umur"),
                        rs.getString("no_telp"),
                        rs.getString("email"),
                        rs.getBytes("foto"),
                        rs.getInt("jumlah_bimbingan"),
                        rs.getInt("kuota")
                );
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
    // ================= GET DOSEN BY ID =================
    public Dosen getDosenById(int idDosen) {
        Dosen d = null;

        String query = "SELECT * FROM dosen WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                d = new Dosen(
                        rs.getInt("id"),
                        rs.getString("nip"),              // ✅ BARU
                        rs.getString("nama"),
                        rs.getString("prodi"),
                        rs.getString("umur"),
                        rs.getString("no_telp"),
                        rs.getString("email"),
                        rs.getBytes("foto"),
                        rs.getInt("jumlah_bimbingan"),
                        rs.getInt("kuota")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }
}

