package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import com.castaware.nocturne.util.NocDateFormat;

@Entity
public class PriceAtMinute 
{
	//
	// Atributos Mapeados
	//	
	@Id    	
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
		
	@Column    
	protected String coin;
	
	@Column
    @Type(type = "com.castaware.nocturne.datatypes.dao.mappings.BigDecimalStringType")
	protected BigDecimal price;
	
	@Column
	protected LocalDateTime datetime;
		
	public PriceAtMinute()
	{		
		
	}
	
	public PriceAtMinute(String coin, BigDecimal price, LocalDateTime datetime)
	{
		super();
		this.coin = coin;
		this.price = price;
		this.datetime = datetime;
	}
	
	public PriceAtMinute(String coin, BigDecimal price, String datetime)
	{
		super();
		this.coin = coin;
		this.price = price;
		this.datetime = NocDateFormat.parse_yyyyMMddHHmmssHifen(datetime);
	}

	public Long getId() {
		return id;
	}

	public String getCoin() {
		return coin;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}	
}





