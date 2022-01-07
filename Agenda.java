public class Agenda {
    private int idAgenda;
    private String DiaSemana;
    private String HoraInicio;
    private String HoraFim;
    private int CRMMedico;

    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    public int getIdAgenda() {
        return idAgenda;
    }

    public void setDiaSemana(String diaSemana) {
        DiaSemana = diaSemana;
    }

    public String getDiaSemana() {
        return DiaSemana;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraFim(String horaFim) {
        HoraFim = horaFim;
    }

    public String getHoraFim() {
        return HoraFim;
    }

    public void setCRM_Medico(int cRM_Medico) {
        CRMMedico = cRM_Medico;
    }
    public int getCRM_Medico() {
        return CRMMedico;
    }
}
