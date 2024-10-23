var list = [
    {companyLV: '상위사업자', companyType: '충전사업자', companyID: 'DAE', companyLVID: null, company: '동아일렉콤', startDate: '2024-01-01', endDate: '2025-01-01', contractStatus: '계약중'},
    {companyLV: '상위사업자', companyType: '위탁운영사', companyID: 'HMX', companyLVID: null, company: '휴멕스', startDate: '2024-01-01', endDate: '2025-01-01', contractStatus: '계약중'},
    {companyLV: '하위사업자', companyType: '충전사업자', companyID: 'HEV', companyLVID: 'HMX', company: '휴멕스EV', startDate: '2024-01-01', endDate: '2025-01-01', contractStatus: '계약중'},
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
                    <td>${list[i].companyLV}</td>
                    <td>${list[i].companyType}</td>
                    <td>${list[i].companyID}</td>
                    <td>${list[i].companyLVID ? list[i].companyLVID : ''}</td>
                    <td>${list[i].company}</td>
                    <td>${list[i].startDate}</td>
                    <td>${list[i].endDate}</td>
                    <td>${list[i].contractStatus}</td>
                </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();
});