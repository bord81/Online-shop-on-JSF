
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@ManagedBean
@RequestScoped
public class UserService {

    private List<UserJsf> users;

    @PostConstruct
    public void loadUsers() {
        EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
        Query query = entityManager.createQuery("select u from UserJsf u", UserJsf.class);
        users = (List<UserJsf>) query.getResultList();
        entityManager.close();
    }

    public List<UserJsf> getUsers() {
        return users;
    }
}
