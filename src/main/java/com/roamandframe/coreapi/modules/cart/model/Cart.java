package com.roamandframe.coreapi.modules.cart.model;

import java.math.BigDecimal;
import java.util.List;

public record Cart(List<CartLine> lines, BigDecimal total) {
}
