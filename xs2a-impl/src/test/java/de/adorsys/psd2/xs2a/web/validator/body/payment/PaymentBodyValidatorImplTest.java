/*
 * Copyright 2018-2019 adorsys GmbH & Co KG
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

package de.adorsys.psd2.xs2a.web.validator.body.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.psd2.xs2a.domain.MessageErrorCode;
import de.adorsys.psd2.xs2a.domain.TppMessageInformation;
import de.adorsys.psd2.xs2a.exception.MessageError;
import de.adorsys.psd2.xs2a.service.mapper.psd2.ErrorType;
import de.adorsys.psd2.xs2a.service.profile.StandardPaymentProductsResolver;
import de.adorsys.psd2.xs2a.web.validator.body.payment.type.PaymentTypeValidator;
import de.adorsys.psd2.xs2a.web.validator.body.payment.type.PaymentTypeValidatorContext;
import de.adorsys.psd2.xs2a.web.validator.header.ErrorBuildingServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentBodyValidatorImplTest {

    private static final String JSON_PAYMENT_PRODUCT = "sepa-credit-transfers";
    private static final String PAIN_PAYMENT_PRODUCT = "pain.001-sepa-credit-transfers";
    private static final String PAYMENT_SERVICE = "some payment service";
    private static final String INVALID_PAYMENT_SERVICE = "invalid payment service";
    private static final String PAYMENT_SERVICE_PATH_VAR = "payment-service";
    private static final String PAYMENT_PRODUCT_PATH_VAR = "payment-product";
    private static final MessageError DESERIALISATION_ERROR =
        new MessageError(ErrorType.PIS_400, TppMessageInformation.of(MessageErrorCode.FORMAT_ERROR, "Cannot deserialize the request body"));

    private PaymentBodyValidatorImpl validator;
    private MessageError messageError;

    @Mock
    private PaymentTypeValidatorContext paymentTypeValidatorContext;
    @Mock
    private StandardPaymentProductsResolver standardPaymentProductsResolver;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PaymentTypeValidator paymentTypeValidator;

    @Before
    public void setUp() {
        messageError = new MessageError(ErrorType.PIS_400);
        validator = new PaymentBodyValidatorImpl(new ErrorBuildingServiceMock(ErrorType.PIS_400), objectMapper, paymentTypeValidatorContext, standardPaymentProductsResolver);
        when(standardPaymentProductsResolver.isRawPaymentProduct(eq(PAIN_PAYMENT_PRODUCT)))
            .thenReturn(true);
        when(standardPaymentProductsResolver.isRawPaymentProduct(eq(JSON_PAYMENT_PRODUCT)))
            .thenReturn(false);
    }

    @Test
    public void validate_shouldExecuteSpecificValidator() throws IOException {
        // Given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        Map<String, String> templates = buildTemplateVariables(JSON_PAYMENT_PRODUCT, PAYMENT_SERVICE);

        mockRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, templates);

        Object paymentBody = new Object();
        when(objectMapper.readValue(mockRequest.getInputStream(), Object.class))
            .thenReturn(paymentBody);

        when(paymentTypeValidatorContext.getValidator(PAYMENT_SERVICE))
            .thenReturn(Optional.of(paymentTypeValidator));

        // When
        validator.validate(mockRequest, messageError);

        // Then
        verify(paymentTypeValidator).validate(paymentBody, messageError);
    }

    @Test
    public void validate_wrongRequestBody_shouldReturnError() throws IOException {
        // Given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Map<String, String> templates = buildTemplateVariables(JSON_PAYMENT_PRODUCT, PAYMENT_SERVICE);
        mockRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, templates);

        when(objectMapper.readValue(mockRequest.getInputStream(), Object.class))
            .thenThrow(new IOException());

        // When
        validator.validate(mockRequest, messageError);

        // Then
        assertEquals(DESERIALISATION_ERROR, messageError);
    }

    @Test
    public void validate_rawPaymentProduct_shouldNotBeValidated() throws IOException {
        // Given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Map<String, String> templates = buildTemplateVariables(PAIN_PAYMENT_PRODUCT, PAYMENT_SERVICE);
        mockRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, templates);

        // When
        validator.validate(mockRequest, messageError);

        // Then
        assertTrue(messageError.getTppMessages().isEmpty());
        // noinspection unchecked
        verify(objectMapper, never()).readValue(any(InputStream.class), any(Class.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_unsupportedPaymentService_shouldThrowException() throws IOException {
        // Given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Map<String, String> templates = buildTemplateVariables(JSON_PAYMENT_PRODUCT, INVALID_PAYMENT_SERVICE);
        mockRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, templates);

        Object paymentBody = new Object();
        when(objectMapper.readValue(mockRequest.getInputStream(), Object.class))
            .thenReturn(paymentBody);

        when(paymentTypeValidatorContext.getValidator(any()))
            .thenReturn(Optional.empty());

        // When
        validator.validate(mockRequest, messageError);
    }

    private Map<String, String> buildTemplateVariables(String paymentProduct, String paymentService) {
        Map<String, String> templates = new HashMap<>();
        templates.put(PAYMENT_PRODUCT_PATH_VAR, paymentProduct);
        templates.put(PAYMENT_SERVICE_PATH_VAR, paymentService);
        return templates;
    }
}
