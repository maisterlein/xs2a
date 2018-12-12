Feature: Account Information Service - Embedded approach
#
#    ####################################################################################################################
#    #                                                                                                                  #
#    # Consent Requests                                                                                                 #
#    #                                                                                                                  #
#    ####################################################################################################################
    Scenario Outline: Successful consent creation with explicit start of authorisation
        Given PSU wants to create a consent <consent-resource>
        And  ASPSP-profile contains parameter signingBasketSupported is <signingBasketSupported>
        And parameter TPP-Explicit-Authorisation-Preferred is <auth-preferred>
        When PSU sends the create consent request
        Then a successful response code and the appropriate consent response data is delivered to the PSU
        And response contains link startAuthorisation
        Examples:
            | consent-resource                     | signingBasketSupported | auth-preferred |
            | consent-dedicated-successful.json    |         true           |     true       |
            | consent-all-accounts-successful.json |         true           |     true       |

    Scenario Outline: Successful consent creation with implicit start of authorisation
        Given PSU wants to create a consent <consent-resource>
        And  ASPSP-profile contains parameter signingBasketSupported is <signingBasketSupported>
        And parameter TPP-Explicit-Authorisation-Preferred is <auth-preferred>
        When PSU sends the create consent request
        Then a successful response code and the appropriate consent response data is delivered to the PSU
        And response contains link startAuthorisationWIthPsuAuthentication
        Examples:
            | consent-resource                     | signingBasketSupported | auth-preferred |
            | consent-dedicated-successful.json    |         false          |     true       |
            | consent-dedicated-successful.json    |         true           |     false      |
            | consent-dedicated-successful.json    |         false          |     false      |
            | consent-dedicated-successful.json    |         true           |     null       |
            | consent-dedicated-successful.json    |         false          |     null       |
            | consent-all-accounts-successful.json |         false          |     true       |
            | consent-all-accounts-successful.json |         true           |     false      |
            | consent-all-accounts-successful.json |         false          |     false      |
            | consent-all-accounts-successful.json |         true           |     null       |
            | consent-all-accounts-successful.json |         false          |     null       |
    @SASRCC
    Scenario Outline: Successful Authorisation Sub-Resources request for consent creation (embedded)
        Given PSU created a consent resource <consent-id>
        And PSU wants to start the authorisation for consent creation using the authorisation data <authorisation-data>
        And consent authorisation is started
        And another consent authorisation is started for this consent
        And AISP wants to get all Authorisation Sub-Resources
        When AISP requests Authorisation Sub-Resources
        Then a successful response code and the array of authorisationIds in response gets returned

        Examples:
            |consent-id                             |authorisation-data                |
            |consent-all-accounts-successful.json   |startAuth-successful.json |
            |consent-all-accounts-successful.json   |consent-all-Auth-successful.json |

