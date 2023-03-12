package net.adonika.gmsprt.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommInfo {

    @CreatedBy
    private Long seqCreate;

    @LastModifiedBy
    private Long seqUpdate;

    @CreatedDate
    @Column(nullable=false, updatable = false)
    private Date dtCreate;

    @LastModifiedDate
    @Column(nullable=false)
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
