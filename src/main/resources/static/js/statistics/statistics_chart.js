document.addEventListener('DOMContentLoaded', function(){
    let label1, label2;
    
    if (STAT === 'PUR') {
        label1 = '매입';
        label2 = '매출';
    } else {
        label1 = '급속';
        label2 = '완속';
    }

    // line Chart
    const lineData = {
        labels: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
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