/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import java.util.Scanner;
import scala.Array;

/**
 *
 * @author zebastit
 */
public class Banco {

    /**
     * @param args the command line arguments
     * Aqui estaran el # de operaciones (0,1) y las mismas operaciones (2,n)
     */
    private static String[] operands;
    public static Scanner sc = new Scanner(System.in);
    static public ActorSystem system = ActorSystem.create("Transacciones");
    static public ActorRef BancoServerCuenta = system.actorOf(Props.create(BancoServer.class), "Servidor");
    //Solo nesesite un Actor para los dos Usuarios ya que su comportamiento es similar
    static public ActorRef UserA = system.actorOf(Props.create(User.class), "UserA");
    static public ActorRef UserB = system.actorOf(Props.create(User.class), "UserB");

    public static void main(String[] args) {
        //Si desea ver si es por argumentos o por I/O 
        //System.out.println("Argumentos" + args.length);

        if (args.length == 0) {
            //Si no hay argumentos Escanearlos
            Integer n = sc.nextInt();
            Integer m = sc.nextInt();
            String[] aux = new String[n + m + 2];
            aux[0] = n.toString();
            aux[1] = m.toString();
            for (int i = 2; i < n + m + 2; i++) {
                aux[i] = sc.next();
            }

            operands = aux;
        } else {
            //Si hay argumentos cogerlos
            operands = args;
        }


        //System.out.println("Empezemos con las Transacciones :");
        int op1 = Integer.parseInt(operands[0]);
        int op2 = Integer.parseInt(operands[1]);
        String[] operandsA = new String[op1];
        String[] operandsB = new String[op2];

        Array.copy(operands, 2, operandsA, 0, op1);
        Array.copy(operands, op1 + 2, operandsB, 0, op2);
        /*Prueba ArrayCopy
        for (String b : operandsB) {
        System.out.println("losB"+b);
        }
        for (String a : operandsA) {
        System.out.println("losA"+a);
        }
         * 
         */
        //Se le dice Atraves de mesnasjes a cada Cliente que debe hacer
        UserA.tell(operandsA, null);
        UserB.tell(operandsB, null);

        system.shutdown();//Manda a terminacion de los actores
        system.awaitTermination();//JOIN//Esperan a que terminen
        //Pruebas de Pedir el slado con mensaje pero hacen conflicto con la funcion system.shoutdown()
        // BancoServerCuenta.tell("Imprimir", null);
        //ActorRef BancoServerImprimir = system.actorOf(Props.create(BancoServer.class), "Imprimir");
        // BancoServerImprimir.tell("Imprimir", null);
        //system.shutdown();//Manda a terminacion de los actores
        //system.awaitTermination();//JOIN//Esperan a que terminen
        //Imprime el saldo por una funcion
        //System.out.println("Saldo en el banco: " + BancoServer.getCuenta());




    }
}

class BancoServer extends UntypedActor {

    private static int cuenta;
    private static boolean otroYaTermino = false;

    @Override
    public void onReceive(Object operacion) throws Exception {
        //System.out.println("Ahh llegado un mensaje" + operacion.toString());
        if (operacion instanceof String) {

            String preps = (String) operacion;
            //Impirime el saldo solo cuando el primero termina
            if (preps.equals("Imprimir")) {
                if (otroYaTermino) {
                    System.out.println("Mensaje->Saldo en el banco: " + BancoServer.getCuenta());
                } else {
                    otroYaTermino = true;
                }
            } //La suma en la cuenta
            else {
                int aux = Integer.parseInt((String) operacion);
                cuenta = getCuenta() + aux;
            }
        } else {

            unhandled(operacion);

        }

    }

    /**
     * @return the cuenta
     */
    public static int getCuenta() {
        return cuenta;
    }
}

class User extends UntypedActor {

    private int numop = 0;
    private String Operands[];

    @Override
    public void onReceive(Object operacion) throws Exception {
        if (operacion instanceof String[]) {

            Operands = (String[]) operacion;
            numop = Operands.length;
            for (int i = 0; i < numop; i++) {
                //System.out.println("Se esta sumando " + Operands[i] + " con " + aux );
                //Sesion Critica
                Banco.BancoServerCuenta.tell(Operands[i], null);

            }
            Banco.BancoServerCuenta.tell("Imprimir", null);

        } else {

            unhandled(operacion);

        }

    }
}
