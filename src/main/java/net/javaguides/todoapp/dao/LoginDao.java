package net.javaguides.todoapp.dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.javaguides.todoapp.model.LoginBean;
import net.javaguides.todoapp.utils.JDBCUtils;
import net.javaguides.todoapp.utils.MD5Util;

public class LoginDao {

	public boolean validate(LoginBean loginBean) throws ClassNotFoundException {
		boolean status = false;
		try (Connection connection = JDBCUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection
						.prepareStatement("select * from users where username = ? and password = ? ")) {
			preparedStatement.setString(1, loginBean.getUsername());
			preparedStatement.setString(2, MD5Util.encrypt(loginBean.getPassword()));

			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			status = rs.next();
			System.out.print("thus");
			System.out.print(status);
			System.out.print("thus");

		} catch (SQLException | NoSuchAlgorithmException e) {
			// process sql exception
			JDBCUtils.printSQLException((SQLException) e);
		}
		return status;
	}
}
