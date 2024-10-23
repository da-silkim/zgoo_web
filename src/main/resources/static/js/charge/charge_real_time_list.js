var list = [
    {company: '동아일렉콤', csName: '가산어반워크1', cpName: 'B2103', cpID: 'DAE00101', cpType: '급속', connectorID: 1, chargeDate: '2024-07-07 15:00:00', userName: 'name1', membershipNum: '1010-0101-0000-1111', kWh: '10.15', hour: '13분', soc: 62},
    {company: '동아일렉콤', csName: '가산어반워크1', cpName: 'B2103', cpID: 'DAE00101', cpType: '급속', connectorID: 1, chargeDate: '2024-07-07 15:00:00', userName: 'name2', membershipNum: '1010-0101-0000-1112', kWh: '10.15', hour: '21분', soc: 40},
    {company: '동아일렉콤', csName: '가산어반워크1', cpName: 'B2103', cpID: 'DAE00101', cpType: '급속', connectorID: 1, chargeDate: '2024-07-07 15:00:00', userName: 'name3', membershipNum: '1010-0101-0000-1113', kWh: '10.15', hour: '17분', soc: 69},
    {company: '동아일렉콤', csName: '가산어반워크1', cpName: 'B2103', cpID: 'DAE00101', cpType: '급속', connectorID: 1, chargeDate: '2024-07-07 15:00:00', userName: 'name4', membershipNum: '1010-0101-0000-1114', kWh: '10.15', hour: '5분', soc: 87},
    {company: '동아일렉콤', csName: '가산어반워크1', cpName: 'B2103', cpID: 'DAE00101', cpType: '급속', connectorID: 1, chargeDate: '2024-07-07 15:00:00', userName: 'name5', membershipNum: '1010-0101-0000-1115', kWh: '10.15', hour: '15분', soc: 66},
    
];
list = list.reverse();

const totalCount = list.length;
const pageSize = 10;
const totalPage = Math.ceil(totalCount / pageSize);

function setPageHtml() {
    let pageHtml =
        `<li class="page-item">
            <a href="#;" class="page-link" onClick="changePage('first');return false;">≪</a>
        </li>
        <li class="page-item">
            <a href="#" class="page-link" onClick="changePage('prev');return false;"><</a>
        </li>
        <li class="page-item active">
            <a href="#;" class="page-link" onClick="changePage(1);return false;">1</a>
        </li>`;
    for(let i = 2; i <= totalPage; i ++){
        pageHtml +=
            `<li class="page-item">
                <a href="#;" class="page-link" onClick="changePage(${i});return false;">${i}</a>
                </li>`;
    }
    pageHtml +=
        `<li class="page-item">
            <a href="#;" class="page-link" onClick="changePage('next');return false;">></a>
        </li>
        <li class="page-item">
            <a href="#;" class="page-link" onClick="changePage('last');return false;">≫</a>
        </li>`;
    document.getElementById("paging").innerHTML = pageHtml;
}

function setList(page){
    let pageCount = 10;
    page = page == null ? 1 : page;
    let startPage = (page - 1) * pageCount + 1;
    let endPage = startPage + pageCount - 1;
    endPage = endPage > totalCount ? totalCount : endPage;
    showList(startPage, endPage);
    let html = `${page}/${totalPage} 쪽 [총 <strong>${totalCount}</strong>건]`;
    document.getElementById("page-info").innerHTML = html;

    document.querySelectorAll("#paging li").forEach( (item) => {
        let str = item.querySelector("#paging li a").innerText;
        if(str.includes(page)) {
            item.classList.add("active");
        }else{
            item.classList.remove("active");
        }
    });
}

function showList(startPage, endPage){
    let html = "";
    for(let i = (startPage - 1) ; i < endPage; i++) {
        html += `<tr id="charge-user-${i}">
                    <td>${list[i].company}</td>
                    <td>${list[i].csName}</td>
                    <td>${list[i].cpName}</td>
                    <td>${list[i].cpID}</td>
                    <td>${list[i].cpType}</td>
                    <td>${list[i].connectorID}</td>
                    <td>${list[i].chargeDate}</td>
                    <td>${list[i].userName}</td>
                    <td>${list[i].membershipNum}</td>
                    <td>${list[i].kWh}</td>
                    <td>${list[i].hour}</td>
                    <td>${list[i].soc}</td>
                </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();
});