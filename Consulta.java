import java.sql.Date;
import java.time.LocalDateTime;

public class Consulta{

private int IdConsulta;
private LocalDateTime DtInicio;
private LocalDateTime DtFim;
private int Realizada;
private double ValorPago;
private int Pago;
private int CRMMedico;
private String CPFPaciente;

public void setIdConsulta(int idConsulta){
     this.IdConsulta = idConsulta;
}

public int getIdConsulta(){
    return IdConsulta;
}

public void setDtInicio(LocalDateTime DtInicio){
    this.DtInicio = DtInicio;
}

public LocalDateTime getDtInicio(){
   return DtInicio;
}

public void setDtFim(LocalDateTime DtFim){
    this.DtFim = DtFim;
}

public LocalDateTime getDtFim(){
   return DtFim;
}


public void setRealizada(int Realizada){
    this.Realizada = Realizada;
}

public int getRealizada(){
   return Realizada;
}

public void setValorPago(double ValorPago){
    this.ValorPago = ValorPago;
}

public double getValorPago(){
   return ValorPago;
}

public void setPago(int Pago){
    this.Pago = Pago;
}

public int getPago(){
    return Pago;
}

public void setCRMMedico(int CRMMedico){
    this.CRMMedico = CRMMedico;
}
public int getCRMMEDICO(){
    return CRMMedico;
}

public void setCPFPaciente(String CPFPaciente){
    this.CPFPaciente = CPFPaciente;
}
public String getCPFPaciente(){
    return CPFPaciente;
}



}
