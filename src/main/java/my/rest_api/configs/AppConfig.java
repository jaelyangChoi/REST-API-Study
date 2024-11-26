package my.rest_api.configs;

import my.rest_api.accounts.Account;
import my.rest_api.accounts.AccountRole;
import my.rest_api.accounts.AccountService;
import my.rest_api.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationRunner testData() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) {
                Account testAdmin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .role(AccountRole.ADMIN)
                        .build();
                accountService.saveAccount(testAdmin);

                Account testUser = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .role(AccountRole.USER)
                        .build();
                accountService.saveAccount(testUser);
            }
        };
    }
}
