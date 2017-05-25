import java.io.Serializable;
import java.net.Socket;
import java.net.ServerSocket;

public class ExtendedRendezvousTest {
    static final int PUERTO = 5000;
    static int idMensajeS; //Id del mensaje del servidor
    static int idMensajeC; //Id del mensaje del cliente
    
    public static void main(String[] args) {
        boolean isServer = "server".equals(args[0]);
        idMensajeS = 1;
	idMensajeC = 1;
        if (isServer) {
            startServer();
        } else {
            startClient();
        }
    }
    
    public static void startServer() {
	try{
	    ServerSocket canalServidor = new ServerSocket(PUERTO);
	    
	   
	    Message enviado, recibido;
	    while(true){
		System.out.println("Esperando conexión");
		Socket canal = canalServidor.accept();
		ExtendedRendezvous<Message> erv = new ExtendedRendezvous<>(canal);
		System.out.println("Cliente conectado, servidor esperando mensaje");
		recibido = erv.getRequest();
		System.out.println("El cliente envió: "+recibido);
	        enviado = new Message(idMensajeS, "Mensaje recibido: "+ idMensajeS++);
		System.out.println("Enviando respuesta");
		erv.response(enviado);
	        erv.close();
	    }
	}catch(Exception e){System.err.println("Error iniciando servidor");}   
    }
    
    public static void startClient() {
	try{

	    
	    
	    Message enviado, recibido;

	    while(true){
		
		Socket canal = new Socket("localhost", PUERTO);
		ExtendedRendezvous<Message> erv = new ExtendedRendezvous<>(canal);
		enviado = new Message(idMensajeC, "Osas "+ idMensajeC++);
		System.out.println("Mensaje enviado a servidor. Cliente esperando respuesta");
		recibido = erv.requestAndAwaitReply(enviado);
		
		System.out.println("El servidor respondió "+recibido);
		//erv.getRequest();
		erv.close();
	    }
	}catch(Exception e){System.err.println("No existe ningún servidor listo para comunicación en puerto: "+PUERTO);}
    }
}
