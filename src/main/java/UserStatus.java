
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "currentUserStatus")
@SessionScoped
public class UserStatus {

    private UserJsf currentUser;

    public UserJsf getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserJsf currentUser) {
        this.currentUser = currentUser;
    }
}
