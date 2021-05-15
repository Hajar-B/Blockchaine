import java.util.ArrayList;

public class Blockchaine {
	public ArrayList<Jointure> Bchaine;
	
	public Blockchaine() {
		this.Bchaine = new ArrayList<Jointure>();
	}
	
	public ArrayList<Jointure> getBchaine() {
		return Bchaine;
	}

	public void setBchaine(ArrayList<Jointure> bchaine) {
		Bchaine = bchaine;
	}
	
	@Override
	public int hashCode() {
		int resultat = 1;
		if(this.Bchaine == null)
			return resultat * 31 + 0;
		for(int i=0; i<this.Bchaine.size(); i++)
			resultat = resultat * 31 + this.Bchaine.get(i).hashCode();
		return resultat;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blockchaine other = (Blockchaine) obj;
		if (Bchaine == null) {
			if (other.Bchaine != null)
				return false;
		} else if (!Bchaine.equals(other.Bchaine))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String chaine = "\t * Blockchaine * \n";
		for(int i=0; i<this.Bchaine.size(); i++) 
			chaine = chaine + 
					"\n-------------------------------\n\tBlock numero "+ i 
					+"\n-------------------------------\n" + 
					this.Bchaine.get(i).toString();
		return chaine;
	}
	
	public int[] calculEtat(int somme, int payeur, int receveur) {
		//je recupere le precedent bloc
		Bloc precBloc = this.Bchaine.get(this.Bchaine.size()-1).getBlocAinserer();
		Etat EtatPrecBloc = precBloc.getEtatFinal();
		//je le stocke dans un nouveau tableau
		
		int[] newTabCrypto = new int[EtatPrecBloc.getTabCrypto().length];
		
		for(int i=0; i<EtatPrecBloc.getTabCrypto().length; i++)
			newTabCrypto[i] = EtatPrecBloc.getTabCrypto()[i];
		
		//je chnage la monnaie du payeur et du receveur
		newTabCrypto[receveur] = newTabCrypto[receveur] + somme;
		newTabCrypto[payeur] = newTabCrypto[payeur] - somme;
		//je renvoie le tableau
		return newTabCrypto;
	}


	//ajouter la mise a jour de hash et sel
	public void addBloc(Jointure j) {
		if(this.Bchaine.size() > 0) {
			int somme = j.getBlocAinserer().getTransactionEffectuee().getSomme();
			int payeur = j.getBlocAinserer().getTransactionEffectuee().getSomme();
			int receveur = j.getBlocAinserer().getTransactionEffectuee().getSomme();
			int[] tabCrypto = j.getBlocAinserer().getEtatFinal().getTabCrypto();
			tabCrypto[receveur] = tabCrypto[receveur] + somme;
			tabCrypto[payeur] = tabCrypto[payeur] - somme;
		}
		this.Bchaine.add(j);
	}
	
	public boolean verif(Bloc bloc) {
		if(this.Bchaine.size() == 0) {
			if(bloc.getTransactionEffectuee() == null)
				return true;
			else
				return false;
		}
		// --------------------- on recupere le bloc precedent
		Bloc precBloc = this.Bchaine.get(this.Bchaine.size()-1).getBlocAinserer();
		//on recupere la transaction de ce precedent bloc
		Etat EtatPrecBloc = precBloc.getEtatFinal();
		//on recupere le tableau
		int[] tabPrecCrypto = EtatPrecBloc.getTabCrypto();
		
		// --------------------- on recupere l'etat final du bloc a verifier
		Etat etatBloc = bloc.getEtatFinal();
		int[] tabCrypto = etatBloc.getTabCrypto();
		
		//on recupere la transaction du bloc a verifier
		Transaction transactionBloc = bloc.getTransactionEffectuee();
		//on recupere la somme, le payeur, le receveur du bloc a verifier
		int sommeBloc = transactionBloc.getSomme();
		int payeurBloc = transactionBloc.getPayeur();
		int receveurBloc = transactionBloc.getReceveur();
		
		//ON COMMENCE LES TESTS
		//le payeur doit avoir assez de cryptomonnaie
		if(sommeBloc > tabPrecCrypto[payeurBloc])
			return false;
		//le payeur a moins d'argent
		if(tabCrypto[payeurBloc] != (tabPrecCrypto[payeurBloc] - sommeBloc))
			return false;
		//le receveur a plus d'argent
		if(tabCrypto[receveurBloc] != (tabPrecCrypto[receveurBloc] + sommeBloc))
			return false;
		return true;
	}
	
