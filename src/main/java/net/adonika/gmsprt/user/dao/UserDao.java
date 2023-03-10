package net.adonika.gmsprt.user.dao;

import net.adonika.gmsprt.comm.dao.CommRepository;
import net.adonika.gmsprt.domain.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CommRepository<UserInfo, Long> {

}
