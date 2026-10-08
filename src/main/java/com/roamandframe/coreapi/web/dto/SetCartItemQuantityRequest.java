package com.roamandframe.coreapi.web.dto;

import jakarta.validation.constraints.Min;

public record SetCartItemQuantityRequest(@Min(0) int quantity) {
}
