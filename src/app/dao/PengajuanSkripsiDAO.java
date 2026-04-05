package app.dao;

import app.database.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PengajuanSkripsiDAO {

    public void insertPengajuan(int idSkripsi, int idDosen) throws Exception {

        // Java 8: string biasa
        String sql = "INSERT INTO pengajuan_skripsi "
                   + "(id_skripsi, id_dosen) "
                   + "VALUES (?, ?)";

        Connection conn = DBConnect.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, idSkripsi);
        stmt.setInt(2, idDosen);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
