package com.woori.community.test;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.woori.community.config.RootApplicationContextConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= RootApplicationContextConfig.class)

public class MySQLConnectionTest {

	@Inject
	private DataSource ds;

	@Test
	public void testConnection() throws Exception {

		try (Connection con = ds.getConnection()) {

			System.out.println("\n >>>>>>>>>> Connection Ãâ·Â : " + con + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
