package kr.hhplus.be.server.config.cache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class PageSerializer extends StdSerializer<Page> {

    public PageSerializer() {
        super(Page.class);
    }

    /**
     * Serialize Page in a custom way.
     *
     * 1. Only class info of Page is serialized using defaultTyping.
     * 2. Generic Class of List is serialized manually.
     * 3. Fields are serialized with no type info.
     *
     * @param value Value to serialize; can <b>not</b> be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @param typeSer Type serializer to use for including type information
     * @throws IOException
     */

    @Override
    public void serializeWithType(Page value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        WritableTypeId id = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.START_OBJECT));
        writeSubClassInPage(value, gen);
        writeFields(value, gen, serializers);
        typeSer.writeTypeSuffix(gen, id);
    }


    @Override
    public void serialize(Page value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        throw new IllegalCallerException("This method is not supposed to be called.");
    }

    private void writeFields(Page value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.getContent() != null && value.getContent().size() > 0) {
            provider.defaultSerializeField("content", value.getContent(), gen);
        }
        gen.writeNumberField("size", value.getSize());
        gen.writeNumberField("number", value.getNumber());
        gen.writeNumberField("totalElements", Long.valueOf(value.getTotalElements()));
    }

    private void writeSubClassInPage(Page value, JsonGenerator gen) throws IOException {
        if (value.getContent() == null || value.getContent().size() == 0) return;
        Class subClass = value.getContent().get(0).getClass();
        gen.writeStringField("class", subClass == null ? "null" : subClass.getName());
    }
}