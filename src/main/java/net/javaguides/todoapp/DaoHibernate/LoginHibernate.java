package net.javaguides.todoapp.DaoHibernate;

import net.javaguides.todoapp.model.LoginBean;
import net.javaguides.todoapp.model.User;
import net.javaguides.todoapp.utils.HibernateUtil;
import net.javaguides.todoapp.utils.JDBCUtils;
import net.javaguides.todoapp.utils.MD5Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class LoginHibernate {
    Session session;

    public boolean validate(LoginBean loginbean) throws ClassNotFoundException {
        boolean status = false;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            String hql = "SELECT * FROM users Where username = :username and password = :password";
            Query query = session.createSQLQuery(hql);
            query.setParameter("username",loginbean.getUsername());
            query.setParameter("password",MD5Util.encrypt(loginbean.getPassword()));
            System.out.println("thu");
            System.out.println(query);
            if(query.getSingleResult() != null) status = true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            // process sql exception
            e.printStackTrace();
        }

        //rolling back
        tx.rollback();
            return status;
        }
    }
