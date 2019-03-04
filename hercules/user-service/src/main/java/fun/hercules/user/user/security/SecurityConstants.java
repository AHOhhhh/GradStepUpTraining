package fun.hercules.user.user.security;

public class SecurityConstants {
    public static final String SECRET = "HerculesSecretKeyToGenJWTs";

    public static final long EXPIRATION_TIME = 900_000; // 10 days

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";
}
