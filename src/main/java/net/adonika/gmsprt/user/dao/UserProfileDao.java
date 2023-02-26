package net.adonika.gmsprt.user.dao;

import net.adonika.gmsprt.comm.CommRepository;
import net.adonika.gmsprt.domain.UserProfileInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileDao extends CommRepository<UserProfileInfo, Long> {

    UserProfileInfo findByProviderAndSid(String provider, String sid);

}
