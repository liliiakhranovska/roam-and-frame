package com.roamandframe.coreapi.modules.catalog.model;

public record WithStock<T>(T item, int quantity) {
}
