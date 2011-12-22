package com.heroku.api;


import com.heroku.api.parser.Json;
import com.heroku.api.parser.Parser;
import com.heroku.api.parser.TypeReference;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public enum IntegrationTestConfig {

    CONFIG("HEROKU_TEST_USERS", "heroku.test.users");

    private String environmentVariable;
    private String systemProperty;
    private static List<TestUser> testUsers;
    private static TestUser defaultUser;

    IntegrationTestConfig(String envvar, String sysprop) {
        this.environmentVariable = envvar;
        this.systemProperty = sysprop;
    }
    
    public TestUser getDefaultUser() {
        if (defaultUser == null) {
            loadUsers();
        }

        return defaultUser;
    }

    public List<TestUser> getTestUsers() {
        if (testUsers == null)
            loadUsers();

        return testUsers;
    }

    private void loadUsers() {
        assertConfigIsPresent();
        Parser jsonParser = Json.getJsonParser();
        testUsers = jsonParser.parse(getConfig().getBytes(), new TypeReference<List<TestUser>>(){}.getType());

        for (TestUser tu : testUsers) {
            if (tu.isDefaultUser()) {
                defaultUser = tu;
                break;
            }
        }

        assertDefaultUserIsPresent();
    }

    private void assertDefaultUserIsPresent() {
        assertNotNull(defaultUser, "A default user must be specified in the list of users.");
    }

    private void assertConfigIsPresent() {
        assertNotNull(
                getConfig(),
                String.format(
                        "Either environment variable %s or system property %s must be defined",
                        environmentVariable,
                        systemProperty
                )
        );
    }

    private String getConfig() {
        return System.getProperty(systemProperty, System.getenv(environmentVariable));
    }

    public String getRequiredConfig() {
        String value = getConfig();
        if (value == null) {
            throw new IllegalStateException(String.format("Either environment variable %s or system property %s must be defined", environmentVariable, systemProperty));
        }
        return value;
    }

    public static class TestUser {
        boolean defaultuser;
        String username;
        String password;
        String apikey;

        private void setDefaultuser(boolean defaultUser) {
            this.defaultuser = defaultUser;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        private void setPassword(String password) {
            this.password = password;
        }

        private void setapikey(String apiKey) {
            this.apikey = apiKey;
        }

        public boolean isDefaultUser() {
            return defaultuser;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getApiKey() {
            return apikey;
        }
    }
}
