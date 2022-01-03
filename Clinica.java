import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Clinica {
    public Scanner scanner;
    private Connection connection;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Clinica() {
        try {
            this.connection = new ConnectionFactory().getConnection();
        } catch (SQLException e) {
            System.out.println("Erro ao se conectar");
            e.printStackTrace();
        }
    }

    // Exibir especialidades
    // V
    public void codigosEspecialidades() {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM especialidade");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Especialidade e = new Especialidade(rs.getInt("Codigo"));
                e.setIndice(rs.getInt("Indice"));
                e.setNome(rs.getString("Nome"));
                e.toString();
                System.out.println("\n");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Não foi possivel acessar à tabela.");
            throw new RuntimeException(e);
        }
    }

    // Exibe Pacientes
    // V
    public void Pacientes(String Nome) {
        try {
            PreparedStatement stmt;
            if (Nome == null) {
                stmt = this.connection.prepareStatement("SELECT * FROM paciente");
            } else {
                stmt = this.connection.prepareStatement("SELECT * FROM paciente WHERE NOME = %" + Nome + "%");
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Paciente p = new Paciente(rs.getString("CPF"), rs.getString("Nome"), rs.getString("Telefone"));
                if (rs.getString("Sexo") != null)
                    p.setSexo(rs.getString("Sexo"));
                if (rs.getString("Endereco") != null)
                    p.setEndereco(rs.getString("Endereco"));
                if (rs.getInt("Idade") != 0)
                    p.setIdade(rs.getInt("Idade"));

                System.out.println(p.toString());
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que retorna os medicos de uma especialidade.
    // V
    public void MedicosEspecialidade(int codigoEspecialidade) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                    "SELECT * FROM Medico AS A JOIN EspecialidadeMedico AS B  WHERE A.CRM = B.CRM AND B.CodigoEspecialidade = "
                            + codigoEspecialidade);
            ResultSet rs = stmt.executeQuery();
            if (rs.wasNull())
                System.out.println("Não existem medicos para essa especialidade na clínica.");
            while (rs.next()) {
                Medico m = new Medico();
                m.setCRM(rs.getInt("CRM"));
                m.setNome(rs.getString("Nome"));
                m.setTelefone(rs.getString("Telefone"));
                System.out.println(m.toString());
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(
                    "Não foi possivel verificar os medicos especialistas da especialidade: " + codigoEspecialidade);
            throw new RuntimeException(e);
        }
    }

    // Verifica os crms e especialidades de medicos pelo nome
    // V
    public void MedicosPorNome(String Nome) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM Medico WHERE Nome LIKE \"%?%\"");
            ResultSet rs = stmt.executeQuery();
            if (rs.wasNull())
                System.out.println("Não existe um médico com um nome parecido.");
            while (rs.next()) {
                int CRM = rs.getInt("CRM");
                PreparedStatement stmt2 = this.connection.prepareStatement(
                        "SELECT A.Nome, A.CRM, C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = "
                                + CRM);
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("Nome: " + rs2.getString("A.Nome") + "  CRM: " + rs2.getInt("A.CRM"));
                while (rs2.next()) {
                    System.out.println("Especialidade: " + rs2.getString("C.Nome"));
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Não foi possível acessar o médico pelo nome.");
            throw new RuntimeException(e);
        }
    }

    public void HistoricoPaciente(String CPF) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                    "SELECT A.Nome, A.CRM, D.Nome FROM Medico A INNER JOIN CONSULTA B ON A.CRM = B.CRMMedico INNER JOIN Historico C ON C.IdConsulta = B.IdConsulta INNER JOIN Doenca D ON C.IdDoenca = D.IdDoenca WHERE B.CPFPaciente = "
                            + CPF);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Histórico do paciente: ");
            while (rs.next()) {
                System.out.println("Doenca: " + rs.getString("D.Nome") + "  Medico responsavel pelo diagnostico: "
                        + rs.getString("A.Nome") + " - CRM: " + rs.getInt("A.CRM"));
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível realizar a visualização do historico");
        }

    }

    // V - V
    public boolean VerificaHorario(String DataInicio, String DataFim, int CRM) {

        try {
            PreparedStatement stmt;
            stmt = this.connection.prepareStatement(

                    "SELECT * FROM CONSULTA WHERE CRMMEDICO = " + CRM + " AND DtInicio BETWEEN STR_TO_DATE(\""
                            + DataInicio +
                            "\", \"%Y-%m-%d %H:%i\") AND STR_TO_DATE(\"" + DataFim
                            + "\", \"%Y-%m-%d %H:%i\") OR DtFim BETWEEN STR_TO_DATE(\"" + DataInicio
                            + "\", \"%Y-%m-%d %H:%i\") AND STR_TO_DATE(\"" + DataFim
                            + "\", \"%Y-%m-%d %H:%i\") OR STR_TO_DATE(\"" + DataInicio
                            + "\", \"%Y-%m-%d %H:%i\") BETWEEN DtInicio AND DtFim;");
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return false;

            int horaInico = LocalDateTime.parse(DataInicio, formatter).getHour();
            int minutoInicio = LocalDateTime.parse(DataInicio, formatter).getMinute();

            int horaFim = LocalDateTime.parse(DataFim, formatter).getHour();
            int minutoFim = LocalDateTime.parse(DataFim, formatter).getMinute();

            PreparedStatement stmt1 = this.connection
                    .prepareStatement("SELECT * FROM agenda WHERE HoraInicio <= MAKETIME(" + horaInico + ", "
                            + minutoInicio + ", 00) AND HoraFim >= MAKETIME(" + horaFim + ", " + minutoFim
                            + ", 00) AND DiaSemana = DAYOFWEEK(\"" + DataInicio + "\") AND  CRMMEDICO = " + CRM + ";");
            ResultSet rs1 = stmt1.executeQuery();

            if (!rs1.next()) {
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Não foi possível veificar o horario.");
            e.printStackTrace();
        }
        return true;
    }

    // Terminar de ajeitar o que vai ser exibido:
    /*
     * Um dos objetivos do sistema é imprimir todo o cronograma de atendimento do
     * dia, para cada um dos médicos, contendo a hora e a especialidade do
     * atendimento, além disso os dados de cada um dos pacientes com consulta: nome,
     * telefone de contato, histórico de doenças com datas e médicos responsáveis
     * pelo diagnóstico.
     */
    // Metodo que retorna a lista das consultas de um dia
    // V
    public void consultasPorMedicoEmUmDia(int CRM, String Dia) {
        try {
            PreparedStatement stmt = this.connection
                    .prepareStatement(
                            "SELECT * FROM Consulta AS A JOIN Medico AS B WHERE A.CRMMedico = B.CRM AND B.CRM = " + CRM
                                    + " AND DtInicio = CONVERT(" + Dia + ", GETDATE())");
            ResultSet rs = stmt.executeQuery();

            PreparedStatement stmt2 = this.connection
                    .prepareStatement(
                            "SELECT A.Nome, C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = "
                                    + CRM);
            ResultSet rs2 = stmt2.executeQuery();
            System.out.println("Medico: " + rs2.getString("A.Nome") + " - Especialidade(s): ");
            while (rs2.next()) {
                System.out.print(rs2.getString("C.Nome"));
            }
            rs2.close();
            stmt2.close();

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                System.out.println(c.toString());

                PreparedStatement stmt1 = this.connection
                        .prepareStatement(
                                "SELECT Nome, Telefone FROM paciente WHERE CPF = " + rs.getString("CPFPaciente"));
                ResultSet rs1 = stmt1.executeQuery();
                System.out.println(
                        "Paciente: " + rs1.getString("Nome") + ", Telefone do paciente: " + rs1.getString("Telefone"));
                rs1.close();
                stmt1.close();

                System.out.println("-----------------------------------------------------");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void consultasPorDia(String Dia) {
        try {
            PreparedStatement stmt = this.connection
                    .prepareStatement("SELECT * FROM Consulta AS A AND DtInicio = CONVERT(" + Dia + ", GETDATE()");
            ResultSet rs = stmt.executeQuery();

            System.out.println("Dia : " + Dia);

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                System.out.println(c.toString());

                PreparedStatement stmt1 = this.connection
                        .prepareStatement(
                                "SELECT Nome, Telefone FROM paciente WHERE CPF = " + rs.getString("CPFPaciente"));
                ResultSet rs1 = stmt1.executeQuery();
                System.out.println(
                        "Paciente: " + rs1.getString("Nome") + ", Telefone do paciente: " + rs1.getString("Telefone"));
                rs1.close();
                stmt1.close();

                PreparedStatement stmt2 = this.connection
                        .prepareStatement(
                                "SELECT A.Nome, C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = ?");
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("Medico :" + rs2.getString("A.Nome") + "   Especialidade(s): ");
                while (rs2.next()) {
                    System.out.print(rs2.getString("C.Nome"));
                }
                rs2.close();
                stmt2.close();

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo para consultas em um intervalo
    // V
    public void consultasEmIntervalo(String DiaInicio, String DiaFinal) {
        try {
            PreparedStatement stmt = this.connection
                    .prepareStatement(
                            "SELECT * FROM Consulta WHERE DtInicio BETWEEN CONVERT(" + DiaInicio
                                    + ", GETDATE()) AND CONVERT( " + DiaFinal + ", GETDATE())");
            ResultSet rs = stmt.executeQuery();

            System.out.println("Do dia: " + DiaInicio + " ao Dia: " + DiaFinal);

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                System.out.println(c.toString());

                PreparedStatement stmt1 = this.connection
                        .prepareStatement(
                                "SELECT Nome, Telefone FROM paciente WHERE CPF = " + rs.getString("CPFPaciente"));
                ResultSet rs1 = stmt1.executeQuery();
                System.out.println(
                        "Paciente: " + rs1.getString("Nome") + ", Telefone do paciente: " + rs1.getString("Telefone"));
                rs1.close();
                stmt1.close();

                PreparedStatement stmt2 = this.connection
                        .prepareStatement(
                                "SELECT A.Nome, C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = "
                                        + c.getCRMMEDICO());
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("Medico: " + rs2.getString("A.Nome") + "Especialidade(s): ");
                while (rs2.next()) {
                    System.out.print(rs2.getString("C.Nome"));
                }
                rs2.close();
                stmt2.close();

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // V
    public void consultasPorMedicoEmIntervalo(int CRM, String DiaInicio, String DiaFinal) {
        try {
            PreparedStatement stmt = this.connection
                    .prepareStatement(
                            "SELECT * FROM Consulta AS A JOIN Medico AS B WHERE A.CRM_Medico = B.CRM AND B.CRM = " + CRM
                                    + " AND DtInicio BETWEEN CONVERT(" + DiaInicio + ", GETDATE()) AND CONVERT( "
                                    + DiaFinal + ", GETDATE())");
            ResultSet rs = stmt.executeQuery();

            PreparedStatement stmt2 = this.connection
                    .prepareStatement(
                            "SELECT C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = "
                                    + CRM);
            ResultSet rs2 = stmt2.executeQuery();
            System.out.println("Especialidade(s): ");

            while (rs2.next()) {
                System.out.print(rs2.getString("C.Nome"));
            }
            rs2.close();
            stmt2.close();

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                System.out.println(c.toString());
                PreparedStatement stmt1 = this.connection
                        .prepareStatement(
                                "SELECT Nome, Telefone FROM paciente WHERE CPF = " + rs.getString("CPFPaciente"));
                ResultSet rs1 = stmt1.executeQuery();
                System.out.println(
                        "Paciente: " + rs1.getString("Nome") + ", Telefone do paciente: " + rs1.getString("Telefone"));
                rs1.close();
                stmt1.close();
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo para consultas de um paciente
    // v
    public void consultasDeUmPaciente(String CPF) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM consulta WHERE CPF = " + CPF);
            ResultSet rs = stmt.executeQuery();

            PreparedStatement stmt1 = this.connection
                    .prepareStatement(
                            "SELECT Nome, Telefone FROM paciente WHERE CPF = " + rs.getString("CPFPaciente"));
            ResultSet rs1 = stmt1.executeQuery();
            System.out.println(
                    "Paciente: " + rs1.getString("Nome") + ", Telefone do paciente: " + rs1.getString("Telefone"));
            rs1.close();
            stmt1.close();

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setDtInicio(rs.getString("DtInicio"));
                c.setDtFim(rs.getString("DtFim"));
                c.setRealizada(rs.getString("Realizada"));
                c.setValorPago(rs.getDouble("ValorPago"));
                c.setPago(rs.getInt("Pago"));
                c.setCRMMedico(rs.getInt("CRMMedico"));
                c.setCPFPaciente(rs.getString("CPFPaciente"));

                System.out.println(c.toString());

                PreparedStatement stmt2 = this.connection
                        .prepareStatement(
                                "SELECT C.Nome FROM Medico AS A INNER JOIN EspecialidadeMedico AS B ON A.CRM = B.CRM INNER JOIN Especialidade AS C ON B.CodigoEspecialidade = C.Codigo WHERE A.CRM = "
                                        + c.getCRMMEDICO());
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("Especialidade(s): ");
                while (rs2.next()) {
                    System.out.print(rs2.getString("C.Nome"));
                }
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que adiciona uma consulta no banco
    // V - V
    public void adicionaConsulta(Consulta consulta) {
        try {

            boolean disponivel = VerificaHorario(consulta.getDtInicio(), consulta.getDtFim(), consulta.getCRMMEDICO());

            if (!disponivel) {
                System.out.println("Não foi possível marcar no uma consulta no horário entre " + consulta.getDtInicio()
                        + " e " + consulta.getDtFim());
            } else {
                String sql = "INSERT INTO CONSULTA(DtInicio, DtFim, Realizada, ValorPago, Pago, CRMMedico, CPFPaciente) VALUES (STR_TO_DATE(\""
                        + consulta.getDtInicio() + "\", \"%Y-%m-%d %H:%i\"),STR_TO_DATE(\"" + consulta.getDtFim()
                        + "\", \"%Y-%m-%d %H:%i\"),?,?,?,?,?)";
                PreparedStatement stmt2 = this.connection.prepareStatement(sql);
                stmt2.setString(1, consulta.getRealizada());
                stmt2.setDouble(2, consulta.getValorPago());
                stmt2.setInt(3, consulta.getPago());
                stmt2.setInt(4, consulta.getCRMMEDICO());
                stmt2.setString(5, consulta.getCPFPaciente());
                stmt2.execute();
                stmt2.close();
                System.out.println("Consulta marcada: \n" + consulta.toString());
            }
        } catch (SQLException e) {
            System.out.println("Não foi possível inserir a consulta.");
            e.printStackTrace();
        }

    }

    // Metodo que adiciona um paciente
    // V - V
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
                String sql = "INSERT INTO paciente(CPF, Nome, Sexo, Telefone) VALUES (?,?,?,?)";
                PreparedStatement stmt2 = this.connection.prepareStatement(sql);
                stmt2.setString(1, paciente.getCPF());
                stmt2.setString(2, paciente.getNome());
                stmt2.setString(3, paciente.getSexo());
                stmt2.setString(4, paciente.getTelefone());

                stmt2.execute();
                stmt2.close();
                System.out.println("Paciente registrado: " + paciente.toString());
            }
        } catch (SQLException e) {
            System.out.println("Não foi possível inserir o paciente na tabela.");
            e.printStackTrace();
        }

    }

    // Remover consulta pelo id
    // V - V
    public void removeConsulta(int Id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM consulta WHERE IdConsulta = " + Id);
            System.out.println("Consulta com id " + Id + " removido.");
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Não foi possivel deletar a consulta: " + Id);
        }
    }

    // V
    public void removeConsultasEmUmIntervalo(int CRM, String Inicio, String Fim) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Consulta WHERE CRM_Medico =" + CRM + " AND DtIncio BETWEEN " + Inicio + " AND " + Fim);
            stmt.execute();
            stmt.close();
            System.out.println(
                    "Excluindo as consultas do dia " + Inicio + " ao dia " + Fim + ", do médico com CRM = " + CRM);
        } catch (SQLException e) {
            System.out.println("Não foi possivel deletar as consultas");
        }
    }

    // Retorna a consulta para ser alterada
    // V - V
    public Consulta pegaConsulta(int id) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM consulta WHERE IdConsulta = " + id)) {
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
            System.out.println("Não foi possível pegar a consulta de id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    // Alterar tabela
    // V - v
    public void alteraConsulta(Consulta consulta) {

        boolean disponivel = VerificaHorario(consulta.getDtInicio(), consulta.getDtFim(), consulta.getCRMMEDICO());

        if (disponivel) {
            String sql = "UPDATE consulta SET DtInicio = ?, DtFim = ?, Realizada = ?, ValorPago = ?, Pago =?, CRMMedico = ?, CPFPaciente = ? WHERE IdConsulta = ?";
            try (PreparedStatement stmt = this.connection.prepareStatement(
                    sql)) {
                stmt.setString(1, consulta.getDtInicio());
                stmt.setString(2, consulta.getDtFim());
                stmt.setString(3, consulta.getRealizada());
                stmt.setDouble(4, consulta.getValorPago());
                stmt.setInt(5, consulta.getPago());
                stmt.setInt(6, consulta.getCRMMEDICO());
                stmt.setString(7, consulta.getCPFPaciente());
                stmt.setInt(8, consulta.getIdConsulta());
                stmt.execute();
                stmt.close();
                System.out.println("Novos dados da consulta: " + consulta.toString());

            } catch (SQLException e) {
                System.out.println("Não foi possível atualizar os dados da consulta.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Não foi possível agendar nos novos horários.");
        }
    }

    // Verifica se existe o paciente, caso ele nao lembre
    // V - V
    public boolean VerificaPaciente(String CPF) {
        try (PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM paciente WHERE CPF = " + CPF)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out
                        .println("CPF já cadastrado: " + rs.getString("CPF") + ", no nome de: " + rs.getString("Nome"));
                return true;
            }else{
                System.out.println("CPF não cadastrado: " + CPF);
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível fazer a verificação do paciente.");
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // Consulta c = new Consulta();
        // c.setCPFPaciente("12345678910");
        // c.setCRMMedico(221425533);
        // c.setDtInicio("2022-03-02 12:30");
        // c.setDtFim("2022-03-02 17:40");
        // c.setPago(0);
        // c.setRealizada("Pendente");
        // c.setValorPago(0);

        Clinica clinica = new Clinica();

        // clinica.adicionaConsulta(c);

        // Paciente p = new Paciente(, "Maria", "198576255887");
        // p.setSexo("Feminino");

        // Consulta a = clinica.pegaConsulta(2);
        // System.out.println(a);

        // a.setPago(1);
        // a.setRealizada("Realizada");

        // clinica.alteraConsulta(a);
        // clinica.adicionaPaciente(p);

        clinica.VerificaPaciente("1111");
        // Menu.options();

    }
}