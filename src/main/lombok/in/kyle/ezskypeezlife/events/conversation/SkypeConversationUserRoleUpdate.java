package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
@AllArgsConstructor
public class SkypeConversationUserRoleUpdate implements SkypeEvent {
    
    @Getter
    private final SkypeConversation conversation;
    @Getter
    private final SkypeUser user;
    @Getter
    private final SkypeUserRole oldRole;
    @Getter
    private final SkypeUserRole newRole;
    
}