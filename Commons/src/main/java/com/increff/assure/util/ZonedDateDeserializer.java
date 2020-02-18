//package com.increff.assure.util;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import com.fasterxml.jackson.databind.node.IntNode;
//import org.springframework.context.annotation.Bean;
//
//import java.io.IOException;
//import java.time.ZonedDateTime;
//
//
//public class ZonedDateDeserializer extends StdDeserializer<ZonedDateTime> {
//
//    protected ZonedDateDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    protected ZonedDateDeserializer(JavaType valueType) {
//        super(valueType);
//    }
//
//    protected ZonedDateDeserializer(StdDeserializer<?> src) {
//        super(src);
//    }
//
//    @Override @Bean
//    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//        System.out.println("hello");
//        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
//        System.out.println(node.get("dayOfMonth"));
//
//        return null;
//    }
//}
