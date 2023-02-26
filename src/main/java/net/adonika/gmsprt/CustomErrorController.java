package net.adonika.gmsprt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @GetMapping(value = {"/error"})
    public String error(HttpServletRequest request, HttpServletResponse response) {

        int status = response.getStatus();
        if (status != HttpStatus.NOT_FOUND.value()) {
            logger.error("error: [{}] {}", status, request.getRequestURI());
            // TODO 에러 상세 내용 -> Exception 이후 현제 컨트롤러로 넘어오기때문에 필요 없을지도
        } else {
            logger.debug("Not Found -> index.html");
        }

        return "index.html";
    }

}
