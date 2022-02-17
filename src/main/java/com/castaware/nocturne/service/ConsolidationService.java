package com.castaware.nocturne.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castaware.nocturne.datatypes.Asset;
import com.castaware.nocturne.datatypes.Pool;
import com.castaware.nocturne.datatypes.PoolLiquidity;
import com.castaware.nocturne.datatypes.PoolStaking;
import com.castaware.nocturne.datatypes.Transaction;
import com.castaware.nocturne.datatypes.TransactionAddGame;
import com.castaware.nocturne.datatypes.TransactionAirDrop;
import com.castaware.nocturne.datatypes.TransactionBridge;
import com.castaware.nocturne.datatypes.TransactionBurn;
import com.castaware.nocturne.datatypes.TransactionDust;
import com.castaware.nocturne.datatypes.TransactionMintNFT;
import com.castaware.nocturne.datatypes.TransactionMisc;
import com.castaware.nocturne.datatypes.TransactionPoolClaim;
import com.castaware.nocturne.datatypes.TransactionPoolCompound;
import com.castaware.nocturne.datatypes.TransactionPoolEarn;
import com.castaware.nocturne.datatypes.TransactionPoolLPAdd;
import com.castaware.nocturne.datatypes.TransactionPoolLPRemove;
import com.castaware.nocturne.datatypes.TransactionPoolSPAdd;
import com.castaware.nocturne.datatypes.TransactionPoolSPRemove;
import com.castaware.nocturne.datatypes.TransactionRamp;
import com.castaware.nocturne.datatypes.TransactionSwap;
import com.castaware.nocturne.datatypes.TransactionTransfer;
import com.castaware.nocturne.datatypes.Wallet;

import eic.tcc.dao.Dao;

@Component
public class ConsolidationService 
{
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final String ASSET = "DSM";
	
	@Autowired
	private Dao dao;
	
	@Autowired
	private PoolService poolService;
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private WalletService walletService;
	
	//
	// Init Cache
	//	
	@PostConstruct
	public void init()
	{
				
	}
	
	//
	// Consolidation
	//
	public void clear(Wallet wallet)
	{
		dao.executeSQL("delete from asset where wallet='"+wallet.getId()+"'");
		
		List<Pool> pools = dao.retrieveBySingleLike(Pool.class, "wallet", wallet);
		
		for (Pool pool : pools) 
		{
			pool.clear();
			dao.persist(pool);
		}
	}
	
	public void reset(Wallet wallet)
	{
		clear(wallet);
		
		List<Transaction> txs = walletService.getAllocatedTx(wallet);
				
		for (Transaction tx : txs) 
		{
			proccess(wallet,tx);
		}
	}	
	
	@Transactional
	public void proccess(Transaction tx)
	{
		if (tx.getFromWallet()!=null)
		{
			proccess(tx.getFromWallet(),tx);
		}
		
		if (tx.getToWallet()!=null)
		{
			proccess(tx.getToWallet(),tx);
		}
	}
	
	@Transactional
	public void proccess(Wallet wallet,Transaction tx)
	{
		LOG.debug("");
		LOG.debug(tx.toString());
		
		if (tx.getSuccess())
		{
			Class<? extends Transaction> specificClass = tx.getClass();
			
			try
			{
				Method specificMethod = this.getClass().getMethod("consolidate",Wallet.class,specificClass);
				specificMethod.invoke(this,wallet,tx);
			}
			catch(Exception e)
			{
				logWallet(wallet);
				throw new IllegalStateException("ERROR -- "+tx,e);
			}		
		}
		else
		{
			applyFee(wallet,tx);
		}		
		
		dao.persist(wallet);
		
		if (wallet.equals(tx.getToWallet()) && tx.getToAlloc()==false)
		{
			tx.setToAlloc(true);
			dao.persist(tx);			
		}
		
		if (wallet.equals(tx.getFromWallet()) && tx.getFromAlloc()==false)
		{
			tx.setFromAlloc(true);
			dao.persist(tx);			
		}		
	}
		
	public void consolidate(Wallet wallet, TransactionMisc tx)
	{
		applyFee(wallet,tx);
	}
	
