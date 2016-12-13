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
		//remove static later
	private static int[][] pixels;
	
	/**
	 * Konstruktor fuer die Klasse Bildbearbeitung, die eine Bilddatei einliest
	 * und das zweidimensionale Pixel-Array pixels befuellt.
	 * @param file Einzulesende Bilddatei
	 */
	private Bildbearbeitung(String file) {
		try {
			BufferedImage img = ImageIO.read(new File(file));
			pixels = new int[img.getWidth()][img.getHeight()];
			for(int i = 0; i < dimX(); i++)
				for(int j = 0; j < dimY(); j++)
				{
				pixels[i][j] = img.getRGB(i, j);
		//		System.out.println(pixels[i][j]);
		}
		} catch (IOException e) {}
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
//a)	take out static after finishing
	/**
	 * Diese Funktion nimmt einen ARGB-Wert und wandelt ihn in 
	 * ein vierelementiges Array um, das die einzelnen Bytes 
	 * als int-Komponenten enthaelt. 
	 * @param ARGB Integre Darstellung eines Pixels mit 4 Byte
	 * Information: alpha-rot-gruen-blau
	 * @return 4-elementiges Array [alpha, red, green, blue]
	 */
	private static int[] getColors(int ARGB) {
		int[] pixelBin=new int[32];		//temporaeres Array mit binaeren Werten der ARGB	(Hilfsarray)
		int[] pixelFin=new int[4];		//Endausgabe fuer Methode	
		String binARGB="";
	//	System.out.println(Integer.toBinaryString(ARGB));		//test only
		binARGB=Integer.toBinaryString(ARGB);		//Umwandlung des ints in binaere Darstellung
		for(int i=0;i<binARGB.length();i++)
		{
			pixelBin[31-i]=binARGB.charAt(binARGB.length()-1-i)-48;		//Hilfsarray pixelBin wird mit den binaeren Werten gefuellt
				//Auffuellung von hinten; -48, da ASCII Wert von 1 49 ist
		}
		for(int j=0;j<4;j++)
		{
			//Aufteilung des Hilfsarrays in 4 8-bit Teile
			
			for(int k=0;k<8;k++)
			{
							//finales Array wird befuellt
				pixelFin[3-j]+=pixelBin[31-(j*8)-k]*(int)Math.pow(2, k);	//nach Binaer-Dezimalkonversion errechnet
							//j*8 ->8 bit jeweils, Aufzaehlung mit 2^k reset, sodass letztlich Farbe eines Pixels in Hexadez. Werten ausgegeben wird: 0-255
	//			System.out.println(pixelFin[3-j]);		//test
			}
		}
		return pixelFin;
	}
	
	//Testmethode
	public static int recolor(int[] color)
	{
		String clr="";
		for(int i=0;i<4;i++)
		{
			clr+=Integer.toBinaryString(color[i]);
			System.out.println(clr);
		}
		return Integer.parseInt(clr,2);
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
//c)	
	/**
	 * Das Bild wird hier rotiert und n*90 Grad.
	 * @param n Anzahl der Vierteldrehungen.
	 */
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
						//tempPixels mit Rotation fuellen, dann pixels=tempPixels;
						//analog mit 270° rot
					}
				}
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
				break;
			default:return;
			
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
					invArr[0]=tempArr[0];	
					invArr[k]=255-tempArr[k];
					//check wird genutzt, um fuehrende Nullen bei den einzelnen Farbwerten einzufuegen
					String check=Integer.toBinaryString(tempArr[k]);
					while(check.length()<8)
					{
						check="0"+check;
					}
					binInv+=check;
				}
				//binaerer Farbcode wird wieder zu int verarbeitet
				pixels[i][j]=-Integer.parseInt(binInv,2);
				//reset des binaer-Strings
				binInv="";
			}
		}
	}
	
//b)	
	/**
	 * Das Bild wird vertikal gespiegelt
	 */
	private void mirror() {
		int temp=0;		//temporaerer Speicher
		for(int i = 0; i < dimX()/2; i++)			// dimX()/2, da sonst die Spiegelung fuer beide Seiten laeuft-> Originalbild
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
		// TODO 8-4-e optional
		return 0;
	}
	
	/**
	 * Diese Funktion betrachtet zu jedem Pixel seine Nachbarschaft, 
	 * summiert gewichtet diese Menge auf und skaliert sie
	 * @param filter 3x3 Umgebungsgewichte
	 */	
	private void meanFilter(double[] filter, double factor) {
		// TODO 8-4-e
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
	private void medianFilter(){
		//TODO 8-4-f
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
/*		
		//test purposes only
    	int[] tarray=new int[4];
  //  	System.out.println(pixels[3][3]);
    	tarray=getColors(-6386301);
    	for(int i=0;i<4;i++)
    	{
    		switch(i)
    		{
    		case 0: System.out.print("Alpha: ");break;
    		case 1: System.out.print("R: ");break;
    		case 2: System.out.print("G: ");break;
    		case 3: System.out.print("B: ");break;
    		}
    		System.out.print(tarray[i]+"\n");
    	}
    	*/
		/*test
		int[] Colors= new int[4];
		Colors=getColors(pixels[3][5]);
		System.out.println(pixels[3][5]);
		System.out.println(Integer.toBinaryString(pixels[3][5]));
		//11111111010111001001010111011100
		System.out.println(recolor(Colors));
		//1111111110111001001010111011100
		 */
		bild.save("done");
		System.out.println("done");
	}
}