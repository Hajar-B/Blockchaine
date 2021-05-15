public class Jointure {
	private Bloc blocAinserer;
	private int sel;
	private int hash;
	
	public Jointure(Bloc blocAinserer, int sel, int hash) {
		this.blocAinserer = blocAinserer;
		this.sel = sel;
		this.hash = hash;
	}
	
	public Bloc getBlocAinserer() {
		return blocAinserer;
	}

	public void setBlocAinserer(Bloc blocAinserer) {
		this.blocAinserer = blocAinserer;
	}

	public int getSel() {
		return sel;
	}

	public void setSel(int sel) {
		this.sel = sel;
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	@Override
	//j.setHash(j.hashCode())
	public int hashCode() {
		int resultat = 1;
		resultat = resultat * 31 + ((this.blocAinserer == null) ? 0 : this.blocAinserer.hashCode());
		resultat = resultat * 31 + this.sel;
		resultat = resultat * 31 + this.hash;
		return resultat;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(! (obj instanceof Jointure))
			return false;
		
		Jointure j = (Jointure) obj;
		if((j.blocAinserer == null && this.blocAinserer != null) 
				|| (j.blocAinserer != null && this.blocAinserer == null))
					return false;
		if(!(j.blocAinserer.equals(this.blocAinserer)))
			return false;
		if(j.sel != this.sel)
			return false;
		if(j.hash != this.hash)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.blocAinserer.toString() + "\n" + "\n\t[sel : " + sel + " | hash : " + hash + "]\n"; 
	}
	
	public static void main(String[] args) {
		//test affichage
		System.out.println("-_-_-_-_-_-_ Test affichage : \n");
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		Transaction t1 = new Transaction(2,1,0);
		Bloc b1 = new Bloc(e1, t1);
		Jointure j1 = new Jointure (b1, 1, 0);
		System.out.println(j1.toString());
		
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
		Jointure j2 = new Jointure (b2, 1, 0);
		Jointure j3 = new Jointure (b3, 1, 0);
		Jointure j4 = new Jointure (b4, 10, 3);
				
		System.out.println(j2.equals(j3));
		System.out.println(j3.equals(j4));
				
		//test hashCode
		System.out.println("\n-_-_-_-_-_-_ Test hashCode : ");
		System.out.println(j2.hashCode());
		System.out.println(j3.hashCode());
		System.out.println(j4.hashCode());
	}

}
