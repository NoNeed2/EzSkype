package in.kyle.ezskypeezlife.internal.packet.messages.image;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 11/3/2015.
 */
public class SkypeSetImagePermissionsPacket extends SkypePacket {
    
    private final String longId;
    
    public SkypeSetImagePermissionsPacket(EzSkype ezSkype, String imageId, String longId) {
        super("https://api.asm.skype.com/v1/objects/{}/permissions", HTTPRequest.PUT, ezSkype, false, imageId);
        this.longId = longId;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("read");
        data.add(longId, jsonArray);
        data.addProperty("type", "pish/image");
        
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.addHeader("Authorization", "skype_token " + ezSkype.getSkypeSession().getXToken());
        webConnectionBuilder.send();
        return null;
    }
}
