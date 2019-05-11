# Release notes v.2.6

## Enriched AccountReference validation
From now on, while creating the consent or payment, every account reference must have only one attribute (IBAN, BBAN,
PAN etc). If there are several attributes in the initial JSON body - the `400 FORMAT ERROR` is returned. The list of
bank supportable attributes is located in the bank_profile ASPSP configuration file (`supportedAccountReferenceFields`
parameter).
