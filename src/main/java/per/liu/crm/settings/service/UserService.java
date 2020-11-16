package per.liu.crm.settings.service;

import per.liu.crm.exception.LoginException;
import per.liu.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
