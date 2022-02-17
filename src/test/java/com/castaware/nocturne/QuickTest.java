package com.castaware.nocturne;

import java.math.BigDecimal;

import org.junit.Test;

public class QuickTest 
{
	@Test
	public void test()
	{
		BigDecimal a = new BigDecimal("925");
		BigDecimal b = new BigDecimal("635");
		
		BigDecimal c = a.subtract(b);
		System.out.println(c);
	}

}
