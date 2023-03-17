package net.adonika.gmsprt.security.dao;

import net.adonika.gmsprt.comm.dao.CommRepository;
import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.domain.AuthTokenInfo;

public interface AuthTokenDao extends CommRepository<AuthTokenInfo, AuthTokenId> {

}
