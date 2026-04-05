package app.models;

public class MahasiswaItem {
    private int idMahasiswa;
    private String nama;

    public MahasiswaItem(int idMahasiswa, String nama) {
        this.idMahasiswa = idMahasiswa;
        this.nama = nama;
    }

    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public String toString() {
        return nama; // yang tampil di ComboBox
    }
}
