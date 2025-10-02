package app.services;

// coleções padrões do Java
import java.util.*;

// importações dos packages
import app.utils.*;

public class ListagemService{

    private final Set<String> lista;

    public ListagemService(Set<String> lista){
        this.lista = lista;
    }

    public Map<String, Object> listar(String service){
        Map<String, Object> resposta = Responses.baseDataOkList();

        resposta.put("lista", new ArrayList<>(lista));

        return Responses.ok(service, resposta);
    }

}