package org.demo;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionPooling {

    public static void main(String[] args) {
        BasicDataSource mysqlDataSource = new BasicDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/dbname");
        mysqlDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        mysqlDataSource.setUsername("username");
        mysqlDataSource.setPassword("password");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = mysqlDataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT id,name FROM EMPLOYEE");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Employ ID " + resultSet.getString("id") + " Name " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }

    }
}
