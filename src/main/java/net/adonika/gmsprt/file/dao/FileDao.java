package net.adonika.gmsprt.file.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.adonika.gmsprt.comm.dao.CommRepository;
import net.adonika.gmsprt.domain.FileInfo;

@Repository
public interface FileDao extends CommRepository<FileInfo, Long> {
    
    Optional<FileInfo> findByAlias(String alias);
    
    Optional<FileInfo> findByAliasAndUse(String alias, Boolean use);
    
    List<FileInfo> findByRefTableAndRefSeq(String refTable, Long refSeq);

}
