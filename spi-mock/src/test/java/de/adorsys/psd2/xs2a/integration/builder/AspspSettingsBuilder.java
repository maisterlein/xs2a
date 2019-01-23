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

package de.adorsys.psd2.xs2a.integration.builder;

import de.adorsys.psd2.aspsp.profile.domain.AspspSettings;
import de.adorsys.psd2.aspsp.profile.domain.BookingStatus;
import de.adorsys.psd2.aspsp.profile.domain.MulticurrencyAccountLevel;
import de.adorsys.psd2.aspsp.profile.domain.SupportedAccountReferenceField;
import de.adorsys.psd2.xs2a.core.profile.PaymentType;

import java.util.*;

import static de.adorsys.psd2.aspsp.profile.domain.BookingStatus.*;
import static de.adorsys.psd2.aspsp.profile.domain.SupportedAccountReferenceField.IBAN;

public class AspspSettingsBuilder {
    private static int FREQUENCY_PER_DAY = 5;
    private static boolean COMBINED_SERVICE_INDICATOR = false;
    private static boolean TPP_SIGNATURE_REQUIRED = false;
    private static final String PIS_REDIRECT_LINK = "http://localhost:4200/pis/{redirect-id}/";
    private static final String AIS_REDIRECT_LINK = "http://localhost:4200/ais/{redirect-id}/";
    private static final MulticurrencyAccountLevel MULTICURRENCY_ACCOUNT_LEVEL = MulticurrencyAccountLevel.SUBACCOUNT;
    private static final List<BookingStatus> AVAILABLE_BOOKING_STATUSES = getBookingStatuses();
    private static final List<SupportedAccountReferenceField> SUPPORTED_ACCOUNT_REFERENCE_FIELDS = getSupportedAccountReferenceFields();
    private static final int CONSENT_LIFETIME = 0;
    private static final int TRANSACTION_LIFETIME = 0;
    private static final boolean ALL_PSD_2_SUPPORT = false;
    private static final boolean BANK_OFFERED_CONSENT_SUPPORT = false;
    private static final boolean TRANSACTIONS_WITHOUT_BALANCES_SUPPORTED = false;
    private static final boolean SIGNING_BASKET_SUPPORTED = true;
    private static final boolean PAYMENT_CANCELLATION_AUTHORIZATION_MANDATED = false;
    private static final boolean PIIS_CONSENT_SUPPORTED = false;
    private static final boolean DELTA_REPORT_SUPPORTED = false;
    private static final long REDIRECT_URL_EXPIRATION_TIME_MS = 600000;
    private static final long NOT_CONFIRMED_CONSENT_EXPIRATION_PERIOD_MS = 86400000;
    private static final long NOT_CONFIRMED_PAYMENT_EXPIRATION_PERIOD_MS = 86400000;
    private static final String PIS_PAYMENT_CANCELLATION_REDIRECT_URL_TO_ASPSP = "http://localhost:4200/pis/cancellation/{redirect-id}/";
    private static Map<PaymentType, Set<String>> SUPPORTED_PAYMENT_TYPE_AND_PRODUCT_MATRIX = buildSupportedPaymentTypeAndProductMatrix();
    private static final long PAYMENT_CANCELLATION_REDIRECT_URL_EXPIRATION_TIME_MS = 600000;

    public static AspspSettings buildAspspSettings() {
        return new AspspSettings(
            FREQUENCY_PER_DAY,
            COMBINED_SERVICE_INDICATOR,
            TPP_SIGNATURE_REQUIRED,
            PIS_REDIRECT_LINK,
            AIS_REDIRECT_LINK,
            MULTICURRENCY_ACCOUNT_LEVEL,
            BANK_OFFERED_CONSENT_SUPPORT,
            AVAILABLE_BOOKING_STATUSES,
            SUPPORTED_ACCOUNT_REFERENCE_FIELDS,
            CONSENT_LIFETIME,
            TRANSACTION_LIFETIME,
            ALL_PSD_2_SUPPORT,
            TRANSACTIONS_WITHOUT_BALANCES_SUPPORTED,
            SIGNING_BASKET_SUPPORTED,
            PAYMENT_CANCELLATION_AUTHORIZATION_MANDATED,
            PIIS_CONSENT_SUPPORTED,
            DELTA_REPORT_SUPPORTED,
            REDIRECT_URL_EXPIRATION_TIME_MS,
            PIS_PAYMENT_CANCELLATION_REDIRECT_URL_TO_ASPSP,
            NOT_CONFIRMED_CONSENT_EXPIRATION_PERIOD_MS,
            NOT_CONFIRMED_PAYMENT_EXPIRATION_PERIOD_MS,
            SUPPORTED_PAYMENT_TYPE_AND_PRODUCT_MATRIX,
            PAYMENT_CANCELLATION_REDIRECT_URL_EXPIRATION_TIME_MS);
    }

    private static List<SupportedAccountReferenceField> getSupportedAccountReferenceFields() {
        return Collections.singletonList(IBAN);
    }

    private static List<BookingStatus> getBookingStatuses() {
        return Arrays.asList(
            BOOKED,
            PENDING,
            BOTH
        );
    }

    private static Map<PaymentType, Set<String>> buildSupportedPaymentTypeAndProductMatrix() {
        Map<PaymentType, Set<String>> matrix = new HashMap<>();
        Set<String> availablePaymentProducts = Collections.singleton("sepa-credit-transfers");
        matrix.put(PaymentType.SINGLE, availablePaymentProducts);
        matrix.put(PaymentType.PERIODIC, availablePaymentProducts);
        matrix.put(PaymentType.BULK, availablePaymentProducts);
        return matrix;
    }
}