package com.castaware.nocturne.datatypes;

import java.util.List;

import org.junit.Test;

import com.castaware.nocturne.datatypes.dao.NocturneDaoTest;

public class NocEventDaoTest extends NocturneDaoTest 
{
	@Test
	public void noAllocTest()
	{
		Wallet wallet = dao.retrieveBySingleLike(Wallet.class, "id", "BINANCE").get(0);
		
		List<Transaction> toEvents = dao.retrieveBySingleLike(Transaction.class,"fromWallet", wallet);
		
		System.out.println(toEvents);
		

	}	
}
