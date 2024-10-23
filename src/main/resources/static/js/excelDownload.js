function excelDownload() {
    let excelTable = "";

    excelTable += "<table border='1'>";
    excelTable += "    <thead>";
    excelTable += "        <tr>";
    excelTable += "            <td>사업자</td>";
    excelTable += "            <td>회원번호</td>";
    excelTable += "            <td>사용자명</td>";
    excelTable += "            <td>사용자ID</td>";
    excelTable += "            <td>회원카드번호</td>";
    excelTable += "            <td>휴대전화</td>";
    excelTable += "            <td>이메일</td>";
    excelTable += "            <td>환경부로밍여부</td>";
    excelTable += "            <td>차량번호</td>";
    excelTable += "            <td>가입일자</td>";
    excelTable += "        </tr>";
    excelTable += "    </thead>";
    excelTable += "    <tbody>";

    if(list.length > 0) {
        for(let i =0; i<list.length; i++){
            excelTable += `<tr id="charge-user-${i}">
                <td>${list[i].company}</td>
                <td>${list[i].membershipNum}</td>
                <td>${list[i].userName}</td>
                <td>${list[i].userID}</td>
                <td>${list[i].membershipCard}</td>
                <td>${list[i].phoneNum}</td>
                <td>${list[i].email}</td>
                <td>${list[i].loam}</td>
                <td>${list[i].carNum}</td>
                <td>${list[i].regDate}</td>
            </tr>`;
        }
    } else {
        excelTable += "<tr>";
        excelTable += "    <td colspan='10'>데이터가 없습니다.";
        excelTable += "</tr>";
    }

    excelTable += "    </tbody>";
    excelTable += "</table>";

    excelDown("test.xml", "sheets1", excelTable);
}

function excelDown(fileName, sheetName, sheetHtml) {
    let html = "";

    html += "<html xmlns='urn:schemas-microsoft-com:office:excel'>";
    html += "    <head>";
    html += "        <meta http-equiv='content-type' content='application/vnd.ms-excel; charset=UTF-8'>";
    html += "        <xml>";
    html += "            <x:ExcelWorkbook>";
    html += "                <x:ExcelWorksheets>";
    html += "                    <x::name>" + sheetName + "</x:name>";
    html += "                    <x:WorksheetOptions><x:Panes></x:Panes></x:WorksheetOptions>";
    html += "                </x:ExcelWorksheets>";
    html += "            </x:ExcelWorkbook>";
    html += "        </xml>";
    html += "    </head>";

    html += "    <body>";
    html += sheetHtml;
    html += "    </body>";
    html += "</html>";

    let dataType = "data:application/vnd.ms-excel";
    let ua = window.navigator.userAgent;
    let blob = new Blob([html], {type: "application/csv; charset=utf-8"});

    let anchor = window.document.createElement('a');
    anchor.href = window.URL.createObjectURL(blob);
    anchor.download = fileName;
    document.body.appendChild(anchor);
    anchor.click();

    // 다운로드 후 제거
    document.body.removeChild(anchor);

}