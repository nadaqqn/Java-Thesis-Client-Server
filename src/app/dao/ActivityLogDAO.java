package app.dao;

import app.database.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActivityLogDAO {

    public static void saveLog(
            int userId,
            String username,
            String role,
            String activity
    ) {
        String sql = "INSERT INTO activity_log (user_id, username, role, activity) VALUES (?,?,?,?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, username);
            ps.setString(3, role);
            ps.setString(4, activity);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
