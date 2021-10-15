package ru.task;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JUnitControllerTest {

    JsonRequest jsonRequest = new JsonRequest();
    Controller controller = new Controller();
    List<String> result;

    @BeforeClass
    public static void init() {
        org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.getInstance();
    }

    @Test
    public void testControllerFile() {
       jsonRequest.setBox(1);
       jsonRequest.setColor("red");
       result = controller.items("file:/Users/Shared/input1.xml", jsonRequest);
       assertEquals(result.toString(), "[2, 3]");
    }

    @Test
    public void testControllerClassPath() {
        init();
        jsonRequest.setBox(1);
        jsonRequest.setColor("red");
        result = controller.items("classpath:input.xml", jsonRequest);
        assertEquals(result.toString(), "[2, 3]");
    }

    @Test
    public void testControllerUrl() {
        jsonRequest.setBox(1);
        jsonRequest.setColor("red");
        result = controller.items("url:file:/Users/Shared/input1.xml ", jsonRequest);
        assertEquals(result.toString(), "[2, 3]");
    }
}
