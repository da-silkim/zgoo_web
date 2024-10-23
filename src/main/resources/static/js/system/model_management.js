var list = [
    {company: '동아일렉콤', manuf: '동아일렉콤', modelName: 'DA-0001', kw: 100, chargeType: '싱글', useYN: '사용', cpType: '급속', connectorType: 'DC콤보', regDate: '2024-11-11'},
    {company: '동아일렉콤', manuf: '동아일렉콤', modelName: 'DA-0001', kw: 100, chargeType: '싱글', useYN: '사용', cpType: '급속', connectorType: 'DC콤보', regDate: '2024-11-11'},
    {company: '동아일렉콤', manuf: '동아일렉콤', modelName: 'DA-0001', kw: 100, chargeType: '싱글', useYN: '사용', cpType: '급속', connectorType: 'DC콤보', regDate: '2024-11-11'},
    {company: '동아일렉콤', manuf: '동아일렉콤', modelName: 'DA-0001', kw: 100, chargeType: '싱글', useYN: '사용', cpType: '급속', connectorType: 'DC콤보', regDate: '2024-11-11'},
    {company: '동아일렉콤', manuf: '동아일렉콤', modelName: 'DA-0001', kw: 100, chargeType: '싱글', useYN: '사용', cpType: '급속', connectorType: 'DC콤보', regDate: '2024-11-11'},
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
                    <th scope="row"><input type="checkbox" data-index="${i}"></th>
                    <td>${list[i].company}</td>
                    <td>${list[i].manuf}</td>
                    <td>${list[i].modelName}</td>
                    <td>${list[i].kw}</td>
                    <td>${list[i].chargeType}</td>
                    <td>${list[i].useYN}</td>
                    <td>${list[i].cpType}</td>
                    <td>${list[i].connectorType}</td>
                    <td>${list[i].regDate}</td>
                </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();
});