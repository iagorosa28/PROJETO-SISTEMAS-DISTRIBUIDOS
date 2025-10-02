package app.routers;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.services.*;

public class Router{

    private final Set<String> users;

    public Router(Set<String> users){
        this.users = users;
    }

    public Map<String, Object> qualService(String service, Map<String, Object> data){
        
        if(service.equals("login")){
            LoginService login = new LoginService(users);
            return login.tratarLogin(data);

        }else if(service.equals("users")){
            UserListService usersList = new UserListService(users);
            return usersList.listarUsers();
        }

        return Responses.error("serviço desconhecido: " + service);

    }

}