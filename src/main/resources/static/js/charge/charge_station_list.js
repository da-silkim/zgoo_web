var list = [
    {company: '동아일렉콤', csName: '가산어반워크1', cpID: 'DAE00101', installAddress: '서울특별시 금천구 가산디지털 2로 135', cpCnt: 2, operate: '운영', operateStart: '00:00', operateEnd: '23:59'},
    {company: '동아일렉콤', csName: '가산어반워크1', cpID: 'DAE00101', installAddress: '서울특별시 금천구 가산디지털 2로 135', cpCnt: 2, operate: '운영', operateStart: '00:00', operateEnd: '23:59'},
    {company: '동아일렉콤', csName: '가산어반워크1', cpID: 'DAE00101', installAddress: '서울특별시 금천구 가산디지털 2로 135', cpCnt: 2, operate: '운영', operateStart: '00:00', operateEnd: '23:59'},
    {company: '동아일렉콤', csName: '가산어반워크1', cpID: 'DAE00101', installAddress: '서울특별시 금천구 가산디지털 2로 135', cpCnt: 2, operate: '운영', operateStart: '00:00', operateEnd: '23:59'},
    {company: '동아일렉콤', csName: '가산어반워크1', cpID: 'DAE00101', installAddress: '서울특별시 금천구 가산디지털 2로 135', cpCnt: 2, operate: '운영', operateStart: '00:00', operateEnd: '23:59'},
    
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
                    <td>${list[i].csName}</td>
                    <td>${list[i].cpID}</td>
                    <td>${list[i].installAddress}</td>
                    <td>${list[i].cpCnt}</td>
                    <td>${list[i].operate}</td>
                    <td>${list[i].operateStart}</td>
                    <td>${list[i].operateEnd}</td>
                </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();
});