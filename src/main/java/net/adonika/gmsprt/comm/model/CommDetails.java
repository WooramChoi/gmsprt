package net.adonika.gmsprt.comm.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommDetails {
    private Long seqCreate;
    private Long seqUpdate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date dtCreate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date dtUpdate;

    public Long getSeqCreate() {
        return seqCreate;
    }

    public void setSeqCreate(Long seqCreate) {
        this.seqCreate = seqCreate;
    }

    public Long getSeqUpdate() {
        return seqUpdate;
    }

    public void setSeqUpdate(Long seqUpdate) {
        this.seqUpdate = seqUpdate;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(Date dtUpdate) {
        this.dtUpdate = dtUpdate;
    }
}
