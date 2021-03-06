package com.sloy.sevibus.resources;


public interface RemoteConfiguration {
    void init();

    void update();

    String getString(String key, String defaultValue);

    boolean isLoginEnabled();

    boolean isLoginSuggestionEnabled();
}