	public int calculHash(Bloc b, int sel) {
		int resultat = 1;
		resultat = resultat * 31 + sel;
		resultat = resultat * 31 + b.hashCode();
		//on prend le dernier element parce que le bloc n'est pas suppose etre insere
		resultat = resultat * 31 + ((this.Bchaine == null || this.Bchaine.size() == 0) ? 0 : this.Bchaine.get(this.Bchaine.size()-1).getHash());
		return resultat;
	}
	
	//bloc suppose coherent donc on a pas a faire verif dedans
	public boolean inserable(Bloc b, int difficulte, int sel) {
		int i = 0;
		int count = 0;
		
		int hash = calculHash(b, sel);
		String hashToBinary = Integer.toBinaryString(hash);
		String formatBinaryHash = String.format("%32s", hashToBinary);
		System.out.println("hash en binaire = " + formatBinaryHash);
		
		while(formatBinaryHash.charAt(i) != '1') {
			count++;
			i++;
		}
		
		if(difficulte == count)
			return true;
		
		return false;
	}

	public static void main(String[] args) {
		Blockchaine b = new Blockchaine();
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		Bloc b1 = new Bloc(e1, null);
		System.out.println("Verif : bloc b1 coherent ? " + b.verif(b1));
		System.out.println("Inserable : bloc b1 inserable ? " + b.inserable(b1, 6, 4000));
		Jointure j1 = new Jointure (b1, 6, 4000);
		b.addBloc(j1);
		//System.out.println(b.toString());
		
		int[] tabCrypto2 = {4,3,1};
		Etat e2 = new Etat(tabCrypto2);
		Transaction t2 = new Transaction(2,1,0);
		Bloc b2 = new Bloc(e2, t2);
		System.out.println("Verif : bloc b2 coherent ? " + b.verif(b2));
		System.out.println("Inserable : bloc b2 inserable ? " + b.inserable(b2, 3, 4000));
		System.out.println("Inserable : bloc b2 inserable ? " + b.inserable(b2, 6, 4000));
		Jointure j2 = new Jointure (b2, 6, 4000);
		b.addBloc(j2);
		System.out.println(b.toString());
		/*
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		int[] tabCrypto2 = {7,45,0};
		Etat e2 = new Etat(tabCrypto2);
		int[] tabCrypto3 = {4,3,1};
		Etat e3 = new Etat(tabCrypto3);
		int[] tabCrypto4 = {2,0,1};
		Etat e4 = new Etat(tabCrypto4);
		
		//Transaction t1 = new Transaction(2,1,0);
		Transaction t2 = new Transaction(24,2,8);
		Transaction t3 = new Transaction(2,1,0);
		Transaction t4 = new Transaction(24,2,0);
		
		Bloc b1 = new Bloc(e1, null);
		Bloc b2 = new Bloc(e2, t2);
		Bloc b3 = new Bloc(e3, t3);
		Bloc b4 = new Bloc(e4, t4);
		
		Jointure j1 = new Jointure (b1, 1, 6);
		Jointure j2 = new Jointure (b2, 11, 3);
		Jointure j3 = new Jointure (b3, 34, 0);
		Jointure j4 = new Jointure (b4, 10, 3);
		
		Blockchaine b = new Blockchaine();
		//b.addBloc(j1);
		//b.addBloc(j2);
		//b.addBloc(j3);
		//b.addBloc(j4);
		
		//System.out.println(b.toString());	
		//System.out.println(b.equals(b));
		//System.out.println(b.hashCode());
		System.out.println("Bloc inserable b1? " + b.verif(j1.getBlocAinserer()));
		b.addBloc(j1);	
		System.out.println("Bloc inserable b2? " + b.verif(j2.getBlocAinserer()));
		System.out.println("Bloc inserable b3? " + b.verif(j3.getBlocAinserer()));
		b.addBloc(j3);
		System.out.println("Bloc inserable b4? " + b.verif(j4.getBlocAinserer()));
		System.out.println(b.toString());
		*/
	}

}
