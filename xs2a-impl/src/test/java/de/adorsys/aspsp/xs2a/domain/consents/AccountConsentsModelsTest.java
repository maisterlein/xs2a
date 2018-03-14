package de.adorsys.aspsp.xs2a.domain.consents;

import com.google.gson.Gson;
import de.adorsys.aspsp.xs2a.spi.domain.AccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.ais.consents.AccountAccess;
import de.adorsys.aspsp.xs2a.spi.domain.ais.consents.CreateConsentReq;
import de.adorsys.aspsp.xs2a.spi.utils.DateUtil;
import de.adorsys.aspsp.xs2a.utils.FileUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountConsentsModelsTest {
    private final String AIC_REQUEST_PATH = "json/AccountInformationConsentRequestTest.json";
    private final String AIC_AVAILABLE_ACCOUNTS_REQUEST_PATH = "json/AicAvailableAccountsRequestTest.json";
    private final String AIC_NO_DEDICATE_REQUEST_PATH = "json/AicNoDedicateAccountTest.json";

    @Test
    public void aicRequest_jsonTest() throws IOException {
        //Given:
        String aicRequestJson = FileUtil.getStringFromFile(AIC_REQUEST_PATH);
        CreateConsentReq expectedAICRequest = getAICRequestTest();
        
        //When:
        CreateConsentReq actualAICRequest = new Gson().fromJson(aicRequestJson, CreateConsentReq.class);
        
        //Then:
        assertThat(actualAICRequest).isEqualTo(expectedAICRequest);
    }

    @Test
    public void aicAvailableAccountsRequest_jsonTest() throws IOException {
        //Given:
        String aicRequestJson = getJsonString(AIC_AVAILABLE_ACCOUNTS_REQUEST_PATH);
        CreateConsentReq expectedAICRequest = getAicAvailableAccountsRequest();

        //When:
        CreateConsentReq actualAICRequest = new Gson().fromJson(aicRequestJson, CreateConsentReq.class);

        //Then:
        assertThat(actualAICRequest).isEqualTo(expectedAICRequest);
    }

    @Test
    public void aicAicNoDedicateAccountRequest_jsonTest() throws IOException {
        //Given:
        String aicRequestJson = getJsonString(AIC_NO_DEDICATE_REQUEST_PATH);
        CreateConsentReq expectedAICRequest = getAicNoDedicatedAccountRequest();

        //When:
        CreateConsentReq actualAICRequest = new Gson().fromJson(aicRequestJson, CreateConsentReq.class);

        //Then:
        assertThat(actualAICRequest).isEqualTo(expectedAICRequest);
    }

    private CreateConsentReq getAicNoDedicatedAccountRequest() {

        AccountAccess accountAccess = new AccountAccess();
        accountAccess.setBalances(new AccountReference[0]);
        accountAccess.setTransactions(new AccountReference[0]);
    
        CreateConsentReq aicRequestObj = new CreateConsentReq();
        aicRequestObj.setAccess(accountAccess);
        aicRequestObj.setRecurringIndicator(true);
        aicRequestObj.setValidUntil(DateUtil.getDateFromDateStringNoTimeZone("2017-11-01"));
        aicRequestObj.setFrequencyPerDay(4);

        return aicRequestObj;
    }

    private CreateConsentReq getAicAvailableAccountsRequest() {

        AccountAccess accountAccess = new AccountAccess();
        accountAccess.setAvailableAccounts("all-accounts");
    
        CreateConsentReq aicRequestObj = new CreateConsentReq();
        aicRequestObj.setAccess(accountAccess);
        aicRequestObj.setRecurringIndicator(false);
        aicRequestObj.setValidUntil(DateUtil.getDateFromDateStringNoTimeZone("2017-08-06"));
        aicRequestObj.setFrequencyPerDay(1);

        return aicRequestObj;
    }

    private CreateConsentReq getAICRequestTest() {

        AccountReference iban1 = new AccountReference();
        iban1.setIban("DE2310010010123456789");
        
        AccountReference iban2 = new AccountReference();
        iban2.setIban("DE2310010010123456790");
        iban2.setCurrency(Currency.getInstance("USD"));
        
        AccountReference iban3 = new AccountReference();
        iban3.setIban("DE2310010010123456788");
        
        AccountReference iban4 = new AccountReference();
        iban4.setIban("DE2310010010123456789");
        
        AccountReference maskedPan = new AccountReference();
        maskedPan.setMaskedPan("123456xxxxxx1234");
        
        AccountReference[] balances = (AccountReference[]) Arrays.asList(iban1, iban2, iban3).toArray();
        AccountReference[] transactions = (AccountReference[]) Arrays.asList(iban4, maskedPan).toArray();
        
        AccountAccess accountAccess = new AccountAccess();
        accountAccess.setBalances(balances);
        accountAccess.setTransactions(transactions);
        
        CreateConsentReq aicRequestObj = new CreateConsentReq();
        aicRequestObj.setAccess(accountAccess);
        aicRequestObj.setRecurringIndicator(true);
        aicRequestObj.setValidUntil(DateUtil.getDateFromDateStringNoTimeZone("2017-11-01"));
        aicRequestObj.setFrequencyPerDay(4);
        
        return aicRequestObj;
    }
}
