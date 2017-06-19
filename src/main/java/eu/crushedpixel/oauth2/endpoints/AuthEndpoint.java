package eu.crushedpixel.oauth2.endpoints;

import eu.crushedpixel.oauth2.mock.MockedApplicationRegistry;
import eu.crushedpixel.oauth2.mock.MockedAuthorizationKeyRegistry;
import eu.crushedpixel.oauth2.provider.ApplicationProvider;
import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/auth")
public class AuthEndpoint {

    @GET
    public Response authorize(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException, OAuthProblemException {
        /*
         * creates an authorization code for an application requesting to use the OAuth2 service
         */

        ApplicationProvider validator = MockedApplicationRegistry.instance;
        AuthorizationCodeProvider provider = MockedAuthorizationKeyRegistry.instance;

        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            // we only use the webapp oauth2 flow
            if (!"code".equals(oauthRequest.getResponseType())) {
                throw new UnsupportedOperationException("Only response_type code supported");
            }

            validator.validateClientId(oauthRequest.getClientId());

            // at this point, the user usually has to grant access to the third-party-app
            // this shouldn't be necessary in our service, as we only register application
            // keys for the installed services (e.g. mediawiki).
            // instead, we just generate an access token and redirect to the desired redirect_uri.

            // we also ignore the requested scope, as all services get to use all features we provide them with.

            // generate authorization code
            String authCode = provider.createAuthorizationCode(oauthRequest.getClientId());

            // send authorization code back
            OAuthResponse response = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND)
                    .setCode(authCode)
                    .setParam("state", oauthRequest.getState())
                    .location(oauthRequest.getRedirectURI())
                    .buildQueryMessage();

            URI url = new URI(response.getLocationUri());
            return Response.status(response.getResponseStatus()).location(url).build();

        } catch (OAuthProblemException e) {
            // TODO: display some different page?
            throw e;
        }
    }

}