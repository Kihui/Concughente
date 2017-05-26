import java.net.Socket;
import java.io.IOException;

/** Clase para manejar al cliente que ordenará el arreglo.
 * Tiene la finalidad de emplear QuickSort.
 */
public class ElPatron{
    private ExtendedRendezvous<Integer[]> erv;
    private Integer[] original;
    private Integer[] arreglado;
    //private Sort algo;

    /** Constructor por parámetros del trabajador 
     * @param puerto el puerto por el que se conectará con el servidor. 
     */
    public ElPatron(int puerto, Integer[] original){
	this.original = original;
	try{
	    Socket canal = new Socket("localhost", puerto);
	    erv = new ExtendedRendezvous<>(canal);
	} catch(IOException ioe){ioe.printStackTrace();}
			
    }

    public Integer[] getArreglado(){
	return arreglado;
    }
    public void run(){
	System.out.println("Enviando arreglo original. Esperando arreglo ordenado");
        arreglado = erv.requestAndAwaitReply(original);	
	erv.close();		
    }

}
