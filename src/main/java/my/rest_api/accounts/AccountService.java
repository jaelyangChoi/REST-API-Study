package my.rest_api.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 내가 정의한 Account 객체를 Spring Security 가 이해할 수 있는 UserDetails 로 변환
     * 사용자 정보는 Security 가 제공하는 User 로 반환
     * 권한 정보는 GrantedAuthority 타입으로 변환
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Account not found with username: " + username)
        );


        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .build();
    }

    public Account saveAccount(Account account) {
        account.encodePassword(passwordEncoder);
        return accountRepository.save(account);
    }
}
