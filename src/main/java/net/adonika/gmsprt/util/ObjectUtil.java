package net.adonika.gmsprt.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectUtil {

    public ObjectUtil() {

    }

    public static List<String> getFieldNames(Class c) {

        List<String> fieldNames = new ArrayList<>();
        Field[] declaredFields = c.getDeclaredFields();
        for(Field field: declaredFields){
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }
}
