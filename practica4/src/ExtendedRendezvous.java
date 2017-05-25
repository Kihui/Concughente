import java.io.Serializable;
import java.net.Socket;

public class ExtendedRendezvous<T extends Serializable> {
    
    private final RemoteMessagePassing<T> channel;
    
    public ExtendedRendezvous(Socket socket) {
        channel = new RemoteMessagePassing<>(socket);
    }
    
    public T requestAndAwaitReply(T obj) {
        response(obj);
	return getRequest();
    }
    
    public T getRequest() {
        return channel.receive();
    }
    
    public void response(T obj) {
        channel.send(obj);
    }
    
    public void close() {
        channel.close();
    }

}
