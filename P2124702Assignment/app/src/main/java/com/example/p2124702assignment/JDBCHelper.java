package com.example.p2124702assignment;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBCHelper {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "mysql://doadmin:AVNS_FgLcXa-Ox1C9aaG50kg@db-mysql-sgp-kiasu-hawker-do-user-12896483-0.b.db.ondigitalocean.com:25060/catalogue?ssl-mode=REQUIRED";
    private static final String USER = "doadmin";
    private static final String PASS = "AVNS_FgLcXa-Ox1C9aaG50kg";

    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
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
                String query = "SELECT * FROM catalogue.hawker";
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
