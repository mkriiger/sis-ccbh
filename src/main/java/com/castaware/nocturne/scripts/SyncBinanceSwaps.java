package com.castaware.nocturne.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.math3.util.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.castaware.nocturne.datatypes.TransactionSwap;
import com.castaware.nocturne.datatypes.Wallet;
import com.castaware.nocturne.util.NocDateFormat;
import com.castaware.nocturne.util.NocLogger;
import com.castaware.nocturne.util.NocMarketUtils;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public class SyncBinanceSwaps 
{	
	private String TRADES_FOLDER="/data/trades/";
	
	@Autowired
	Dao dao;
	
	@Test
	public void sync()
	{
		LogManager.getLogger("com.castaware.nocturne").setLevel(Level.TRACE);
		NocLogger LOG = new NocLogger(Level.INFO); 
		
		try
		{
			Wallet wallet = dao.retrieveBySingleLike(Wallet.class,"id","BINANCE").get(0);
			
			Set<File> 				  sheetFiles = retrieveSheets();
			List<Map<String, String>> sheetData  = retrieveData(sheetFiles);	
			
			int count = 1;
			
			for(Map<String,String> cellData : sheetData)
			{
				LocalDateTime timestamp = NocDateFormat.parse_yyyyMMddHHmmssHifen(cellData.get("Date(UTC)"));
				String		  market    = cellData.get("Market");
				String        type      = cellData.get("Type");
				BigDecimal    amount    = new BigDecimal(cellData.get("Amount"));
				BigDecimal    total     = new BigDecimal(cellData.get("Total"));
				BigDecimal    feeAmount = new BigDecimal(cellData.get("Fee"));
				String        feeAsset  = cellData.get("Fee Coin");
				
				String fromAsset,toAsset;
				BigDecimal fromAmount,toAmount;
				String description = "BINANCE "+(count++);
				
				Pair<String,String> pair = NocMarketUtils.splitPair(market);
				
				if(type.equals("BUY"))
				{
					fromAsset=pair.getSecond();
					toAsset=pair.getFirst();
					fromAmount=total;
					toAmount=amount;
				}
				else
				{
					fromAsset=pair.getFirst();
					toAsset=pair.getSecond();
					fromAmount=amount;
					toAmount=total;
				}
				
				boolean newTx;
				TransactionSwap tx;
				
				List<TransactionSwap> txs = dao.retrieveBySingleLike(TransactionSwap.class, 
																	  "description",
																	  description);
				
				if (txs.size()>1)
					throw new IllegalStateException("Duplicate TX: "+description);
				
				if (txs.size()==1)
				{
					newTx=false;
					tx=txs.get(0);
				}
				else
				{
					newTx=true;
					tx=new TransactionSwap();
					tx.setToAlloc(false);
					tx.setFromAlloc(false);
				}
				
				tx.setFromAsset(fromAsset);
				tx.setFromAmount(fromAmount);
				tx.setToAsset(toAsset);
				tx.setToAmount(toAmount);
				tx.setFeeAsset(feeAsset);
				tx.setFeeAmount(feeAmount);
				tx.setDescription(description);
				tx.setTimestamp(timestamp);
				tx.setFromWallet(wallet);
				tx.setSuccess(true);
				
				LOG.info(String.format("%s - %s",newTx?"NEW":"OLD",tx));
				
				dao.persist(tx);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private List<Map<String, String>> retrieveData(Set<File> files) throws FileNotFoundException, IOException 
	{
		List<Map<String,String>> sheetData = new ArrayList<Map<String,String>>();
		
		for (File file : files) 
		{
			FileInputStream inputStream = new FileInputStream(file);
			Workbook 		workbook 	= new XSSFWorkbook(inputStream);
			Sheet 			firstSheet 	= workbook.getSheetAt(0);
		
			Iterator<Row> rows = firstSheet.iterator();
		
			// Indexa as colunas pelo cabeçalho
			TreeMap<Integer,String> headerMap = new TreeMap<Integer,String>();
			Iterator<Cell> headerCells = rows.next().cellIterator();
			
			do
			{
				Cell headerCell = headerCells.next();
				headerMap.put(headerCell.getColumnIndex(), headerCell.getStringCellValue());
			}
			while (headerCells.hasNext());
		
			List<Map<String,String>> cellsData = new ArrayList<Map<String,String>>();
			
			do
			{
				Iterator<Cell> cells = rows.next().cellIterator();
				Map<String,String> cellData = new TreeMap<String,String>(); 
				
				do  
				{
					Cell cell = cells.next();
					cellData.put(headerMap.get(cell.getColumnIndex()), cell.getStringCellValue());																													
				}
				while (cells.hasNext());
				
				cellsData.add(cellData);
			}
			while (rows.hasNext()); 
				
			Collections.reverse(cellsData);
			sheetData.addAll(cellsData);
			
			workbook.close();
			inputStream.close();
			
			
		}
		return sheetData;
	}

	private Set<File> retrieveSheets() throws URISyntaxException 
	{
		// 1 - Lista as planilhas disponíveis
		URL  url    = this.getClass().getResource(TRADES_FOLDER);
		File folder = Paths.get(url.toURI()).toFile();	
		
		if(!folder.isDirectory())
			throw new IllegalArgumentException(folder+" is not a folder");
		
		Set<File> files = new TreeSet<File>();
		
		for (String fileName: folder.list()) 
		{
			files.add(new File(folder.getAbsolutePath()+"/"+fileName));
		}
		
		if (files.size()==0)
			throw new IllegalArgumentException(folder+" is empty");
		
		return files;
	}
}
