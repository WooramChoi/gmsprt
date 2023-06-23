package net.adonika.gmsprt.comm.service.impl;

import org.springframework.stereotype.Service;

import net.adonika.gmsprt.comm.service.CommAuthManager;
import net.adonika.gmsprt.domain.CommAuthInfo;

@Service("commAuthManager")
public class CommAuthManagerImpl implements CommAuthManager {

    @Override
    public boolean isReadable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser) {
        
        boolean isAccessable = false;
        if (!isAccessable && commAuthInfo.getOtherReadable()) {
            isAccessable = true;
        }
        if (!isAccessable && commAuthInfo.getGroupReadable()) {
            if (commAuthInfo.getSeqGroup() != null && commAuthInfo.getSeqGroup() == seqGroup) {
                isAccessable = true;
            }
        }
        if (!isAccessable && commAuthInfo.getUserReadable()) {
            if (commAuthInfo.getSeqCreate() != null && commAuthInfo.getSeqCreate() == seqUser) {
                isAccessable = true;
            }
        }
        
        return isAccessable;
    }

    @Override
    public boolean isWritable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser) {
        
        boolean isWriatable = false;
        if (!isWriatable && commAuthInfo.getOtherWritable()) {
            isWriatable = true;
        }
        if (!isWriatable && commAuthInfo.getGroupWritable()) {
            if (commAuthInfo.getSeqGroup() != null && commAuthInfo.getSeqGroup() == seqGroup) {
                isWriatable = true;
            }
        }
        if (!isWriatable && commAuthInfo.getUserWritable()) {
            if (commAuthInfo.getSeqCreate() != null && commAuthInfo.getSeqCreate() == seqUser) {
                isWriatable = true;
            }
        }
        
        return isWriatable;
    }

    @Override
    public boolean isExecutable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser) {

        boolean isExecutable = false;
        if (!isExecutable && commAuthInfo.getOtherExecutable()) {
            isExecutable = true;
        }
        if (!isExecutable && commAuthInfo.getGroupExecutable()) {
            if (commAuthInfo.getSeqGroup() != null && commAuthInfo.getSeqGroup() == seqGroup) {
                isExecutable = true;
            }
        }
        if (!isExecutable && commAuthInfo.getUserExecutable()) {
            if (commAuthInfo.getSeqCreate() != null && commAuthInfo.getSeqCreate() == seqUser) {
                isExecutable = true;
            }
        }
        
        return isExecutable;
    }

}
