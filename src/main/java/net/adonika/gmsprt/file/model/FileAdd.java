package net.adonika.gmsprt.file.model;

import javax.validation.constraints.NotBlank;

public class FileAdd {
    
    private Long seqUser;
    
    private Long seqGroup;
    
    @NotBlank
    private String refTable;
    
    private Long refSeq;

    public Long getSeqUser() {
        return seqUser;
    }

    public void setSeqUser(Long seqUser) {
        this.seqUser = seqUser;
    }

    public Long getSeqGroup() {
        return seqGroup;
    }

    public void setSeqGroup(Long seqGroup) {
        this.seqGroup = seqGroup;
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

}
