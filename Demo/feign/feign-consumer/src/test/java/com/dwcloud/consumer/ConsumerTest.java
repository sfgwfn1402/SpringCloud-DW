package com.dwcloud.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConsumerTest {

    @Resource
    DemoConsumer demoConsumer;

    @Test
    public void checkSuccess() {
        System.out.println(demoConsumer.checkSuccess());
    }

}
