import java.util.ArrayList;
import java.util.Scanner;

/**
 * MineurHandler est une classe qui permet de saisir et de stocke l'ensemble des transactions qui sont saisies par des clients.
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class MineurHandler implements Runnable {
	private ArrayList<String> transactionEnAttente;
	
	public MineurHandler(){
		transactionEnAttente = new ArrayList<>();
	}
	
	public ArrayList<String> getTransactionEnAttente(){
		return transactionEnAttente;
	}
	
	public void setTransactionEnAttente(ArrayList<String> transactionEnAttente){
		this.transactionEnAttente = transactionEnAttente;
	}
	
	@Override
	public void run(){
		while(true){
			Scanner s = new Scanner(System.in);
			String nom = s.nextLine();		
			getTransactionEnAttente().add(nom);
		}
	}
}
