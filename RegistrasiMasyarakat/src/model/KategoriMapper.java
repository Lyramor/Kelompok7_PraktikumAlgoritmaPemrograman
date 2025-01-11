package model;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface KategoriMapper {
    // CREATE
    @Insert("INSERT INTO kategori_sampah (nama_kategori) VALUES (#{namaKategori})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(KategoriModel kategori);

    // READ ALL
    @Select("SELECT * FROM kategori_sampah")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "namaKategori", column = "nama_kategori")
    })
    List<KategoriModel> getAllKategori();

    // READ BY ID
    @Select("SELECT * FROM kategori_sampah WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "namaKategori", column = "nama_kategori")
    })
    KategoriModel getKategoriById(@Param("id") int id);

    // UPDATE
    @Update("UPDATE kategori_sampah SET nama_kategori = #{namaKategori} WHERE id = #{id}")
    void update(KategoriModel kategori);

    // DELETE
    @Delete("DELETE FROM kategori_sampah WHERE id = #{id}")
    void delete(@Param("id") int id);
}