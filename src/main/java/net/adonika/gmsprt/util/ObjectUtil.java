package net.adonika.gmsprt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectUtil {

    public ObjectUtil() {

    }

    public static List<String> getFieldNames(Class c) {

        List<String> fieldNames = new ArrayList<>();
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    /**
     * 로깅용 임시 메서드 *
     * TODO slf4j 에 설정해서 toString 으로 사용해야한다 *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        String msg;
        ObjectMapper om = new ObjectMapper();
        try {
            msg = om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            msg = "{}";
        }
        return msg;
    }
}
