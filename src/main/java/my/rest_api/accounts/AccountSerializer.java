package my.rest_api.accounts;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

//@JsonComponent 를 사용하지 않는 이유: Account 정보를 전부 넘겨줘야 할 때도 있으므로. Event에서 사용할때만 Id로 제한하고 싶은 상황.
public class AccountSerializer extends JsonSerializer<Account> {
    @Override
    public void serialize(Account account, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", account.getId());
        gen.writeEndObject();
    }
}
