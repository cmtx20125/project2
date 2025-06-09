package com.example.pet;

import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.controller.SmsController;
import com.example.pet.controller.UserController;
import com.example.pet.dao.entity.User;
import com.example.pet.mapper.UserMapper;
import com.example.pet.service.SmsService;
import com.example.pet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PetApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private UserMapper userMapper;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void loginChild_Success() {
		// Arrange
		String username = "小明";
		String password = "123456";
		User mockUser = new User();
		mockUser.setUserName(username);
		mockUser.setUserPwd(password);

		when(userMapper.selectOne(any())).thenReturn(mockUser);

		// Act
		HttpResponseEntity response = userController.loginChild(username, password);

		// Assert
		assertEquals("666", response.getCode());
		assertEquals("登录成功", response.getMessage());
		assertEquals(mockUser, response.getData());
	}

	@Test
	void loginChild_Failure() {
		// Arrange
		String username = "wrongUser";
		String password = "wrongPass";

		when(userMapper.selectOne(any())).thenReturn(null);

		// Act
		HttpResponseEntity response = userController.loginChild(username, password);

		// Assert
		assertEquals("0", response.getCode());
		assertEquals("用户或密码错误", response.getMessage());
		assertNull(response.getData());
	}

	@Test
	void getUserById_Success() {
		// Arrange
		String userId = "123";
		User mockUser = new User();
		mockUser.setUserId(userId);
		mockUser.setUserPic("image.jpg");

		when(userMapper.selectOne(any())).thenReturn(mockUser);

		// Act
		HttpResponseEntity response = userController.getUserById(userId);

		// Assert
		assertEquals("666", response.getCode());
		assertEquals("登录成功", response.getMessage());
		assertNotNull(response.getData());
		User returnedUser = (User) response.getData();
		assertTrue(returnedUser.getUserPic().startsWith("http://127.0.0.1:9005/pet/"));
	}

	@Test
	void getUserById_Failure() {
		// Arrange
		String userId = "nonexistent";

		when(userMapper.selectOne(any())).thenReturn(null);

		// Act
		HttpResponseEntity response = userController.getUserById(userId);

		// Assert
		assertEquals("0", response.getCode());
		assertEquals("用户或密码错误", response.getMessage());
		assertNull(response.getData());
	}

	@Test
	void updateUserV_Success() throws Exception {
		// Arrange
		String name = "newName";
		String gender = "male";
		String phone = "1234567890";
		String password = "newPass";
		String id = "123";
		String address = "newAddress";
		String birthTimeStr = "2000-01-01";
		MultipartFile mockFile = new MockMultipartFile("test.jpg", new byte[0]);

		User mockUser = new User();
		mockUser.setUserId(id);

		when(userMapper.selectOne(any())).thenReturn(mockUser);
		when(userService.uploadToMinio(any())).thenReturn("uploaded.jpg");
		when(userService.updateUser(any())).thenReturn(1);

		// Act
		HttpResponseEntity response = userController.updateUser(
				name, gender, phone, password, id, address, birthTimeStr, mockFile
		);

		// Assert
		assertEquals("666", response.getCode());
		assertEquals("更新成功", response.getMessage());
		assertEquals(1, response.getData());
	}

	@Test
	void updateUserV_Failure() throws Exception {
		// Arrange
		String name = "newName";
		String gender = "male";
		String phone = "1234567890";
		String password = "newPass";
		String id = "123";
		String address = "newAddress";
		String birthTimeStr = "2000-01-01";
		MultipartFile mockFile = new MockMultipartFile("test.jpg", new byte[0]);

		User mockUser = new User();
		mockUser.setUserId(id);

		when(userMapper.selectOne(any())).thenReturn(mockUser);
		when(userService.uploadToMinio(any())).thenReturn("uploaded.jpg");
		when(userService.updateUser(any())).thenReturn(0);

		// Act
		HttpResponseEntity response = userController.updateUser(
				name, gender, phone, password, id, address, birthTimeStr, mockFile
		);

		// Assert
		assertEquals("0", response.getCode());
		assertEquals("更新失败", response.getMessage());
		assertEquals(0, response.getData());
	}

	@Test
	void updateUsers_Success() {
		// Arrange
		User user = new User();
		user.setUserId("123");

		when(userService.updateUser(any())).thenReturn(1);

		// Act
		Response response = userController.updateUsers(user);

		// Assert
		assertTrue(response.isSuccess());
		assertEquals("成功", response.getMessage());
	}

	@Test
	void updateUsers_Failure() {
		// Arrange
		User user = new User();
		user.setUserId("123");

		when(userService.updateUser(any())).thenReturn(0);

		// Act
		Response response = userController.updateUsers(user);

		// Assert
		assertFalse(response.isSuccess());
		assertEquals("失败", response.getMessage());
	}

	@Test
	void sendPhone_Exists() {
		// Arrange
		String phone = "1234567890";
		User mockUser = new User();
		mockUser.setUserPhone(phone);

		when(userMapper.selectOne(any())).thenReturn(mockUser);

		// Act
		Response response = userController.getPhone(phone);

		// Assert
		assertFalse(response.isSuccess());
		assertEquals("手机号存在", response.getMessage());
	}

	@Test
	void sendPhone_NotExists() {
		// Arrange
		String phone = "nonexistent";

		when(userMapper.selectOne(any())).thenReturn(null);

		// Act
		Response response = userController.getPhone(phone);

		// Assert
		assertTrue(response.isSuccess());
		assertEquals("手机号不存在", response.getMessage());
	}

	@Mock
	private SmsService smsService;


	@InjectMocks
	private SmsController smsController;



	@Test
	void TC_SEND_CODE_001_成功发送短信验证码() {
		// 模拟短信服务成功
		when(smsService.sendVerificationCode(any(), any())).thenReturn(true);

		Response response = smsController.sendCode("13800138000");

		assertTrue(response.isSuccess());
		assertEquals("验证码获取成功", response.getMessage());

	}

	@Test
	void TC_SEND_CODE_002_短信验证码发送失败() {
		// 模拟短信服务失败
		when(smsService.sendVerificationCode(any(), any())).thenReturn(false);

		Response response = smsController.sendCode("13800138000");

		assertFalse(response.isSuccess());
		assertEquals("验证码获取失败", response.getMessage());
	}




}
