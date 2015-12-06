import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import cart.*;

public class SessionListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent arg0) {
        arg0.getSession().setAttribute("cart", new ShoppingCart());
    }
   public void sessionDestroyed(HttpSessionEvent arg0) {
        arg0.getSession().removeAttribute("cart");
   }
}