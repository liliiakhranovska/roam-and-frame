package com.roamandframe.coreapi.modules.customer.model;

import java.util.UUID;

public record CustomerAccount(UUID id, String email, String passwordHash) {}

