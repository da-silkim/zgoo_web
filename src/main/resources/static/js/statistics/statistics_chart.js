document.addEventListener('DOMContentLoaded', function () {
    let label1, label2;

    // 현재 언어 감지 함수
    function isEnglishLanguage() {
        // currentLanguage 변수 확인
        if (typeof currentLanguage !== 'undefined') {
            return currentLanguage === 'en';
        } else {
            // currentLanguage가 없으면 브라우저 언어로 판단
            var browserLang = navigator.language || navigator.userLanguage;
            return browserLang.startsWith('en');
        }
    }

    // 현재 언어에 따른 라벨 설정 (리소스 메시지 사용)
    function getLabels() {
        // chartLabels 객체가 정의되어 있는지 확인
        if (typeof chartLabels !== 'undefined') {
            if (STAT === 'PUR') {
                return { label1: chartLabels.purchase, label2: chartLabels.sales };
            } else {
                return { label1: chartLabels.fast, label2: chartLabels.slow };
            }
        } else {
            // fallback: chartLabels가 정의되지 않은 경우
            var isEnglish = isEnglishLanguage();

            if (STAT === 'PUR') {
                if (isEnglish) {
                    return { label1: 'Purchase', label2: 'Sales' };
                } else {
                    return { label1: '매입', label2: '매출' };
                }
            } else {
                if (isEnglish) {
                    return { label1: 'Fast', label2: 'Slow' };
                } else {
                    return { label1: '급속', label2: '완속' };
                }
            }
        }
    }

    // 현재 언어에 따른 월별 라벨 설정 (리소스 메시지 사용)
    function getMonthLabels() {
        // monthLabels 객체가 정의되어 있는지 확인
        if (typeof monthLabels !== 'undefined') {
            return [
                monthLabels.january,
                monthLabels.february,
                monthLabels.march,
                monthLabels.april,
                monthLabels.may,
                monthLabels.june,
                monthLabels.july,
                monthLabels.august,
                monthLabels.september,
                monthLabels.october,
                monthLabels.november,
                monthLabels.december
            ];
        } else {
            // fallback: monthLabels가 정의되지 않은 경우
            var isEnglish = isEnglishLanguage();
            if (isEnglish) {
                return ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            } else {
                return ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
            }
        }
    }

    const labels = getLabels();
    label1 = labels.label1;
    label2 = labels.label2;

    // line Chart
    const lineData = {
        labels: getMonthLabels(),
        datasets: [
            {
                label: label1,
                data: [1000000, 1000000, 2000000, 2000000, 2300000, 2400000, 3300000, 3400000, 1000000, 4200000, 3500000, 3300000,],
                borderColor: ['#379C6D'],
                backgroundColor: ['#379C6D'],
                fill: false
            },
            {
                label: label2,
                data: [2000000, 2025000, 2804500, 4000000, 3000000, 3500000, 6500000, 5400000, 3900000, 8600000, 7500000, 7300000,],
                borderColor: ['#E58A10'],
                backgroundColor: ['#E58A10'],
                fill: false
            }
        ]
    }
    var ctx = document.getElementById("lineChart").getContext('2d');
    var lineChart = new Chart(ctx, {
        type: 'line',
        data: lineData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                },
                filler: {
                    propagate: false,
                }
            },
            interaction: {
                intersect: false,
            },
            scales: {
                x: {
                    ticks: {
                        font: {
                            size: 14,
                        }
                    },
                },
                y: {
                    min: 0,
                    max: 10000000,
                    beginAtZero: true
                }
            }
        }
    });

    // bar Chart
    const barData = {
        labels: ["2024", "2025"],
        datasets: [
            {
                label: label1,
                data: [3000000, 4000000],
                backgroundColor: ['#379C6D'],
                minBarThickness: 10,
                maxBarThickness: 200,
            },
            {
                label: label2,
                data: [6000000, 8000000],
                backgroundColor: ['#E58A10'],
                minBarThickness: 10,
                maxBarThickness: 200,
            }
        ]
    }
    var ctx = document.getElementById("barChart").getContext('2d');
    var barChart = new Chart(ctx, {
        type: 'bar',
        data: barData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                }
            },
            scales: {
                x: {
                    ticks: {
                        font: {
                            size: 14,
                        }
                    },
                },
                y: {
                    min: 0,
                    max: 10000000,
                    beginAtZero: true
                }
            }
        }
    });
});