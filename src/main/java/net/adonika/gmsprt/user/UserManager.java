package net.adonika.gmsprt.user;

import net.adonika.gmsprt.domain.UserInfo;

import java.util.List;

public interface UserManager {

    UserInfo create(UserInfo userInfo);

    UserInfo getUserInfo(Long seqUser);

    UserInfo update(UserInfo userInfo, List<String> ignores);

}
