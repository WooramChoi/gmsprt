package net.adonika.gmsprt.props;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import net.adonika.gmsprt.util.DateUtil;

@ConfigurationProperties(prefix = "storage")
@ConstructorBinding
public class StorageProperties {
    
    private final String fileSeparator;
    private final String path;
    
    public StorageProperties(String fileSeparator, String path) {
        System.out.println("## call storage properties constructor ##");
        System.out.println("## fileSeparator: " + fileSeparator);
        System.out.println("## path: " + path);
        this.fileSeparator = fileSeparator;
        this.path = path;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    /**
     * storage.path 반환
     * "{날짜포맷}" 형식은 당일 날짜로 변환
     * ex1) "/files/{yyyyMM}/{dd}/" -> "/files/199701/01/"
     * "{날짜포맷이 아닌 문자열}" 은 문자열로 변환
     * ex2) "/files/{test}/" -> "/files/test/"
     * @return storage.path
     */
    public String getPath() {
        
        Date now = new Date();
        String formattedPath = path;
        
        Pattern pattern = Pattern.compile("\\{[^\\{]+\\}");
        Matcher matcher = pattern.matcher(formattedPath);
        
        while(matcher.find()) {
            String group = matcher.group();
            String patternDate = group.substring(1, group.length()-1);
            String formattedDate = "";
            try {
                formattedDate = DateUtil.dateToString(now, patternDate);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                formattedDate = patternDate;
            }
            formattedPath = formattedPath.replace(group, formattedDate);
        }
        
        return formattedPath;
    }

}
