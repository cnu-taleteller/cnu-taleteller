package com.cnu.taleteller.backend.domain.tool.repository;

import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, String> {

    @Query("SELECT f FROM UploadFile f WHERE f.book.bookId = :bookId")
    List<UploadFile> findAllByBookId(@Param("bookId") Long bookId);

}
