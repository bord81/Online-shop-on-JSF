
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ODBUtil {

    private static final String oDBfile = Main.getUserDir() + "/ROOT/jsf_test.odb";
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(oDBfile);

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
