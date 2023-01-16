package com.example.p2124702assignment;

import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.util.Log;
import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBCHelper {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/items";
    private static final String USER = "root";
    private static final String PASS = "Password@123";

    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/items","root","Password@123");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Integer> getID() {
        ArrayList<Integer> idList = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM items_table";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    idList.add(id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return idList;
    }

    public ArrayList<String> getName() {
        ArrayList<String> list = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM catalogue.hawker";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String rs = resultSet.getString("name");
                    list.add(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public ArrayList<Integer> getStall() {
        ArrayList<Integer> list = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM catalogue.hawker";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int rs = resultSet.getInt("stall_amount");
                    list.add(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public ArrayList<String> getLocation() {
        ArrayList<String> list = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM catalogue.hawker";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String rs = resultSet.getString("location");
                    list.add(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public ArrayList<String> getStatus() {
        ArrayList<String> list = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM catalogue.hawker";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String rs = resultSet.getString("status");
                    list.add(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public ArrayList<String> getImage() {
        ArrayList<String> list = null;
        Connection connection = JDBCHelper.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM catalogue.hawker";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String rs = resultSet.getString("image");
                    list.add(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
