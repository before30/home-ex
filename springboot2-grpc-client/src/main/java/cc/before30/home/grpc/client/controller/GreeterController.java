package cc.before30.home.grpc.client.controller;

import cc.before30.home.grpc.client.service.GreeterService;
import cc.before30.home.grpc.client.service.GreeterWithCircuitBreakerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * GreeterController
 *
 * @author before30
 * @since 2019-06-09
 */

@RestController
@RequestMapping("/api/greeter")
public class GreeterController {

    private final GreeterService greeterService;
    private final GreeterWithCircuitBreakerService greeterWithCircuitBreakerService;

    public GreeterController(GreeterService greeterService,
                             GreeterWithCircuitBreakerService greeterWithCircuitBreakerService) {
        this.greeterService = greeterService;
        this.greeterWithCircuitBreakerService = greeterWithCircuitBreakerService;
    }

    @GetMapping("/c1")
    public String c1() throws ExecutionException, InterruptedException {
        return greeterService.greet(RandomStringUtils.randomAlphabetic(10));
    }

    @GetMapping("/c10-1")
    public String c10_1() throws ExecutionException, InterruptedException {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add(RandomStringUtils.randomAlphabetic(10));
        }
        greeterService.greets(names);
        return "done";
    }

    @GetMapping("/c10-2")
    public String c10_2() throws ExecutionException, InterruptedException {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add(RandomStringUtils.randomAlphabetic(10));
        }
        greeterService.greets2(names);
        return "done";
    }

    @GetMapping("/c2")
    public String c2() throws ExecutionException, InterruptedException {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add(RandomStringUtils.randomAlphanumeric(10));
        }

        greeterWithCircuitBreakerService.greets(names);
        return "done";
    }
}
