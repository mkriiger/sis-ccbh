package com.castaware.nocturne.datatypes.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eic.tcc.dao.Dao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({
	"file:src/main/webapp/WEB-INF/spring-servlet.xml"
})
public abstract class NocturneDaoTest 
{
	@Autowired
	protected Dao dao;
	
	
}
