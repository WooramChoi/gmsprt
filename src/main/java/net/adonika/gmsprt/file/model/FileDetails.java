package net.adonika.gmsprt.file.model;

import net.adonika.gmsprt.comm.model.CommDetails;

public class FileDetails extends CommDetails {
    private Long seqFile;
    private String name;
    private Long size;
    private Boolean use;
    private String refTable;
    private Long refSeq;
    private String alias;
    private String ext;
    
    public String getFilename() {
        // TODO ext 가 null 인 케이스를 인정해야할까? 일단 유연하게 해놓자
        String filename = alias;
        if (ext != null && !ext.isEmpty()) {
            filename += "." + ext;
        }
        return filename;
    }

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
