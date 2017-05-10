
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Contact {

    @EJB
    private MailSender mailSender;
    private String firstname;
    private String fullname;
    private String senderemail;
    private String recemail;
    private String message;
    private String mlogin;
    private String mpass;
    private String host;
    private Integer port;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setSenderemail(String senderemail) {
        this.senderemail = senderemail;
    }

    public void setRecemail(String recemail) {
        this.recemail = recemail;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getSenderemail() {
        return senderemail;
    }

    public String getRecemail() {
        return recemail;
    }

    public String getMessage() {
        return message;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMlogin() {
        return mlogin;
    }

    public void setMlogin(String mlogin) {
        this.mlogin = mlogin;
    }

    public String getMpass() {
        return mpass;
    }

    public void setMpass(String mpass) {
        this.mpass = mpass;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @PostConstruct
    public void init() {
        mailSender = new MailSender();
    }

    public void sendMail() {
        if (firstname != null) {
            mailSender.sendMail(firstname, fullname, senderemail, recemail, message, host, port, mlogin, mpass);
        }
    }

}
