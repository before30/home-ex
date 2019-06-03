package cc.before30.home.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Junit5ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("test Message REST API")
    void testMessage() {
        String message = this.restTemplate.getForObject("/hello", String.class);
        assertEquals("world", message);
    }

}