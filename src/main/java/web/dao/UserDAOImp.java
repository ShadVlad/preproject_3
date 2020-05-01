package web.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImp implements UserDAO {
    //private SessionFactory sessionFactory;
    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        if(user != null) {
            Query query = (Query) entityManager.createQuery("DELETE FROM User user WHERE user.id = :paramId");
            query.setParameter("paramId",user.getId());
            query.executeUpdate();

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listAllUsers() {
        return entityManager.createQuery("SELECT user FROM User user").getResultList();
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByUserName(String userName) {
        Query query = (Query) entityManager.createQuery("SELECT user FROM User user WHERE user.userName=:paramUserName");
        query.setParameter("paramUserName", userName);
        return (User) query.uniqueResult();
    }

    @Override
    public Role getRoleByName(String role) {
        Query query = (Query) entityManager.createQuery("SELECT role FROM Role role WHERE role.role=:paramRole");
        query.setParameter("paramRole", role);
        return (Role) query.uniqueResult();
    }

    @Override
    public List<Role> listAllRoles() {
        return entityManager.createQuery("SELECT role FROM Role role").getResultList();
    }
}


