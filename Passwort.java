public class Passwort {	
	public static void main(String[] args){		
		String eingabe = "";
		int index = 0;
		
		if(args.length > 0)
			eingabe = args[0];
		else 
			return;
		
		if(args.length > 1)
			index = Integer.parseInt(args[1]);
		
		if(index == 0)
			for(int i = 0; i < 26; i++) 
				System.out.println("Verschiebung: "+i+"\n"+code(eingabe, i));
			//robjvsmrox qveomugexcmr. ne rkcd nso kepqklo qovyocd. wird bei einer Verschiebung von 16 in
			//herzlichen glueckwunsch. du hast die aufgabe geloest. decodiert
		else
			System.out.println(code(eingabe, index));
	}	

	public static String code(String eingabe, int index){
		String ausgabe = "";
		char temp=' ';
		//65-90 Grossbuchstaben, hier nicht benoetigt
		//97-122 Kleinbuchstaben in ASCII
		for(int i=0;i<eingabe.length();i++)
		{
			if (!((int)eingabe.charAt(i) < 123 && (int)eingabe.charAt(i) > 96 )){
				// wenn Zeichen ausser a..z enthalten sind, transformiere diese nicht
				ausgabe = ausgabe + eingabe.charAt(i);
			}
			else
			{
				temp=(char)((int)eingabe.charAt(i)+index);
			
				if(temp>122)
				{
					//loop back to start of alphabet
					temp-=26;
				}
				ausgabe+=temp;
			}
		}
		return ausgabe;
	}
}