package ru.myorder.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.myorder.models.Product;

import java.io.IOException;

public class ProductSerializer  extends StdSerializer<Product> {

    public ProductSerializer() {this(null);};

    public ProductSerializer(Class<Product> p){super(p);};

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException{
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", product.getId());
        jsonGenerator.writeStringField("name", product.getName());
        jsonGenerator.writeNumberField("categoryId", product.getCategory().getId());
        jsonGenerator.writeEndObject();
    }


}
