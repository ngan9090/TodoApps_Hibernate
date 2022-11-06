package net.javaguides.todoapp.DaoHibernate;

import net.javaguides.todoapp.model.Todo;

import java.sql.SQLException;
import java.util.List;

public interface TodoHibernate {

        void insertTodo(Todo todo) throws SQLException;

        Todo selectTodo(long todoId);

        List<Todo> selectAllTodos();

        boolean deleteTodo(int id) throws SQLException;

        boolean updateTodo(Todo todo) throws SQLException;

}
