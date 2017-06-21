package eu.crushedpixel.oauth2.endpoints;

import eu.crushedpixel.oauth2.mock.MockedAccessTokenRegistry;
import eu.crushedpixel.oauth2.mock.MockedApplicationRegistry;
import eu.crushedpixel.oauth2.mock.MockedAuthorizationCodeRegistry;
import eu.crushedpixel.oauth2.models.AccessToken;
import eu.crushedpixel.oauth2.provider.AccessTokenProvider;
import eu.crushedpixel.oauth2.provider.ApplicationProvider;
import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/token")
public class TokenEndpoint {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(MultivaluedMap<String, String> form) throws Exception {
        /*
         * exchanges the authorization code granted by GET /auth for an access token
         */

        ApplicationProvider validator = MockedApplicationRegistry.instance;
        AuthorizationCodeProvider codeProvider = MockedAuthorizationCodeRegistry.instance;
        AccessTokenProvider tokenProvider = MockedAccessTokenRegistry.instance;

        try {
            // we can't use Oltu's handy OAuthTokenRequest here, because
            // for some reason, it requires the parameters to be query parameters
            // instead of the form body the protocol requires.

            String grantType = form.getFirst("grant_type");
            String clientId = form.getFirst("client_id");
            String clientSecret = form.getFirst("client_secret");
            //String redirectURI = form.getFirst("redirect_uri");
            String code = form.getFirst("code");

            // we only use the webapp oauth2 flow
            if (!"authorization_code".equals(grantType)) {
                throw new UnsupportedOperationException("Only grant_type authorization_code supported");
            }

            validator.validateClientSecret(clientId, clientSecret);
            codeProvider.validateAuthorizationCode(clientId, code);

            // we should by protocol compare the redirectURI with the redirectURI supplied
            // when the authorization code was retrieved, but this only adds unnecessary complexity
            // which we don't need as we're only communicating internally anyway

            // create access token connected to auth code
            AccessToken accessToken = tokenProvider.createAccessToken(clientId, code);

            // send access token back
            OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken.accessToken)
                    .setExpiresIn("3600") // TODO: actually calculate expiration time from now
                    .buildJSONMessage();

            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

        } catch (OAuthProblemException e) {
            OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();

            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        }
    }

}
