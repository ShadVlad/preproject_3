package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void update(User user);
    void delete(User user);
    List<User> listAllUsers();
    User getUserById(Long id);
    User getUserByName(String name);
    Role getRoleByName(String name);
    List<Role> rolesList();
    //public Long count();
}
