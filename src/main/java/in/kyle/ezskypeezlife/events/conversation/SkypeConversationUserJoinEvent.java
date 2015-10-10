package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
@AllArgsConstructor
public class SkypeConversationUserJoinEvent implements SkypeEvent {
    
    private final SkypeConversation conversation;
    private final SkypeUser user;
    
}
