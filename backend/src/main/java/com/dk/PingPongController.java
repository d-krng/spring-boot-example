package com.dk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String result) {}

    @RequestMapping(method = RequestMethod.GET, value = "/ping")
    public PingPong getPingPong() {
    return new PingPong("PongPingPong");
    }
}
