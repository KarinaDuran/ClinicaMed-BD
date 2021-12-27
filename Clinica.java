import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Clinica {
    public Scanner scanner;
    private Connection connection;

    public Clinica() {
        try {
            this.connection = new ConnectionFactory().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Consulta> ListaDeConsulta(){
        try{
            List<Consulta> x = new ArrayList<Consulta>(); 
        }catch(){

        }
    }
    public void adicionaConsulta(Consulta consulta) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM CONSULTA WHERE CRMMEDICO = " + consulta.getCRMMEDICO() + " AND " + consulta.getDtInicio()
                        + " BETWEEN dtInicio AND dtFim OR " + consulta.getDtFim() + " BETWEEN dtInicio AND dtFim")) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Não foi possível marcar no uma consulta no horário entre " + consulta.getDtInicio()
                        + " e " + consulta.getDtFim());
                System.out.println("Já existia um horário agendado das " + rs.getString("dtInicio") + " às "
                        + rs.getString("dtFim") + ". Tente outro horário.");
            } else {

                String sql = "INSERT INTO CONSULTA(IdConsulta, DtInicio, DtFIm, Realizada, ValorPago, Pago, CRMMedico, CPFPaciente) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement stmt2 = this.connection.prepareStatement(sql);
            
                stmt2.setInt(1, consulta.getIdConsulta());
                // stmt2.setDate(2, consulta.getDtInicio());
                // stmt2.setDate(3, consulta.getDtFim());
                stmt2.setInt(4, consulta.getRealizada());
                stmt2.setDouble(5, consulta.getValorPago());
                stmt2.setInt(6, consulta.getPago());
                stmt2.setInt(7, consulta.getCRMMEDICO());
                stmt2.setString(8, consulta.getCPFPaciente());
                stmt2.execute();
                stmt2.close();
                System.out.println("Consulta marcada: " + consulta.toString());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void options() {
        System.out.print("========== BEM-VINDO AO SISTEMA CLINICAMED. ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Agendar Consulta.\n");
        System.out.println("(1) Visualizar consultas do dia. \n");
        System.out.println("(2) Visualizar consultas dos proximos N dias.\n");
        System.out.println("(3) Visualizar consultas dos ultimos N dias.\n");
        System.out.println("(4) Alterar dados de uma consulta.\n");
        System.out.println("(5) Excluir uma consulta.\n");
        System.out.println("(6) Encerrar.\n");
        System.out.println("======================================================");
    }



    public static void main(String [] args){
        Clinica c1 = new Clinica();

        
        String comando;
        do{
            options();
            comando = c1.scanner.nextLine();

            switch(comando){

                case "0":
                    System.out.println("Insira os dados da consulta. ");
                    Consulta x = new Consulta();
                    System.out.println("Id da Consulta: ");
                    x.setIdConsulta(Integer.parseInt(c1.scanner.nextLine()));

                    System.out.println("Data e horário de início: ");
                    x.setDtInicio(c1.scanner.nextLine());

                    System.out.println("Data e horário de fim: ");
                    String id = c1.scanner.nextLine();
                    x.setIdConsulta(Integer.parseInt(id));

                    System.out.println("Realizada (digite 1 para sim ou 0 para não): ");
                    x.setRealizada(Integer.parseInt(c1.scanner.nextLine()));

                    System.out.println("Valor Pago: ");
                    x.setValorPago(Double.parseDouble(c1.scanner.nextLine()));

                    System.out.println("Foi pago (digite 1 para sim ou 0 para não): ");
                    x.setPago(Integer.parseInt(c1.scanner.nextLine()));

                    System.out.println("CRM do médico: ");
                    x.setCRMMedico(Integer.parseInt(c1.scanner.nextLine()));

                    System.out.println("CPF do paciente: ");
                    x.setCPFPaciente(c1.scanner.nextLine());

                    c1.adicionaConsulta(x);
                    break; 
                case "1":

            }

        }while(!comando.equals("5"));


    }
}