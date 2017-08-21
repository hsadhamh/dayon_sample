package lab.factor.dayon.headerlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassanhussain on 8/20/2017.
 */

public class GenericMessage {
    List<Object> mCustomEvents = new ArrayList<>();
    public List<Object> getCustomEvents() { return mCustomEvents; }
    public void AddMessage(Object o){ getCustomEvents().add(o); }
}
