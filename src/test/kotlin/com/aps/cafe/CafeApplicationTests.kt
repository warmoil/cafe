package com.aps.cafe

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.DefaultAsserter.assertEquals

@SpringBootTest

class CafeApplicationTests {

	@Test
	fun contextLoads() {
		assertEquals("",1,1)
	}

}