	public void consolidate(Wallet wallet, TransactionTransfer tx)
	{
		String 		  assetName = tx.getAsset();
		BigDecimal 	  amount    = tx.getAmount();
		LocalDateTime timestamp = tx.getTimestamp();
		
		Asset asset = getAsset(assetName,wallet);
		
		// FROM
		if (wallet.equals(tx.getFromWallet()))
		{
			wallet.withdraw(amount, asset);
			sub(amount, asset);			
			applyFee(wallet, tx);
		}
		
		// TO
		if (wallet.equals(tx.getToWallet()))
		{			
			BigDecimal usdtQuote = tx.getAssetQuote(); 
					
			if (usdtQuote==null)
				usdtQuote=priceService.priceAtMinute(asset.getName(), timestamp);
			
			double usdtValue = usdtQuote.doubleValue()*amount.doubleValue();
			wallet.deposit(usdtValue);
			addByValue(amount, usdtValue, asset);								
		}			
		
		logAsset(tx, assetName, asset);
	}
		
	public void consolidate(Wallet wallet, TransactionBridge tx)
	{
		String 		assetName = tx.getAsset();
		BigDecimal 	amount    = tx.getAmount();
		
		Asset 	  	  asset     = getAsset(assetName,wallet);
		LocalDateTime timestamp = tx.getTimestamp();
		
		// FROM
		if (wallet.equals(tx.getFromWallet()))
		{
			wallet.withdraw(amount, asset);
			sub(amount, asset);
			applyFee(wallet, tx);			
		}
		
		// TO
		if (wallet.equals(tx.getToWallet()))
		{		
			BigDecimal usdtQuote = tx.getAssetQuote(); 
			
			if (usdtQuote==null)
				usdtQuote=priceService.priceAtMinute(assetName, timestamp);
			
			double usdtValue = usdtQuote.doubleValue()*amount.doubleValue();			
			wallet.deposit(usdtValue);
			addByValue(amount, usdtValue, asset);
			
			String 	   brigdeFeeAsset  = tx.getBridgeFeeAsset();
			BigDecimal bridgeFeeAmount = tx.getBridgeFeeAmount();
			
			if (brigdeFeeAsset!=null)
			{
				Asset feeAsset = getAsset(brigdeFeeAsset,wallet);
				wallet.withdraw(bridgeFeeAmount, feeAsset);
				sub(bridgeFeeAmount, feeAsset);
			}
		}	
		
		logAsset(tx, assetName, asset);
	}

	public void consolidate(Wallet wallet, TransactionBurn tx)
	{
		String 		assetName = tx.getAsset();
		BigDecimal 	amount    = tx.getAmount();
		Asset       asset     = getAsset(assetName,wallet);
		
		wallet.withdraw(amount, asset);
		sub(amount, asset);	
		
		logAsset(tx, assetName, asset);
	}
	
	public void consolidate(Wallet wallet, TransactionRamp tx)
	{
		String 		  assetName = tx.getAsset();
		BigDecimal 	  amount    = tx.getAmount();
		LocalDateTime timestamp = tx.getTimestamp();
		
		double usdtQuote;
		double usdtvalue;
		
		if (tx.getAssetQuote()==null)
		{
			usdtQuote = priceService.priceAtMinute(assetName, timestamp).doubleValue();
			usdtvalue  = usdtQuote*amount.doubleValue();
		}
		else
		{
			usdtQuote = tx.getAssetQuote().doubleValue();
			usdtvalue  = usdtQuote*amount.doubleValue();
		}
		
		Asset asset = getAsset(assetName,wallet);
		wallet.deposit(usdtvalue);
		addByValue(amount, usdtvalue, asset);
		
		logAsset(tx, assetName, asset);
	}
	
