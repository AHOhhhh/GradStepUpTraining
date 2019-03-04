package fun.hercules.order.order.platform.security;

public class SecurityConstants {
    public static final String SECRET = "HerculesSecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 900_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGN_UP_URL = "/users";
    public static final String USER_NAME = "username";
    public static final String USER = "user";
    public static final String USER_ID = "userId";
}
