package com.youlx;

import com.youlx.testUtils.MvcHelpers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class TlxApplicationTests {
    @Autowired
    private MvcHelpers commonHelpers;

    @Test
    void contextLoads() {
    }
}
