package com.ers.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ers.dao.ReimbursementDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.service.ReimbursementService;
import com.ers.service.UserService;

public class Tests {
	
	@Mock
	private UserDaoImpl uDao;
	@Mock
	private ReimbursementDaoImpl rDao;
	
	private UserService uServ;
	private ReimbursementService rServ;

	@BeforeEach
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		uServ = new UserService(uDao);
		rServ = new ReimbursementService(rDao);
	}
	
	@Test
	public void testGetUserSuccess() {
	}
}
