package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;

public class LoginService{

    private final Set<String> users;

    public LoginService(Set<String> users) {
        this.users = users;
    }

    public Map<String, Object> tratarLogin(Map<String,Object> data){
        String user = Utils.verificaString(data.get("user"));

        if(user == null || user.trim().isEmpty()){
            return Responses.serviceError("login", "dados de usuário inválidos");
        }

        user = user.trim();

        if(users.contains(user)){
            return Responses.serviceError("login", "usuário já existente");
        }

        users.add(user);

        Map<String,Object> resposta = Responses.baseDataOk();
        resposta.put("description", user + "cadastrado!");

        return Responses.ok("login", resposta);
    }

}