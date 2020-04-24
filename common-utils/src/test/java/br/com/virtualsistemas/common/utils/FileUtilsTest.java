package br.com.virtualsistemas.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {

	@Test
	public void checksum1() {
		Assert.assertEquals("1128e1d617bbb307ceab6da6599b079ed2cb58b43bd687d38a41bf2b966c30b4",
				FileUtils.checksum(FileUtilsTest.class.getResourceAsStream("/META-INF/vs-logo.png")));
	}
	
	@Test
	public void checksum2() {
		Assert.assertEquals("1128e1d617bbb307ceab6da6599b079ed2cb58b43bd687d38a41bf2b966c30b4",
				FileUtils.checksum(FileUtilsTest.class.getResource("/META-INF/vs-logo.png")));
	}
}