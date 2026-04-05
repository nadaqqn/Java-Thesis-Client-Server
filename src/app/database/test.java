
package app.database;


public class test {
    public static void main(String[] args) {
        System.out.println("Testing Koneksi ke Database...");

        try {
            if (DBConnect.getConnection() != null) {
                System.out.println("Koneksi Berhasil!");
            } else {
                System.out.println("Koneksi Gagal!");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menghubungkan ke database:");
            e.printStackTrace();
        }
    }
}
