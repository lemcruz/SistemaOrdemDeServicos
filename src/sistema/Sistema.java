package sistema;
import java.util.ArrayList;

public class Sistema{
    
    private ArrayList<TipoServico> bancoDeServicos;
    private ArrayList<Pessoa> bancoDeUsuarios;
    private int indexUsuarioLogado;
    GerenciadorDB dbManager;

    public Sistema(ArrayList<Pessoa> bancoUsuarios){
        this.bancoDeUsuarios = bancoUsuarios;
    }
 
    public Sistema(){
        dbManager = GerenciadorDB.getInstanceOf();
        this.bancoDeUsuarios = dbManager.getUsersDB();
        this.bancoDeServicos = dbManager.getServicosDB();
        indexUsuarioLogado = -1;
    }
    
    public int getIndexUsuarioLogado() {
        return indexUsuarioLogado;
    }
    
    public Pessoa getUsuarioLogado(){
        if(this.indexUsuarioLogado != -1)
            return this.bancoDeUsuarios.get(indexUsuarioLogado);
        return null;
    }

    public void setIndexUsuarioLogado(int indexUsuarioLogado) {
        this.indexUsuarioLogado = indexUsuarioLogado;
    }
    public void updateDB(GerenciadorDB.DB_TYPE tipo){
        switch (tipo){
            case USERS : 
                dbManager.updateDB(bancoDeUsuarios, GerenciadorDB.DB_TYPE.USERS);
                break;
            case SERVICOS : 
                dbManager.updateDB(bancoDeServicos, GerenciadorDB.DB_TYPE.SERVICOS);
                break;
            case PEDIDOS:
                break;
        }
    }
    public boolean login(String nomeUsuario, String senha) {
        boolean logou = false;
        for (Pessoa u : bancoDeUsuarios){
            if (u.getNomeUsuario().equals(nomeUsuario) && u.getSenha().equals(senha)) {                
                u.setAutenticado(true);                
                this.setIndexUsuarioLogado(this.bancoDeUsuarios.indexOf(u));
                logou = true;                
            }
        }        
        return logou;
    }

    public void logoff(String nomeUsuario, String senha) {

        this.bancoDeUsuarios.forEach(u -> {
            if (u.getNomeUsuario().equals(nomeUsuario) && u.getSenha().equals(senha)) {
                u.setAutenticado(false);
                this.setIndexUsuarioLogado(-1);
            }
        });

    }
    
    public void cadastraAdmin(String nome, String endereco, String email,  
                            String telefone, String nomeUsuario, String senha){

        if(this.bancoDeUsuarios.get(this.indexUsuarioLogado).isAutenticado()){
            if(this.bancoDeUsuarios.get(this.indexUsuarioLogado) instanceof Administrador){
                this.bancoDeUsuarios.add(new Administrador(nome, endereco, email, telefone, nomeUsuario, senha));
                this.updateDB(GerenciadorDB.DB_TYPE.USERS);
            }
            else{
                System.out.println("Usuario não possui as permissoes de administrador. ");
            }
        }
        else{
            System.out.println("Usuario não autenticado. ");
        }
    }

    public void cadastraProfissional(String nome, String endereco, String email,
                            String telefone, String nomeUsuario, String senha){

        if(this.bancoDeUsuarios.get(this.indexUsuarioLogado).isAutenticado()){
                this.bancoDeUsuarios.add(new Profissional(nome, endereco, email,
                                                telefone, nomeUsuario, senha));
            this.updateDB(GerenciadorDB.DB_TYPE.USERS);
        }
        else{
            System.out.println("Usuario não autenticado. ");
        }

    }

    public void cadastraCliente(String nome, String endereco, String email, 
                            String telefone, String nomeUsuario, String senha){

        if(this.bancoDeUsuarios.get(this.indexUsuarioLogado).isAutenticado()){
            this.bancoDeUsuarios.add(new Cliente(nome, endereco, email, 
                                        telefone, nomeUsuario, senha));
            this.updateDB(GerenciadorDB.DB_TYPE.USERS);
        }
        else{
            System.out.println("Usuario não autenticado. ");
        }

    }
    
    public void listarUsuarios(){

        this.bancoDeUsuarios.forEach(usuario -> {
            System.out.println("Login usuario: " + usuario.getNomeUsuario() + " Tipo usuario: " + usuario.getClass().getName());
        });

    }
    
    public void cadastrarTipoServico(int codigo,String descricao,boolean ativo){
        if(this.bancoDeUsuarios.get(this.indexUsuarioLogado).isAutenticado()){
            this.bancoDeServicos.add(new TipoServico(codigo,descricao,ativo,-1));
        }
        else{
            System.out.println("Usuario não autenticado. ");
        }
    }
    
    public void updateTipoServico(TipoServico s,TipoServico sUpdate){
        for(int i=0;i<this.bancoDeServicos.size();i++){
            if(this.bancoDeServicos.get(i).equals(s)){
                this.bancoDeServicos.set(i,sUpdate);
            }
        }    
    }
    
    public void validarServico(TipoServico s){
        for(int i=0;i<this.bancoDeServicos.size();i++){
            if(this.bancoDeServicos.get(i).equals(s)){
                s.setAtivo(true);
                this.bancoDeServicos.set(i,s);
            }
        }   
    }
    
    public void deleteTipoServico(TipoServico s){
        this.bancoDeServicos.remove(s);   
    }
    
    public void selecionaServicosPrestados(ArrayList<TipoServico> servicosPrestados,Profissional p){
        for(int i=0;i<bancoDeUsuarios.size();i++){
            if(bancoDeUsuarios.get(i).equals(p)){
                p.setServicosPrestados(servicosPrestados);
            }
        }
    }
}