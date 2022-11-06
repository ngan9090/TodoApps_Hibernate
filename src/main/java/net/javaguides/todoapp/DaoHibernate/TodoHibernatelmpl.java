package net.javaguides.todoapp.DaoHibernate;

import net.javaguides.todoapp.model.Todo;
import net.javaguides.todoapp.utils.HibernateUtil;
import net.javaguides.todoapp.utils.JDBCUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodoHibernatelmpl implements TodoHibernate {

    Session session;

    Transaction transaction = null;
    private static final String INSERT_TODOS_SQL = "INSERT INTO todos"
            + "  (title, username, description, target_date,  is_done) VALUES " + " (:title, :username, :description, :target_date, :is_done);";

    private static final String SELECT_TODO_BY_ID = "select id,title,username,description,target_date,is_done from todos where id = :id";
    private static final String SELECT_ALL_TODOS = "select * from todos";
    private static final String DELETE_TODO_BY_ID = "delete from todos where id = :id";
    private static final String UPDATE_TODO = "update todos set title = :title, username= :username, description = :description, target_date = :target_date, is_done = :is_done where id = :id";

    public TodoHibernatelmpl() {
    }

    @Override
    public void insertTodo(Todo todo) throws SQLException {
        System.out.println(INSERT_TODOS_SQL);
        Session session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        try {
            session.save(todo);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public Todo selectTodo(long todoId) {
        Todo todo = null;
        // Step 1: Establishing a Connection
      try{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Todo> criteriaQuery = builder.createQuery(Todo.class);
        Root<Todo> rootSelectToDo = criteriaQuery.from(Todo.class);
        criteriaQuery.select(rootSelectToDo).where(builder.equal(rootSelectToDo.get("id"), todoId));
        todo = session.createQuery(criteriaQuery).getSingleResult();
        Todo newTodo = new Todo();
          newTodo.setTitle(todo.getTitle());
          newTodo.setDescription(todo.getDescription());
          newTodo.setStatus(todo.getStatus());
          newTodo.setTargetDate(todo.getTargetDate());
    } catch (Exception e) {
        e.printStackTrace();
    }
        return todo;
    }

    @Override
    public List<Todo> selectAllTodos() {
        List<Todo> todos = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Todo> criteriaQuery = builder.createQuery(Todo.class);
            Root<Todo> root = criteriaQuery.from(Todo.class);
            todos = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todos;
    }

    @Override
    public boolean deleteTodo(int id) throws SQLException {
        boolean rowDeleted;
        rowDeleted = false;
        try {
            List<Todo> todos = session.createSQLQuery(DELETE_TODO_BY_ID)
                    .setParameter("id", id)
                    .list();
            for (Todo todo : todos) {
                session.delete(todo);
            }
            session.delete(id);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rowDeleted;
    }

    @Override
    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated;
        rowUpdated = false;
        try{
            Query q = session.createSQLQuery(UPDATE_TODO);
            q.setParameter("title",todo.getTitle() );
            q.setParameter("username",todo.getUsername() );
            q.setParameter("description",todo.getDescription() );
            q.setParameter("target_date",todo.getTargetDate());
            q.setParameter("is_done",todo.getStatus() );
            q.setParameter("id",todo.getId() );
           Todo stockTran = (Todo)q.getSingleResult();
           System.out.println(stockTran);
           System.out.println(todo.getDescription());
            stockTran.setTitle(todo.getTitle());
            stockTran.setDescription(todo.getDescription());
            stockTran.setUsername(todo.getUsername());
            stockTran.setStatus(todo.getStatus());
            stockTran.setTargetDate(todo.getTargetDate());
            session.update(stockTran);
            rowUpdated = q.executeUpdate() > 0;
            transaction.commit();}
        catch(Exception e){
            e.printStackTrace();
        }
        return rowUpdated;
    }
}
