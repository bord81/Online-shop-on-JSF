
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import java.util.*;

@ManagedBean
@RequestScoped
public class CartShow {

    @ManagedProperty(value = "#{currentUserStatus}")
    private UserStatus userStatus;
    @ManagedProperty(value = "#{itemsList}")
    private ItemsShow itemsShow;
    @ManagedProperty(value = "#{cManager}")
    private CartManager cartManager;
    private List<CartItem> items;
    private Map<Integer, CartItem> citems;
    private int cartamount = 0;
    private int totalcount = 0;
    private List<UserJsf> userList = new ArrayList<>();
    private Map<String, Object> cookieMap;
    private static final Logger logger = LogManager.getLogger(CartShow.class);

    public List<CartItem> getItems() {
        return items;
    }

    public int getCartamount() {
        return cartamount;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setItemsShow(ItemsShow itemsShow) {
        this.itemsShow = itemsShow;
    }

    public void setCartManager(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    @PostConstruct
    public void loadCart() {
        UserJsf currentUser = userStatus.getCurrentUser();
        if (currentUser != null) {
            formCart(currentUser.getBasket());
            logger.info("loadCart: Cart for current user loaded");
        } else {
            cookieMap = cartManager.getCookieMap();
            if (cookieMap.get("c0ntAct") != null) {
                EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
                Query query = entityManager.createQuery("select u from UserJsf u", UserJsf.class);
                userList = (List<UserJsf>) query.getResultList();
                entityManager.close();
                Cookie cookie = (Cookie) cookieMap.get("c0ntAct");
                String userCookie = cookie.getValue();
                String currentBasket = "";
                for (UserJsf user : userList) {
                    if (user.getCookie().equals(userCookie)) {
                        currentBasket = user.getBasket();
                        break;
                    }
                }
                formCart(currentBasket);
            } else {
                formEmpty();
            }
        }
    }

    private void formCart(String jsonBasket) {
        try {
            CartList cartList = new Gson().fromJson(jsonBasket, CartList.class);
            if (cartList!=null) {
                List<ItemJsf> allItems = itemsShow.getItems();
                items = new ArrayList<>();
                citems = new HashMap();
                Map ids = new HashMap();
                CartItem cartItem;
                for (Integer integer : cartList.items) {
                    int nextId = allItems.get(integer - 1).getId();
                    if (!ids.containsKey(nextId)) {
                        Integer count = 1;
                        ids.put(nextId, count);
                        cartItem = new CartItem(allItems.get(integer - 1).getId(), count, allItems.get(integer - 1).getImage(), allItems.get(integer - 1).getsImage(), allItems.get(integer - 1).getName(), allItems.get(integer - 1).getFullName(), allItems.get(integer - 1).getDescript(), allItems.get(integer - 1).getPrice());
                        citems.put(nextId, cartItem);
                        totalcount++;
                        cartamount = cartamount + allItems.get(integer - 1).getPrice();
                    } else {
                        Integer incr = (Integer) ids.get(nextId);
                        incr++;
                        ids.put(nextId, incr);
                        cartItem = new CartItem(allItems.get(integer - 1).getId(), incr, allItems.get(integer - 1).getImage(), allItems.get(integer - 1).getsImage(), allItems.get(integer - 1).getName(), allItems.get(integer - 1).getFullName(), allItems.get(integer - 1).getDescript(), allItems.get(integer - 1).getPrice() * incr);
                        citems.remove(nextId);
                        citems.put(nextId, cartItem);
                        totalcount++;
                        cartamount = cartamount + allItems.get(integer - 1).getPrice();
                    }
                    items = new ArrayList<CartItem>(citems.values());
                }
            } else {
                formEmpty();
            }
        } catch (JsonSyntaxException ex) {
            formEmpty();
            ex.printStackTrace();
        } catch (JsonParseException ex) {
            formEmpty();
            ex.printStackTrace();
        }
    }

    private void formEmpty() {
        citems = new HashMap();
        citems.put(0, new CartItem(0, 0, "", "", "", "", "", 0));
        items = new ArrayList<CartItem>(citems.values());
    }
}