	public void consolidate(Wallet wallet, TransactionSwap tx)
	{
		// FROM //
		String 	   fromAssetName = tx.getFromAsset();
		BigDecimal fromAmount    = tx.getFromAmount();
		
		Asset fromAsset = getAsset(fromAssetName,wallet);
		sub(fromAmount, fromAsset);
		
		// TO //
		String     toAssetName = tx.getToAsset();
		BigDecimal toAmount    = tx.getToAmount();
				
		LocalDateTime timestamp = tx.getTimestamp();
		
		// Atualiza preços dos ativos
		priceService.updatePriceAtMinute(fromAssetName, fromAmount, toAssetName, toAmount, timestamp);
				
		Asset  toAsset = getAsset(toAssetName,wallet);
		
		double usdtQuote = priceService.priceAtMinute(fromAssetName, timestamp).doubleValue();
		double usdtValue  = usdtQuote*fromAmount.doubleValue();
		
		String 	   feeAsset	  = tx.getFeeAsset();
		BigDecimal feeAmount  = tx.getFeeAmount();
		
		if (feeAsset!=null && feeAmount.doubleValue()>0)
		{
			double fromFeeQuote = priceService.priceAtMinute(feeAsset, timestamp).doubleValue();
			double fromFeeValue = fromFeeQuote*feeAmount.doubleValue();
			usdtValue += fromFeeValue;
		}										
		
		addByValue(toAmount, usdtValue, toAsset);
		
		// FEE //
		applyFee(wallet,tx);
		
		logAsset(tx, toAssetName, toAsset);
		logAsset(tx, fromAssetName, fromAsset);		
	}
	
	public void consolidate(Wallet wallet, TransactionPoolLPAdd tx)
	{
		String 	   assetXName  = tx.getAssetX();
		BigDecimal amountX     = tx.getAmountX();
		
		String     assetYName  = tx.getAssetY();
		BigDecimal amountY     = tx.getAmountY();
				
		BigDecimal amountLP    = tx.getPoolAmount();
		
		Asset  xAsset  = getAsset(assetXName,wallet);
		Asset  yAsset  = getAsset(assetYName,wallet);
				
		// 1 - Remove a liquidez do spot
		String        poolName = tx.getPoolName();
		PoolLiquidity pool     = getPool(poolName, wallet, PoolLiquidity.class);
		
		// 1 - Adiciona liquidez no pool
		pool.setAssetX(assetXName);
		pool.setAssetY(assetYName);
		pool.add(amountX,xAsset.getEntryQuote(),
				 amountY,yAsset.getEntryQuote(),
				 amountLP);
			
		dao.persist(pool);
		
		// 2 - Remove os tokens spot
		sub(amountX, xAsset);
		sub(amountY, yAsset);		
		applyFee(wallet,tx);
		
		logAsset(tx, assetXName, xAsset);
		logAsset(tx, assetYName, yAsset);
	}
	
	public void consolidate(Wallet wallet, TransactionPoolLPRemove tx)
	{
		String     poolName = tx.getPoolName();
		
		String 	   assetXName = tx.getAssetX();
		BigDecimal amountX    = tx.getAmountX();
		
		String     assetYName = tx.getAssetY();
		BigDecimal amountY    = tx.getAmountY();
		
		BigDecimal removedAmount = tx.getPoolAmount();
		
		Asset  xAsset  = getAsset(assetXName,wallet);
		Asset  yAsset  = getAsset(assetYName,wallet);
				
		PoolLiquidity pool = getPool(poolName,wallet,PoolLiquidity.class);

		BigDecimal depositedAmount = pool.getDepositedAmount();
		BigDecimal withdrawnAmount = pool.getWithdrawnAmount();
		
		double xAssetEntryQuote = pool.getEntryQuoteX();
		double yAssetEntryQuote = pool.getEntryQuoteY();
		
		double xAssetExitQuote = priceService.priceAtMinute(assetXName, tx.getTimestamp()).doubleValue();
		double yAssetExitQuote = priceService.priceAtMinute(assetYName, tx.getTimestamp()).doubleValue();
		double xAssetExitValue = amountX.doubleValue()*xAssetExitQuote;
		double yAssetExitValue = amountY.doubleValue()*yAssetExitQuote;
		
		// 1 - Devolve a liquidez para o spot
		if (withdrawnAmount.doubleValue() > depositedAmount.doubleValue())
		{
			addEarn(amountX,xAsset);
			addEarn(amountY,yAsset);
		}
		else if(removedAmount.add(withdrawnAmount).doubleValue() > depositedAmount.doubleValue())
		{
			double deposited  = depositedAmount.doubleValue();
			double withdrawn  = withdrawnAmount.doubleValue();
			double restitued  = deposited-withdrawn;
			
			double removed    = removedAmount.doubleValue(); 
			double difference = removed - restitued; 
			double percentual = difference / removed;
			
			double bonusX = amountX.doubleValue()*percentual;
			double bonusY = amountY.doubleValue()*percentual;
			
			double restitutionX = amountX.doubleValue()-bonusX; 
			double restitutionY = amountY.doubleValue()-bonusY;
			
			addByQuote(new BigDecimal(restitutionX),xAssetEntryQuote,xAsset);
			addByQuote(new BigDecimal(restitutionY),yAssetEntryQuote,yAsset);
			addEarn(new BigDecimal(bonusX),xAsset);
			addEarn(new BigDecimal(bonusY),yAsset);
		}
		else
		{
			addByQuote(amountX,xAssetEntryQuote,xAsset);
			addByQuote(amountY,yAssetEntryQuote,yAsset);
		}
				
		// 2 - Remove a liquidez no pool
		double exitValue = xAssetExitValue + yAssetExitValue;
		pool.sub(removedAmount,exitValue);		
		dao.persist(pool);
		
		// 3 - Aplica taxas
		applyFee(wallet,tx);
		
		logAsset(tx, assetXName, xAsset);
		logAsset(tx, assetYName, yAsset);
	}
	
