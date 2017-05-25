import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RemoteMessagePassing<T extends Serializable> {

    private ObjectOutputStream canalOut;
    private ObjectInputStream canalIn;
    private Object monitorEnvio;
    private Object monitorRecibo;
    private Socket canal;
        
    public RemoteMessagePassing(Socket socket) {
	try {
	    canal = socket; 
	    canalOut = new ObjectOutputStream(socket.getOutputStream());
	    canalIn = new ObjectInputStream(socket.getInputStream());	    
	} catch (Exception e){
	    System.err.println("Error en la creación del canal");
	    e.printStackTrace();
	}
	monitorEnvio = new Object();
        monitorRecibo = new Object();
    }
    
    public void send(T obj) {
	synchronized (monitorEnvio){
	    try{		
	        canalOut.writeObject(obj);
	        canalOut.flush();//?
		canalOut.close();//maybe quitar
	    } catch(IOException ioe){
		System.err.println("Error al enviar mensaje.");
		ioe.printStackTrace();
	    }
	}
	
    }
    
    public T receive() {
	T recibido = null;
	synchronized (monitorRecibo){
	    try{
	        recibido = castGenerico(canalIn.readObject());
		canalIn.close();//maybe quitar??
	    } catch (Exception e){
		System.err.println("Error al recibir mensaje.");
		e.printStackTrace();
	    }
	}
	if(recibido == null)
	    System.err.println("Error al procesar mensaje. Mensaje vacío.");
	return recibido;
    }

    @SuppressWarnings("unchecked")
    private T castGenerico(Object obj){
	return (T) obj;
    }
    
    public void close() {
	try{
	    if (canalIn != null)
	        canalIn.close();
	    if (canalOut != null)
	        canalOut.close();
	    if (canal != null)
		canal.close();
	} catch(IOException ioe){
	    System.err.println("Error cerrando canal.");
	    ioe.printStackTrace();
	}
    }
}

