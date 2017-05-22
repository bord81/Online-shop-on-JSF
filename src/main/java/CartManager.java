
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;

@ManagedBean(name = "cManager")
@RequestScoped
public class CartManager {

    @ManagedProperty(value = "#{currentUserStatus}")
    private UserStatus userStatus;
    private List<UserJsf> userList = new ArrayList<>();
    private Map<String, String> mapParams;
    private Map<String, Object> cookieMap;
    private static final Logger logger = LogManager.getLogger(CartManager.class);

    public Map<String, Object> getCookieMap() {
        return cookieMap;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @PostConstruct
    public void loadCart() {
        mapParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        cookieMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        logger.info("loadCart: init done");
    }

    public void addItem() {
        if (userStatus.getCurrentUser() != null) {
            UserJsf currentUser = userStatus.getCurrentUser();
            CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
            cartList.items.add(Integer.valueOf(mapParams.get("add")));
            currentUser.setBasket(new Gson().toJson(cartList));
            updateUsersDb(currentUser);
            logger.info("addItem: current user was updated");
        } else {
            if (cookieMap.get("c0ntAct") != null) {
                EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
                Query query = entityManager.createQuery("select u from UserJsf u", UserJsf.class);
                userList = (List<UserJsf>) query.getResultList();
                entityManager.close();
                Cookie cookie = (Cookie) cookieMap.get("c0ntAct");
                String userCookie = cookie.getValue();
                UserJsf currentUser = new UserJsf();
                boolean isPresent = false;
                for (UserJsf user : userList) {
                    if (user.getCookie().equals(userCookie)) {
                        currentUser = new UserJsf(user.getId(), user.getCookie(), user.getBasket());
                        isPresent = true;
                        break;
                    }
                }
                if (!isPresent) {
                    logger.info("addItem: new user was updated");
                    addNewUser();
                } else {
                    CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
                    cartList.items.add(Integer.valueOf(mapParams.get("add")));
                    currentUser.setBasket(new Gson().toJson(cartList));
                    userStatus.setCurrentUser(currentUser);
                    updateUsersDb(currentUser);
                    logger.info("addItem: current user was updated first time based on cookie");
                }
            } else {
                addNewUser();
                logger.info("addItem: new user was updated");
            }
        }
    }

    public void removeItem() {
        if (userStatus.getCurrentUser() != null) {
            UserJsf currentUser = userStatus.getCurrentUser();
            CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
            cartList.items.remove(Integer.valueOf(mapParams.get("id")));
            currentUser.setBasket(new Gson().toJson(cartList));
            updateUsersDb(currentUser);
            logger.info("removeItem: current user was updated");
        } else {
            if (cookieMap.get("c0ntAct") != null) {
                EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
                Query query = entityManager.createQuery("select u from UserJsf u", UserJsf.class);
                userList = (List<UserJsf>) query.getResultList();
                entityManager.close();
                Cookie cookie = (Cookie) cookieMap.get("c0ntAct");
                String userCookie = cookie.getValue();
                UserJsf currentUser = new UserJsf();
                for (UserJsf user : userList) {
                    if (user.getCookie().equals(userCookie)) {
                        currentUser = new UserJsf(user.getId(), user.getCookie(), user.getBasket());
                        break;
                    }
                }
                CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
                cartList.items.remove(Integer.valueOf(mapParams.get("id")));
                currentUser.setBasket(new Gson().toJson(cartList));
                userStatus.setCurrentUser(currentUser);
                updateUsersDb(currentUser);
                logger.info("removeItem: current user was updated first time based on cookie");
            }
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            logger.error("IO Exception" + e);
        }
    }

    public void removeAll() {
        if (userStatus.getCurrentUser() != null) {
            UserJsf currentUser = userStatus.getCurrentUser();
            CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
            cartList.items.clear();
            currentUser.setBasket(new Gson().toJson(cartList));
            updateUsersDb(currentUser);
            logger.info("removeAll: current user was updated");
        } else {
            if (cookieMap.get("c0ntAct") != null) {
                EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
                Query query = entityManager.createQuery("select u from UserJsf u", UserJsf.class);
                userList = (List<UserJsf>) query.getResultList();
                entityManager.close();
                Cookie cookie = (Cookie) cookieMap.get("c0ntAct");
                String userCookie = cookie.getValue();
                UserJsf currentUser = new UserJsf();
                for (UserJsf user : userList) {
                    if (user.getCookie().equals(userCookie)) {
                        currentUser = new UserJsf(user.getId(), user.getCookie(), user.getBasket());
                        break;
                    }
                }
                CartList cartList = new Gson().fromJson(currentUser.getBasket(), CartList.class);
                cartList.items.clear();
                currentUser.setBasket(new Gson().toJson(cartList));
                userStatus.setCurrentUser(currentUser);
                updateUsersDb(currentUser);
                logger.info("removeItem: current user was updated first time based on cookie");
            }
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            logger.error("IO Exception" + e);
        }
    }

    private void addNewUser() {
        UUID uuid = UUID.randomUUID();
        String cookie = uuid.toString();
        Map<String, Object> properties = new HashMap<>();
        properties.put("maxAge", 31536000);
        try {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("c0ntAct", URLEncoder.encode(cookie, "UTF-8"), properties);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException" + e);
        }
        CartList cartList = new CartList();
        cartList.items.add(Integer.valueOf(mapParams.get("add")));
        String cart = new Gson().toJson(cartList);
        SecureRandom secureRandom = new SecureRandom();
        int id = secureRandom.nextInt();
        UserJsf newUser = new UserJsf(id, cookie, cart);
        userStatus.setCurrentUser(newUser);
        updateUsersDb(newUser);
    }

    private void updateUsersDb(UserJsf u) {
        jpaCall(u);
    }

    private void jpaCall(UserJsf u) {
        EntityManager entityManager = ODBUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(u);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
