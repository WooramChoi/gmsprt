package net.adonika.gmsprt.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserForm;
import net.adonika.gmsprt.user.model.UserModify;
import net.adonika.gmsprt.user.model.UserVO;

public interface UserManager {
    
    UserVO addUser(UserAdd userAdd);
    
    UserVO modifyUser(Long seqUser, UserModify userModify);
    
    void removeUser(Long seqUser);
    
    UserVO findUser(Long seqUser);
    
    List<UserVO> findUser(UserForm userForm);
    
    Page<UserVO> findUser(UserForm userForm, Pageable pageable);
    
}
