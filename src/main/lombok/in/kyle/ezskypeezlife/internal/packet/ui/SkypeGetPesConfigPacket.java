package in.kyle.ezskypeezlife.internal.packet.ui;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.Data;

import java.io.IOException;

/**
 * Created by Kyle on 12/7/2015.
 */
public class SkypeGetPesConfigPacket extends SkypePacket {
    
    public SkypeGetPesConfigPacket(EzSkype ezSkype, int platformId, String version) {
        super("https://a.config.skype.com/config/v1/Skype/{}_{}.0/SkypePersonalization?apikey=skype.com", HTTPRequest.GET, ezSkype, 
                false, Integer.toString(platformId), version);
    }
    
    @Override
    protected PesReturnObject run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        logger.info("Getting PES version: {}", webConnectionBuilder.getUrl());
        JsonObject data = webConnectionBuilder.getAsJsonObject();
        String configUrl = data.get("pes_config").getAsString();
        webConnectionBuilder.setUrl(configUrl);
        return new PesReturnObject(configUrl, webConnectionBuilder.getAsJsonObject());
    }
    
    @Data
    public static class PesReturnObject {
        
        private final String pesConfigUrl;
        private final JsonObject data;
    }
}
