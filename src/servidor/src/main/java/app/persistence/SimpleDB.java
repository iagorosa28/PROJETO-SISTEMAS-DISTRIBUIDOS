package app.persistence;

import java.util.*;
import java.sql.*;

public class SimpleDB{

    private final String url;   // Ex: "jdbc:sqlite:/app/data/app.db"
    private final String table; // Ex: "users" ou "channels"

    public SimpleDB(String url, String table){
        this.url = url;
        this.table = table;
        criarTabela(); // se não existir, criar
    }

    private void criarTabela(){
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (name TEXT PRIMARY KEY)";
        try(Connection c = DriverManager.getConnection(url); // abre conexão com o banco (dentro do try fecha automaticamente)
            Statement st = c.createStatement()){
            st.execute(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addNome(String name){
        String sql = "INSERT INTO " + table + " (name) VALUES (?)";
        try(Connection c = DriverManager.getConnection(url);
            PreparedStatement ps = c.prepareStatement(sql)){ // Prepara o comando
            ps.setString(1, name); // Preenche o primeiro '?' com o valor de 'name'
            ps.executeUpdate();  // Executa o comando (INSERT)
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean verificaNome(String name){
        String sql = "SELECT 1 FROM " + table + " WHERE name = ? LIMIT 1"; // 'SELECT 1' apenas retorna 1 se achar oq quero; 'LIMIT 1' para na primeira ocorrência
        try(Connection c = DriverManager.getConnection(url);
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, name);
            try(ResultSet rs = ps.executeQuery()){ // Executa a consulta e obtém o cursor de resultados
                return rs.next();  // Se tem pelo menos uma linha, existe -> retorna true; senão false
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarNomes(){
        List<String> saida = new ArrayList<>();
        String sql = "SELECT name FROM " + table + " ORDER BY name";
        try(Connection c = DriverManager.getConnection(url);
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){ 
            while (rs.next()){ // Percorre cada linha do resultado.
                saida.add(rs.getString("name")); // Pega a primeira coluna 'name' da linha atual e adiciona na lista
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return saida;
    }

}
