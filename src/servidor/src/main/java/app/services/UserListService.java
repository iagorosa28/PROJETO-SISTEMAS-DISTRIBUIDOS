package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;

public class UserListService{

    private final Set<String> users;

    public UserListService(Set<String> users){
        this.users = users;
    }

    public Map<String, Object> listarUsers(){
        Map<String, Object> resposta = Responses.baseDataOkList();

        resposta.put("users", new ArrayList<>(users));

        return Responses.ok("users", resposta);
    }

}