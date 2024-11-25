package my.rest_api.common;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "my-app")
public class AppProperties {
    //스프링 부트가 바인딩해준다
    @NotEmpty
    private String adminUsername;
    @NotEmpty
    private String adminPassword;
    @NotEmpty
    private String userUsername;
    @NotEmpty
    private String userPassword;
}
