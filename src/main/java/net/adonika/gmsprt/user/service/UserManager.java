package net.adonika.gmsprt.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserSearch;
import net.adonika.gmsprt.user.model.UserModify;
import net.adonika.gmsprt.user.model.UserDetails;

public interface UserManager {
    
    UserDetails addUser(UserAdd userAdd);
    
    UserDetails modifyUser(Long seqUser, UserModify userModify);
    
    void removeUser(Long seqUser);
    
    UserDetails findUser(Long seqUser);
    
    List<UserDetails> findUser(UserSearch userSearch);
    
    Page<UserDetails> findUser(UserSearch userSearch, Pageable pageable);
    
}
