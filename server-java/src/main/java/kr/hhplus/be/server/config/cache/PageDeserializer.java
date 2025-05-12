package kr.hhplus.be.server.config.cache;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDeserializer extends StdDeserializer<PageImpl> {
    private static final String CONTENT = "content";
    private static final String NUMBER = "number";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String CLASS = "class";

    public PageDeserializer() {
        super((JavaType) null);
    }

    /**
     * Deserialize Page. This is matched with PageSerializer.
     *
     * @param p Parsed used for reading JSON content
     * @param ctxt Context that can be used to access information about
     *   this deserialization activity.
     *
     * @throws IOException
     * @throws JacksonException
     */
    @Override
    public PageImpl deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        CollectionType valuesListType = null;
        List<?> list = new ArrayList();
        int pageNumber = -1;
        int pageSize = -1;
        long total = -1L;

        String propName = p.getCurrentName();
        do {
            p.nextToken();
            switch (propName) {
                case CLASS:
                    String className = ctxt.readValue(p, String.class);
                    Class clazz = getClass(className);
                    valuesListType = ctxt.getTypeFactory().constructCollectionType(List.class, clazz);
                    break;
                case CONTENT:
                    list = ctxt.readValue(p, valuesListType);
                    break;
                case NUMBER:
                    pageNumber = ctxt.readValue(p, Integer.class);
                    break;
                case SIZE:
                    pageSize = ctxt.readValue(p, Integer.class);
                    break;
                case TOTAL_ELEMENTS:
                    total = ctxt.readValue(p, Integer.class);
                    break;
                default:
                    p.skipChildren();
                    break;
            }
        } while (((propName = p.nextFieldName())) != null);

        validate(pageNumber, pageSize, total, p);
        return new PageImpl<>(list, PageRequest.of(pageNumber, pageSize), total);
    }

    private void validate(int pageNumber, int pageSize, long total, JsonParser p) throws JsonParseException {
        if (pageNumber == -1 || pageSize == -1 || total == -1L) {
            throw new JsonParseException(p, "Invalid JSON format.");
        }
    }

    private Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}