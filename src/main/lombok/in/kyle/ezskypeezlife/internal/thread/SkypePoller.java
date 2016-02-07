package in.kyle.ezskypeezlife.internal.thread;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypePullPacket;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeControlClearTypingType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeControlTypingType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeEventCallType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeMediaFlikMessageType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypePollMessageType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeTextType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeThreadActivityAddMemberType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeThreadActivityDeleteMemberType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeThreadActivityPictureUpdateType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeThreadActivityRoleUpdateType;
import in.kyle.ezskypeezlife.internal.thread.poll.SkypeThreadActivityTopicUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle on 10/5/2015.
 * <p>
 * Pulls new Skype events from the server
 */
public class SkypePoller implements Runnable {
    
    private final EzSkype ezSkype;
    private final AtomicBoolean active;
    private final List<SkypePollMessageType> messageTypes;
    
    public SkypePoller(EzSkype ezSkype) {
        Thread.currentThread().setName("Skype-Poller-Thread-" + ezSkype.getLocalUser().getUsername());
        this.ezSkype = ezSkype;
        this.active = ezSkype.getActive();
        
        // @formatter:off
        this.messageTypes = Arrays.asList(
                new SkypeControlClearTypingType(),
                new SkypeControlTypingType(),
                new SkypeEventCallType(),
                new SkypeTextType(),
                new SkypeThreadActivityAddMemberType(),
                new SkypeThreadActivityDeleteMemberType(),
                new SkypeThreadActivityPictureUpdateType(),
                new SkypeThreadActivityRoleUpdateType(),
                new SkypeThreadActivityTopicUpdate(),
                new SkypeMediaFlikMessageType()
        );
        // @formatter:on
    }
    
    @Override
    public void run() {
        try {
            while (active.get()) {
                SkypePullPacket skypePullPacket = new SkypePullPacket(ezSkype);
                JsonObject responseData = (JsonObject) skypePullPacket.executeSync();
                EzSkype.LOGGER.debug(responseData.toString());
                if (responseData.entrySet().size() != 0) {
                    if (responseData.has("eventMessages")) {
                        JsonArray messages = responseData.getAsJsonArray("eventMessages");
                        
                        for (JsonElement jsonObject : messages) {
                            try {
                                extractInfo(jsonObject.getAsJsonObject());
                            } catch (Exception e) {
                                EzSkype.LOGGER.error("Error extracting info from:\n" + jsonObject, e);
                                ezSkype.getErrorHandler().handleException(e);
                            }
                        }
                    } else {
                        EzSkype.LOGGER.error("Bad poll response: " + responseData);
                    }
                }
            }
        } catch (Exception e) {
            EzSkype.LOGGER.error("Error with poll", e);
            ezSkype.getErrorHandler().handleException(e);
        }
    }
    
    private void extractInfo(JsonObject jsonObject) throws Exception {
        JsonObject resource = jsonObject.getAsJsonObject().getAsJsonObject("resource");
    
        if (resource.has("messagetype")) {
            String messageType = resource.get("messagetype").getAsString();
            
            for (SkypePollMessageType skypePollMessageType : messageTypes) {
                if (skypePollMessageType.accept(messageType)) {
                    EzSkype.LOGGER.debug("Extraction approach set to {} for message type {} for message {}", skypePollMessageType
                            .getClass().getName(), messageType, resource);
                    skypePollMessageType.extract(ezSkype, jsonObject, resource);
                    return;
                }
            }
            EzSkype.LOGGER.debug("Could not extract info from message of type {} because it is not a known type", messageType);
        } else {
            EzSkype.LOGGER.debug("Unknown message, does not contain message type: {}", jsonObject);
        }
    }
}
