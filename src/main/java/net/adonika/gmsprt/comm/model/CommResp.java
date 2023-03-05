package net.adonika.gmsprt.comm.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommResp {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date dtCreate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date dtUpdate;

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
