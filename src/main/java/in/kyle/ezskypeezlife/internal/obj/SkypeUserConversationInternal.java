package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeUserConversationInternal extends SkypeConversationInternal {
    
    // @formatter:off
    public SkypeUserConversationInternal(
            EzSkype ezSkype,
            String longId,
            String topic,
            boolean historyEnabled,
            boolean joinEnabled,
            String url
    ) {
        super(ezSkype, longId, topic, historyEnabled, joinEnabled, url, SkypeConversationType.USER, new ArrayList<>());
    }
    // @formatter:on
}
