package blacktokkies.toquiz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping()
    String Test(){
        return "Hello world";
    }
}
