package banco;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Banco {
	
	public static void main(String[] args) {

		ActorSystem system = ActorSystem.create("Transacciones");

		ActorRef Cliente1 = system.actorOf(Props.create(Cliente.class),"Cliente1");
		
		ActorRef Cliente2 = system.actorOf(Props.create(Cliente.class),"Cliente2");

		Cliente1.tell(47, null);
		Cliente1.tell(10, null);
		Cliente1.tell(-5, null);

		Cliente2.tell(-15, null);
		Cliente2.tell(5, null);

		system.shutdown();

		system.awaitTermination();
		
		
		System.out.println("Saldo en la cuenta: " + Cliente.cuenta);

	}

}

class Cliente extends UntypedActor {
	
	static Integer cuenta = 0;

	@Override
	public void onReceive(Object operacion) throws Exception {

		if (operacion instanceof Integer) {

			Integer o = (Integer) operacion;
			
			cuenta += o;
			//System.out.println(cuenta);

		}

		else {

			unhandled(operacion);

		}

	}
}
