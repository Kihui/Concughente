import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RemoteMessagePassing<T extends Serializable> {

    //private Socket canal;
    private ObjectOutputStream canalOut;
    private ObjectInputStream canalIn;
    private Object monitorEnvio;
    private Object monitorRecibo;
    
    public RemoteMessagePassing(Socket socket) {
	try {
	    canalOut = new ObjectOutputStream(socket.getOutputStream());
	    canalIn = new ObjectInputStream(socket.getInputStream());
	    //socket.close();
	} catch (Exception e){e.printStackTrace();}
	monitorEnvio = new Object();
        monitorRecibo = new Object();
    }
    
    public void send(T obj) {
	synchronized (monitorEnvio){
	    try{
	        canalOut.writeObject(obj);
	        canalOut.flush();
	    } catch(IOException ioe){
		System.err.println("Error al recibir mensaje.");
		ioe.printStackTrace();
	    }
	}
	
    }
    
    public T receive() {
	T valor = null;
	synchronized (monitorRecibo){
	    try{	    
		valor = castGenerico(canalIn.readObject());
	    } catch (Exception e){
		System.err.println("Error al recibir mensaje.");
		e.printStackTrace();
	    }
	}
	return valor;
    }

    @SuppressWarnings("unchecked")
    private T castGenerico(Object obj){
	return (T) obj;
    }
    
    public void close() throws IOException {
	try{
	    if (canalIn != null)
	        canalIn.close();
	    if (canalOut != null)
	        canalOut.close();
	} catch(IOException ioe){
	    System.err.println("Error cerrando canal.");
	    ioe.printStackTrace();}
    }
}

