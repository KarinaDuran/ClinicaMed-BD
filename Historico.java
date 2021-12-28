public class Historico {
    private int IdHistorico;
    private int IdConsulta;
    private int IdDoenca;
    private String RemediosReceitados;
    private String TratamentosRecomendados;
    private String Observacoes;

    public void setIdHistorico(int idHistorico) {
        IdHistorico = idHistorico;
    }

    public int getIdHistorico() {
        return IdHistorico;
    }

    public void setIdConsulta(int idConsulta) {
        IdConsulta = idConsulta;
    }

    public int getIdConsulta() {
        return IdConsulta;
    }

    public void setIdDoenca(int idDoenca) {
        IdDoenca = idDoenca;
    }

    public int getIdDoenca() {
        return IdDoenca;
    }

    public void setRemediosReceitados(String remediosReceitados) {
        RemediosReceitados = remediosReceitados;
    }

    public String getRemediosReceitados() {
        return RemediosReceitados;
    }

    public void setTratamentosRecomendados(String tratamentosRecomendados) {
        TratamentosRecomendados = tratamentosRecomendados;
    }

    public String getTratamentosRecomendados() {
        return TratamentosRecomendados;
    }

    public void setObservacoes(String observacoes) {
        Observacoes = observacoes;
    }

    public String getObservacoes() {
        return Observacoes;
    }
}
