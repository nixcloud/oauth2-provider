package eu.crushedpixel.oauth2.mock;

import eu.crushedpixel.oauth2.models.AccessToken;
import eu.crushedpixel.oauth2.provider.AccessTokenProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.HashMap;
import java.util.Map;

public class MockedAccessTokenRegistry implements AccessTokenProvider {

    // the internal access token representation,
    // containing an absolute timestamp at which
    // the token expires.
    class AccessTokenInternal {

        public final String accessToken;
        public final long expires;

        public AccessTokenInternal(String accessToken, long expires) {
            this.accessToken = accessToken;
            this.expires = expires;
        }

    }

    public static MockedAccessTokenRegistry instance = new MockedAccessTokenRegistry();

    private final Map<String, AccessTokenInternal> accessTokens = new HashMap<String, AccessTokenInternal>() {
        {
            put("CLIENT_0", new AccessTokenInternal("ACCESS_TOKEN_0", 0));
            put("CLIENT_1", new AccessTokenInternal("ACCESS_TOKEN_1", 0));
            put("CLIENT_2", new AccessTokenInternal("ACCESS_TOKEN_2", 0));
            put("CLIENT_3", new AccessTokenInternal("ACCESS_TOKEN_3", 0));
        }
    };

    @Override
    public void validateAccessToken(String clientId, String accessToken) throws OAuthProblemException {
        AccessTokenInternal token = accessTokens.get(clientId);
        if (token == null || !token.accessToken.equals(accessToken)) { // TODO: check token expiry timestamp
            throw OAuthProblemException.error("Invalid access token");
        }
    }

    @Override
    public AccessToken createAccessToken(String clientId) {
        // TODO: actually generate value and store it in db
        AccessTokenInternal token = accessTokens.get(clientId);
        if (token == null) {
            throw new UnsupportedOperationException("Only the hardcoded applications are allowed as of now");
        }

        // TODO: actually calculate time till expiry
        return new AccessToken(token.accessToken, 3600);
    }
}
