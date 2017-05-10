
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "itemsList")
@RequestScoped
public class ItemsShow {

    private static final Logger logger = LogManager.getLogger(ItemsShow.class);
    private List<ItemJsf> items;
    private String price;

    @PostConstruct
    public void loadItems() {
        EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
        Query query = entityManager.createQuery("select c from ItemJsf c", ItemJsf.class);
        items = query.getResultList();
        entityManager.close();
        logger.info("loadItems: Items loaded from db");
        logger.info("updateUsersDb: successful db update");
    }

    public List<ItemJsf> getItems() {
        if (items == null) {
            items = new ArrayList<>();
            items.add(new ItemJsf(0, "", "", "", "", "", 0));
        }
        return items;
    }

    public String getOnePrice() {
        return price;
    }

    public ItemJsf getOneItem() {
        Map<String, String> mapParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int id = Integer.parseInt(mapParams.get("id"));
        if (items != null && !items.isEmpty() && id - 1 < items.size() && id - 1 >= 0) {
            price = String.valueOf(items.get(id - 1).getPrice());
            logger.info("getOneItem: one item returned");
            return items.get(id - 1);
        }
        logger.debug("getOneItem: empty item returned");
        return new ItemJsf(0, "", "", "", "", "", 0);
    }
}
