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

package de.adorsys.psd2.xs2a.service.discovery;

import de.adorsys.psd2.xs2a.service.mapper.psd2.ServiceType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.ServletContext;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceTypeDiscoveryServiceTest {

    private MockHttpServletRequest request;
    private ServiceTypeDiscoveryService cut;


    @Test
    public void getServiceType() {
        request = new MockHttpServletRequest("GET", "/v1/consents");
        cut = new ServiceTypeDiscoveryService(request);
        ServiceType result = cut.getServiceType();

        assertEquals("AIS", result.name());
    }

    @Test
    public void getServiceTypeWithContextPath() {
        request = new MockHttpServletRequest("GET", "/xs2a/v1/consents");
        request.setContextPath("/xs2a");
        cut = new ServiceTypeDiscoveryService(request);
        ServiceType result = cut.getServiceType();

        assertEquals("AIS", result.name());
    }
}
