package in.kyle.ezskypeezlife;

import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptchaHandler;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeEndpoint;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 10/10/2015.
 */
public class EzSkypeBuilder {
    
    private final SkypeCredentials credentials;
    private final List<SkypeEndpoint> skypeEndpoints;
    private String url;
    private SkypeCaptchaHandler skypeCaptchaHandler;
    private boolean debug;
    private Proxy proxy;
    
    /**
     * Constructs a new EzSkype builder, this will build a Skype instance from scratch
     * If you don't enable a feature, events related to that feature will not fire
     *
     * @param credentials - The login credentials
     */
    public EzSkypeBuilder(SkypeCredentials credentials) {
        this.credentials = credentials;
        this.skypeEndpoints = new ArrayList<>();
        this.url = null;
    }
    
    /**
     * This constructor is for guest accounts only, the URL is the url to join the Skype chat
     * Constructs a new EzSkype builder, this will build a Skype instance from scratch
     * If you don't enable a feature, events related to that feature will not fire
     *
     * @param username - The desired username
     * @param url      - The url to join the Skype chat
     */
    public EzSkypeBuilder(String username, String url) {
        this(new SkypeCredentials(username));
        this.url = url;
    }
    
    /**
     * Adds a Skype endpoint to pull from
     *
     * @param skypeEndpoint - The endpoint to pull
     */
    public EzSkypeBuilder with(SkypeEndpoint skypeEndpoint) {
        skypeEndpoints.add(skypeEndpoint);
        return this;
    }
    
    public EzSkypeBuilder debug(boolean debugMode) {
        debug = debugMode;
        return this;
    }
    
    /**
     * Adds a handler that is called when EzSkype encounters a captcha on login
     *
     * @param skypeCaptchaHandler - The captcha solver
     */
    public EzSkypeBuilder setCaptchaHandler(SkypeCaptchaHandler skypeCaptchaHandler) {
        this.skypeCaptchaHandler = skypeCaptchaHandler;
        return this;
    }
    
    /**
     * Sets a proxy to be used with all outgoing connections
     *
     * @param proxy - The proxy to be used
     */
    public EzSkypeBuilder setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }
    
    /**
     * Creates the Skype instance and logs in
     *
     * @return - The EzSkype instance
     * @throws Exception
     */
    public EzSkype buildAndLogin() throws Exception {
        EzSkype ezSkype = new EzSkype(credentials);
        ezSkype.setDebug(debug);
        if (proxy != null) {
            ezSkype.setProxy(proxy);
        }
        ezSkype.setCaptchaHandler(skypeCaptchaHandler);
        SkypeEndpoint[] endpoints = skypeEndpoints.toArray(new SkypeEndpoint[skypeEndpoints.size()]);
        if (credentials.isGuestAccount()) {
            ezSkype.loginGuest(endpoints, url);
        } else {
            ezSkype.login(endpoints);
        }
        
        return ezSkype;
    }
}
