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

package de.adorsys.psd2.consent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AspspConsentDataRemoteUrls {
    private static final String ASPSP_CONSENT_DATA_ENDPOINT = "/aspsp-consent-data/consents/{consent-id}";

    @Value("${consent-service.baseurl:http://localhost:38080/api/v1}")
    private String consentServiceBaseUrl;

    /**
     * Returns URL-string to CMS endpoint that gets aspsp consent data by consent ID / payment ID
     *
     * @return String
     */
    public String getAspspConsentData() {
        return consentServiceBaseUrl + ASPSP_CONSENT_DATA_ENDPOINT;
    }

    /**
     * Returns URL-string to CMS endpoint that updates aspsp consent data by consent ID / payment ID
     *
     * @return String
     */
    public String updateAspspConsentData() {
        return consentServiceBaseUrl + ASPSP_CONSENT_DATA_ENDPOINT;
    }

    /**
     * Returns URL-string to CMS endpoint that delete aspsp consent data by consent ID / payment ID
     *
     * @return String
     */
    public String deleteAspspConsentData() {
        return consentServiceBaseUrl + ASPSP_CONSENT_DATA_ENDPOINT;
    }
}
