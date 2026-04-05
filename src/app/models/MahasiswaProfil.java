package app.models;

public class MahasiswaProfil {
    private int idMahasiswa;
    private String nama;
    private String nim;
    private String email;
    private String noHp;

    public MahasiswaProfil(int idMahasiswa, String nama, String nim, String email, String noHp) {
        this.idMahasiswa = idMahasiswa;
        this.nama = nama;
        this.nim = nim;
        this.email = email;
        this.noHp = noHp;
    }

    public int getIdMahasiswa() { return idMahasiswa; }
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public String getEmail() { return email; }
    public String getNoHp() { return noHp; }

    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
}
