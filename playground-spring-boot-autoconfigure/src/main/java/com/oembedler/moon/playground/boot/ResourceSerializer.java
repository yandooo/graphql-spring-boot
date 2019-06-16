package com.oembedler.moon.playground.boot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@JsonComponent
public class ResourceSerializer extends JsonSerializer<Resource> {
    @Override
    public void serialize(final Resource value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        final String content = Streams.asString(value.getInputStream(), StandardCharsets.UTF_8.name());
        gen.writeString(content);
    }
}
