package ru.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class DataBase {
    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;

    static void connect() throws SQLException, ClassNotFoundException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db_container.s3db");
    }

    static void createTableBox() {
        try {
            statmt.execute(
                    "CREATE TABLE if not exists 'Box' (" +
                            "'id' INTEGER PRIMARY KEY, " +
                            "'contained_in' INTEGER);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createTableItem() {
        try {
            statmt.execute(
                    "CREATE TABLE if not exists 'Item' (" +
                            "'id' INTEGER PRIMARY KEY, " +
                            "'contained_in' INTEGER REFERENCES BOX(id), " +
                            "'color' text);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void dropTables() throws SQLException {
        statmt = conn.createStatement();
        statmt.execute("DROP TABLE IF EXISTS 'Box';");
        statmt.execute("DROP TABLE IF EXISTS 'Item';");
    }

    static void insertIntoBox (Integer id, Integer contained_in) throws SQLException {
        statmt.execute("INSERT INTO 'Box' ('id','contained_in') " +
                "VALUES ('"+ id +"', '" + contained_in +"'); ");
    }

    static void insertIntoItem (Integer id, Integer contained_in, String color) throws SQLException {
        statmt.execute("INSERT INTO 'Item' ('id','contained_in','color') " +
                "VALUES ('"+ id +"', '" + contained_in +"', '" + color +"'); ");
    }

    static List<String> getItemId(int contained_in, String color) throws SQLException {
        resSet = statmt.executeQuery(
                "select id from( " +
                        "select i.id, color, table_box.contained_in " +
                        "from item i, " +
                               "(with recursive tr(id, contained_in) as ( " +
                                 "select id, contained_in from box " +
                                 "union all " +
                                 "select b.id, tr.contained_in from box b " +
                                 "join tr on b.contained_in = tr.id) " +
                              "select * from tr " +
                              "where contained_in is not null " +
                              "union " +
                              "select id, id contained_in from box) table_box " +
                        "where i.contained_in = table_box.id " +
                        "and color is not null) " +
                    "where contained_in = "+contained_in+" and color='"+color+"'");
        java.util.List<String> itemList = new ArrayList<>();
        while(resSet.next())
        {
            String item = resSet.getString(1);
            itemList.add(item);
        }
        return itemList;
    }

    static void closeDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
    }
}
