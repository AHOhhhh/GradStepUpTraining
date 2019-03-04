package fun.hercules.webapi.security;

public class SecurityConstants {
    public static final String SECRET = "HerculesSecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 1_800_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/webapi/enterprise-admin";
    public static final String USER_NAME = "username";
    public static final String USER_ID = "userId";
}
