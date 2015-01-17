package multiplayer;

import game.system.application.Application;
import game.system.command.Command;
import game.system.command.CommandParseException;

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

import characters.Character;
import characters.Party;

public class ConnectorManager implements Runnable{
	
	private static final int DEFAULT_PORT = 25256;
	
	private String key;
	private HashMap<String,Party> players;
	private Socket serverConnector;
	private ArrayList<String> queue;
	
	private boolean connected = false;
	
	public void connect(){
		byte[] host = {127,0,0,1};
		queue = new ArrayList<String>();
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
	
	public void send(String signal) throws IOException
	{
		if(!connected){
			queue.add(signal);
		}
		else{
			PrintWriter out = new PrintWriter(serverConnector.getOutputStream());
	        out.println(signal.replaceAll("\\{key\\}", key));
	        out.flush();
		}
	}
	
	public void receive() throws IOException{
		BufferedReader in = new BufferedReader (new InputStreamReader (serverConnector.getInputStream()));
        String signal = in.readLine();
        
        try {
			Command commande = new Command(signal);
			commande.execute();
		} catch (CommandParseException | SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void setKey(String key){
		this.key = key;
		connected = key != null;
	}

	/**
	 * Sérialize les attributs de l'équipe en cours.
	 * @return
	 */
	public String createPlayerInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(Application.application().getGame().getParty().getMap().getId());
		sb.append(" ");
		sb.append((int) Application.application().getGame().getParty().getAbsoluteX());
		sb.append(" ");
		sb.append((int) Application.application().getGame().getParty().getAbsoluteY());
		for(Character c : Application.application().getGame().getParty()){
			sb.append(" ");
			sb.append(c.getClass().getSimpleName());
			sb.append(" ");
			sb.append(c.getName());
		}
		return sb.toString();
	}

	public void addPlayer(String key, Party party) {
		players.put(key, party);
	}
	
	@Override
	public void run() {
		while(true){
			try {
				if(queue.size() > 0){
					for(String signal : queue){
						send(signal);
					}
					queue.clear();
				}
				receive();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public void sendInformationsToServer() {
		try {
			send("set-client-data {key} " + createPlayerInfo());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
