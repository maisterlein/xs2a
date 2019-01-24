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

package de.adorsys.psd2.xs2a.service.mapper.psd2;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static de.adorsys.psd2.xs2a.service.mapper.psd2.ErrorType.*;
import static de.adorsys.psd2.xs2a.service.mapper.psd2.ServiceType.PIS;

@Component
public class ServiceTypeToErrorTypeMapper {
    private final static Map<ServiceType, Map<Integer, ErrorType>> serviceTypeToHttpCodeAndErrorType;

    static {
        Map<Integer, ErrorType> pisHttpCodeToErrorType = new HashMap<>();
        pisHttpCodeToErrorType.put(400, PIS_400);
        pisHttpCodeToErrorType.put(401, PIS_401);
        pisHttpCodeToErrorType.put(403, PIS_403);
        pisHttpCodeToErrorType.put(404, PIS_404);
        pisHttpCodeToErrorType.put(405, PIS_405);
        pisHttpCodeToErrorType.put(409, PIS_409);

        // TODO do the same for AIS and PIIS

        serviceTypeToHttpCodeAndErrorType = new HashMap<>();
        serviceTypeToHttpCodeAndErrorType.put(PIS, pisHttpCodeToErrorType);
    }

    public ErrorType mapToErrorType(ServiceType serviceType, int httpCode) {
        return Optional.ofNullable(serviceTypeToHttpCodeAndErrorType.get(serviceType))
                   .map(m -> m.get(httpCode))
                   .orElse(null);
    }
}
