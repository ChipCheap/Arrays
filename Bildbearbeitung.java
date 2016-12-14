import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Random;
import java.util.Arrays;

/**
 * Die Klasse Bildbearbeitung laedt eine Bilddatei und fuehrt 
 * abhaengig von gewaehlten Optionen eine Reihe von 
 * Bildmanipulationen aus. 
 */
public class Bildbearbeitung {
	private  int[][] pixels;
	
	/**
	 * Konstruktor fuer die Klasse Bildbearbeitung, die eine Bilddatei einliest
	 * und das zweidimensionale Pixel-Array pixels befuellt.
	 * @param file Einzulesende Bilddatei
	 */
	private Bildbearbeitung(String file) {
		try 
		{
			BufferedImage img = ImageIO.read(new File(file));
			pixels = new int[img.getWidth()][img.getHeight()];
			for(int i = 0; i < dimX(); i++)
				for(int j = 0; j < dimY(); j++)
				{
					pixels[i][j] = img.getRGB(i, j);
				}
		} 
		catch (IOException e) {}
	}	
	
	private int dimX(){
		return pixels.length;
	}
	
	private int dimY(){
		if(dimX() == 0)
			return 0;
		return pixels[0].length;
	}

	/** 
	 * Diese Funktion schreibt den Inhalt des Pixelarrays in die
	 * Datei 'ausgabe.png'. Falls diese Datei nicht vorhanden ist, 
	 * wird sie angelegt. Eine vorhandene Datei wird eventuell ueberschrieben!
	 */
	private void save(String option){
		BufferedImage img = new BufferedImage(dimX(), dimY(), 3);
		for(int i = 0; i < dimX(); i++)
			for(int j = 0; j < dimY(); j++)
				img.setRGB(i, j, pixels[i][j]);
		
		try {
			File outputfile = new File("ausgabe-" + option + ".png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {}
	}
//a)	
	/**
	 * Diese Funktion nimmt einen ARGB-Wert und wandelt ihn in 
	 * ein vierelementiges Array um, das die einzelnen Bytes 
	 * als int-Komponenten enthaelt. 
	 * @param ARGB Integre Darstellung eines Pixels mit 4 Byte
	 * Information: alpha-rot-gruen-blau
	 * @return 4-elementiges Array [alpha, red, green, blue]
	 */
	private int[] getColors(int ARGB) {
		int[] pixelBin=new int[32];		//temporaeres Array mit binaeren Werten der ARGB	(Hilfsarray)
		int[] pixelFin=new int[4];		//Endausgabe fuer Methode	
		String binARGB="";				//binaerer ARGB String
		binARGB=Integer.toBinaryString(ARGB);		//Umwandlung des ints in binaere Darstellung
		for(int i=0;i<binARGB.length();i++)
		{
			pixelBin[31-i]=binARGB.charAt(binARGB.length()-1-i)-48;		//Hilfsarray pixelBin wird mit den binaeren Werten gefuellt
				//Auffuellung von hinten; -48, da ASCII Wert von 1 49 ist
		}
		for(int j=0;j<4;j++)		//Aufteilung des Hilfsarrays in 4 8-bit Teile
		{
			for(int k=0;k<8;k++)
			{
							//finales Array wird befuellt
				pixelFin[3-j]+=pixelBin[31-(j*8)-k]*(int)Math.pow(2, k);	//nach Binaer-Dezimalkonversion errechnet
							//j*8 ->8 bit jeweils, Aufzaehlung mit 2^k reset, sodass letztlich Farbe eines Pixels in Hexadez. Werten ausgegeben wird: 0-255
			}
		}
		return pixelFin;
	}
	
	//Test- und Hilfsmethode fuer e) 
	public static int recolor(int[] color)
	{
		String clr="";
		for(int i=0;i<4;i++)
		{
			String check=Integer.toBinaryString(color[i]);
			while(check.length()<8)
			{
				check="0"+check;
			}
			clr+=check;
		}
		try
		{
			return -Integer.parseInt(clr,2);
		}
		catch(Exception e)
		{
			return Integer.parseInt(clr,2);
		}
	}
	
	/**
	 * Ein vierelementiges Array mit kleinen (< 1 byte) int-Werten 
	 * wird zu einem einzigen 4 byte Integer zusammengesetzt. 
	 * @param array 4-elementiges Array
	 * @return Eine Integerdarstellung einer Farbe in ARGB-Format.
	 */
	private int setColors(int[] array) { 
		int alpha = array[0] << 24;
		int red = array[1] << 16;
		int green = array[2] << 8;
		int blue = array[3];
		return alpha | red | green | blue;
	}
//c)			nicht diese Methode bewerten! weiter unten aktualisierte Version
	/**
	 * Das Bild wird hier rotiert und n*90 Grad.
	 * @param n Anzahl der Vierteldrehungen.
	 */
	/*		antik, rudimentaer und unelegant
	private void rotate(int n) 
	{
		int[][] tempPixels=new int[dimY()][dimX()];
		int temp=0;
		switch(n%4)
		{
			case 1:
				for(int i = 0; i < dimX(); i++)
				{
					for(int j = 0; j < dimY(); j++)
					{
						   tempPixels[dimY()-1-j][i] = pixels[i][j]; 
					}
				}
				pixels = tempPixels;
				break;
			case 2:
				for(int i = 0; i < dimX(); i++)
				{
					//dimY()/2, da sonst die Rotation zweimal durchgefuehrt wird -> Original
					for(int j = 0; j < dimY()/2; j++)
					{
						temp=pixels[i][j];
						pixels[i][j]=pixels[dimX()-1-i][dimY()-1-j];
						pixels[dimX()-1-i][dimY()-1-j]=temp;
					}
				}
				break;
			case 3:
			//UNELEGANZ
				rotate(1);
				rotate(1);
				rotate(1);
				break;
			default:return;
			
		}
	}
	*/
//c)	
	public void rotate(int n)
	{
		int counter=0;			//zum Zaehlen der Iterationen
		while(counter<n)
		{
			int[][] tempPixels=new int[dimY()][dimX()];		//Dimensionen umgedreht
			for(int i = 0; i < dimX(); i++)
			{
				for(int j = 0; j < dimY(); j++)
				{
					tempPixels[dimY()-1-j][i] = pixels[i][j]; //Pixelauffuellung von rechts oben nach links oben
				}
			}
			pixels = tempPixels;
			counter++;
		}
	}
//d)	
	/**
	 * Die Farben werden invertiert: Farbe = (255-Farbe)
	 */
	private void invert() {
		int[] invArr=new int[4];		//invertierte Farbwerte werden hier gespeichert
		int[] tempArr=new int[4];		//speichert temporaer die Original-Farben
		String binInv="";				//binaerer String der invertierten Farb-ints 
		for(int i = 0; i < dimX(); i++)
		{
			for(int j = 0; j < dimY(); j++)
			{
				tempArr=getColors(pixels[i][j]);
				for(int k=1;k<4;k++)
				{
					
					invArr[k]=255-tempArr[k];
					
					//check wird genutzt, um fuehrende Nullen bei den einzelnen Farbwerten einzufuegen
					String check=Integer.toBinaryString(tempArr[k]);
					while(check.length()<8)
					{
						check="0"+check;
					}
					binInv+=check;				//ohne fuehrende Nullen, ist der int evtl. kein 32-bit Farbcode mehr
				}
				invArr[0]=tempArr[0];	
				pixels[i][j]=-Integer.parseInt(binInv,2);		//binaerer Farbcode wird wieder zu int verarbeitet
				binInv="";			//reset des binaer-Strings
			}
		}
	}
	
//b)	
	/**
	 * Das Bild wird vertikal gespiegelt
	 */
	private void mirror() {
		int temp=0;		//temporaerer Speicher
		for(int i = 0; i < dimX()/2; i++)			// dimX()/2, da sonst die Spiegelung fuer beide Seiten laeuft -> Originalbild
		{
			for(int j = 0; j < dimY(); j++)
			{
				temp=pixels[i][j];							//Speicherung des Pixelwertes links
				pixels[i][j] = pixels[dimX()-1-i][j];		//Ersetzung des Wertes links im Bild mit dem des Wertes auf der anderen Seite	
				pixels[dimX()-1-i][j] = temp;				//gespeicherter Wert links wird rechts aufgetragen
			}
		}
	}

	/**
	 * Hilfsfunktion zum Zugriff, die Randpunkten gueltige 
	 * nullwertige Nachbarn zuweist.
	 */	
	private int getPixel(int i, int j) {
		try
		{
			return pixels[i][j];
		}
		catch(Exception e)
		{
			//falls out of bounds Exc (nicht im Bild) -> 0
			return 0;
		}
	} 
	/**
	 * Diese Funktion betrachtet zu jedem Pixel seine Nachbarschaft, 
	 * summiert gewichtet diese Menge auf und skaliert sie
	 * @param filter 3x3 Umgebungsgewichte
	 */	
//e) unfinished, no working solution :<	
	private void meanFilter(double[] filter, double factor) {
		int[][] pixelsTemp=new int[dimX()][dimY()];
		for(int l=0;l<dimX();l++)
		{
			for(int m=0;m<dimY();m++)
			{
				pixelsTemp[l][m]=pixels[l][m];
			}
		}
		
		int[] tempArr=new int[4];
		int[] argbArr= new int[36];			//Farbcodes
		int[] meanArr= new int[4];			//Durchschnitt der Farbcodes
		for(int i = 0; i < dimX(); i++)
		{
	//		System.out.println("i: "+i);
			for(int j = 0; j < dimY(); j++)
			{
	//			System.out.println("j: "+j);
				int b=-1,c=-1,counter=0;
				//for a in here
				
					
					while(b<2)
					{
						c=-1;
						while(c<2)
						{
							
							tempArr=getColors(getPixel(i+c,j+b));
							for(int d=0;d<4;d++)
							{
								argbArr[counter*4+d]=tempArr[d];		
	//							System.out.print("t: "+tempArr[d]+" ");
							}
							counter++;
		//					System.out.print("C: "+counter+" ");
							c++;
						}	
						b++;
					}	
					//print stuff test
	/*				for(int a=0;a<9;a++)
					{
						int g=0;
						while(g<4)
						{
							System.out.print(argbArr[a*4+g]+" ");
							g++;
						}
						
					}			*/
					//befuellte argb Werte
			//		System.out.println("It-end----");
				double sumClr=0;
				for(int e=0;e<4;e++)
				{
					for(int f=0;f<9;f++)
					{
						sumClr+=argbArr[f*4+e]*filter[f];
					}
		//			System.out.print("sC: "+sumClr+" ");
					meanArr[e]=(int)((sumClr/9)*factor);
	//				System.out.print("mean: "+meanArr[e]+" ");
				}
				//cut out pixelsTemp if it doesnt work
				pixelsTemp[i][j]=recolor(meanArr);
			}
		}
		pixels=pixelsTemp;
	}
	
	
	/** 
	 * Gaussfilter
	 */	
	private void gauss(){
		double[] filter = {1,2,1,2,4,2,1,2,1};
		meanFilter(filter, 1.0/16.0);
	}
	
	/** 
	 * Blurfilter/ Lowpassfilter
	 */
	private void lpf(){
		double[] filter = {1,1,1,1,1,1,1,1,1};
		meanFilter(filter,1.0/9.0);
	}
	
	/** 
	 * Hochpassfilter 1
	 */
	private void hpf1(){
		double[] filter = {0,-1,0,-1,4,-1,0,-1,0};
		meanFilter(filter,1.0);
	}
	
	/** 
	 * Hochpassfilter 2
	 */
	private void hpf2(){
		double[] filter = {-1,-1,-1,-1,9,-1,-1,-1,-1};
		meanFilter(filter,1.0);
	}
	
	/** 
	 * Medianfilter
	 */	
//f)	
	private void medianFilter()
	{
		//temp Array fuer Speicherung der ints anliegender Pixel
		int[] area = new int[5];
		for(int i = 0; i < dimX(); i++)
		{
			for(int j = 0; j < dimY(); j++)
			{
					//Werte der benachbarten Pixel
							area[0]=getPixel(i,j-1);
							area[1]=getPixel(i-1,j);
							area[2]=getPixel(i,j);
							area[3]=getPixel(i+1,j);
							area[4]=getPixel(i,j+1);
					//Sortierung
				Arrays.sort(area);
				//update pixels
				pixels[i][j]=area[2];
			}
		}
	}

	/** 
	 * Fuegt auf n Pixeln Rauschen hinzu.
	 */	
	private void jitter(int n) {
		Random random = new Random();
		for(int i = 0; i < n; i++) {
			int x = random.nextInt(dimX());
			int y = random.nextInt(dimY());
			int[] colors = getColors(getPixel(x,y));
			colors[1] = random.nextInt(256);
			colors[2] = random.nextInt(256);
			colors[3] = random.nextInt(256);
			pixels[x][y] = setColors(colors);
		}
	}
 
    public static void main(String[] args) 
    {	
    	
		int argCount = args.length;

		if(argCount == 0) {
			System.out.println("Verwendung: java Bildbearbeitung <FILE> [-options]");
			System.out.println("wobei options Folgendes umfasst:");
			System.out.println("\t -rot90 90 Grad rotieren");
			System.out.println("\t -rot180 180 Grad rotieren");
			System.out.println("\t -rot270 270 Grad rotieren");
			System.out.println("\t -i Farben invertieren");
			System.out.println("\t -m Horizontal spiegeln");
			System.out.println("\t -lpf Verwaschen");
			System.out.println("\t -median Median-Filter");
			System.out.println("\t -gauss Gauss-Filter");
			System.out.println("\t -hpf1 Hochpassfilter1");
			System.out.println("\t -hpf2 Hochpassfilter2");
			System.out.println("\t -jitter Verrauscht das Bild");
			return;
		}

		// load image as specified in first argument args[0]
		Bildbearbeitung bild = new Bildbearbeitung(args[0]);
		
		for(int i = 1; i < argCount; i++) {
			String option = args[i];
			System.out.println("Processing: " + option);
			if(option.equals("-rot90"))
				bild.rotate(1);
			else if(option.equals("-rot180"))
				bild.rotate(2);
			else if(option.equals("-rot270"))
				bild.rotate(3);
			else if(option.equals("-i"))
				bild.invert();
			else if(option.equals("-m"))
				bild.mirror();	
			else if(option.equals("-gauss"))
				bild.gauss();
			else if(option.equals("-median"))
				bild.medianFilter();
			else if(option.equals("-lpf"))
				bild.lpf();
			else if(option.equals("-jitter"))
				bild.jitter(50000);
			else if(option.equals("-hpf1"))
				bild.hpf1();
			else if(option.equals("-hpf2"))
				bild.hpf2();
		
			bild.save(""+i);
		}
		bild.save("done");
		System.out.println("done");
	}
}