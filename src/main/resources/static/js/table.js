document.addEventListener('DOMContentLoaded', () => {
    printErrorList();
    printNoticeList();
})

var errorList = [
    {chargingStationID:'DAE100', chargerID: '01', errorCode: '1001', errorContent: '비상정지 버튼 눌림1', errorDate: '2024-07-09 13:14:11', division: '처리중' },
    {chargingStationID:'DAE100', chargerID: '01', errorCode: '1001', errorContent: '비상정지 버튼 눌림2', errorDate: '2024-07-09 13:14:11', division: '처리중' },
    {chargingStationID:'DAE100', chargerID: '01', errorCode: '1001', errorContent: '비상정지 버튼 눌림3', errorDate: '2024-07-09 13:14:11', division: '처리중' },
];

function printErrorList() {
    let html = "";
    errorList = errorList.reverse();
    const totalCount = errorList.length;

    for(let i = 0; i < totalCount; i++ ){
        html += `<tr id="charge-error-${i}">
                    <td>${errorList[i].chargingStationID}</td>
                    <td>${errorList[i].chargerID}</td>
                    <td>${errorList[i].errorCode}</td>
                    <td>${errorList[i].errorContent}</td>
                    <td>${errorList[i].errorDate}</td>
                    <td>${errorList[i].division}</td>
                </tr>`
    }
    document.getElementById("errorList").innerHTML = html;
}

var noticeList = [
    {type:'안내', title:'공지사항입니다.',date:'2024-10-18 14:51:11'},
    {type:'안내', title:'공지사항입니다.',date:'2024-10-18 14:51:11'},
    {type:'안내', title:'공지사항입니다.',date:'2024-10-18 14:51:11'},
]

function printNoticeList() {
    let html = "";
    noticeList = noticeList.reverse();
    const noticeCount = noticeList.length;

    for(let i=0; i <noticeCount; i++){
        html += `<div class="row pb-2 mb-4 main-article-list">
                    <div class="col-7 text-left">[${noticeList[i].type}] ${noticeList[i].title}</div>
                    <div class="col-5 text-right">${noticeList[i].date}</div>
                </div>`
    }
    document.getElementById("noticeList").innerHTML = html;
}