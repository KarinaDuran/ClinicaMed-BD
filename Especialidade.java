public class Especialidade {
 private int codigo;
 private int Indice;
 private String Nome;
 
 public Especialidade(int codigo){
    this.codigo = codigo;
 }

public void setCodigo(int codigo) {
    this.codigo = codigo;
}
public int getCodigo() {
    return codigo;
}
public void setIndice(int indice) {
    Indice = indice;
}
public int getIndice() {
    return Indice;
}
public void setNome(String nome) {
    Nome = nome;
}
public String getNome() {
    return Nome;
}

@Override
public String toString(){
    return "Codigo:  " + codigo + "Indice:  "+ Indice + "Nome:  " + Nome;
}

}
