package de.adorsys.psd2.xs2a.service.payment.sca;

import de.adorsys.psd2.xs2a.core.consent.AspspConsentData;
import de.adorsys.psd2.xs2a.core.pis.TransactionStatus;
import de.adorsys.psd2.xs2a.core.profile.PaymentType;
import de.adorsys.psd2.xs2a.core.psu.PsuIdData;
import de.adorsys.psd2.xs2a.core.tpp.TppInfo;
import de.adorsys.psd2.xs2a.core.tpp.TppRole;
import de.adorsys.psd2.xs2a.domain.ErrorHolder;
import de.adorsys.psd2.xs2a.domain.MessageErrorCode;
import de.adorsys.psd2.xs2a.domain.pis.*;
import de.adorsys.psd2.xs2a.service.context.SpiContextDataProvider;
import de.adorsys.psd2.xs2a.service.mapper.psd2.ServiceType;
import de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers.*;
import de.adorsys.psd2.xs2a.spi.domain.SpiContextData;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiBulkPayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiPaymentInfo;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiPeriodicPayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiBulkPaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiPaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiPeriodicPaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiSinglePaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.psu.SpiPsuData;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponseStatus;
import de.adorsys.psd2.xs2a.spi.service.BulkPaymentSpi;
import de.adorsys.psd2.xs2a.spi.service.CommonPaymentSpi;
import de.adorsys.psd2.xs2a.spi.service.PeriodicPaymentSpi;
import de.adorsys.psd2.xs2a.spi.service.SinglePaymentSpi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScaPaymentServiceTest {
    private static final String PAYMENT_ID = "d6cb50e5-bb88-4bbf-a5c1-42ee1ed1df2c";
    private static final String ASPSP_ACCOUNT_ID = "3278921mxl-n2131-13nw";
    private static final String PRODUCT = "sepa-credit-transfers";
    private static final String TEST_ASPSP_DATA = "Test aspsp data";
    private static final PsuIdData PSU_DATA = new PsuIdData("psuId", "psuIdType", "psuCorporateId", "psuCorporateIdType");
    private static final AspspConsentData ASPSP_CONSENT_DATA = new AspspConsentData(new byte[16], "some consent id");
    private static final SpiContextData SPI_CONTEXT_DATA = getSpiContextData();
    private static final TppInfo TPP_INFO = buildTppInfo();
    //SinglePayment
    private static final SinglePayment SINGLE_PAYMENT = new SinglePayment();
    private static final SpiSinglePayment SPI_SINGLE_PAYMENT = new SpiSinglePayment(PRODUCT);
    private static final SpiSinglePaymentInitiationResponse SPI_SINGLE_PAYMENT_RESPONSE = buildSpiSinglePaymentInitiationResponse();
    private static final SpiResponse<SpiSinglePaymentInitiationResponse> SPI_SINGLE_RESPONSE = buildSpiResponse(SPI_SINGLE_PAYMENT_RESPONSE);
    private static final SinglePaymentInitiationResponse SINGLE_PAYMENT_RESPONSE = new SinglePaymentInitiationResponse();
    //PeriodicPayment
    private static final PeriodicPayment PERIODIC_PAYMENT = new PeriodicPayment();
    private static final SpiPeriodicPayment SPI_PERIODIC_PAYMENT = new SpiPeriodicPayment(PRODUCT);
    private static final SpiPeriodicPaymentInitiationResponse SPI_PERIODIC_PAYMENT_RESPONSE = buildSpiPeriodicPaymentInitiationResponse();
    private static final SpiResponse<SpiPeriodicPaymentInitiationResponse> SPI_PERIODIC_RESPONSE = buildSpiResponse(SPI_PERIODIC_PAYMENT_RESPONSE);
    private static final PeriodicPaymentInitiationResponse PERIODIC_PAYMENT_RESPONSE = new PeriodicPaymentInitiationResponse();
    //BulkPayment
    private static final BulkPayment BULK_PAYMENT = buildBulkPayment(SINGLE_PAYMENT);
    private static final SpiBulkPayment SPI_BULK_PAYMENT = new SpiBulkPayment();
    private static final SpiBulkPaymentInitiationResponse SPI_BULK_PAYMENT_RESPONSE = buildSpiBulkPaymentInitiationResponse();
    private static final SpiResponse<SpiBulkPaymentInitiationResponse> SPI_BULK_RESPONSE = buildSpiResponse(SPI_BULK_PAYMENT_RESPONSE);
    private static final BulkPaymentInitiationResponse BULK_PAYMENT_RESPONSE = new BulkPaymentInitiationResponse();
    //CommonPayment
    private static final CommonPayment COMMON_PAYMENT = buildCommonPayment();
    private static final SpiPaymentInfo SPI_PAYMENT_INFO = new SpiPaymentInfo(PRODUCT);
    private static final SpiResponse<SpiPaymentInitiationResponse> SPI_COMMON_RESPONSE = buildSpiResponse(SPI_SINGLE_PAYMENT_RESPONSE);
    private static final CommonPaymentInitiationResponse COMMON_PAYMENT_RESPONSE = new CommonPaymentInitiationResponse();

    @InjectMocks
    private RedirectScaPaymentService scaPaymentService;

    @Mock
    private SinglePaymentSpi singlePaymentSpi;
    @Mock
    private PeriodicPaymentSpi periodicPaymentSpi;
    @Mock
    private BulkPaymentSpi bulkPaymentSpi;
    @Mock
    private CommonPaymentSpi commonPaymentSpi;
    @Mock
    private Xs2aToSpiSinglePaymentMapper xs2AToSpiSinglePaymentMapper;
    @Mock
    private Xs2aToSpiPeriodicPaymentMapper xs2aToSpiPeriodicPaymentMapper;
    @Mock
    private Xs2aToSpiBulkPaymentMapper xs2aToSpiBulkPaymentMapper;
    @Mock
    private Xs2aToSpiPaymentInfo xs2aToSpiPaymentInfo;
    @Mock
    private SpiToXs2aPaymentMapper spiToXs2aPaymentMapper;
    @Mock
    private SpiContextDataProvider spiContextDataProvider;
    @Mock
    private SpiErrorMapper spiErrorMapper;

    @Before
    public void init() {
        when(spiContextDataProvider.provideWithPsuIdData(PSU_DATA)).thenReturn(SPI_CONTEXT_DATA);
    }

    @Test
    public void createSinglePayment_success() {
        //Given
        when(xs2AToSpiSinglePaymentMapper.mapToSpiSinglePayment(SINGLE_PAYMENT, PRODUCT))
            .thenReturn(SPI_SINGLE_PAYMENT);
        when(singlePaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_SINGLE_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(SPI_SINGLE_RESPONSE);
        when(spiToXs2aPaymentMapper.mapToPaymentInitiateResponse(eq(SPI_SINGLE_PAYMENT_RESPONSE), eq(SPI_SINGLE_RESPONSE.getAspspConsentData())))
            .thenReturn(SINGLE_PAYMENT_RESPONSE);

        //When
        SinglePaymentInitiationResponse actualResponse = scaPaymentService.createSinglePayment(SINGLE_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isNull();
        assertThat(actualResponse).isEqualTo(SINGLE_PAYMENT_RESPONSE);
    }

    @Test
    public void createSinglePayment_singlePaymentSpi_initiatePayment_failed() {
        //Given
        SpiResponse<SpiSinglePaymentInitiationResponse> expectedFailureResponse = SpiResponse.<SpiSinglePaymentInitiationResponse>builder()
            .aspspConsentData(AspspConsentData.emptyConsentData().respondWith(TEST_ASPSP_DATA.getBytes()))
            .fail(SpiResponseStatus.LOGICAL_FAILURE);
        ErrorHolder expectedError = ErrorHolder.builder(MessageErrorCode.RESOURCE_UNKNOWN_404)
            .messages(Collections.singletonList("Payment not found"))
            .build();

        when(xs2AToSpiSinglePaymentMapper.mapToSpiSinglePayment(SINGLE_PAYMENT, PRODUCT))
            .thenReturn(SPI_SINGLE_PAYMENT);
        when(singlePaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_SINGLE_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(expectedFailureResponse);
        when(spiErrorMapper.mapToErrorHolder(expectedFailureResponse, ServiceType.PIS))
            .thenReturn(expectedError);

        //When
        SinglePaymentInitiationResponse actualResponse = scaPaymentService.createSinglePayment(SINGLE_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getErrorHolder()).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isEqualToComparingFieldByField(expectedError);
    }

    @Test
    public void createPeriodicPayment_success() {
        //Given
        when(xs2aToSpiPeriodicPaymentMapper.mapToSpiPeriodicPayment(PERIODIC_PAYMENT, PRODUCT))
            .thenReturn(SPI_PERIODIC_PAYMENT);
        when(periodicPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_PERIODIC_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(SPI_PERIODIC_RESPONSE);
        when(spiToXs2aPaymentMapper.mapToPaymentInitiateResponse(eq(SPI_PERIODIC_PAYMENT_RESPONSE), eq(SPI_PERIODIC_RESPONSE.getAspspConsentData())))
            .thenReturn(PERIODIC_PAYMENT_RESPONSE);

        //When
        PeriodicPaymentInitiationResponse actualResponse = scaPaymentService.createPeriodicPayment(PERIODIC_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isNull();
        assertThat(actualResponse).isEqualTo(PERIODIC_PAYMENT_RESPONSE);
    }

    @Test
    public void createPeriodicPayment_periodicPaymentSpi_initiatePayment_failed() {
        //Given
        SpiResponse<SpiPeriodicPaymentInitiationResponse> expectedFailureResponse = SpiResponse.<SpiPeriodicPaymentInitiationResponse>builder()
            .aspspConsentData(AspspConsentData.emptyConsentData().respondWith(TEST_ASPSP_DATA.getBytes()))
            .fail(SpiResponseStatus.LOGICAL_FAILURE);
        ErrorHolder expectedError = ErrorHolder.builder(MessageErrorCode.RESOURCE_UNKNOWN_404)
            .messages(Collections.singletonList("Payment not found"))
            .build();

        when(xs2aToSpiPeriodicPaymentMapper.mapToSpiPeriodicPayment(PERIODIC_PAYMENT, PRODUCT))
            .thenReturn(SPI_PERIODIC_PAYMENT);
        when(periodicPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_PERIODIC_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(expectedFailureResponse);
        when(spiErrorMapper.mapToErrorHolder(expectedFailureResponse, ServiceType.PIS))
            .thenReturn(expectedError);

        //When
        PeriodicPaymentInitiationResponse actualResponse = scaPaymentService.createPeriodicPayment(PERIODIC_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getErrorHolder()).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isEqualToComparingFieldByField(expectedError);
    }

    @Test
    public void createBulkPayment_success() {
        //Given
        when(xs2aToSpiBulkPaymentMapper.mapToSpiBulkPayment(BULK_PAYMENT, PRODUCT))
            .thenReturn(SPI_BULK_PAYMENT);
        when(bulkPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_BULK_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(SPI_BULK_RESPONSE);
        when(spiToXs2aPaymentMapper.mapToPaymentInitiateResponse(eq(SPI_BULK_PAYMENT_RESPONSE), eq(SPI_PERIODIC_RESPONSE.getAspspConsentData())))
            .thenReturn(BULK_PAYMENT_RESPONSE);

        //When
        BulkPaymentInitiationResponse actualResponse = scaPaymentService.createBulkPayment(BULK_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isNull();
        assertThat(actualResponse).isEqualTo(BULK_PAYMENT_RESPONSE);
    }

    @Test
    public void createBulkPayment_bulkPaymentSpi_initiatePayment_failed() {
        //Given
        SpiResponse<SpiBulkPaymentInitiationResponse> expectedFailureResponse = SpiResponse.<SpiBulkPaymentInitiationResponse>builder()
            .aspspConsentData(AspspConsentData.emptyConsentData().respondWith(TEST_ASPSP_DATA.getBytes()))
            .fail(SpiResponseStatus.LOGICAL_FAILURE);
        ErrorHolder expectedError = ErrorHolder.builder(MessageErrorCode.RESOURCE_UNKNOWN_404)
            .messages(Collections.singletonList("Payment not found"))
            .build();

        when(xs2aToSpiBulkPaymentMapper.mapToSpiBulkPayment(BULK_PAYMENT, PRODUCT))
            .thenReturn(SPI_BULK_PAYMENT);
        when(bulkPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_BULK_PAYMENT, AspspConsentData.emptyConsentData()))
            .thenReturn(expectedFailureResponse);
        when(spiErrorMapper.mapToErrorHolder(expectedFailureResponse, ServiceType.PIS))
            .thenReturn(expectedError);

        //When
        BulkPaymentInitiationResponse actualResponse = scaPaymentService.createBulkPayment(BULK_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getErrorHolder()).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isEqualToComparingFieldByField(expectedError);
    }

    @Test
    public void createCommonPayment_success() {
        when(spiContextDataProvider.provide(PSU_DATA, TPP_INFO))
            .thenReturn(SPI_CONTEXT_DATA);
        when(xs2aToSpiPaymentInfo.mapToSpiPaymentRequest(COMMON_PAYMENT, PRODUCT))
            .thenReturn(SPI_PAYMENT_INFO);
        when(commonPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_PAYMENT_INFO, AspspConsentData.emptyConsentData()))
            .thenReturn(SPI_COMMON_RESPONSE);
        when(spiToXs2aPaymentMapper.mapToCommonPaymentInitiateResponse(SPI_COMMON_RESPONSE.getPayload(), COMMON_PAYMENT.getPaymentType(), SPI_COMMON_RESPONSE.getAspspConsentData()))
            .thenReturn(COMMON_PAYMENT_RESPONSE);

        //When
        CommonPaymentInitiationResponse actualResponse = scaPaymentService.createCommonPayment(COMMON_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isNull();
        assertThat(actualResponse).isEqualTo(COMMON_PAYMENT_RESPONSE);
    }

    @Test
    public void createCommonPayment_commonPaymentSpi_initiatePayment_failed() {
        //Given
        SpiResponse<SpiPaymentInitiationResponse> expectedFailureResponse = SpiResponse.<SpiPaymentInitiationResponse>builder()
            .aspspConsentData(AspspConsentData.emptyConsentData().respondWith(TEST_ASPSP_DATA.getBytes()))
            .fail(SpiResponseStatus.LOGICAL_FAILURE);
        ErrorHolder expectedError = ErrorHolder.builder(MessageErrorCode.RESOURCE_UNKNOWN_404)
            .messages(Collections.singletonList("Payment not found"))
            .build();

        when(spiContextDataProvider.provide(PSU_DATA, TPP_INFO))
            .thenReturn(SPI_CONTEXT_DATA);
        when(xs2aToSpiPaymentInfo.mapToSpiPaymentRequest(COMMON_PAYMENT, PRODUCT))
            .thenReturn(SPI_PAYMENT_INFO);
        when(commonPaymentSpi.initiatePayment(SPI_CONTEXT_DATA, SPI_PAYMENT_INFO, AspspConsentData.emptyConsentData()))
            .thenReturn(expectedFailureResponse);
        when(spiErrorMapper.mapToErrorHolder(expectedFailureResponse, ServiceType.PIS))
            .thenReturn(expectedError);

        //When
        CommonPaymentInitiationResponse actualResponse = scaPaymentService.createCommonPayment(COMMON_PAYMENT, TPP_INFO, PRODUCT, PSU_DATA);

        //Then
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getErrorHolder()).isNotNull();
        assertThat(actualResponse.getErrorHolder()).isEqualToComparingFieldByField(expectedError);
    }

    private static SpiContextData getSpiContextData() {
        return new SpiContextData(
            new SpiPsuData("psuId", "psuIdType", "psuCorporateId", "psuCorporateIdType"),
            new TppInfo(),
            UUID.randomUUID()
        );
    }

    private static TppInfo buildTppInfo() {
        TppInfo tppInfo = new TppInfo();
        tppInfo.setAuthorisationNumber("registrationNumber");
        tppInfo.setTppName("tppName");
        tppInfo.setTppRoles(Collections.singletonList(TppRole.PISP));
        tppInfo.setAuthorityId("authorityId");
        return tppInfo;
    }

    private static BulkPayment buildBulkPayment(SinglePayment singlePayment) {
        BulkPayment bulkPayment = new BulkPayment();
        bulkPayment.setPayments(Collections.singletonList(singlePayment));
        bulkPayment.setRequestedExecutionDate(LocalDate.now());
        bulkPayment.setBatchBookingPreferred(false);
        return bulkPayment;
    }

    private static CommonPayment buildCommonPayment() {
        CommonPayment request = new CommonPayment();
        request.setPaymentType(PaymentType.SINGLE);
        request.setPaymentProduct("sepa-credit-transfers");
        request.setPaymentData(new byte[16]);
        request.setTppInfo(TPP_INFO);
        return request;
    }

    private static SpiSinglePaymentInitiationResponse buildSpiSinglePaymentInitiationResponse() {
        SpiSinglePaymentInitiationResponse response = new SpiSinglePaymentInitiationResponse();
        response.setPaymentId(PAYMENT_ID);
        response.setTransactionStatus(TransactionStatus.RCVD);
        response.setAspspAccountId(ASPSP_ACCOUNT_ID);
        return response;
    }

    private static SpiPeriodicPaymentInitiationResponse buildSpiPeriodicPaymentInitiationResponse() {
        SpiPeriodicPaymentInitiationResponse response = new SpiPeriodicPaymentInitiationResponse();
        response.setPaymentId(PAYMENT_ID);
        response.setTransactionStatus(TransactionStatus.RCVD);
        response.setAspspAccountId(ASPSP_ACCOUNT_ID);
        return response;
    }

    private static SpiBulkPaymentInitiationResponse buildSpiBulkPaymentInitiationResponse() {
        SpiBulkPaymentInitiationResponse response = new SpiBulkPaymentInitiationResponse();
        response.setPaymentId(PAYMENT_ID);
        response.setTransactionStatus(TransactionStatus.RCVD);
        response.setAspspAccountId(ASPSP_ACCOUNT_ID);
        return response;
    }

    private static <T> SpiResponse<T> buildSpiResponse(T payload) {
        return SpiResponse.<T>builder()
            .payload(payload)
            .aspspConsentData(ASPSP_CONSENT_DATA)
            .success();
    }
}
