package content.Json;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginPost {
    private User user;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User u) {
    	user = u;
    }
}