import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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


    public void imprimeLista(List<?> lista){
        System.out.println("++++++++++ Resultados ++++++++++++");
        for (int i =0; i< lista.size(); i++){
            System.out.println("---------------------------------------");
            System.out.println(lista.get(i).toString());
        }
        System.out.println("++++++++++++++++++++++++++++++++");
    }
 
    //Exibir especialidades
    public List<Especialidade> codigosEspecialidades(){
        try{
            List<Especialidade> especialidades = new ArrayList<Especialidade>(); 
            //Inserir query da julia
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM especialidade");
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Especialidade e = new Especialidade(rs.getInt("Codigo"));
                e.setIndice(rs.getInt("Indice"));
                e.setNome(rs.getString("Nome"));
             

                especialidades.add(e);
            }
            rs.close();
            stmt.close();
            return especialidades;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    //Metodo que retorna os medicos de uma especialidade.
    public List<Medico> MedicosEspecialidade(int codigoEspecialidade){
        try{
            List<Medico> MedicosEspecialistas = new ArrayList<Medico>(); 
            //Inserir query da julia Inner join com a tabela de medicoEspecialidade e medico pra pegar o nome e crm de toods daquela especialidade
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM especialidade");
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Medico m = new Medico();
                m.setCRM(rs.getInt("CRM"));
                m.setNome(rs.getString("Nome"));
                m.setTelefone(rs.getString("Telefone"));
                MedicosEspecialistas.add(m);
            }
            rs.close();
            stmt.close();
            return MedicosEspecialistas;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    

    //Metodo que retorna a lista das consultas de um dia
    public List<Consulta> consultasDoDia(int CRM, String Dia){
        try{
            List<Consulta> consultas = new ArrayList<Consulta>(); 
            //Inserir query da julia
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM consulta WHERE CRMMEDICO =  " + CRM );
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                consultas.add(c);
            }
            rs.close();
            stmt.close();
            return consultas;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Metodo para consultas em um intervalo
    public List<Consulta> consultasEmIntervalo(int CRM, String DiaInicio, String DiaFinal){
        try{
            List<Consulta> consultas = new ArrayList<Consulta>(); 
            //Inserir query da julia
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM consulta WHERE CRMMEDICO =  " + CRM );
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                consultas.add(c);
            }
            rs.close();
            stmt.close();
            return consultas;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Metodo para consultas de um paciente
    public List<Consulta> consultasDeUmPaciente(String CPF){
        try{
            List<Consulta> consultas = new ArrayList<Consulta>(); 
            //Inserir query da julia
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM consulta WHERE CRMMEDICO = " );
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                consultas.add(c);
            }
            rs.close();
            stmt.close();
            return consultas;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Metodo que adiciona uma consulta no banco
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
        
                String sql = "INSERT INTO CONSULTA(DtInicio, DtFIm, Realizada, ValorPago, Pago, CRMMedico, CPFPaciente) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement stmt2 = this.connection.prepareStatement(sql);
                stmt2.setString(2, consulta.getDtInicio());
                stmt2.setString(3, consulta.getDtFim());
                stmt2.setString(4, consulta.getRealizada());
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

    //Metodo que adiciona um paciente
    public void adicionaPaciente(Paciente paciente) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM paciente WHERE CPF = " + paciente.getCPF())) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Não foi possível inserir o paciente. Ja existe alguem cadastrado com este CPF");
            } else {
                String sql = "INSERT INTO paciente(CPF, Nome, Telefone) VALUES (?,?,?)";
                PreparedStatement stmt2 = this.connection.prepareStatement(sql);
                stmt2.setString(1, paciente.getCPF());
                stmt2.setString(2, paciente.getNome());
                stmt2.setString(3, paciente.getTelefone());

        
                stmt2.execute();
                stmt2.close();
                System.out.println("Paciente registrado: " + paciente.toString());
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
        System.out.println("(1) Visualizar consultas do dia. \n");//parametro
        System.out.println("(2) Visualizar consultas em um intervalo de dias.\n");//Intervalo
        System.out.println("(3) Alterar dados de uma consulta.\n");
        System.out.println("(4) Excluir uma consulta.\n");
        System.out.println("(5) Visualizar as consultas de um paciente\n");
        System.out.println("(6) Encerrar.\n");
        System.out.println("======================================================");
    }

    
    public static void optionsPaciente() {
        System.out.print("========== Sobre o paciente ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Paciente ja possui cadastro\n");
        System.out.println("(1) Novo paciente. \n");
        System.out.println("(2) Voltar ao inicio.\n");
        System.out.println("======================================================");
    }

    public static void optionsMedico() {
        System.out.print("========== Sobre o medico ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Deseja agendar consulta pelo nome do medico.\n");
        System.out.println("(1) Deseja agendar consulta pelo CRM do medico. \n");
        System.out.println("(2) Gostaria de ver as opções de uma determinada especialidade.\n");
        System.out.println("======================================================");
    }

    public static void optionsVisualizacao() {
        System.out.print("========== Sobre a visualizacao de consultas ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Deseja visualizar por medico.\n");
        System.out.println("(1) Deseja visualizar tudo. \n");
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
                    optionsPaciente();
                    String comandoPaciente = c1.scanner.nextLine();
                    if(comandoPaciente.equals("0")){
                        System.out.println("Informe o CPF do paciente.");
                    }else if(comandoPaciente.equals("1")){
                        System.out.println();
                    }

                    Consulta x = new Consulta();

                    System.out.println("CPF do paciente: ");
                    x.setCPFPaciente(c1.scanner.nextLine());
                    System.out.println("Insira os dados da consulta. ");
                
                    System.out.println("Data e horário de início: ");
                    x.setDtInicio(c1.scanner.nextLine());

                    System.out.println("Data e horário de fim: ");
                    String id = c1.scanner.nextLine();
                    x.setIdConsulta(Integer.parseInt(id));

                    System.out.println("Realizada (digite 1 para sim ou 0 para não): ");
                    x.setRealizada(c1.scanner.nextLine());

                    System.out.println("Valor Pago: ");
                    x.setValorPago(Double.parseDouble(c1.scanner.nextLine()));

                    System.out.println("Foi pago (digite 1 para sim ou 0 para não): ");
                    x.setPago(Integer.parseInt(c1.scanner.nextLine()));

                    System.out.println("CRM do médico: ");
                    x.setCRMMedico(Integer.parseInt(c1.scanner.nextLine()));

                    c1.adicionaConsulta(x);
                    break; 
                case "1":

            }

        }while(!comando.equals("6"));


    }
}