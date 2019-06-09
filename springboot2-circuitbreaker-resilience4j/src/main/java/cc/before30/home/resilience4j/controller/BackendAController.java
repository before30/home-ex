package cc.before30.home.resilience4j.controller;

import cc.before30.home.resilience4j.domain.ABizService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BackendAController
 *
 * @author before30
 * @since 2019-06-06
 */

@RestController
@RequestMapping(value = "/backendA")
public class BackendAController {

    private final ABizService aBizService;

    public BackendAController(@Qualifier("ABizService") ABizService aBizService) {
        this.aBizService = aBizService;
    }

    @GetMapping("failure")
    public String failure(){
        return aBizService.failure();
    }

    @GetMapping("success")
    public String success(){
        return aBizService.success();
    }

    @GetMapping("ignore")
    public String ignore(){
        return aBizService.ignore();
    }

    @GetMapping("recover")
    public String methodWithRecovery(){
        return aBizService.methodWithRecovery().get();
    }
}
