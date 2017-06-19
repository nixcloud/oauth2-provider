package eu.crushedpixel.oauth2.provider;

import eu.crushedpixel.oauth2.models.AccessToken;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

public interface AccessTokenProvider {

    public void validateAccessToken(String clientId, String accessToken) throws OAuthProblemException;
    public AccessToken createAccessToken(String clientId);

}
