# Installing tomcat and OAuth2 server

Install tomcat7 (for example using `apt-get install tomcat7`)
Create a tomcat admin user by adding the following to `/etc/tomcat7/tomcat-users.xml`:

```
<tomcat-users>
<role rolename="admin"/>
<role rolename="admin-gui"/>
<role rolename="manager-gui"/>
<user username="admin" password="nimda" roles="admin,admin-gui,manager-gui"/>
</tomcat-users>
```

Install java 8 runtime or newer on the VM and configure tomcat to use it (the OAuth2 server uses Java 8 features) by editing the `JAVA_HOME` variable in `/etc/default/tomcat7` accordingly.

Restart the tomcat service.

Build the OAuth2 server .war package by running `mvn package`

Use the webinterface at `http://{ipaddress}:8080/manager/html/` with the admin user registered earlier to deploy the OAuth2.war

# Installing mediawiki and the OAuth2Client extension

Follow the installation instructions of my mediawiki OAuth2Client extension fork: `https://github.com/CrushedPixel/MW-OAuth2Client`
Use the following configuration, replacing `{ipaddress}` with the VM's ip address:

```
$wgOAuth2Client['client']['id']     = 'CLIENT_0'; // will be injected later
$wgOAuth2Client['client']['secret'] = 'SECRET_0'; // will be injected later

$wgOAuth2Client['configuration']['authorize_endpoint']     = 'http://{ipaddress}:8080/oauth2/auth';
$wgOAuth2Client['configuration']['access_token_endpoint']  = 'http://{ipaddress}:8080/oauth2/token';
$wgOAuth2Client['configuration']['api_endpoint']           = 'http://{ipaddress}:8080/oauth2/user';
$wgOAuth2Client['configuration']['redirect_uri']           = 'http://{ipaddress}/mediawiki/index.php/Special:OAuth2Client/callback';

$wgOAuth2Client['configuration']['username'] = 'username';
$wgOAuth2Client['configuration']['email'] = 'email';
```

The "OAuth2 login" button in the upper right corner of mediawiki should now be functional.
