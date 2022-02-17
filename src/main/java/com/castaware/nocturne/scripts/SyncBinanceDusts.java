package com.castaware.nocturne.scripts;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.api.BinanceApi;
import com.castaware.nocturne.api.wrappers.BwDustTransfer;
import com.castaware.nocturne.datatypes.Transaction;
import com.castaware.nocturne.datatypes.TransactionDust;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.util.NocDateFormat;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class SyncBinanceDusts 
{
	private Logger LOG = LoggerFactory.getLogger(SyncBinanceDusts.class);
	
	@Autowired
	protected Dao dao;
	
	@Test
	public void sync()
	{
		try
		{
			Wallet wallet = dao.retrieveBySingleLike(Wallet.class,"id","BINANCE").get(0);
			
			String info = "=== Sync Binance Dust ===";
			LOG.info(StringUtils.repeat("=",info.length()));
			LOG.info(info);
			LOG.info(StringUtils.repeat("=",info.length()));
			
			LocalDateTime from  = fetchStartDate(wallet, NocSyncMode.QUICK, TransactionDust.class);
			LocalDateTime to    = fetchNextDate(from);
			LocalDateTime end   = NocDateFormat.toEndOfDay(LocalDateTime.now());
					
			BinanceApi api = new BinanceApi(wallet.getApiKey(),wallet.getSecretKey());
			
			boolean done = false;
			
			do
			{
				if (to.equals(end) || to.isAfter(end))
				{
					done = true;
					to = end;
				}
				
				String debug = String.format("-- Checking %s to %s until %s --",NocDateFormat.format_ddMMyyyyHHmm(from),NocDateFormat.format_ddMMyyyyHHmm(to),NocDateFormat.format_ddMMyyyyHHmm(end)); 
				LOG.debug(StringUtils.repeat("-",debug.length()));
				LOG.debug(debug);
				LOG.debug(StringUtils.repeat("-",debug.length()));
				
				sync(api,wallet,from,to,end);

				if (!done)
				{
					from = to.plus(1,ChronoUnit.SECONDS);
					
					if (from.isAfter(end))
						break;
					
					to = fetchNextDate(from).isAfter(end) ? end : fetchNextDate(from);
				}
			}
			while(!done);
		}
		catch(Throwable e)
		{
			String error = "=== ERROR while syncing: "+e.getMessage()+" ===";			
			LOG.error(StringUtils.repeat("=",error.length()));
			LOG.error(error);
			LOG.error(StringUtils.repeat("=",error.length()));			
			
			try {Thread.sleep(500);} catch (InterruptedException e1) {}			
			e.printStackTrace();			
		}
	}

	public LocalDateTime fetchNextDate(LocalDateTime date) 
	{
		return NocDateFormat.toEndOfMonth(date);
	}
	
	public void sync(BinanceApi api, Wallet wallet, LocalDateTime from,LocalDateTime to, LocalDateTime end)
	{
		List<BwDustTransfer> wrappers = api.dustTransferHistory(from, to);
				
		for (BwDustTransfer wrapper : wrappers) 
		{
			TransactionDust tx = new TransactionDust();
			
			tx.setDescription(NocDateFormat.format_yyyyMMddHHmmssHifen(wrapper.getTimestamp())+" DUST "+wrapper.getFromAsset());
			tx.setFromWallet(wallet);
			tx.setFromAsset(wrapper.getFromAsset());
			tx.setFromAmount(wrapper.getFromAmount());
			tx.setToAsset(wrapper.getToAsset());
			tx.setToAmount(wrapper.getToAmount());
			tx.setFeeAsset(wrapper.getFeeAsset());
			tx.setFeeAmount(wrapper.getFeeAmount());
			tx.setTimestamp(wrapper.getTimestamp());
			tx.setToAlloc(false);
			tx.setFromAlloc(false);
			
			persist(tx);
		}
	}
	
	protected void persist(Transaction tx) 
	{
		if (isPersisted(tx))
		{
			LOG.trace(">>> OK - "+tx);
		}
		else
		{
			LOG.info(">>> NEW - "+tx);
			dao.persist(tx);
		}
	}
	
	protected void purgeWallet(Wallet wallet) 
	{
		dao.deleteBySingleLike(TransactionDust.class,"wallet",wallet);
	}
	
	protected LocalDateTime fetchStartDate(Wallet wallet,NocSyncMode mode,Class<? extends Transaction> eventClass)
	{
		if (mode==NocSyncMode.FULL || mode==NocSyncMode.PURGE)
		{
			return NocDateFormat.parse_yyyyMMddHHmmssHifen("2022-01-01 00:00:00");
		}
		else
		{
			Transaction tx = dao.retrieveLastBySingleLike(eventClass,"fromWallet",wallet);
		
			if(tx==null)
			{
				return NocDateFormat.parse_yyyyMMddHHmmssHifen("2022-01-01 00:00:00");
			}
			else
			{
				return NocDateFormat.toStartOfDay(tx.getTimestamp());
			}
		}
	}
	
	protected boolean isPersisted(Transaction tx)
	{
		String description = tx.getDescription();
		
		List<?> objects = dao.retrieveBySingleLike(tx.getClass(),"description",description);
		
		if (objects.size()==0)
			return false;
		else if (objects.size()==1)
			return true;
		else
			throw new IllegalArgumentException("Duplicated id "+description+" in "+dao.getClass().getSimpleName());
	}
}
