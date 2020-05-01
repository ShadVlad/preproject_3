package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDAO;
import web.model.Role;
import web.model.User;

import java.util.List;

@Transactional
@Service
public class UserServiceImp implements UserService {
    UserDAO userDAO;

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDAO = userDao;
    }

    @Override
    public void add(User user) {

        userDAO.add(user);
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public List<User> listAllUsers() {

        return userDAO.listAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userDAO.getUserByUserName(name);
    }

    @Override
    public Role getRoleByName(String name) {
        return userDAO.getRoleByName(name);
    }

    @Override
    public List<Role> rolesList() {
        return userDAO.listAllRoles();
    }

//    @Override
//    public Long count() {
//        return userDAO.count();
}
