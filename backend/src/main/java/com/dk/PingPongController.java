package com.dk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private static int COUNTER = 0;
    record PingPong(String result) {}

    @RequestMapping(method = RequestMethod.GET, value = "/ping")
    public PingPong getPingPong() {
    return new PingPong("Pongsssssss: %s" .formatted(++COUNTER));
    }
}
