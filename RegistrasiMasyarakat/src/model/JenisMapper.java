package model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface JenisMapper {

    // CREATE
    @Insert("INSERT INTO jenis_sampah (nama_jenis, kategori_id) VALUES (#{namaJenis}, #{kategoriId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertJenis(@Param("namaJenis") String namaJenis, @Param("kategoriId") int kategoriId);

    // READ ALL
    @Select("SELECT js.id, js.nama_jenis, js.kategori_id, ks.nama_kategori " +
            "FROM jenis_sampah js " +
            "LEFT JOIN kategori_sampah ks ON js.kategori_id = ks.id")
    List<JenisModel> getAllJenis();

    // READ BY ID
    @Select("SELECT js.id, js.nama_jenis, js.kategori_id, ks.nama_kategori " +
            "FROM jenis_sampah js " +
            "LEFT JOIN kategori_sampah ks ON js.kategori_id = ks.id " +
            "WHERE js.id = #{id}")
    JenisModel getJenisById(@Param("id") int id);

    // UPDATE
    @Update("UPDATE jenis_sampah SET nama_jenis = #{namaJenis}, kategori_id = #{kategoriId} WHERE id = #{id}")
    int updateJenis(@Param("id") int id, @Param("namaJenis") String namaJenis, @Param("kategoriId") int kategoriId);

    // DELETE
    @Delete("DELETE FROM jenis_sampah WHERE id = #{id}")
    int deleteJenis(@Param("id") int id);
}
