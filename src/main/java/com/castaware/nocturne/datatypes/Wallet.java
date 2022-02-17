package com.castaware.nocturne.datatypes;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Wallet implements Comparable<Wallet>  
{
	@Id    		
	protected String id;
	
	@Column
    protected String name;
	
	@Column
	@Enumerated(EnumType.STRING)    
	protected Network network = Network.BINANCE;
	
	@Column	    
	protected String provider;
	
	@Column
    protected String apiKey;
	
	@Column
    protected String secretKey;
	
	@Column
    protected String address;
	
	@Column
	protected Boolean visible;
	
	@Column
	protected Boolean selected;
	
	@Column(name="ordered")
	protected Integer order;
	
	@Transient
	protected LocalDate creation;
	
	@Column
	protected Double deposited = 0d;
	
	@Column
	protected Double withdrawn = 0d;
	
	public void deposit(double deposit)
	{
		deposited+=deposit;
	}
	
	public void withdraw(double withdraw)
	{
		withdrawn+=withdraw;
	}
	
	public void withdraw(BigDecimal amount, Asset asset)
	{
		double quote = asset.getEntryQuote();
		double value = amount.doubleValue()*quote;
		withdraw(value);
	}
	
	@Override
	public String toString() {
		return "NeoWallet [id=" + id + ", name=" + name + ", network=" + network + ", provider=" + provider
				+ ", apiKey=" + apiKey + ", secretKey=" + secretKey + ", address=" + address + ", creation=" + creation
				+ "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getCreation() {
		return creation;
	}

	public void setCreation(LocalDate creation) {
		this.creation = creation;
	}
	
	public Boolean getVisible() {
		return visible;
	}
	
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public Boolean getSelected() {
		return selected;
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
	public Double getDeposited() {
		return deposited;
	}

	public void setDeposited(Double deposited) {
		this.deposited = deposited;
	}

	public Double getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(Double withdrawn) {
		this.withdrawn = withdrawn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wallet other = (Wallet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Wallet o) 
	{
		return this.id.compareTo(o.id);
	}
}





