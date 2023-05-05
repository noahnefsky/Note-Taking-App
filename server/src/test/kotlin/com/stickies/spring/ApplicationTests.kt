package com.stickies.spring
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class ApplicationTests {
	@Autowired
	private val stickiesAppContext: ApplicationContext? = null

	@Test
	fun contextLoads() {
		//make sure the application context is not null
		assertThat(stickiesAppContext).isNotNull();
	}
}