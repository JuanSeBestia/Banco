/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

/**
 *
 * @author zebastit
 */
public class ThreadOperation extends Thread {

    private int temp = 0, numop = 0, start = 2, pers;
    private String Operands[];
    
    public ThreadOperation(int pers, String Operands[]) {
        this.pers = pers;
        this.Operands = Operands;

    }

    @Override
    public void run() {
        numop = Integer.parseInt(Operands[pers]);
        if (pers == 1) {
            start = 2 + Integer.parseInt(Operands[0]);
        }

        for (int i = start; i < numop + start; i++) {
            temp = Banco.getCuenta();
            System.out.println("Se esta sumando " + Operands[i] + " con " + temp + "por la persona" + pers);
            temp = temp + Integer.parseInt(Operands[i]);
            Banco.setCuenta(temp);
        }


    }
}
