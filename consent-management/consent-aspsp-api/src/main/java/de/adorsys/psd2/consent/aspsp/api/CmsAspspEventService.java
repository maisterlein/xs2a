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

package de.adorsys.psd2.consent.aspsp.api;

import de.adorsys.psd2.consent.api.event.CmsEvent;
import de.adorsys.psd2.consent.api.event.EventType;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public interface CmsAspspEventService {
    /**
     * Returns a list of Event objects, recorded in given time period
     *
     * @param start First date of the period
     * @param end   Last date of the period
     * @return List of Event objects, recorded in given time period
     */
    List<CmsEvent> getEventsForPeriod(@NotNull OffsetDateTime start, @NotNull OffsetDateTime end);

    /**
     * Returns a list of Event objects, recorded in given time period
     *
     * @param start     First date of the period
     * @param end       Last date of the period
     * @param consentId Id of the consent
     * @return List of Event objects, recorded in given time period
     */
    List<CmsEvent> getEventsForPeriodAndConsentId(@NotNull OffsetDateTime start, @NotNull OffsetDateTime end, String consentId);

    /**
     * Returns a list of Event objects, recorded in given time period
     *
     * @param start     First date of the period
     * @param end       Last date of the period
     * @param paymentId Id of the payment
     * @return List of Event objects, recorded in given time period
     */
    List<CmsEvent> getEventsForPeriodAndPaymentId(@NotNull OffsetDateTime start, @NotNull OffsetDateTime end, String paymentId);

    /**
     * Returns a list of Event objects, recorded in given time period
     *
     * @param start     First date of the period
     * @param end       Last date of the period
     * @param eventType The searched type of the events
     * @return List of Event objects, recorded in given time period
     */
    List<CmsEvent> getEventsForPeriodAndEventType(@NotNull OffsetDateTime start, @NotNull OffsetDateTime end, EventType eventType);

}
