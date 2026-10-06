package com.roamandframe.coreapi.security;

import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.service.CustomerAccountService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final CustomerAccountService customerAccountService;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public AuthenticationService(CustomerAccountService customerAccountService,
                                 PasswordHasher passwordHasher,
                                 TokenIssuer tokenIssuer) {
        this.customerAccountService = customerAccountService;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    public String authenticate(String email, String rawPassword) {
        CustomerAccount account = customerAccountService.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(rawPassword, account.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        return tokenIssuer.issueToken(account.id(), account.email());
    }
}
