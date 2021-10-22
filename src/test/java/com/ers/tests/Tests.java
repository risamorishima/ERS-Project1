package com.ers.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ers.AES;
import com.ers.ReimbursementController;
import com.ers.UserController;
import com.ers.dao.ReimbursementDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.Reimbursement;
import com.ers.model.ReimbursementStatus;
import com.ers.model.ReimbursementType;
import com.ers.model.User;
import com.ers.model.UserRole;
import com.ers.service.ReimbursementService;
import com.ers.service.UserService;

public class Tests {
	
	@Mock
	private UserDaoImpl uDao;
	@Mock
	private ReimbursementDaoImpl rDao;
	@Mock
	private HttpSession session;
	@Mock
	private HttpServletRequest req;
	@Mock
	private HttpServletResponse res;
	
	private UserService uServ;
	private ReimbursementService rServ;
	
	private User testEmployee1;
	private User testEmployee2;
	private User testEmployee3;
	private User testFinanceManager1;
	private User testFinanceManager2;
	
	private User testEmployee1Decrypted;
	
	private List<User> userList;
	private List<User> employeeList;
	
	private UserRole employeeRole;
	private UserRole managerRole;
	
	private Reimbursement testReimb1;
	private Reimbursement testReimb2;
	private Reimbursement testReimb3;
	private Reimbursement testReimb4;
	
	private List<Reimbursement> reimbList;
	private List<Reimbursement> employee1ReimbList;
	
	private ReimbursementStatus pending;
	private ReimbursementStatus approved;
	private ReimbursementStatus denied;
	
	private ReimbursementType type1;
	private ReimbursementType type2;
	private ReimbursementType type3;
	private ReimbursementType type4;
	
	private UserController uControl;
	private ReimbursementController rControl;
	
	
	AES aes;
	String secretKey = "somesecretkey";
	
	private Date submitDate = new Date(System.currentTimeMillis());
	
