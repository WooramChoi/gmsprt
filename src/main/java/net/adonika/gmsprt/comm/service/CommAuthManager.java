package net.adonika.gmsprt.comm.service;

import net.adonika.gmsprt.domain.CommAuthInfo;

public interface CommAuthManager {
    
    boolean isReadable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser);
    
    boolean isWritable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser);
    
    boolean isExecutable(CommAuthInfo commAuthInfo, Long seqGroup, Long seqUser);

}
