package model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface KategoriMapper {

    // CREATE
    @Insert("INSERT INTO kategori_sampah (nama_kategori) VALUES (#{namaKategori})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertKategori(@Param("namaKategori") String namaKategori);

    // READ ALL
    @Select("SELECT * FROM kategori_sampah")
    List<KategoriModel> getAllKategori();

    // READ BY ID
    @Select("SELECT * FROM kategori_sampah WHERE id = #{id}")
    KategoriModel getKategoriById(@Param("id") int id);

    // UPDATE
    @Update("UPDATE kategori_sampah SET nama_kategori = #{namaKategori} WHERE id = #{id}")
    int updateKategori(@Param("id") int id, @Param("namaKategori") String namaKategori);

    // DELETE
    @Delete("DELETE FROM kategori_sampah WHERE id = #{id}")
    int deleteKategori(@Param("id") int id);
}
