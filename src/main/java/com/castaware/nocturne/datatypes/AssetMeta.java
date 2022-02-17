package com.castaware.nocturne.datatypes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class AssetMeta
{
	@Id
	private String asset;
	
	@Column
	private String category;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Ecosystem ecosystem;
	
	@Column
	private String coingecko;

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public void setEcosystem(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
	}

	public String getCoingecko() {
		return coingecko;
	}

	public void setCoingecko(String coingecko) {
		this.coingecko = coingecko;
	}
}