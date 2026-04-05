package app.dao;

public class BimbinganContext {
    public int idBimbingan;
    public int idMahasiswa;
    public int idDosen;
    public int pertemuanKe;

    public BimbinganContext(int idBimbingan, int idMahasiswa, int idDosen, int pertemuanKe) {
        this.idBimbingan = idBimbingan;
        this.idMahasiswa = idMahasiswa;
        this.idDosen = idDosen;
        this.pertemuanKe = pertemuanKe;
    }
}
