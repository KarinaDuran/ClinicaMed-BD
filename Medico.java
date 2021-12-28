public class Medico {
    private int CRM;
    private String Nome;
    private String Telefone;

    public void setCRM(int cRM) {
        CRM = cRM;
    }

    public int getCRM() {
        return CRM;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getNome() {
        return Nome;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getTelefone() {
        return Telefone;
    }

    @Override
    public String toString(){
        return "CRM:  " + CRM + "/nNome:  " + Nome + "/nTelefone:  " + Telefone;
    }
}
