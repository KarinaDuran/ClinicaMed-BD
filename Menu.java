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
                    fazConsulta(CPF, con, clin);


                case "1":
                    System.out.println("Insira o CRM do médico. ");
                    int CRM =(Integer.parseInt(scan.nextLine()));
                    System.out.println("Siga o formato de data AAAA-MM-DD ");
                    System.out.println("Insira o dia da consulta. ");
                    String data =(scan.nextLine());
                    clin.consultasPorMedicoEmUmDia(CRM, data);
                case "2":
                String optCon;
                do{
                    optionsVisualizacao();
                    optCon = scan.nextLine();
                    switch(optCon){
                        case "0": {
                            System.out.println("Insira o CRM do medico. ");
                            int CRMMedico =(Integer.parseInt(scan.nextLine()));
                            System.out.println("Siga o formato de data AAAA-MM-DD ");
                            System.out.println("Insira o data inicial. ");
                            String data1 =(scan.nextLine());
                            System.out.println("Insira o data final. ");
                            String data2 =(scan.nextLine());
                            clin.consultasPorMedicoEmIntervalo(CRMMedico, data1, data2);
                        }
                        case "1": {
                            System.out.println("Siga o formato de data AAAA-MM-DD ");
                            System.out.println("Insira o data inicial. ");
                            String data1 =(scan.nextLine());
                            System.out.println("Insira o data final. ");
                            String data2 =(scan.nextLine());
                            clin.consultasEmIntervalo(data1, data2);
                        }
                    }
                } while (!optCon.equals("2"));

                case "3":
                    System.out.println("Parte 4");
                    break;
                case "4":
                    System.out.println("Parte 5");
                    break;
                case "5":
                    System.out.println("Parte 6");
                    break;
                case "6":
                    System.out.println("Parte 6");
                    break;
            }

        } while (!comando.equals("7"));

    }

    public static void fazConsulta(String cpf, Consulta con, Clinica clin){
        Scanner c1 = new Scanner(System.in);
                System.out.println("Insira os dados da consulta. ");
                System.out.println("Siga o formato de data AAAA-MM-DD ");
                System.out.println("Data e horário de início: ");
                con.setDtInicio(c1.nextLine());

                System.out.println("Data e horário de fim: ");
                String id = c1.nextLine();
                con.setIdConsulta(Integer.parseInt(id));

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

    public static void optionsPaciente() {
        /*
         * verifica de entrada se o paciente ja tem cadastro ou não
         * se não tiver, tem que completar o cadastro
         * se tiver fdc
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
        System.out.println("(0) Desaja procurar um medico pelo nome.\n");// dar return nos crms
        System.out.println("(1) Deseja agendar consulta pelo CRM do medico. \n");
        System.out.println("(2) Gostaria de ver as opções de uma determinada especialidade.\n");// CRM
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