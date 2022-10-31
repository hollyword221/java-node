

import java.io.*;
import java.net.*;

public class EchoPeer {
    public Socket m_socket = null;
    public ObjectInputStream m_in;
    public ObjectOutputStream m_out;
    public String m_name = "";

    EchoPeer() {
    }

    public boolean isConnected() {
        return true;
    }

    
    public boolean connect(String name, String hostName, int portNumber) {
        try {
            m_socket = new Socket(hostName, portNumber);
            // takes input from the client socket 
            m_in = new ObjectInputStream(m_socket.getInputStream()); 
            //writes on client socket
            m_out = new ObjectOutputStream(m_socket.getOutputStream());
            
            
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            return false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);

            return false;
        }

        this.m_name = name;
        return true;
    }

    public byte[] recv() {
    	Object obj;
		try {
			obj = m_in.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return (byte[]) obj;
    }
    public boolean send(byte[] data) {
        try {
            m_out.writeObject(data);
            m_out.flush();
            
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void close() {
    	try {
			m_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
