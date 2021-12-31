import java.util.Scanner;

public class Menu {
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

        Clinica clin = new Clinica();
        Consulta con = new Consulta();
        String comando;
        Scanner scan = new Scanner(System.in);
        do {

            comando = scan.nextLine();

            switch (comando) {
                case "0":
                    System.out.println("Insira o CPF do paciente");
                    String CPF = scan.nextLine();
                    if(clin.VerificaPaciente(CPF)){
                        System.out.println("Paciente ja tem cadastro");

                    } else {
                        
                        System.out.println("Paciente não tem cadastro\n");
                        System.out.println( "Insira o nome do cliente");
                        String nome =(scan.nextLine());
                        System.out.println("Insira o telefone do cliente");
                        String telefone = (scan.nextLine());
                        
                        Paciente pac = new Paciente(CPF, nome, telefone);

                        clin.adicionaPaciente(pac);                   
                    }
                    fazConsulta(CPF, con, clin,0);
                    break;

                case "1":
                    String optCon,data;
                    do{
                        optionsVisualizacao();
                        optCon = scan.nextLine();
                        switch(optCon){
                            case "0": {
                                System.out.println("Insira o CRM do médico. ");
                                int CRM =(Integer.parseInt(scan.nextLine()));
                                System.out.println("Siga o formato de data AAAA-MM-DD ");
                                System.out.println("Insira o dia da consulta. ");
                                data =(scan.nextLine());
                                clin.consultasPorMedicoEmUmDia(CRM, data);
                                break;
                            }
                            case "1": {
                                System.out.println("Siga o formato de data AAAA-MM-DD ");
                                System.out.println("Insira o dia da consulta. ");
                                data =(scan.nextLine());
                                clin.consultasPorDia(data);
                                break;
                            }
                        }
                    } while (!optCon.equals("2"));
                    break;
                case "2":
                    String optCon2,data1, data2;
                    do{
                        optionsVisualizacao();
                        optCon2 = scan.nextLine();
                        switch(optCon2){
                            case "0": {
                                System.out.println("Insira o CRM do medico. ");
                                int CRMMedico =(Integer.parseInt(scan.nextLine()));
                                System.out.println("Siga o formato de data AAAA-MM-DD ");
                                System.out.println("Insira o data inicial. ");
                                data1 =(scan.nextLine());
                                System.out.println("Insira o data final. ");
                                data2 =(scan.nextLine());
                                clin.consultasPorMedicoEmIntervalo(CRMMedico, data1, data2);
                                break;
                            }
                            case "1": {
                                System.out.println("Siga o formato de data AAAA-MM-DD ");
                                System.out.println("Insira o data inicial. ");
                                data1 =(scan.nextLine());
                                System.out.println("Insira o data final. ");
                                data2 =(scan.nextLine());
                                clin.consultasEmIntervalo(data1, data2);
                                break;
                            }
                        }
                    } while (!optCon2.equals("2"));
                    break;
                case "3":
                    System.out.println("Insira o id da consulta a ser alterada");
                    String id = scan.nextLine();
                    int idConsulta = (Integer.parseInt(id));
                    Consulta conAlterada = clin.pegaConsulta(idConsulta);
                    altConsulta(conAlterada);
                    break;
                case "4":
                    System.out.println("Insira o id da consulta a ser exlcluida");
                    String idc = scan.nextLine();
                    int idCon = (Integer.parseInt(idc));
                    clin.removeConsulta(idCon);
                    break;

                case "5":
                    excluiIntervalo(clin);
                    break;
                case "6":
                System.out.println("Insira o CPF do paciente a ser consultado");
                String cpfpac= scan.nextLine();
                clin.consultasDeUmPaciente(cpfpac);
                    break;
            }
            
        } while (!comando.equals("7"));
        scan.close();
    }

    public static void excluiIntervalo(Clinica clin){
        // String comando;
         Scanner scan = new Scanner(System.in);
        String dataInicio,dataFim;
        int crm;
        System.out.println("Siga o formato de data AAAA-MM-DD ");
        System.out.println("Data e horário de início: ");
        dataInicio = scan.nextLine();
        System.out.println("Data e horário de fim: ");
        dataFim = scan.nextLine();
        System.out.println("CRM do médico: ");
        crm = Integer.parseInt(scan.nextLine());
        clin.removeConsultasEmUmIntervalo(crm, dataInicio, dataFim);
        scan.close();

    }

    public static Consulta altConsulta(Consulta con){
        Scanner scan = new Scanner(System.in);
        String comando;
        do{
            optionsConsulta();
            comando = scan.nextLine();
            switch (comando){
                case "0":
                    System.out.println("Siga o formato de data AAAA-MM-DD ");
                    System.out.println("Data e horário de início: ");
                    con.setDtInicio(scan.nextLine());
                case "1":
                    System.out.println("Siga o formato de data AAAA-MM-DD ");
                    System.out.println("Data e horário de fim: ");
                    con.setDtFim(scan.nextLine());
                case "2":
                    System.out.println("Realizada (digite 1 para sim ou 0 para não): ");
                    con.setRealizada(scan.nextLine());
                case "3":                    
                    System.out.println("Valor Pago: ");
                    con.setValorPago(Double.parseDouble(scan.nextLine()));
                case "4":                    
                    System.out.println("Foi pago (digite 1 para sim ou 0 para não): ");
                    con.setPago(Integer.parseInt(scan.nextLine()));
                case "5":
                    System.out.println("CRM do médico: ");
                    con.setCRMMedico(Integer.parseInt(scan.nextLine()));
                case "6":
                    System.out.println("Insira o CPF do paciente");
                    con.setCPFPaciente( scan.nextLine());               
            }

        } while (!comando.equals("7"));
        scan.close();
        return con;
    }   

    public static void fazConsulta(String cpf, Consulta con, Clinica clin, int caso){
        Scanner c1 = new Scanner(System.in);
                
                System.out.println("Insira os dados da consulta. ");
            
                
                con.setCPFPaciente(cpf);

                System.out.println("Siga o formato de data AAAA-MM-DD ");
                System.out.println("Data e horário de início: ");
                con.setDtInicio(c1.nextLine());

                System.out.println("Data e horário de fim: ");
                con.setDtFim(c1.nextLine());
                // String id = c1.nextLine();
                // con.setIdConsulta(Integer.parseInt(id));

                System.out.println("Realizada (digite 1 para sim ou 0 para não): ");
                con.setRealizada(c1.nextLine());

                System.out.println("Valor Pago: ");
                con.setValorPago(Double.parseDouble(c1.nextLine()));

                System.out.println("Foi pago (digite 1 para sim ou 0 para não): ");
                con.setPago(Integer.parseInt(c1.nextLine()));

                System.out.println("CRM do médico: ");
                con.setCRMMedico(Integer.parseInt(c1.nextLine()));
 
                clin.adicionaConsulta(con);

                c1.close();
    }

    

    // public static void optionsMedico() {
    //     System.out.print("========== Sobre o medico ==========\n");
    //     System.out.println("- Insira o código do comando desejado:\n");
    //     System.out.println("(0) Desaja procurar um medico pelo nome.\n");// dar return nos crms
    //     System.out.println("(1) Deseja agendar consulta pelo CRM do medico. \n");
    //     System.out.println("(2) Gostaria de ver as opções de uma determinada especialidade.\n");// CRM
    //     System.out.println("======================================================");
    // }

    public static void optionsConsulta() {

        System.out.print("========== Sobre Consulta ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) alterar a data inicial .\n");// dar return nos crms
        System.out.println("(1) alterar o data final. \n");
        System.out.println("(2) alterar se ela foi realizada ou não. \n");
        System.out.println("(3) alterar o valor pago. \n");
        System.out.println("(4) alterar o se foi pago ou não. \n");
        System.out.println("(5) alterar o CRM do medico. \n");
        System.out.println("(6) alterar o CPF do paciente .\n");// CRM
        System.out.println("(7) voltar ao inicio");
        System.out.println("======================================================");
    }


    public static void optionsVisualizacao() {
        System.out.print("========== Sobre a visualizacao de consultas ==========\n");
        System.out.println("- Insira o código do comando desejado:\n");
        System.out.println("(0) Deseja visualizar por medico.\n");
        System.out.println("(1) Deseja visualizar tudo. \n");
        System.out.println("(2) Sair. \n");
        System.out.println("======================================================");
    }

    public static void main(String[] args){
       options();
            
        }
    }