	public void consolidate(Wallet wallet, TransactionPoolSPAdd tx)
	{
		String     spName     = tx.getPoolName();
		String 	   assetName  = tx.getAsset();
		BigDecimal amount     = tx.getAmount();
		
		if (!assetName.contains("/"))
		{
			Asset		asset = getAsset(assetName,wallet);
			PoolStaking sp = getPool(spName,wallet,PoolStaking.class);
			
			// 1 - adiciona liquidez no sp
			sp.setAsset(assetName);
			sp.addByQuote(amount,asset.getEntryQuote());
			dao.persist(sp);
			
			// 2 - remove a liquidez do spot
			sub(amount, asset);
			
			logAsset(tx, assetName, asset);
		}
				
		applyFee(wallet,tx);		
	}
	
	public void consolidate(Wallet wallet, TransactionPoolSPRemove tx)
	{
		String     poolName   = tx.getPoolName();
		String 	   assetName  = tx.getAsset();
		BigDecimal amount     = tx.getAmount();
		
		if (!assetName.contains("/"))
		{
			Asset asset = getAsset(assetName,wallet);
			PoolStaking pool = getPool(poolName,wallet,PoolStaking.class);
			
			BigDecimal subQuote = priceService.priceAtMinute(assetName, tx.getTimestamp());
			BigDecimal subValue = subQuote.multiply(amount);
			
			// 1 - adiciona liquidez em spot
			double entryQuote = pool.getEntryQuote();
			double entryValue = entryQuote*amount.doubleValue();
			addByValue(amount, entryValue, asset);
			
			// 2 - Remove a liquidez da pool
			pool.sub(amount,subValue.doubleValue());
			dao.persist(pool);
			
			logAsset(tx, assetName, asset);
		}
		
		applyFee(wallet,tx);
	}
	
	public void consolidate(Wallet wallet, TransactionPoolClaim tx)
	{
		String 		assetName = tx.getAsset();
		BigDecimal 	amount    = tx.getAmount();				
		Asset       asset     = getAsset(assetName,wallet);
		
		BigDecimal  quote = tx.getQuote();
		
		if(quote==null)
		{
			addEarn(amount,asset);
		}
		else
		{
			addByQuote(amount, quote.doubleValue(), asset);	
		}
					
		applyFee(wallet,tx);
		
		logAsset(tx, assetName, asset);
	}
	
	public void consolidate(Wallet wallet, TransactionPoolEarn tx)
	{
		String 		  assetName = tx.getAsset();
		BigDecimal 	  amount    = tx.getAmount();		
		Asset		  asset 	= getAsset(assetName,wallet);
		
		addEarn(amount, asset);
		
		if (tx.getPoolName()!=null)
		{
			if (tx.getFromWallet().getId().equals("BINANCE"))
			{
				PoolStaking pool  = getPool(tx.getPoolName(),wallet,PoolStaking.class);
				pool.setAsset(assetName);
				dao.persist(pool);
			}
		}
					
		applyFee(wallet,tx);
		
		logAsset(tx, assetName, asset);
	}

