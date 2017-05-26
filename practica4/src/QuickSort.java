import java.util.Random;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;


/** Clase para implementar una paralelización de QuickSort  
 * utilizando paso de mensajes como elemento de comunicación
 * entre los procesos concurrentes.
 *
 */
public class QuickSort{

    /** Atributos */
    private ExtendedRendezvous<Integer[]> erv;
    private Integer[] arreglo;
    private boolean f;
    private final int PUERTO = 5000;

    /** Constructor por argumento de tamaño de la clase.
     *
     */
    private QuickSort(boolean f, int s) {
	this.f = f;
	if(!f)
	    arreglo = randomList(s);	
    }

    /** Método privado para ordenar un arreglo de enteros.
     * 
     */
    private void quickSort(Integer[] a, int ini, int fin) {
        if(fin - ini < 1)
            return;
        int i = ini + 1; 
        int j = fin;
        while(i < j) 
            if(a[i].compareTo(a[ini]) > 0
	       && a[j].compareTo(a[ini]) <= 0)
                intercambia(a, i++, j--);
            else
                if(a[i].compareTo(a[ini]) <= 0)
                    i++;
                else
                    j--;
	if(a[i].compareTo(a[ini]) > 0)
	    i--;
	intercambia(a,i,ini);
	quickSort(a,ini,i-1);
	quickSort(a,i+1,fin);
    }

    private void intercambia(Integer[] a, int p, int q) { 
        if(p == q)
            return;
        Integer w = a[p];
        a[p] = a[q];
        a[q] = w;
    }
    
    /** Método privado para generar un arreglo aleatorio de enteros.
     * 
     */
    private Integer[] randomList(int t) {
	System.out.println(t);
	Random r = new Random(9);
	Integer[] out = new Integer[t];
	for(int i = 0; i < out.length; i++)
	    out[i] = r.nextInt();
	System.out.println(aCadena(out));
	return out;
    }

    /** Ejecución del algoritmo paralelizado
     * @param 
     */
    public void startServer(){	  
	//System.out.println("\nArreglo original\n"+arreglo.toString()+"\nIniciando Servidor...");
	try{
	    ServerSocket canalServidor = new ServerSocket(PUERTO);
	    while(true){
		
		System.out.println("\nEsperando conexión en puerto "+ PUERTO);
		Socket canal = canalServidor.accept();
		System.out.println("Cliente conectado");
		erv = new ExtendedRendezvous<>(canal);
		arreglo = erv.getRequest();
		System.out.println("Arreglo recibido:\n"+aCadena(arreglo));
		quickSort(arreglo,0,arreglo.length-1);
		System.out.println("Enviando arreglo ordenado:\n"+aCadena(arreglo));
		erv.response(arreglo);
	    }
	} catch(IOException ioe){
	    System.err.println("Error");
	    ioe.printStackTrace();
	}
    }

    /** Ejecución del algoritmo paralelizado
     * @param 
     */
    public void startClient(){
        ElPatron ep = new ElPatron(PUERTO, arreglo);
	ep.run();
	System.out.println("Arreglo ordenado recibido:\n"+aCadena(ep.getArreglado()));
    }
    

    public void run(){
	if(f)
	    startServer();
	else
	    startClient();
    }

    private String aCadena(Integer[] a){
        String s = "[";
	for(int i = 0; i < a.length; i++)
	    s += a[i]+", ";
	return s+"]";
    }
	
    public static void main(String[] args) {
	//verificacion de args help
	boolean isServer = "server".equals(args[0]);
	if(isServer)
	    new QuickSort(isServer, 0).run();
	else
	    new QuickSort(isServer, Integer.parseInt(args[1])).run();        
    }
}
