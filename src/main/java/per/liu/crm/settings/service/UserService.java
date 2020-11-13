package per.liu.crm.settings.service;

import per.liu.crm.exception.LoginException;
import per.liu.crm.settings.domain.User;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
