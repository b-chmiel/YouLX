package com.youlx.api;

public class Routes {
    private static final String prefix = "/api";

    public class Auth {
        public static final String LOGIN = prefix + "/login";
        public static final String LOGOUT = prefix + "/logout";
    }

    public class Offer {
        public static final String OFFERS = prefix + "/offers";
    }

    public class User {
        public static final String ME = prefix + "/me";
    }
}
