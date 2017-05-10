
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.Host;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String userDir = System.getProperty("user.dir");
    
    public static String getUserDir() {
        return userDir;
    }
    
    public static void main(String[] args) {
        Main launcher = new Main();
        try {
            launcher.startServer();
        } catch (Exception e) {
            logger.error("Couldn't start the server" + e);
            System.out.println("Couldn't start the server");
        }
    }
    
    private void startServer() throws Exception {
        File war = new File(System.getProperty("java.class.path"));
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8091);
        tomcat.setBaseDir(userDir);
        
        Host host = tomcat.getHost();
        host.setAppBase(userDir);
        host.setAutoDeploy(true);
        host.setDeployOnStartup(true);
        Context appContext = tomcat.addWebapp(host, "", war.getAbsolutePath());
        System.out.println("Deployed " + appContext.getBaseName() + " as " + appContext.getDocBase());
        logger.info("Deployed " + appContext.getBaseName() + " as " + appContext.getDocBase());
        tomcat.start();
        tomcat.getServer().await();
    }
}
