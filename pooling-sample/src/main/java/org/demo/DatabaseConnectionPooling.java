package org.demo;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionPooling {

    public static void main(String[] args) {
        BasicDataSource mysqlDataSource = new BasicDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        mysqlDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        mysqlDataSource.setUsername("root");
        mysqlDataSource.setPassword("root123");

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new SQLQueryWorker(mysqlDataSource));
            thread.start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            mysqlDataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class SQLQueryWorker implements Runnable {
        private BasicDataSource mysqlDataSource;

        SQLQueryWorker(BasicDataSource mysqlDataSource) {
            this.mysqlDataSource = mysqlDataSource;
        }

        public void run() {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                connection = mysqlDataSource.getConnection();
                preparedStatement = connection.prepareStatement("SELECT id,name FROM EMPLOYEE");
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    System.out
                            .println("Employ ID " + resultSet.getString("id") + " Name " + resultSet.getString("name"));
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
}
