document.addEventListener('DOMContentLoaded', function () {
    // doughnt chart
    var ctx = document.getElementById("doughnutChart").getContext('2d');
    var doughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [
                `${i18n.testRun}(${opStatus.opTestPer}%)`,
                `${i18n.operating}(${opStatus.operatingPer}%)`,
                `${i18n.operationStop}(${opStatus.opStopPer}%)`
            ],
            datasets: [{
                data: [opStatus.opTestCount, opStatus.operatingCount, opStatus.opStopCount],
                backgroundColor: ['#EB5B00', '#144693', '#d9d9d9'],
                hoverBackgroundColor: ['#EF9651', '#5073A8', '#E8EAEE'],
                borderWidth: 0,
            }]
        },
        options: {
            responsive: true, // 반응형 옵션 추가
            maintainAspectRatio: false, // 비율 유지 비활성화
            aspectRatio: 1, // 너비:높이 비율 설정 (정사각형)
            plugins: {
                legend: {
                    position: 'bottom', // 범례 위치 설정
                    align: 'center', // 범례 정렬
                },
            },
            // layout: {
            //     padding: {
            //         top: 10,
            //         bottom: 10
            //     }
            // }
        }
    });

    // bar chart
    var sido = [];
    var speedFastCount = [];
    var speedLowCount = [];
    var speedDespnCount = [];

    chargerCountList.forEach(item => {
        sido.push(item.sido);
        speedFastCount.push(item.speedFastCount);
        speedLowCount.push(item.speedLowCount);
        speedDespnCount.push(item.speedDespnCount);
    });

    ctx = document.getElementById("barChart").getContext('2d');
    var barChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: sido,
            datasets: [{
                label: i18n.fast,
                data: speedFastCount,
                backgroundColor: ['#144693']
            }, {
                label: i18n.slow,
                data: speedLowCount,
                backgroundColor: ['#26C59E']
            }, {
                label: i18n.dispenser,
                data: speedDespnCount,
                backgroundColor: ['#FFD36E']
            },]
        },
        options: {
            responsive: true,    // 반응형 옵션 추가
            maintainAspectRatio: false, // 종횡비 유지
            maxBarThickness: 50, // 막대의 최대 너비 설정
            plugins: {
                legend: {
                    position: 'top', // 범례 위치 설정
                    align: 'center',   // 범례 정렬
                    maxWidth: 120,     // 범례의 최대 너비 설정
                    labels: {
                        font: {
                            size: 14   // 범례 폰트 크기 조정
                        }
                    }
                },
            },
            scales: {
                x: {
                    stacked: true,
                    ticks: {
                        color: '#000',
                        font: {
                            size: 14,
                        }
                    },
                },
                y: {
                    min: 0,
                    stacked: true,
                    ticks: {
                        color: '#000',
                        font: {
                            size: 15,
                            weight: 'bold',
                            lineHeight: 1.2,
                        }
                    },
                },
            },
            onClick: (e, elements) => {
                if (elements.length > 0) {
                    const element = elements[0];
                    const datasetIndex = element.datasetIndex;
                    const index = element.index;
                    const dataset = barChart.data.datasets[datasetIndex];
                    const label = barChart.data.labels[index];
                    getFacilityList(label, dataset.label);
                }
            }
        },
    });

    function getFacilityList(sido, type) {
        $.ajax({
            url: '/charger/get/facility',
            method: 'GET',
            data: { sido: sido, type: type },
            success: function (response) {
                document.getElementById('facilityText').innerHTML = `${sido} / ${type}`;
                drawPieChart(response);
            },
            error: function (error) {
                console.error(error);
            }
        });
    }

    // treemap chart
    const treemapData = [
        { what: i18n.available, value: connStatus.availableCount, color: '#28a745' },
        { what: i18n.preparing, value: connStatus.preparingCount, color: '#ffc107' },
        { what: i18n.charging, value: connStatus.chargingCount, color: '#007bff' },
        { what: i18n.chargingError, value: connStatus.suspendedEvCount, color: '#A9A3A3' },
        { what: i18n.chargerError, value: connStatus.suspendedEvseCount, color: '#DBDBDB' },
        { what: i18n.finishing, value: connStatus.finishingCount, color: '#EC5228' },
        { what: i18n.reserved, value: connStatus.reservedCount, color: '#702EC3' },
        { what: i18n.unavailable, value: connStatus.unavailableCount, color: '#BF3131' },
        { what: i18n.faulted, value: connStatus.faultedCount, color: '#dc3545' },
        { what: i18n.disconnected, value: connStatus.disconnectedCount, color: '#A9A3A3' },
    ];
    var ctx = document.getElementById("treeChart").getContext('2d');
    var treemapChart = new Chart(ctx, {
        type: 'treemap',
        data: {
            datasets: [{
                label: 'Charge',
                tree: treemapData,
                key: 'value',
                borderWidth: 0,
                borderRadius: 6,
                spacing: 1,
                backgroundColor(ctx) {
                    if (ctx.type !== 'data') {
                        return 'transparent';
                    }
                    return ctx.raw._data.color;
                },
                labels: {
                    align: 'center',
                    display: true,
                    formatter(ctx) {
                        if (ctx.type !== 'data') {
                            return;
                        }
                        return [ctx.raw.v, ctx.raw._data.what];
                    },
                    color: ['white', 'whiteSmoke'],
                    font: [{ size: 24, weight: 'bold' }, { size: 16 }],
                    position: 'center'
                },
            }]
        },
        options: {
            responsive: true, // 반응형 옵션 추가
            maintainAspectRatio: false,
            events: [],
            plugins: {
                legend: {
                    display: false  // 범례 제거
                },
                tooltip: {
                    enabled: false,
                }
            },
        }
    });

    let pieChart = null;

    // pie chart
    drawPieChart(facilityList);
    function drawPieChart(facilityList) {
        var facility = [];
        var faData = [];

        facilityList.forEach(item => {
            facility.push(item.facility);
            faData.push(item.count);
        });

        var ctx = document.getElementById("pieChart").getContext('2d');
        const Utils = {
            PIE_CHART_COLORS: {
                p1: 'rgb(54, 48, 98)',
                p2: 'rgb(77, 76, 125)',
                p3: 'rgb(130, 115, 151)',
                p4: 'rgb(216, 185, 195)',
                p5: 'rgb(93, 93, 93)',
                p6: 'rgb(217, 217, 217)',
            }
        };

        if (pieChart !== null) {
            pieChart.destroy();
        }

        pieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: facility,
                datasets: [{
                    data: faData,
                    backgroundColor: Object.values(Utils.PIE_CHART_COLORS),
                }]
            },
            options: {
                responsive: true, // 반응형 옵션 추가
                maintainAspectRatio: false, // 비율 유지 비활성화
                aspectRatio: 1, // 너비:높이 비율 설정 (정사각형)
                plugins: {
                    legend: {
                        position: 'right', // 범례 위치 설정
                        // align: 'left', // 범례 정렬
                        labels: {
                            font: {
                                size: 14
                            }
                        }
                    },
                },
            }
        });
    }

    // 언어 변경 시 차트 업데이트 함수
    window.updateChartsLanguage = function (newLanguage) {
        // 언어 변경 시 페이지가 새로고침되므로 차트 업데이트는 불필요
        // 하지만 즉시 업데이트가 필요한 경우를 위해 유지
        currentLanguage = newLanguage;

        // Doughnut Chart 업데이트
        if (doughnutChart) {
            doughnutChart.data.labels = [
                `${i18n.testRun}(${opStatus.opTestPer}%)`,
                `${i18n.operating}(${opStatus.operatingPer}%)`,
                `${i18n.operationStop}(${opStatus.opStopPer}%)`
            ];
            doughnutChart.update();
        }

        // Bar Chart 업데이트
        if (barChart) {
            barChart.data.datasets[0].label = i18n.fast;
            barChart.data.datasets[1].label = i18n.slow;
            barChart.data.datasets[2].label = i18n.dispenser;
            barChart.update();
        }

        // Treemap Chart 업데이트
        if (treemapChart) {
            treemapChart.data.datasets[0].tree = [
                { what: i18n.available, value: connStatus.availableCount, color: '#28a745' },
                { what: i18n.preparing, value: connStatus.preparingCount, color: '#ffc107' },
                { what: i18n.charging, value: connStatus.chargingCount, color: '#007bff' },
                { what: i18n.chargingError, value: connStatus.suspendedEvCount, color: '#A9A3A3' },
                { what: i18n.chargerError, value: connStatus.suspendedEvseCount, color: '#DBDBDB' },
                { what: i18n.finishing, value: connStatus.finishingCount, color: '#EC5228' },
                { what: i18n.reserved, value: connStatus.reservedCount, color: '#702EC3' },
                { what: i18n.unavailable, value: connStatus.unavailableCount, color: '#BF3131' },
                { what: i18n.faulted, value: connStatus.faultedCount, color: '#dc3545' },
                { what: i18n.disconnected, value: connStatus.disconnectedCount, color: '#A9A3A3' },
            ];
            treemapChart.update();
        }
    };
});