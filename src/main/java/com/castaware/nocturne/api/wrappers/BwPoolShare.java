package com.castaware.nocturne.api.wrappers;

import com.google.gson.JsonObject;

public class BwPoolShare 
{
	public int    poolId;
	public String poolName;
	public long   updateTime;
	
	public JsonObject share;
	public JsonObject liquidity;
	
	@Override
	public String toString() {
		return "BwSwapPosition [poolId=" + poolId + ", poolName=" + poolName + ", updateTime=" + updateTime + ", share="
				+ share + ", liquidity=" + liquidity + "]";
	}
}


