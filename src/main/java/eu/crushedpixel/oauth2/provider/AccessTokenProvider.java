package eu.crushedpixel.oauth2.provider;

import eu.crushedpixel.oauth2.models.AccessToken;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

public interface AccessTokenProvider {

    public AccessToken getAccessToken(String accessToken) throws OAuthProblemException;
    public AccessToken createAccessToken(String clientId, String authorizationCode);

}
