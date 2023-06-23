package net.adonika.gmsprt.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CommAuthInfo extends CommInfo {
    
    @Column(length = 10)
    private Long seqGroup;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean userReadable = true;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean userWritable = true;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean userExecutable = false;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean groupReadable = true;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean groupWritable = true;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean groupExecutable = false;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean otherReadable = true;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean otherWritable = false;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean otherExecutable = false;

    public Long getSeqGroup() {
        return seqGroup;
    }

    public void setSeqGroup(Long seqGroup) {
        this.seqGroup = seqGroup;
    }

    public Boolean getUserReadable() {
        return userReadable;
    }

    public void setUserReadable(Boolean userReadable) {
        this.userReadable = userReadable;
    }

    public Boolean getUserWritable() {
        return userWritable;
    }

    public void setUserWritable(Boolean userWritable) {
        this.userWritable = userWritable;
    }

    public Boolean getUserExecutable() {
        return userExecutable;
    }

    public void setUserExecutable(Boolean userExecutable) {
        this.userExecutable = userExecutable;
    }

    public Boolean getGroupReadable() {
        return groupReadable;
    }

    public void setGroupReadable(Boolean groupReadable) {
        this.groupReadable = groupReadable;
    }

    public Boolean getGroupWritable() {
        return groupWritable;
    }

    public void setGroupWritable(Boolean groupWritable) {
        this.groupWritable = groupWritable;
    }

    public Boolean getGroupExecutable() {
        return groupExecutable;
    }

    public void setGroupExecutable(Boolean groupExecutable) {
        this.groupExecutable = groupExecutable;
    }

    public Boolean getOtherReadable() {
        return otherReadable;
    }

    public void setOtherReadable(Boolean otherReadable) {
        this.otherReadable = otherReadable;
    }

    public Boolean getOtherWritable() {
        return otherWritable;
    }

    public void setOtherWritable(Boolean otherWritable) {
        this.otherWritable = otherWritable;
    }

    public Boolean getOtherExecutable() {
        return otherExecutable;
    }

    public void setOtherExecutable(Boolean otherExecutable) {
        this.otherExecutable = otherExecutable;
    }

}
