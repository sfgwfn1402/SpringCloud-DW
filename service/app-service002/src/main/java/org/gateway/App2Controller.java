package org.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app1")
public class App2Controller {

    @GetMapping("/test")
    public Object test() throws InterruptedException {
//        Thread.sleep(3000);
        return "app2";
    }
}