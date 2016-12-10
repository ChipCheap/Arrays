
public class Array {
	private static int[] tarray;		//zu Testzwecken t(est)array
	private static boolean insideArray =true;		//boolscher Wert zur Bestimmung, ob ein Index noch in einem Array noch existiert (siehe a))
	private static int arrayLength=0;	//Wert für array.length()
	private static int arrayMax=0;		//Maximum eines Arrays
	private static int indMax=0;		//Index des Maximums
	public static void main(String[] args)
	{	//reines Testing
		
		
		tarray = new int[5];
		tarray[0]=3;
		tarray[1]=2;
		tarray[2]=4;
		tarray[3]=5;
	//	tarray[4]=2;
	//	tarray[5]=21;
		arrayLength=arrayLength(tarray);
		System.out.println(show(tarray));
	/*	int i=0;
		while(i<arrayLength)
		{
			System.out.println(tarray[i]);
			i++;
		}
		*/
	//	System.out.println("Laenge:" +arrayLength);
	//	square(tarray);
	//	swap(tarray,3,2);
	//	int[] testarray=resize(tarray,8);
	//	sort(tarray);
		System.out.println(median(tarray));
	}
//8.1.
//a)	
	/**
	 * gibt die Zahl aus dem int-Array mit Index i aus
	 * @param array vom Typ int
	 * @param i	index des auszugebenden Wertes
	 * @return	Wert im Array  mit Index i
	 */
	public static int arrayGet(int[] array, int i)
	{
		if(i<0)
		{
			System.out.println("keine negativen Indizes");
			return 0;
		}
		//falls i größer ist als die Länge des Arrays, wird der Fehler behandelt 
			//zudem wird die Variable insideArray auf falsch gesetzt, bedeutet, dass gewisser Index i nicht mehr
			//im Array enthalten ist (fuer spaetere Anwendung nuetzlich 
		try
		{
			return array[i];
		}
		catch	(Exception e)
		{
			insideArray=false;
	//		System.out.println("Index groesser als Array-Laenge");	//aufgrund der vielen arrayLength(..) Aufrufe wird diese Nachricht
																	//in der Konsole gespammt
			
		}
		return 0;			
	}
	//Hilfsmethode
	public static int arrayLength(int[] array)
	{
		int i=0;
		while(insideArray)
		{
			arrayGet(array,i);
			i++;
		}
		arrayLength=i-1;
		insideArray=true;
		return arrayLength;
	}
//b)	
	/**
	 * berechnet die Summe der Eintraege
	 * @param array vom Typ int
	 * @return Summe "sum" aller Eintraege im eingegebenen Array
	 */
	public static int sum(int[] array)
	{
		int sum=0;
		/*	antiker Code ohne Hilfemethode
		int i=0;
		int sum=0;
		while(insideArray)
		{
			sum+=arrayGet(array,i);
			i++;
		}
		arrayLength=i-1;
		return sum;
		*/
		arrayLength=arrayLength(array);
		for (int i=0;i<=arrayLength;i++)
		{
			sum+=arrayGet(array,i);
		}
		return sum;
	}
//c)	
	/**
	 * berechnet das Mittel aller Eintraege im Array
	 * @param array vom Typ int 
	 * @return Mittelwert der Eintraege im Array
	 */
	//double ergibt hier in meinen Augen mehr Sinn als ganzzahlige Div
	public static double mean(int[] array)
	{
		return (double)sum(array)/arrayLength;
	}
//d)	
	/**
	 * quadriert alle Eintraege des Arrays
	 * @param array vom Typ int 
	 */
	public static void square(int[] array)
	{
		arrayLength=arrayLength(array);
		for(int i=0;i<arrayLength;i++)
		{
			array[i]*=array[i];
		}
	}
//e)
	/**
	 * errechnet den hoechsten Wert im Array
	 * @param array vom Typ int 
	 * @return Maximum des eingegebenen Arrays
	 */
	public static int max(int[] array)
	{
		int max=0;		//Maximum
		arrayLength=arrayLength(array);
		for(int i=0;i<arrayLength;i++)
		{
			if (arrayGet(array,i)>max)
			{
				max=array[i];
				indMax=i;
			}
		}
		arrayMax=max;
		System.out.println("indMax:"+indMax+"\narrayMax:"+arrayMax);
		return max;	
	}
//8.2.
//a)
	/**
	 * tauscht Wert vom Index i mit dem vom Index j aus und vice versa 
	 * @param array vom Typ int 
	 * @param i	Index 1
	 * @param j Index 2
	 */
	public static void swap(int[] array, int i, int j)
	{
		arrayLength=arrayLength(array);
		if(j>arrayLength-1||j<0||i<0||i>arrayLength-1)
		{
			System.out.println("Indizes zu gross");
			return;
		}
		int temp=0;		//temporaer Wert zu Abspeicherung eines Wertes
		temp=arrayGet(array,j);
		array[j]=array[i];
		array[i]=temp;
	}
//b)
	/**
	 * sortiert die Eintraege des eingegebenen Arrays
	 * @param array vom Typ int
	 */
	//b) wird hier nach d) programmiert, da d) als Hilfsmethode angewandt wird
	public static void sort(int[] array)
	{
		arrayLength=arrayLength(array);
		int[] tempArr=array;
		int[] sortedArr=new int[arrayLength];
		for(int i=0;i<arrayLength(array);i++)
		{
			sortedArr[arrayLength-1-i]=max(tempArr);
			swap(tempArr,indMax,arrayLength(tempArr)-1);
			tempArr=resize(tempArr,arrayLength(tempArr)-1);
			System.out.println("tempArr:"+show(tempArr)+"\nsortedArr"+show(sortedArr));
		}
		
		for(int j=0;j<arrayLength(array);j++)
		{
			array[j]=sortedArr[j];
		}
	}
//c)
	/**
	 * ermittelt den Median eines Arrays
	 * @param array vom Typ int
	 * @return Median des Arrays
	 */
	//hier wird automatisch das Array sortiert, damit die Median-Berechnung vereinfacht wird
	public static int median(int[] array)
	{
		sort(array);
		arrayLength=arrayLength(array);
	/*	if(arrayLength%2==0)							keine Notwendigkeit der Fallunterscheidung bei Sortierung
		{
			if(array[arrayLength/2]>array[arrayLength]-1)		Bedingung nicht notwendig, da nach Groesse sortiert wird
			{
			return array[arrayLength/2];			bei geraden sowie ungeraden Laengen an Arrays wird derselbe Index ausgegeben, Laenge/2
			}
		}
		else
		{	
	*/
			return array[arrayLength/2];
	//	}
	}
//d)
	/**
	 * schneidet Eintraege vom eingegebenen Array ab, oder fuegt Nullen hinzu
	 * @param array vom Typ int
	 * @param length Laenge des neuen Arrays
	 * @return	Array aus den Werten des eingegebenen Arrays mit der neuen Laenge
	 */
	//wird fuer Aufgabe b) genutzt, s.o.
	public static int[] resize(int[] array, int length)
	{
		arrayLength=arrayLength(array);
		int[] newArr=new int[length];
		if(length>arrayLength)
		{
			for(int i=0;i<arrayLength;i++)
			{
				newArr[i]=array[i];
			}
		}
		else
		{
			for(int i=0;i<length;i++)
			{
				newArr[i]=array[i];
			}
		}
		System.out.println(show(newArr));
		return newArr;
	}
//e)	
	/**
	 * erschafft einen String, der die Eintraege eines Arrays beinhaltet
	 * @param array vom Typ int
	 * @return	String S, der aus allen Eintraegen des Arrays besteht
	 */
	public static String show(int[] array)
	{
		String S="[ ";
		int i=0;
		arrayLength=arrayLength(array);
		while(i<arrayLength)
		{
			S+=array[i]+" ";
			i++;
		}
		S+="]";
		return S;
		
	}
}
