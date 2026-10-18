package com.roamandframe.coreapi.security;

import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
import com.roamandframe.coreapi.security.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerService customerService;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public AuthenticationServiceImpl(CustomerService customerService,
                                     PasswordHasher passwordHasher,
                                     TokenIssuer tokenIssuer) {
        this.customerService = customerService;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public String authenticate(String email, String rawPassword) {
        CustomerAccount account = customerService.findAccountByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(rawPassword, account.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        return tokenIssuer.issueToken(account.id(), account.email());
    }
}
