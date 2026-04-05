package app.models;

public class DosenProfil {

    private int id;
    private String nip;
    private String nama;
    private String prodi;
    private String umur;
    private String noTelp;
    private String email;
    private byte[] foto;
    private int jumlahBimbingan;
    private int kuota;

    // ✅ DEFAULT CONSTRUCTOR (WAJIB UNTUK DAO)
    public DosenProfil() {
    }

    // ✅ CONSTRUCTOR LENGKAP
    public DosenProfil(
        int id,
        String nip,
        String nama,
        String prodi,
        String umur,
        String noTelp,
        String email,
        byte[] foto,
        int jumlahBimbingan,
        int kuota
    ) {
        this.id = id;
        this.nip = nip;
        this.nama = nama;
        this.prodi = prodi;
        this.umur = umur;
        this.noTelp = noTelp;
        this.email = email;
        this.foto = foto;
        this.jumlahBimbingan = jumlahBimbingan;
        this.kuota = kuota;
    }

    // ===== GETTER =====
    public int getId() { return id; }
    public String getNip() { return nip; }
    public String getNama() { return nama; }
    public String getProdi() { return prodi; }
    public String getUmur() { return umur; }
    public String getNoTelp() { return noTelp; }
    public String getEmail() { return email; }
    public byte[] getFoto() { return foto; }
    public int getJumlahBimbingan() { return jumlahBimbingan; }
    public int getKuota() { return kuota; }

    // ===== SETTER (MINIMAL YANG DIPAKAI DAO) =====
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setProdi(String prodi) { this.prodi = prodi; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}
