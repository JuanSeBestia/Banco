/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zebastit
 */
public class Banco {

    /**
     * @param args the command line arguments
     * Aqui estaran el # de operaciones (0,1) y las mismas operaciones (2,n)
     */
    private static Integer cuenta = new Integer(0);
    private static String[] operands;
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        //System.out.println("Argumentos" + args.length);
        if (args.length == 0) {
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

            operands = args;
        }
        try {
            ThreadOperation hilo1 = new ThreadOperation(0, operands);
            ThreadOperation hilo2 = new ThreadOperation(1, operands);
            //System.out.println("Empezemos con las Transacciones :");
            hilo1.start();
            hilo2.start();
            hilo1.join();
            hilo2.join();
            System.out.println("Saldo en el banco: " + cuenta);
        } catch (InterruptedException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error XP:" + ex);
        }


    }

    /**
     * @return the cuenta
     */
    public static Integer getCuenta() {
        return cuenta;
    }

    /**
     * @param aCuenta the cuenta to set
     */
    public static void setCuenta(Integer aCuenta) {
        cuenta = aCuenta;
    }
}
