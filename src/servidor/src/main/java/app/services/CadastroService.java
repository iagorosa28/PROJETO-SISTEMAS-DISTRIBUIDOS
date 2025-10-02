package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;

public class CadastroService{

    private final Set<String> lista;

    public CadastroService(Set<String> lista) {
        this.lista = lista;
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

        if(lista.contains(name)){
            if(service.equals("login")){
                return Responses.serviceError("login", "usuário já existente");
            }else if(service.equals("channel")){
                return Responses.serviceError("channel", "channel já existente");
            }
        }

        lista.add(name);

        Map<String,Object> resposta = Responses.baseDataOk();
        // resposta.put("description", name + "cadastrado!");

        return Responses.ok(service, resposta);
    }

}