package com.iann.springbatchsample;

import com.iann.springbatchsample.domain.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MailSendingTest {
    @Autowired
    EmailService emailService ;

    @Test
    public void simpleMailTest() {
        emailService.sendSimpleMessage("azurest0124@naver.com", "test1111", "test1111");
    }

    @Test
    public void attachMailTest() {
        emailService.sendAttachMessage("azurest0124@naver.com", "attachTest", "attach test", "jaehoon oppa","D:\\jaehoon.png");
    }
}
