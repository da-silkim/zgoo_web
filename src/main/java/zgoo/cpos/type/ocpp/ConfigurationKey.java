package zgoo.cpos.type.ocpp;

public enum ConfigurationKey {
    // == Core Profile == //
    AllowOfflineTxForUnknownId("Unknown Offline Authorization의 지원 여부"),
    AuthorizationCacheEnabled("Authorization Cache의 지원 여부"),
    AuthorizeRemoteTxRequests("원격으로 시작된 트랜잭션에도 로컬에서 시작한것 처럼 미리 Authorize 해야하는 지의 여부"),
    BlinkRepeat("신호를 보낼 때 충전기가 조명을 깜박이는 횟수(times)"),
    ClockAlignedDataInterval("clock-aligned data 전송 간격(seconds)"),
    ConnectionTimeOut("충전을 시작하고 운전자가 커넥터를 차량에 삽입하지 못해 초기 상태로 돌아갈때 까지의 간격(seconds)"),
    ConnectorPhaseRotation("커넥터 당 phase rotation"),
    ConnectorPhaseRotationMaxLength("ConnectorPhaseRotation Configuration Key의 최대 항목 수"),
    GetConfigurationMaxKeys("GetConfiguration에서 설정할 수 있는 구성키의 최대 수"),
    HeartbeatInterval("Heartbeat를 전송해야 하는 간격(seconds)"),
    LightIntensity("충전기 조명 강도(%)"),
    LocalAuthorizeOffline("오프라인에서, 로컬로 인증된 식별자에 대한 트랜잭션의 시작 여부"),
    LocalPreAuthorize("온라인 상태에서, Authorize.conf를 기다리거나 요청하지 않고 로컬로 인증된 식별자에 대한 트랜잭션을 시작할지의 여부"),
    MaxEnergyOnInvalidId("트랜잭션이 시작된 후 중앙 시스템에 의해 식별자가 무효화될 때 차량이 충전할 수 있는 최대 에너지(Wh)"),
    MeterValuesAlignedData("ClockAlignedDataInterval 초마다 MeterValues.req PDU에 포함 될 Clock-aligned measurand"),
    MeterValuesAlignedDataMaxLength("MeterValuesAlignedData Configuration Key의 최대 항목 수"),
    MeterValuesSampledData("매 MeterValueSampleInterval 초마다 MeterValues.req PDU에 포함된 Sampled measurands."),
    MeterValuesSampledDataMaxLength("MeterValuesSampledData Configuration Key의 최대 항목 수"),
    MeterValueSampleInterval("MeterValues PDUs에 의해 전송되도록 의도된 sampling of metering (or other) data의 interval."),
    MinimumStatusDuration("StatusNotification.req PDU가 중앙 시스템으로 전송되기 전에 충전기 or 커넥터 상태가 안정화되기 까지의 minimum duration"),
    NumberOfConnectors("충전기의 물리적인 커넥터 수"),
    ResetRetries("충전기가 성공하지 못한 reset에 대한 시도 횟수"),
    StopTransactionOnEVSideDisconnect("충전기는 EV에서 케이블을 분리할 때 트랜잭션을 administratively stop해야 한다"),
    StopTransactionOnInvalidId(
            "해당 트랜잭션에 대한 StartTransaction.conf에서 non- Accepted authorization status를 받을 때 충전기가 진행 중인 트랜잭션을 중지할지의 여부"),
    StopTxnAlignedData(
            "트랜잭션의 모든 ClockAlignedDataInterval에 대해 StopTransaction.req MeterValues.req의 TransactionData element에 포함될 Clock-aligned periodic measurand"),
    StopTxnAlignedDataMaxLength("StopTxnAlignedData Configuration Key의 최대 항목 수"),
    StopTxnSampledData(
            "charging session 시작 후 매 MeterValueSampleInterval 초마다 StopTransaction.req PDU의 TransactionData element에 포함 될 Sampled measurands"),
    StopTxnSampledDataMaxLength("StopTxnSampledData Configuration Key의 최대 항목 수"),
    SupportedFeatureProfiles("지원되는 Feature Profiles의 목록."),
    SupportedFeatureProfilesMaxLength("SupportedFeatureProfiles Configuration Key의 최대 항목 수"),
    TransactionMessageAttempts("중앙 시스템이 transaction-related message를 처리하지 못한 경우 충전기에서 해당 메시지를 submit 하려고 시도하는 횟수"),
    TransactionMessageRetryInterval("중앙 시스템에서 처리하지 못한 transaction-related message를 재전송 하기 전에 충전기가 대기해야 하는 시간"),
    UnlockConnectorOnEVSideDisconnect("충전기는 케이블을 EV에서 분리할 때 충전기 쪽의 케이블을 잠금 해제해야 한다."),
    WebSocketPingInterval("ping 사이의 시간(초)"),

    // == Local Auth List Management Profile == //
    LocalAuthListEnabled("Local Authorization List의 사용 여부"),
    LocalAuthListMaxLength("Local Authorization List에 저장할 수 있는 최대 ID의 수"),
    SendLocalListMaxLength("single SendLocalList.req에서 보낼 수 있는 최대 ID의 수"),

    // == Reservation Profile == //
    ReserveConnectorZeroSupported("충전기에서 connector 0에 대한 예약 지원"),

    // == Smart Charging Profile == //
    ChargeProfileMaxStackLevel("ChargingProfile의 최대 StackLevel"),
    ChargingScheduleAllowedChargingRateUnit("ChargingSchedule에서 사용할 수 있는 supported quantities의 목록"),
    ChargingScheduleMaxPeriods("ChargingSchedule당 정의할 수 있는 최대 periods 수"),
    ConnectorSwitch3to1PhaseSupported("충전기가 트랜잭션 중에 3 phase에서 1 phase로 전환하는 것을 지원"),
    MaxChargingProfilesInstalled("한 번에 설정 할 수 있는 최대 Charging profiles 수"),

    // == Security == //
    AdditionalRootCertificateCheck("true로 설정하면 인증서 유형이 CentralSystemRootCertificate인 인증서를 한 번에 하나만 설치할 수 있습니다."),
    AuthorizationKey("최소 길이는 16바이트. 무작위로 생성된 바이너리. 16진수 표시(최대 20바이트, 최대 40자리 16진수 문자열로 표시)"),
    CertificateSignedMaxChainSize("CertificateSigned.req PDU에서 'certificateChain' 필드의 크기를 제한할 수 있습니다. 최대 10,000자로 제한"),
    CertificateStoreMaxLength("충전기에 설치할 수 있는 최대 Root/CA certificates 수"),
    CpoName("충전소 인증서에 사용된 CPO 이름, SignCertificate.req를 통해 CSR 전송에 사용할 CPO 이름"),
    SecurityProfile("충전소에서 사용하는 보안 프로필을 설정하는 데 사용. 기본값: 0"),

    BasicAuthPassword("뭔지 모르겠넹"),
    NotSupportedKey("지원 안하는 키");

    private final String description;

    ConfigurationKey(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
