package model;
public class KategoriModel {
    private int id;
    private String namaKategori;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    @Override
    public String toString() {
        return "KategoriModel{" +
                "id=" + id +
                ", namaKategori='" + namaKategori + '\'' +
                '}';
    }
}
