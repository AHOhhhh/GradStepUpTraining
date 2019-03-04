package fun.hercules.order.order.common.signature.openapp;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AppCredentialResolver {

    private final ConcurrentHashMap<String, AppCredential> appCredentialCache = new ConcurrentHashMap<>();

    private OpenAppCredentialClient credentialClient;

    public AppCredentialResolver(@Lazy OpenAppCredentialClient credentialClient) {
        this.credentialClient = credentialClient;
    }

    public AppCredential get(String appName) {
        AppCredential credential = appCredentialCache.get(appName);
        if (credential != null) {
            return credential;
        } else {
            List<AppCredential> credentials = credentialClient.get(appName);
            if (credentials.isEmpty()) {
                throw new NotFoundException(ErrorCode.ORDER_NOT_FOUND,
                        String.format("can't find open app credential by name %s", appName));
            }
            credential = credentials.get(0);
            appCredentialCache.put(appName, credential);
            return credential;
        }
    }
}
