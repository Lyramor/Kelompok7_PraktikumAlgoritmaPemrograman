package model;

public class JenisModel {
    private int id;
    private String namaJenis;
    private int kategoriId;
    private String namaKategori; // Optional: digunakan jika ingin menampilkan nama kategori

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    @Override
    public String toString() {
        return "JenisModel{" +
                "id=" + id +
                ", namaJenis='" + namaJenis + '\'' +
                ", kategoriId=" + kategoriId +
                ", namaKategori='" + namaKategori + '\'' +
                '}';
    }
}
