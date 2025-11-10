package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.persistence.*;

public class CadastroService{

    private final SimpleDB db;

    public CadastroService(SimpleDB db) {
        this.db = db;
    }

    public Map<String, Object> tratarCadastro(String service, Map<String,Object> data){
        String name = null;

        if(service.equals("login")){
            name = Utils.verificaString(data.get("user"));
        }else if(service.equals("channel")){
            name = Utils.verificaString(data.get("channel"));
        }

        if(name == null || name.trim().isEmpty()){
            if(service.equals("login")){
                return Responses.serviceError("login", "dados de usuário inválidos");
            }else if(service.equals("channel")){
                return Responses.serviceError("channel", "dados de channel inválidos");
            }
        }

        name = name.trim();

        if(!db.verificaNome(name)){
            db.addNome(name);
        }

        Map<String,Object> resposta = Responses.baseDataOk();
        // resposta.put("description", name + "cadastrado!");

        return Responses.ok(service, resposta);
    }

}