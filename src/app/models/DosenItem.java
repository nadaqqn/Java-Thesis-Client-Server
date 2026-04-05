package app.models;

public class DosenItem {
    private int id;
    private String nama;

    public DosenItem(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return nama; // tampil di ComboBox
    }
}
