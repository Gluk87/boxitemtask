package ru.task;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @PostMapping("/items")
    public List<String> items(@RequestParam(value = "link") String link, @RequestBody JsonRequest request) {
        int box = request.getBox();
        String color = request.getColor();
        return BaseResponse.baseResponse(box,color,link);
    }
}