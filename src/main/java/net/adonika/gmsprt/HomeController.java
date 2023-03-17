package net.adonika.gmsprt;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.adonika.gmsprt.util.ObjectUtil;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController() {
    }
    
    @RequestMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<String> getToken(@PathVariable String provider, HttpServletRequest request) {
        logger.info("provider: {}", provider);
        Map<String, String[]> parameterMap = request.getParameterMap();
        logger.info("parameterMap: {}", ObjectUtil.toJson(parameterMap));
        return ResponseEntity.ok("OK");
    }
}
