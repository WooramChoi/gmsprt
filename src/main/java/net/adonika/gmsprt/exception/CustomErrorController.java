package net.adonika.gmsprt.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @GetMapping(value = {"/error"})
    public String error(HttpServletRequest request, HttpServletResponse response) {

        int status = response.getStatus();
        if (status < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        	logger.debug("Under 500 -> index.html");
        	return "index.html";
        } else {
        	logger.error("error: [{}] {}", status, request.getRequestURI());
        	// TODO 에러 상세 내용 -> Exception 이후 현제 컨트롤러로 넘어오기때문에 필요 없을지도
        	return "redirect:/error";
        }
    }

}
