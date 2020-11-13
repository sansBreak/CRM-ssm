package per.liu.crm.settings.dao;

import per.liu.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {

    User login(Map<String, String> map);
}