	public void consolidate(Wallet wallet,TransactionPoolCompound tx)
	{
		BigDecimal  amount = tx.getAmount();
		Pool	    pool   = poolService.getPool(tx.getPoolName());
		
		pool.addEarn(amount);
		dao.persist(pool);			
		
		applyFee(wallet,tx);
	}
	
	public void consolidate(Wallet wallet, TransactionAirDrop tx)
	{
		String 		  assetName = tx.getAsset();
		BigDecimal 	  amount    = tx.getAmount();
		LocalDateTime timestamp = tx.getTimestamp();

		BigDecimal    usdtQuote = tx.getAssetQuote();
				
		if(usdtQuote==null)
			usdtQuote = priceService.priceAtMinute(assetName, timestamp);
		
		Asset asset = getAsset(assetName,wallet);
		addByQuote(amount, usdtQuote.doubleValue(), asset);	
		
		applyFee(wallet,tx);
		
		logAsset(tx, assetName, asset);
	}

	public void consolidate(Wallet wallet, TransactionDust tx)
	{
		// FROM //
		String 	   fromAssetName = tx.getFromAsset();
		BigDecimal fromAmount    = tx.getFromAmount();
		
		Asset fromAsset = getAsset(fromAssetName,wallet);
		sub(fromAmount, fromAsset);
		
		// TO //
		String     toAssetName = tx.getToAsset();
		BigDecimal toAmount    = tx.getToAmount();
				
		LocalDateTime timestamp = tx.getTimestamp();
		
		// Atualiza preços dos ativos
		priceService.updatePriceAtMinute(fromAssetName, fromAmount, toAssetName, toAmount, timestamp);
						
		Asset toAsset = getAsset(toAssetName,wallet);
		
		BigDecimal usdtQuote = priceService.priceAtMinute(fromAssetName, timestamp);
		BigDecimal usdtCost  = usdtQuote.multiply(fromAmount);
		
		String 	   feeAsset	  = tx.getFeeAsset();
		BigDecimal feeAmount  = tx.getFeeAmount();
		
		if (feeAsset!=null && feeAmount.doubleValue()>0)
		{
			BigDecimal fromFeeQuote = priceService.priceAtMinute(feeAsset, timestamp);
			BigDecimal fromFeeValue = fromFeeQuote.multiply(feeAmount);
			usdtCost = usdtCost.add(fromFeeValue);
		}										
		
		addByValue(toAmount, usdtCost.doubleValue(), toAsset);
		applyFee(wallet,tx);
	}

	public void consolidate(Wallet wallet, TransactionAddGame tx)
	{
		String 	   assetName  = tx.getAsset();
		BigDecimal amount     = tx.getAmount();

//		String     gameName   = tx.getGameName();
//		Game game  = gamesMap.getGame(gameName);
//		dao.persist(game);		
//		game.addByQuote(amount,asset.getEntryQuote());
		
		// 2 - remove a liquidez do spot
		Asset asset = getAsset(assetName,wallet);
		wallet.withdraw(amount, asset);
		sub(amount, asset);
		
		applyFee(wallet,tx);
	}
	
	public void consolidate(Wallet wallet, TransactionMintNFT tx)
	{
		String 	   assetName  = tx.getAsset();
		BigDecimal amount     = tx.getAmount();
		
		Asset asset = getAsset(assetName,wallet);
		asset.sub(amount);
		
//		String nftName = tx.getNftName();
//		String nftId = tx.getNftId();		
//		NFT nft = nftsMap.getNFT(nftName);
//		nft.setId(nftId);
		
		applyFee(wallet,tx);
	}
	
