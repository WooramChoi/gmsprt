package net.adonika.gmsprt.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// TODO JPA 에서 사용하는 Entity 변수와 DB 컬럼간의 형변환 어뎁터. util 패키지가 아닌 다른 곳에 있어야한다.
@Converter
public class BooleanYnConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equalsIgnoreCase(dbData);
    }

}