	@BeforeEach
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);		
		aes = new AES();
		uServ = new UserService(uDao);
		rServ = new ReimbursementService(rDao);
		employeeRole = new UserRole(1, "Employee");
		managerRole = new UserRole(2, "Finance Manager");
		
		testEmployee1 = new User(99, "testingEmployee1", aes.encrypt("testemp1", secretKey), "john", "doe", "johndoe123@email.com", employeeRole);
		testEmployee2 = new User(100, "testingEmployee2", aes.encrypt("testemp12", secretKey), "another", "doe", "anotherdoe123@email.com", employeeRole);
		testEmployee3 = new User(101, "testingEmployee3", aes.encrypt("testemp123", secretKey), "another", "john", "anotherjohn123@email.com", employeeRole);
		
		testFinanceManager1 = new User(999, "testingManager1", aes.encrypt("testmanager1", secretKey), "john", "doe", "johndoe1@email.com", managerRole);
		testFinanceManager2 = new User(1000, "testingManager2", aes.encrypt("testmanager12", secretKey), "another", "doe", "anotherdoe1@email.com", managerRole);

		userList = new ArrayList<>();
		userList.add(testEmployee1);
		userList.add(testEmployee2);
		userList.add(testEmployee3);
		userList.add(testFinanceManager1);
		userList.add(testFinanceManager2);
		employeeList = Arrays.asList(testEmployee1, testEmployee2, testEmployee3);

		when(uDao.getByUsername("testingEmployee1")).thenReturn(testEmployee1);
		when(uDao.getByUsername("testingManager1")).thenReturn(testFinanceManager1);
		when(uDao.getById(99)).thenReturn(testEmployee1);
		when(uDao.getAll()).thenReturn(userList);
		
		pending = new ReimbursementStatus(1, "Pending");
		approved = new ReimbursementStatus(2, "Approved");
		denied = new ReimbursementStatus(3, "Denied");
		
		type1 = new ReimbursementType(1, "Type1");
		type2 = new ReimbursementType(2, "Type2");
		type3 = new ReimbursementType(3, "Type3");
		type4 = new ReimbursementType(4, "Type4");
		
		testReimb1 = new Reimbursement(1, 100, "test1", submitDate, null, 99, 0, pending, type1);
		testReimb2 = new Reimbursement(2, (float)32.50, "test2", submitDate, null, 99, 0, pending, type2);
		testReimb3 = new Reimbursement(3, (float)2000, "test3", submitDate, submitDate, 100, 999, approved, type3);
		testReimb4 = new Reimbursement(4, (float)32.50, "test4", submitDate, submitDate, 101, 1000, denied, type4);
		
		reimbList = new ArrayList<>();
		reimbList.add(testReimb1);
		reimbList.add(testReimb2);
		reimbList.add(testReimb3);
		reimbList.add(testReimb4);
		
		employee1ReimbList = Arrays.asList(testReimb1, testReimb2);
		when(rDao.getById(1)).thenReturn(testReimb1);
		when(rDao.getStatusId("Pending")).thenReturn(1);
		when(rDao.getStatusId("Approved")).thenReturn(2);
		when(rDao.getStatusId("Denied")).thenReturn(3);
		when(rDao.getTypeId("Type1")).thenReturn(1);
		when(rDao.getTypeId("Type2")).thenReturn(2);
		when(rDao.getTypeId("Type3")).thenReturn(3);
		when(rDao.getTypeId("Type4")).thenReturn(4);
		when(rDao.getAll()).thenReturn(reimbList);
		when(rDao.insert(testReimb1)).thenReturn(testReimb1);
		
		uControl = new UserController(uDao);
		rControl = new ReimbursementController(rDao);
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession()).thenReturn(session);
		when(req.getSession(false)).thenReturn(session);
	}
	
	@Test
	public void testAES() {
		String randomInput = "sdfgbahjkdgayufsga";
        String encrypted = aes.encrypt(randomInput, secretKey);
        String decrypted = aes.decrypt(encrypted, secretKey);
        assertEquals(randomInput,decrypted);
	}
	
	@Test
	public void testGetUserByIdSuccess() {
		assertEquals(testEmployee1, uServ.getById(99));
	}
	
	@Test
	public void testGetUserByIdFailure() {
		assertThrows(NullPointerException.class, ()->uServ.getById(0));
	}
	
	@Test
	public void testGetUserByUsernameSuccess() {
		assertEquals(testEmployee1,uServ.getByUsername("testingEmployee1"));
	}
	
	@Test
	public void testGetUserByUsernameFailure() {
		assertThrows(NullPointerException.class, ()->uServ.getByUsername("non-existing"));
	}
	
	@Test
	public void testGetAllEmployee() {
		assertEquals(employeeList,uServ.getAllEmployee());
	}
	
	@Test
	public void testDecryptUserv() {
		testEmployee1Decrypted = testEmployee1;
		testEmployee1Decrypted.setPassword("testemp1");
		assertEquals(testEmployee1Decrypted, uServ.decryptPassword(testEmployee1));
	}
	
	@Test
	public void testGetReimbByIdSuccess() {
		assertEquals(testReimb1, rServ.getById(1));
	}
	
	@Test
	public void testGetReimbByIdFailure() {
		assertThrows(NullPointerException.class, ()->rServ.getById(0));
	}
	
	@Test
	public void testGetStatusIdSuccess() {
		assertEquals(1,rServ.getStatusId("Pending"));
	}
	
	@Test
	public void testGetStatusIdFailure() {
		assertThrows(NullPointerException.class, ()->rServ.getStatusId("non-existing"));
	}
	
	@Test
	public void testGetTypeIdSuccess() {
		assertEquals(1,rServ.getTypeId("Type1"));
	}
	
	@Test
	public void testGetTypeIdFailure() {
		assertThrows(NullPointerException.class, ()->rServ.getTypeId("non-existing"));
	}
	
	@Test
	public void testGetAllReimbById() {
		assertEquals(employee1ReimbList, rServ.getAllByUserId(99));
	}
	
	@Test
	public void testLoginUserInfo() {
		when(req.getParameter("username")).thenReturn("testingEmployee1");
		when(req.getParameter("password")).thenReturn("testemp1");
		session.setAttribute("currentUser", testEmployee1);
		uControl.login(req);
		
		Map<String, Object> testInfo = new HashMap<>();
		testInfo.put("wrongcreds", false);
		testEmployee1Decrypted = testEmployee1;
		testEmployee1Decrypted.setPassword("testemp1");
		testInfo.put("user", testEmployee1Decrypted);
		
		assertEquals(testInfo, uControl.userInfo);
	}
	
	@Test
	public void testLoginReturnEmployee() {
		when(req.getParameter("username")).thenReturn("testingEmployee1");
		when(req.getParameter("password")).thenReturn("testemp1");
		session.setAttribute("currentUser", testEmployee1);
		assertEquals("html/employee.html", uControl.login(req));
	}
	
	@Test
	public void testLoginReturnManager() {
		when(req.getParameter("username")).thenReturn("testingManager1");
		when(req.getParameter("password")).thenReturn("testmanager1");
		session.setAttribute("currentUser", testFinanceManager1);
		assertEquals("html/manager.html", uControl.login(req));
	}
	
	@Test
	public void testLogoutReturn() {
		assertEquals("html/index.html", uControl.logout(req));
	}
	
	@Test
	public void testLogoutInvalidated() {
		uControl.logout(req);
		verify(session, times(1)).invalidate();
	}
	
}
