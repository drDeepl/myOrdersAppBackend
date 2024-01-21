package ru.myorder.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.myorder.models.PurchasedProduct;

import java.io.IOException;

public class PurchasedProductSerializer extends StdSerializer<PurchasedProduct> {

    public PurchasedProductSerializer(){
        this(null);
    }

    public PurchasedProductSerializer(Class<PurchasedProduct> pp){
        super(pp);
    }

    @Override
    public void serialize(PurchasedProduct purchasedProduct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException{
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", purchasedProduct.getId());
        jsonGenerator.writeNumberField("userId", purchasedProduct.getUser().getId());
        jsonGenerator.writeStringField("productName", purchasedProduct.getProduct().getName());
        jsonGenerator.writeNumberField("categoryId", purchasedProduct.getProduct().getCategory().getId());
        jsonGenerator.writeNumberField("count", purchasedProduct.getCount());
        jsonGenerator.writeStringField("unitMeasurement", purchasedProduct.getMeasurementUnit().getName());
        jsonGenerator.writeNumberField("price", purchasedProduct.getPrice());
        jsonGenerator.writeStringField("purchaseDate", purchasedProduct.getPurchaseDate().toString());
        jsonGenerator.writeEndObject();
    }
}
