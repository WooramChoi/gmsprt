package net.adonika.gmsprt.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectUtil {

    public ObjectUtil() {
    }

    /**
     * Class 내의 멤버변수들의 이름을 반환한다.
     * @param clazz - 대상 Class
     * @return String[] - 멤버변수명
     */
    public static String[] getFieldNames(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        String[] fieldNames = new String[declaredFields.length];
        for (int i = 0; i<length; i++) {
            fieldNames[i] = declaredFields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 로깅용 임시 메서드
     * TODO slf4j 에 설정해서 toString 으로 사용해야한다.
     * @param obj - 대상 Object
     * @return String - JSON 문자열
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
    
    /**
     * target 에서 fieldName 에 해당하는 field 를 찾아 source 의 내용을 복사한다.
     * @param target - 대상 Object
     * @param fieldName - source 를 붙여넣고 싶은 target 내의 Field 이름
     * @param source - target 내의 Field 에 붙여넣고 싶은 source
     * @throws NoSuchFieldException - fieldName 에 해당하는 field 를 찾지 못함
     */
    public static void copyToField(Object target, String fieldName, Object source) throws NoSuchFieldException {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            Object instance = field.getType().getDeclaredConstructor().newInstance();
            
            BeanUtils.copyProperties(source, instance);
            
            field.setAccessible(true);
            field.set(target, instance);
            field.setAccessible(false);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
