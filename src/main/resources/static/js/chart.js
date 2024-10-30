// charts
document.addEventListener('DOMContentLoaded', function(){
    // doughnt chart
    var ctx =  document.getElementById("doughnutChart").getContext('2d');
    var doughnutChart =  new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ["운영중", "운영중지"],
            datasets: [{
                data: [60, 40],
                backgroundColor: ['#144693', '#d9d9d9'], // 색상 추가
                hoverBackgroundColor: ['#5073A8', '#E8EAEE'], // 호버 색상 추가                    
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
    ctx =  document.getElementById("barChart").getContext('2d');
    var barChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ["서울특별시", "경기도", "인천광역시", "충청남도", "대전광역시", "전라북도", "광주광역시", "강원도", "대구광역시", "제주도"],
            datasets: [{
                label: "급속",
                data: [2500, 4000, 5100, 3400, 4003, 2510, 5841, 3540, 6612, 5312],
                backgroundColor: ['#144693']
            },{
                label: "완속",
                data: [4500, 5400, 3400, 3511, 3210, 6540, 3455, 3533, 2104, 1202],
                backgroundColor: ['#26C59E']
            },]
        },
        options: {
            responsive: true,    // 반응형 옵션 추가
            maintainAspectRatio: false, // 종횡비 유지
            maxBarThickness: 50, // 막대의 최대 너비 설정
            plugins: {
                legend: {
                    position: 'right', // 범례 위치 설정
                    align: 'center',   // 범례 정렬
                    maxWidth: 90,      // 범례의 최대 너비 설정
                    labels: {
                        font: {
                            size: 14   // 범례 폰트 크기 조정
                        }
                    }
                },                        
            },
            scales: {
                x: {
                    stacked:true,
                    ticks: {
                        color: '#000',
                        font: {
                            size: 14,
                        }
                    },
                },
                y: {
                    // beginAtZero: true,
                    min: 0,
                    max: 10000,
                    stacked:true,
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
        },
    });

    // treemap chart
    // B43F3F ED9455 686D76
    var ctx =  document.getElementById("treeChart").getContext('2d');
    var DATA = [
        {
            what: '대기',
            value: 200,
            color: '#327EC3'
        },
        {
            what: '충전중',
            value: 50,
            color: '#2EC8A3'
        },
        {
            what: '고장/점검',
            value: 10,
            color: '#C1315C'
        },
        {
            what: '예약',
            value: 20,
            color: '#702EC3'
        },
        {
            what: '통신장애',
            value: 10,
            color: '#A9A3A3'
        },
    ];
    
    let treemapChart =  new Chart(ctx, {
        type: 'treemap',
        data: {
            datasets: [{
                label: 'Charge',
                tree: DATA,
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
                    font: [{size: 28, weight: 'bold'}, {size: 18}],
                    // font: { size: 20 },
                    position: 'center' // 라벨 위치 가운데로 설정
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

    // pie chart
    var ctx =  document.getElementById("pieChart").getContext('2d');
    // const DATA_COUNT = 5;
    // const NUMBER_CFG = {count: DATA_COUNT, min: 0, max: 100};
    const Utils = {
        PIE_CHART_COLORS: {
            p1: 'rgb(54, 48, 98)',
            p2: 'rgb(77, 76, 125)',
            p3: 'rgb(130, 115, 151)',
            p4: 'rgb(216, 185, 195)',
            p5: 'rgb(93, 93, 93)',
            p6: 'rgb(217. 217. 217)',
        }
    };
    var pieChart =  new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["시청", "공영주차장", "아파트", "고속도로휴게소", "마트"],
            datasets: [{
                data: [15, 35, 20, 20, 10],
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
                    // align: 'center', // 범례 정렬
                    labels: {
                        font: {
                            size: 16
                        }
                    }
                },                        
            },
        }
    });
});