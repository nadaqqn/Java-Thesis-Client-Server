package app.dao;

import app.database.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DokumenPenelitianDAO {

    public void insertDokumen(int idSkripsi, String namaFile, String pathFile) throws Exception {

        // Java 8: string biasa
        String sql = "INSERT INTO dokumen_penelitian "
                   + "(id_skripsi, nama_file, path_file) "
                   + "VALUES (?, ?, ?)";

        Connection conn = DBConnect.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, idSkripsi);
        stmt.setString(2, namaFile);
        stmt.setString(3, pathFile);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