	private void applyFee(Wallet wallet, Transaction tx) 
	{
		String 	   feeAsset  = tx.getFeeAsset();
		BigDecimal feeAmount = tx.getFeeAmount();
		
		if (feeAsset!=null && feeAmount.doubleValue()>0)
		{
			Asset asset = getAsset(feeAsset,wallet);
			
			double feeQuote = asset.getEntryQuote();
			double feeValue = feeAmount.doubleValue()*feeQuote;
			wallet.withdraw(feeValue);
			
			sub(feeAmount, asset);
		}
	}

	//
	// Add-Sub
	//
	private void addEarn(BigDecimal amount, Asset asset) 
	{
		LOG.debug(asset.toString());
		asset.addEarn(amount);
		persistAsset(asset);
		LOG.debug(asset.toString());
	}
	
	private void addByValue(BigDecimal amount, double value, Asset asset) 
	{
		LOG.debug(asset.toString());
		asset.addByValue(amount,value);
		persistAsset(asset);
		LOG.debug(asset.toString());
	}
	
	private void addByQuote(BigDecimal amount, double quote, Asset asset) 
	{
		LOG.debug(asset.toString());
		asset.addByQuote(amount,quote);
		persistAsset(asset);
		LOG.debug(asset.toString());
	}

	private void sub(BigDecimal amount, Asset asset)
	{
		LOG.debug(asset.toString());
		asset.sub(amount);
		
		if (asset.getAmount().doubleValue()>0)
			persistAsset(asset);
		else
			deleteAsset(asset);
		
		LOG.debug(asset.toString());
	}		
		
	//
	// Asset Management
	//
	private Map<String,Asset> assetCache = new TreeMap<String,Asset>();
	
	private Asset getAsset(String assetName, Wallet wallet)
	{
		String assetId = wallet.getId()+" "+assetName;
		
		// CACHE HIT
		Asset cachedAsset = assetCache.get(assetId);
		
		if (cachedAsset!=null)
			return cachedAsset;
		
		// CACHE MISS
		List<Asset> assets = dao.retrieveBySingleLike(Asset.class,"id",assetId);
		
		if (assets.size()>1)
			throw new IllegalStateException("Duplicated entry for "+assetId);
		else if (assets.size()==1)
			return assets.get(0);
		else
		{
			Asset asset = new Asset(assetName,wallet);
			persistAsset(asset);
			return asset;
		}			
	}
	
	private void persistAsset(Asset asset) 
	{
		dao.persist(asset);		
		assetCache.put(asset.getId(), asset);
	}
	
	private void deleteAsset(Asset asset) 
	{
		dao.delete(asset);
		assetCache.remove(asset.getId());
	}
	
	//
	// PoolManagement
	//
	@SuppressWarnings("unchecked")
	private <POOL extends Pool> POOL getPool(String poolName, Wallet wallet, Class<POOL> poolClazz)
	{
		String hql = "FROM Pool "
				   + "WHERE id LIKE ?0";
	
		List<Pool> pools = (List<Pool>)dao.queryHQL(hql, poolName);
		
		if (pools.size()>1)
			throw new IllegalStateException("Duplicated entry for "+poolName);
		else if (pools.size()==1)
			return (POOL)pools.get(0);
		else
		{
			try
			{
				POOL pool = poolClazz.newInstance();
				pool.setId(poolName);
				pool.setWallet(wallet);
				dao.persist(pool);
				return pool;		
			}
			catch (Exception e) 
			{
				throw new IllegalStateException(e);
			}						
		}			
	}	
	
	//
	// Utils
	//
	public void logWallet(Wallet wallet)
	{
		List<Asset> assets = dao.retrieveBySingleLike(Asset.class,"wallet",wallet);
		List<Pool>  pools  = poolService.getPools(wallet);
		
		LOG.info("# "+wallet.getName());
		LOG.info("######## ASSETS");
		for (Asset asset : assets) 
		{
			LOG.info("# "+asset.toString());
		}
		LOG.info("######## POOLS ");
		for (Pool pool : pools) 
		{
			LOG.info("# "+pool.toString());
		}
		LOG.info("");
	}
	
	private void logAsset(Transaction tx, String assetName, Asset asset) 
	{
		if (assetName.equals(ASSET))
		{
			LOG.info("");
			LOG.info(">> "+tx.toString());
			LOG.info("## "+asset.toString());
		}
	}
}
