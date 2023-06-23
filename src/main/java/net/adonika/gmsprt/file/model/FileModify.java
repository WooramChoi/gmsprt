package net.adonika.gmsprt.file.model;

import net.adonika.gmsprt.comm.model.CommModify;

public class FileModify extends CommModify {
    
    private Boolean use;
    
    private String refTable;
    
    private Long refSeq;
    
    // TODO CommAuthInfo 관련 어떻게 할래?
    // CommAuthInfo 를 상속받는 domain 이라면, 반드시 관련기능이 구현되어야하는 복잡함이 생겼는데.

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
    
}
