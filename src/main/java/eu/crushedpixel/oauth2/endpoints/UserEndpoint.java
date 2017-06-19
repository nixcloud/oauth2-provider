package eu.crushedpixel.oauth2.endpoints;

import eu.crushedpixel.oauth2.filters.AuthenticatedMethod;
import eu.crushedpixel.oauth2.mock.MockedAuthorizationCodeRegistry;
import eu.crushedpixel.oauth2.models.AccessToken;
import eu.crushedpixel.oauth2.models.NixcloudUser;
import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserEndpoint {

    /**
     * Simple wrapper to achieve the JSON structure expected by the mediawiki OAuth2 extension.
     */
    private class UserResponse {

        public final NixcloudUser user;

        public UserResponse(NixcloudUser user) {
            this.user = user;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AuthenticatedMethod
    public Response user(@Context HttpServletRequest request) throws OAuthSystemException {
        /*
         * return some information about the authenticated nixcloud user
         * for the client to display (id, username...)
         */

        AuthorizationCodeProvider codeProvider = MockedAuthorizationCodeRegistry.instance;

        try {
            // access token provided by the AuthenticatedMethod annotation
            AccessToken token = (AccessToken) request.getAttribute("accessToken");

            NixcloudUser user = codeProvider.getAuthorizationCode(token.authorizationCode).user;

            // return user information as json
            return Response.ok(new UserResponse(user)).build();

        } catch (OAuthProblemException e) {
            OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();

            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        }
    }

}
