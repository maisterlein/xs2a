/**
 * XS2A REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0
 * Contact: fpo@adorsys.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { Model49 } from './model49';


/**
 * Address
 */
export interface Address {
    /**
     * Building number
     */
    buildingNumber?: string;
    /**
     * City
     */
    city?: string;
    /**
     * Country
     */
    country: Model49;
    /**
     * Postal code
     */
    postalCode?: string;
    /**
     * Street
     */
    street?: string;
}