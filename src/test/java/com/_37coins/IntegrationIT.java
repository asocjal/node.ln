package com._37coins;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import org.junit.BeforeClass;
import org.junit.Test;

import com._37coins.bcJsonRpc.BitcoindClientFactory;
import com._37coins.bcJsonRpc.BitcoindInterface;
import com._37coins.bcJsonRpc.events.WalletListener;
import com._37coins.bcJsonRpc.pojo.Info;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationIT {
	
	static BitcoindInterface client;
	
	@BeforeClass
	static public void before() throws MalformedURLException, IOException{
		BitcoindClientFactory clientFactory = new BitcoindClientFactory(new URL("http://localhost:18332/"), "cd", "cd");
		client = clientFactory.getClient();
	}
	
//	@Test
//	public void testInfo() throws JsonProcessingException{
//		Info info = client.getinfo();
//		System.out.println(new ObjectMapper().writeValueAsString(info));
//	}
	
	@Test
	public void testBalance(){
		BigDecimal balance = client.getbalance();
		
        System.out.println("balance = " + balance);
		System.out.println("block count = " + client.getblockcount());

        assertTrue(balance.compareTo(BigDecimal.ZERO) >= 0);
	}

	@Test
	public void testListener() throws IOException, InterruptedException{
		
		Observer observer = new Observer() {

			@Override
			public void update(Observable arg0, Object arg1) {
				System.out.println("Event observed:\n" + arg1);
			}
			
		};
		
		
		
		WalletListener listener = new WalletListener(client);
		
		listener.addObserver(observer);
		
		System.out.println("Obserwujemy");
		
		Thread.sleep(10000000);
	}
	
}
