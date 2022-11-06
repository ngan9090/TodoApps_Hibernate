package net.javaguides.todoapp.DaoHibernate;

import net.javaguides.todoapp.model.User;
import net.javaguides.todoapp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;

public class UserHibernate {

    public boolean registerEmployee(User employee) throws ClassNotFoundException {
        boolean status = false;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            String hql = "INSERT INTO users  (first_name, last_name, username, password) VALUES "
                    + " (:first_name, :last_name, :username, :password);";
            Query query = session.createSQLQuery(hql);
            query.setParameter("first_name",employee.getFirstName());
            query.setParameter("last_name", employee.getLastName());
            query.setParameter("username",employee.getUsername());
            query.setParameter("password",employee.getPassword());
            System.out.println(query);
            status = true;
        } catch (Exception e) {
            // process sql exception
            e.printStackTrace();
        }

        //rolling back
        tx.rollback();
        return status;
    }
}
