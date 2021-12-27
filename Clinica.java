import java.sql.*;

public class Clinica {

    private Connection connection;

    public Clinica() {
        try {
            this.connection = new ConnectionFactory().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
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
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}