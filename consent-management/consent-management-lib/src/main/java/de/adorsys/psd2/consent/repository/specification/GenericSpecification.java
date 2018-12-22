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

package de.adorsys.psd2.consent.repository.specification;

import de.adorsys.psd2.consent.domain.PsuData;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.Join;

import static de.adorsys.psd2.consent.repository.specification.EntityAttribute.*;
import static de.adorsys.psd2.consent.repository.specification.EntityAttributeSpecificationProvider.provideSpecificationForEntityAttribute;

public abstract class GenericSpecification {

    public <T> Specification<T> byPsuIdIdAndInstanceId(String psuId, String instanceId) {
        Specification<T> aisConsentSpecification = (root, query, cb) -> {
            Join<T, PsuData> aisConsentPsuDataJoin = root.join(PSU_DATA_ATTRIBUTE);
            return cb.equal(aisConsentPsuDataJoin.get(PSU_ID_ATTRIBUTE), psuId);
        };
        return Specifications.where(aisConsentSpecification)
                   .and(provideSpecificationForEntityAttribute(INSTANCE_ID_ATTRIBUTE, instanceId));
    }
}