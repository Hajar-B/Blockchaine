public class Bloc {
	private Etat etatFinal;
	private Transaction transactionEffectuee;
	
	public Bloc(Etat etatFinal, Transaction transactionEffectuee) {
		this.etatFinal = etatFinal;
		this.transactionEffectuee = transactionEffectuee;
	}
	
	public Etat getEtatFinal() {
		return etatFinal;
	}

	public void setEtatFinal(Etat etatFinal) {
		this.etatFinal = etatFinal;
	}

	public Transaction getTransactionEffectuee() {
		return transactionEffectuee;
	}

	public void setTransactionEffectuee(Transaction transactionEffectuee) {
		this.transactionEffectuee = transactionEffectuee;
	}

	@Override
	public int hashCode() {
		int resultat = 1;
		resultat = resultat * 31 + ((this.etatFinal == null) ? 0 : this.etatFinal.hashCode());
		resultat = resultat * 31 + ((this.transactionEffectuee == null) ? 0 : this.transactionEffectuee.hashCode());
		return resultat;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(! (obj instanceof Bloc))
			return false;
		
		Bloc b = (Bloc) obj;
		if((b.etatFinal == null && this.etatFinal != null)
				|| (b.etatFinal != null && this.etatFinal == null)) 
					return false;
		
		else if((b.transactionEffectuee == null && this.transactionEffectuee != null)
				|| (b.transactionEffectuee != null && this.transactionEffectuee == null))
					return false;	
		
		else if (!(b.etatFinal.equals(this.etatFinal)) 
				|| (!(b.transactionEffectuee.equals(this.transactionEffectuee))))
					return false;
		return true;
	}

	@Override
	public String toString() {
		if(this.transactionEffectuee == null) {
			String chaine = "\t * Transaction * \t\n";
			return this.etatFinal.toString() + chaine + "Transaction NULL";
		}
		return this.etatFinal.toString() + this.transactionEffectuee.toString();
	}
	
	public static void main(String[] args) {
		//test affichage
		System.out.println("-_-_-_-_-_-_ Test affichage : \n");
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		Transaction t1 = new Transaction(2,1,0);
		Bloc b1 = new Bloc(e1, t1);
		System.out.println(b1.toString());		
		
		//test egalite
		System.out.println("\n-_-_-_-_-_-_ Test egalite : ");
		int[] tabCrypto2 = {7,45,0};
		Etat e2 = new Etat(tabCrypto2);
		int[] tabCrypto3 = {7,45,0};
		Etat e3 = new Etat(tabCrypto3);
		int[] tabCrypto4 = {2,0,1};
		Etat e4 = new Etat(tabCrypto4);
		Transaction t2 = new Transaction(24,2,8);
		Transaction t3 = new Transaction(24,2,8);
		Transaction t4 = new Transaction(24,2,0);
		Bloc b2 = new Bloc(e2, t2);
		Bloc b3 = new Bloc(e3, t3);
		Bloc b4 = new Bloc(e4, t4);
		
		System.out.println(b2.equals(b3));
		System.out.println(b3.equals(b4));
		
		//test hashCode
		System.out.println("\n-_-_-_-_-_-_ Test hashCode : ");
		System.out.println(b2.hashCode());
		System.out.println(b3.hashCode());
		System.out.println(b4.hashCode());
	}

}
