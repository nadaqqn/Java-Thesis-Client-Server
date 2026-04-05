package app.session;

public class DosenSession {

    private int activeDosenId;
    private Integer activeMahasiswaId = null;
    
    private static int idUser;
    private static int idDosen;
    private static String nama;
    private static String role;

    public static void setSession(int userId, int dosenId, String namaDosen, String userRole) {
        idUser = userId;
        idDosen = dosenId;
        nama = namaDosen;
        role = userRole;
    }

    public static int getIdUser() {
        return idUser;
    }

    public static int getIdDosen() {
        return idDosen;
    }

    public static String getNama() {
        return nama;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        idUser = 0;
        idDosen = 0;
        nama = null;
        role = null;
    }
}
