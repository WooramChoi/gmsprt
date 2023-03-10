package net.adonika.gmsprt.user.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.adonika.gmsprt.comm.dao.CommRepository;
import net.adonika.gmsprt.domain.UserProfileInfo;

@Repository
public interface UserProfileDao extends CommRepository<UserProfileInfo, Long> {

    Optional<UserProfileInfo> findByProviderAndSid(String provider, String sid);

}
