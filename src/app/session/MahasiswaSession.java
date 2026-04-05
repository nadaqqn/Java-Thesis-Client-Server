package app.session;

public class MahasiswaSession {

    private int activeMahasiswaId;
    private Integer activeDosenId = null;

    private static int idUser;
    private static int idMahasiswa;
    private static String nama;
    private static String role;

    public static void setSession(int userId, int mahasiswaId, String namaMahasiswa, String userRole) {
        idUser = userId;
        idMahasiswa = mahasiswaId;
        nama = namaMahasiswa;
        role = userRole;
    }

    public static int getIdUser() {
        return idUser;
    }

    public static int getIdMahasiswa() {
        return idMahasiswa;
    }

    public static String getNama() {
        return nama;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        idUser = 0;
        idMahasiswa = 0;
        nama = null;
        role = null;
    }
}
