package net.adonika.gmsprt.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

/**
 * Spring default = BasicErrorController *
 * ControllerAdvice 에서 에러 처리 후 응답하는게 먼저다. *
 * ErrorController 는 에러 내용을 담고 error.path 로 Redirect 하는 순서이므로, *
 * Controller 를 타기 전에 발생되는 예외들이 이곳으로 올 것으로 예상된다... *
 */
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping(
            produces = {"text/html"}
    )
    public String errorHtml(HttpServletRequest request, HttpServletResponse response) {
        logger.error("error: [{}] {}", response.getStatus(), request.getRequestURI());
        return "index.html";
    }

//    @RequestMapping
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//        HttpStatus status = this.getStatus(request);
//        if (status == HttpStatus.NO_CONTENT) {
//            return new ResponseEntity(status);
//        } else {
//            Map<String, Object> body = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
//            return new ResponseEntity(body, status);
//        }
//    }

}
