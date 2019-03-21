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

package de.adorsys.psd2.stub.impl;

import de.adorsys.psd2.xs2a.core.consent.AspspConsentData;
import de.adorsys.psd2.xs2a.core.pis.TransactionStatus;
import de.adorsys.psd2.xs2a.spi.domain.SpiContextData;
import de.adorsys.psd2.xs2a.spi.domain.authorisation.SpiScaConfirmation;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiPaymentExecutionResponse;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiSinglePaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.service.SinglePaymentSpi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SinglePaymentSpiMockImpl implements SinglePaymentSpi {
    private static final String TEST_ASPSP_DATA = "Test aspsp data";

    @Override
    @NotNull
    public SpiResponse<SpiSinglePaymentInitiationResponse> initiatePayment(@NotNull SpiContextData contextData, @NotNull SpiSinglePayment payment, @NotNull AspspConsentData initialAspspConsentData) {
        log.info("SinglePaymentSpi#initiatePayment: contextData {}, spiSinglePayment {}, aspspConsentData {}", contextData, payment, initialAspspConsentData);
        SpiSinglePaymentInitiationResponse response = new SpiSinglePaymentInitiationResponse();
        response.setTransactionStatus(TransactionStatus.RCVD);
        response.setPaymentId(UUID.randomUUID().toString());
        response.setAspspAccountId("11111-11118");

        return SpiResponse.<SpiSinglePaymentInitiationResponse>builder()
                   .aspspConsentData(initialAspspConsentData.respondWith(TEST_ASPSP_DATA.getBytes()))
                   .payload(response)
                   .success();
    }

    @Override
    @NotNull
    public SpiResponse<SpiSinglePayment> getPaymentById(@NotNull SpiContextData contextData, @NotNull SpiSinglePayment payment, @NotNull AspspConsentData aspspConsentData) {
        log.info("SinglePaymentSpi#getPaymentById: contextData {}, spiSinglePayment {}, aspspConsentData {}", contextData, payment, aspspConsentData);

        return SpiResponse.<SpiSinglePayment>builder()
                   .aspspConsentData(aspspConsentData)
                   .payload(payment)
                   .success();
    }

    @Override
    @NotNull
    public SpiResponse<TransactionStatus> getPaymentStatusById(@NotNull SpiContextData contextData, @NotNull SpiSinglePayment payment, @NotNull AspspConsentData aspspConsentData) {
        log.info("SinglePaymentSpi#getPaymentStatusById: contextData {}, spiSinglePayment {}, aspspConsentData {}", contextData, payment, aspspConsentData);

        return SpiResponse.<TransactionStatus>builder()
                   .aspspConsentData(aspspConsentData)
                   .payload(payment.getPaymentStatus())
                   .success();
    }

    @Override
    @NotNull
    public SpiResponse<SpiPaymentExecutionResponse> executePaymentWithoutSca(@NotNull SpiContextData contextData, @NotNull SpiSinglePayment payment, @NotNull AspspConsentData aspspConsentData) {
        log.info("SinglePaymentSpi#executePaymentWithoutSca: contextData {}, spiSinglePayment {}, aspspConsentData {}", contextData, payment, aspspConsentData);

        return SpiResponse.<SpiPaymentExecutionResponse>builder()
                   .aspspConsentData(aspspConsentData)
                   .payload(new SpiPaymentExecutionResponse(TransactionStatus.ACCP))
                   .success();
    }

    @Override
    @NotNull
    public SpiResponse<SpiPaymentExecutionResponse> verifyScaAuthorisationAndExecutePayment(@NotNull SpiContextData contextData, @NotNull SpiScaConfirmation spiScaConfirmation, @NotNull SpiSinglePayment payment, @NotNull AspspConsentData aspspConsentData) {
        log.info("SinglePaymentSpi#verifyScaAuthorisationAndExecutePayment: contextData {}, spiScaConfirmation{}, spiSinglePayment {}, aspspConsentData {}", contextData, spiScaConfirmation, payment, aspspConsentData);

        return SpiResponse.<SpiPaymentExecutionResponse>builder()
                   .aspspConsentData(aspspConsentData)
                   .payload(new SpiPaymentExecutionResponse(TransactionStatus.ACCP))
                   .success();
    }
}