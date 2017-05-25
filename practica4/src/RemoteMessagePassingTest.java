import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class RemoteMessagePassingTest {

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
	    Socket canal;
	    RemoteMessagePassing<Message> rmp;
	    Message enviado, recibido;
	    while(true){
		System.out.println("\nEsperando conexión en puerto "+ PUERTO);
		canal = canalServidor.accept();
		System.out.println("Cliente conectado");		
		rmp = new RemoteMessagePassing<Message>(canal);
		recibido = (Message) rmp.receive();
		System.out.println(recibido.toString() + " recibido");
		//enviado = new Message(idMensajeS++, "Mensaje servidor " + idMensajeS);
		//rmp.send(enviado);
		//System.out.println("Mensaje: " + enviado.toString() + " enviado al cliente");
		rmp.close();
	    }
	} catch(IOException ioe){ioe.printStackTrace();}
    }
    
    public static void startClient() {
        try{
	    Socket canal;
	    RemoteMessagePassing <Message> rmp;
	    Message enviado, recibido;
	    while (true){
		canal = new Socket("localhost", PUERTO);
		rmp = new RemoteMessagePassing<Message>(canal);
		enviado = new Message(idMensajeC++, "¡Hola, servidor!");
		rmp.send(enviado);
		System.out.println(enviado.toString() + " enviado al servidor");
		//recibido = (Message)rmp.receive();
		//System.out.println("Mensaje " + recibido.toString() + " recibido en el cliente");
		//rmp.close();
	    }
	} catch(IOException ioe){
	    System.err.println("No existe ningún servidor listo para comunicación en puerto: "+PUERTO);
	    ioe.printStackTrace();}
    }

}
