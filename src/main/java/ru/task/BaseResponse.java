package ru.task;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

public class BaseResponse extends DataBase {
    public static List<String> baseResponse(int box, String color, String link){
        List<String> list = null;
        try {
            connect();
            dropTables();
            createTableBox();
            createTableItem();
            LoadXML.loadXML(link);
            list = getItemId(box,color);
            closeDB();
        } catch (SQLException | ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }
        return list;
    }
}