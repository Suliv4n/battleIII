package multiplayer;

import game.system.Command;
import game.system.CommandParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.newdawn.slick.SlickException;

import characters.Party;

public class ConnectorManager implements Runnable{
	
	private static final int DEFAULT_PORT = 25256;
	
	private String key;
	private HashMap<String,Party> players;
	private Socket serverConnector;
	
	public void connect(){
		byte[] host = {127,0,0,1};
		try {
			serverConnector = new Socket(InetAddress.getByAddress(host), DEFAULT_PORT);
			initConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean initConnection(){
		new Thread(this).start();
		return true;
	}
	
	public void send(String signal, ConnectionCallBack callBack) throws IOException
	{
		PrintWriter out = new PrintWriter(serverConnector.getOutputStream());
        out.println(signal);
        out.flush();
        
        if(callBack != null){
        	callBack.callBack();
        }
	}
	
	public void receive() throws IOException{
		BufferedReader in = new BufferedReader (new InputStreamReader (serverConnector.getInputStream()));
        String signal = in.readLine();
        
        try {
			Command commande = new Command(signal);
			System.out.println(commande.execute());
		} catch (CommandParseException | SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void setKey(String key){
		this.key = key;
	}

	
	@Override
	public void run() {
		while(true){
			try {
				receive();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
