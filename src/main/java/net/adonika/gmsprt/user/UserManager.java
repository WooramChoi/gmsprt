package net.adonika.gmsprt.user;

import net.adonika.gmsprt.domain.UserInfo;

import java.util.List;

public interface UserManager {

    UserInfo create(String name, String email, String urlPicture);

    UserInfo getUserInfo(Long seqUser);

    UserInfo update(UserInfo userInfo, List<String> ignores);

}
