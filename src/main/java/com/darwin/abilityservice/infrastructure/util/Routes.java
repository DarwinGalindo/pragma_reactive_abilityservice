package com.darwin.abilityservice.infrastructure.util;

public final class Routes {
    public static final String ABILITY_RESOURCE = "/abilities";
    public static final String ABILITY_RESOURCE_ID = "/abilities/{id}";
    public static final String ABILITY_RESOURCE_ID_EXISTS = "/abilities/{id}/exists";
    public static final String TECHNOLOGY_RESOURCE_ID = "/technologies/{id}/exists";
    public static final String TECHNOLOGY_RESOURCE_ID_EXISTS = "/technologies/{id}/exists";

    private Routes() {
    }
}
