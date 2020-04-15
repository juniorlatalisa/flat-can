package br.com.virtualsistemas.common.validations;

import org.junit.Assert;
import org.junit.Test;

public class CPFValidatorTest {

	@Test
	public void isValid() {
		Assert.assertTrue(CPFValidator.isValid("111.444.777-35"));
	}
	
	@Test
	public void isInvalid() {
		Assert.assertFalse(CPFValidator.isValid("111.444.777-36"));
	}

}