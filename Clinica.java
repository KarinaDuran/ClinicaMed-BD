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
            System.out.println("Erro ao se conectar");
            e.printStackTrace();
        }
    }

    public void imprimeLista(List<?> lista) {
        System.out.println("++++++++++ Resultados ++++++++++++");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("---------------------------------------");
            System.out.println(lista.get(i).toString());
        }
        System.out.println("++++++++++++++++++++++++++++++++");
    }

    // Exibir especialidades   -> printar
    public List<Especialidade> codigosEspecialidades() {
        try {
            List<Especialidade> especialidades = new ArrayList<Especialidade>();
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM especialidade");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Especialidade e = new Especialidade(rs.getInt("Codigo"));
                e.setIndice(rs.getInt("Indice"));
                e.setNome(rs.getString("Nome"));

                especialidades.add(e);
            }
            rs.close();
            stmt.close();
            return especialidades;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Exibe Pacientes
    public List<Paciente> Pacientes(String Nome) { 
        // try passar para void
        try {
            List<Paciente> pacientes = new ArrayList<Paciente>();
            PreparedStatement stmt;
            if (Nome == null) {
                stmt = this.connection.prepareStatement("SELECT * FROM paciente");
            } else {
                stmt = this.connection.prepareStatement("SELECT * FROM paciente WHERE NOME = %" + Nome + "%");
            }
            ResultSet rs = stmt.executeQuery();
                // fazer iterações que printem os nomes e as especialidades( ou mais coiasas)
                //while dentro de while
            while (rs.next()) {
                // passar para print
                Paciente p = new Paciente(rs.getString("CPF"), rs.getString("Nome"), rs.getString("Telefone"));
                p.setSexo(rs.getString("Sexo"));
                p.setEndereco(rs.getString("Endereco"));
                p.setIdade(rs.getInt("Idade"));

                pacientes.add(p);
            }
            rs.close();
            stmt.close();
            return pacientes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que retorna os medicos de uma especialidade.
    public List<Medico> MedicosEspecialidade(int codigoEspecialidade) {
        try {
            List<Medico> MedicosEspecialistas = new ArrayList<Medico>();
            // Inserir query da julia Inner join com a tabela de medicoEspecialidade e
            // medico pra pegar o nome e crm de toods daquela especialidade
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM especialidade");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medico m = new Medico();
                m.setCRM(rs.getInt("CRM"));
                m.setNome(rs.getString("Nome"));
                m.setTelefone(rs.getString("Telefone"));
                MedicosEspecialistas.add(m);
            }
            rs.close();
            stmt.close();
            return MedicosEspecialistas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Verifica os crms e especialidades de medicos pelo nome
    public List<Medico> MedicosPorNome(String Nome) {
        try {
            List<Medico> Medicos = new ArrayList<Medico>();
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM medico WHERE Nome = " + Nome);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medico m = new Medico();
                m.setCRM(rs.getInt("CRM"));
                m.setNome(rs.getString("Nome"));
                m.setTelefone(rs.getString("Telefone"));
                Medicos.add(m);
            }
            rs.close();
            stmt.close();
            return Medicos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que retorna a lista das consultas de um dia
    public List<Consulta> consultasPorMedicoEmUmDia(int CRM, String Dia) {
        try {
            List<Consulta> consultas = new ArrayList<Consulta>();
            // Inserir query da julia
            PreparedStatement stmt = this.connection
                    .prepareStatement("SELECT * FROM consulta WHERE CRMMEDICO =  " + CRM);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo para consultas em um intervalo
    public List<Consulta> consultasEmIntervalo(int CRM, String DiaInicio, String DiaFinal) {
        try {
            List<Consulta> consultas = new ArrayList<Consulta>();
            // Inserir query da julia
            PreparedStatement stmt = this.connection
                    .prepareStatement("SELECT * FROM consulta WHERE CRMMEDICO =  " + CRM);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo para consultas de um paciente
    public List<Consulta> consultasDeUmPaciente(String CPF) {
        try {
            List<Consulta> consultas = new ArrayList<Consulta>();
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM consulta WHERE CPF = " + CPF);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que adiciona uma consulta no banco
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

    // Metodo que adiciona um paciente

    /*
     * Quando uma consulta é solicitada a CLIMED, a recepcionista deverá saber o
     * nome e o telefone de contato do paciente
     * 
     */
    public void adicionaPaciente(Paciente paciente) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM paciente WHERE CPF = " + paciente.getCPF())) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println(
                        "Não foi possível inserir o paciente. Ja existe alguem cadastrado com este CPF, no nome de: "
                                + rs.getString("Nome"));
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

    // Remover consulta pelo id
    public void removeConsulta(int Id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM consulta WHERE IdConsulta = " + Id);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Não foi possivel deletar a consulta: " + Id);
        }
    }

    public void removeConsultasEmUmIntervalo(int CRM, String Inicio, String Fim) {

    }

    public Consulta pegaConsulta(int id) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM paciente WHERE IdConsulta = " + id)) {
            ResultSet rs = stmt.executeQuery();
            Consulta c = new Consulta();
            if (rs.next()) {
                c.setIdConsulta(rs.getInt("IdConsulta"));
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
            }
            rs.close();
            stmt.close();
            return c;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void alteraConsulta(Consulta consulta) {
        String sql = "UPDATE consulta SET DtInicio = ?, DtFim = ?, Realizada = ?, ValorPago = ?, Pago =?, CRMMedico = ?, CPFPaciente = ? WHERE";// Karina
                                                                                                                                                // do
                                                                                                                                                // futuro
        try (PreparedStatement stmt = this.connection.prepareStatement(
                sql)) {
            stmt.setString(2, consulta.getDtInicio());
            stmt.setString(3, consulta.getDtFim());
            stmt.setString(4, consulta.getRealizada());
            stmt.setDouble(5, consulta.getValorPago());
            stmt.setInt(6, consulta.getPago());
            stmt.setInt(7, consulta.getCRMMEDICO());
            stmt.setString(8, consulta.getCPFPaciente());
            stmt.execute();
            stmt.close();
            System.out.println("Novos dados da consulta: " + consulta.toString());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Verifica se existe o paciente, caso ele nao lembre
    public void VerificaPaciente(String CPF) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM paciente WHERE CPF = " + CPF)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out
                        .println("CPF já cadastrado: " + rs.getString("CPF") + ", no nome de: " + rs.getString("Nome"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void options() {
        System.out.println("========== BEM-VINDO AO SISTEMA CLINICAMED. ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Agendar Consulta.\n");
        System.out.println("(1) Visualizar consultas do dia. \n");// parametro o dia
        System.out.println("(2) Visualizar consultas em um intervalo de dias.\n");// dois parametros (comeco e fim)
        System.out.println("(3) Alterar dados de uma consulta.\n");
        System.out.println("(4) Excluir uma consulta.\n");
        System.out.println("(5) Excluir consultas por intervalo de dias.\n");
        System.out.println("(6) Visualizar as consultas de um paciente\n");
        System.out.println("(7) Encerrar.\n");
        System.out.println("======================================================");
    }

    public static void optionsPaciente() {
        /* verifica de entrada se o paciente ja tem cadastro ou não
        se não tiver, tem que completar o cadastro
        se tiver fdc
        */
        System.out.print("========== Sobre o paciente ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Paciente ja possui cadastro\n");
        System.out.println("(1) Novo paciente. \n");
        System.out.println("(2) Verificar se o paciente ja esta cadastrado.");
        System.out.println("(3) Voltar ao inicio.\n");
        System.out.println("======================================================");
    }

    public static void optionsMedico() {
        System.out.print("========== Sobre o medico ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Deseja agendar consulta pelo nome do medico.\n");//excluir ou dar return nos crms
        System.out.println("(1) Deseja agendar consulta pelo CRM do medico. \n");
        System.out.println("(2) Gostaria de ver as opções de uma determinada especialidade.\n");//CRM 
        System.out.println("======================================================");
    }

    // Para qualquer uma das visualizacoes
    public static void optionsVisualizacao() {
        System.out.print("========== Sobre a visualizacao de consultas ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Deseja visualizar por medico.\n");
        System.out.println("(1) Deseja visualizar tudo. \n");
        System.out.println("======================================================");
    }

    public static void main(String[] args) {
        Clinica c1 = new Clinica();

        String comando;
        do {
            options();
            comando = c1.scanner.nextLine();

            // switch (comando) {
            // case "0":
            // optionsPaciente();
            // String comandoPaciente = c1.scanner.nextLine();
            // if (comandoPaciente.equals("0")) {
            // System.out.println("Informe o CPF do paciente.");
            // } else if (comandoPaciente.equals("1")) {
            // System.out.println();
            // }

            // Consulta x = new Consulta();

            // System.out.println("CPF do paciente: ");
            // x.setCPFPaciente(c1.scanner.nextLine());
            // System.out.println("Insira os dados da consulta. ");

            // System.out.println("Data e horário de início: ");
            // x.setDtInicio(c1.scanner.nextLine());

            // System.out.println("Data e horário de fim: ");
            // String id = c1.scanner.nextLine();
            // x.setIdConsulta(Integer.parseInt(id));

            // System.out.println("Realizada (digite 1 para sim ou 0 para não): ");
            // x.setRealizada(c1.scanner.nextLine());

            // System.out.println("Valor Pago: ");
            // x.setValorPago(Double.parseDouble(c1.scanner.nextLine()));

            // System.out.println("Foi pago (digite 1 para sim ou 0 para não): ");
            // x.setPago(Integer.parseInt(c1.scanner.nextLine()));

            // System.out.println("CRM do médico: ");
            // x.setCRMMedico(Integer.parseInt(c1.scanner.nextLine()));

            // c1.adicionaConsulta(x);
            // break;
            // case "1":

            // }

        } while (!comando.equals("7"));

    }
}