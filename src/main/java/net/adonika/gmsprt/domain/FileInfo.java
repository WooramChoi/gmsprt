package net.adonika.gmsprt.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import net.adonika.gmsprt.util.BooleanYnConverter;

@Entity
@Table(
        name = "FILE_INFO",
        indexes = {
                @Index(columnList = "alias", unique = true)
        }
)
public class FileInfo extends CommAuthInfo {
    
    // TODO 순서 컬럼?
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ_FILE", length = 10)
    private Long seqFile;
    
    @Column(length = 120, updatable = false)
    private String name;
    
    @Column(length = 12, updatable = false)
    private Long size;
    
    @Column(name="yn_use", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Convert(converter = BooleanYnConverter.class)
    private Boolean use = true;
    
    // SELECT * FROM ${table} WHERE ${table.key} = ${ref}
    @Column(length = 30)
    private String refTable;
    
    // table 이 동적으로 변하기때문에 FK 를 사용하지 않는다.
    @Column(length = 10)
    private Long refSeq;
    
    // Real location: ${path}${alias}.${ext}
    @Column(length = 240)
    private String path;
    
    // 영문 소문자 10 글자
    @Column(length = 10, updatable = false)
    private String alias;
    
    @Column(length = 6, updatable = false)
    private String ext;

    public Long getSeqFile() {
        return seqFile;
    }

    public void setSeqFile(Long seqFile) {
        this.seqFile = seqFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public Long getRefSeq() {
        return refSeq;
    }

    public void setRefSeq(Long refSeq) {
        this.refSeq = refSeq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

}
