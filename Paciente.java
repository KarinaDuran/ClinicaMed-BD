public class Paciente {
    private String CPF;
    private String Nome;
    private String Sexo;
    private String Telefone;
    private String Endereco;
    private int Idade;
 
    public Paciente(String CPF, String Nome, String Telefone){
        this.CPF = CPF;
        this.Nome = Nome;
        this.Telefone = Telefone;
    }
    
    public void setCPF(String CPF){
        this.CPF = CPF;
    }
    
    public String getCPF(){
        return CPF;
    }
    
    public void setNome(String Nome){
        this.Nome = Nome;
    }

    public String getNome(){
        return Nome;
    }

    public void setSexo(String Sexo){
        this.Sexo = Sexo;
    }

    public String getSexo(){
        return Sexo;
    }

    public void setTelefone(String Telefone){
        this.Telefone = Telefone;
    }

    public String getTelefone(){
        return Telefone;
    }

    public void setEndereco(String Endereco){
        this.Endereco = Endereco;
    }

    public String getEndereco(){
        return Endereco;
    }
    
    public void setIdade(int Idade){
        this.Idade = Idade;
    } 

    public int getIdade(){
        return Idade;
    }

    @Override
    public String toString(){
        String retorno;
        retorno = "CPF: " + CPF + ", Nome: "+ Nome + ", Telefone: " +Telefone;
        
        if(Sexo != null) retorno += ", Sexo: " + Sexo;
        if(Endereco != null) retorno += ", Endereco: " + Endereco;
        if(Idade != 0) retorno += ", Idade: "+ Idade;
        
        return retorno;
    }
}
