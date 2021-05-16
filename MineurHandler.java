import java.util.ArrayList;
import java.util.Scanner;

public class MineurHandler implements Runnable {
	private ArrayList<String> transactionEnAttente;
	
	public void MineurHandler(){
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
