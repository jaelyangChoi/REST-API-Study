package my.rest_api.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String refreshToken;
    private String expiration;
}
