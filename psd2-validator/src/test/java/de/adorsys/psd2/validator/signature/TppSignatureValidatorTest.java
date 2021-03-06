/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.psd2.validator.signature;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tomitribe.auth.signatures.MissingRequiredHeaderException;

import de.adorsys.psd2.validator.certificate.util.CertificateUtils;

public class TppSignatureValidatorTest {

	private String signature;

	private String tppEncodedValidCert = "";

	private String tppEncodedInvalidCert = "";

	@Before
	public void init() {

		signature = new SignatureGeneratorUtil().generateSignature();

		CertificateUtils certificateUtils = new CertificateUtils();
		tppEncodedValidCert = certificateUtils.getCertificateByName("certificateValid.crt");

		tppEncodedInvalidCert = certificateUtils.getCertificateByName("certificateInvalid.crt");

	}

	@Test
	public void when_ValidSignature_Expected_true() throws NoSuchAlgorithmException, SignatureException, IOException {

		Map<String, String> headersMap = new HashMap<>();
	    headersMap.put("Digest", new SignatureGeneratorUtil().generateDigest());
		headersMap.put("TPP-Transaction-ID", "3dc3d5b3-7023-4848-9853-f5400a64e80f");
		headersMap.put("x-request-id", "99391c7e-ad88-49ec-a2ad-99ddcb1f7721");
		headersMap.put("PSU-ID", "PSU-1234");
		headersMap.put("Timestamp", "Sun, 06 Aug 2017 15:02:37 GMT");

		boolean verifySignature = new TppSignatureValidator().verifySignature(signature, tppEncodedValidCert, headersMap);
		Assert.assertEquals(verifySignature,
				true);
	}

	@Test(expected = MissingRequiredHeaderException.class)
	public void when_ValidSignature_And_MissingHeaderAttribute_Expected_MissingRequiredHeaderException()
			throws NoSuchAlgorithmException, SignatureException, IOException {

		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Digest", new SignatureGeneratorUtil().generateDigest());
		headersMap.put("TPP-Transaction-ID", "xxxxxx");
		headersMap.put("x-request-id", "0000000");
		headersMap.put("PSU-ID", "PSU-1234");

		new TppSignatureValidator().verifySignature(signature, tppEncodedValidCert, headersMap);
	}

	@Test
	public void when_ValidSignatureAndFalseCert_Expected_False()
			throws NoSuchAlgorithmException, SignatureException, IOException {

		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("Digest", new SignatureGeneratorUtil().generateDigest());
		headersMap.put("TPP-Transaction-ID", "3dc3d5b3-7023-4848-9853-f5400a64e80f");
		headersMap.put("x-request-id", "99391c7e-ad88-49ec-a2ad-99ddcb1f7721");
		headersMap.put("PSU-ID", "PSU-1234");
		headersMap.put("Timestamp", "Sun, 06 Aug 2017 15:02:37 GMT");

		Assert.assertFalse(new TppSignatureValidator().verifySignature(signature, tppEncodedInvalidCert, headersMap));
	}
}
