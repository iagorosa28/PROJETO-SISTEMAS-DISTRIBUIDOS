package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;
import app.persistence.*;

public class ListagemService{

    private final SimpleDB db;

    public ListagemService(SimpleDB db){
        this.db = db;
    }

    public Map<String, Object> listar(String service){
        Map<String, Object> resposta = Responses.baseDataOkList();

        List<String> nomes = db.listarNomes();
        resposta.put("lista", new ArrayList<>(nomes));

        return Responses.ok(service, resposta);
    }

}