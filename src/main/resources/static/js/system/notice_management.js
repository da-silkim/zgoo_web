var list = [
    {noticeType: '회원공지', title: '회원 공지입니다.', writterID: 'admin1', writterName: 'name1', regDate: '2024-01-01', company: '동아일렉콤'},
    {noticeType: '시스템', title: '시스템 공지입니다.', writterID: 'admin2', writterName: 'name2', regDate: '2024-01-01', company: '동아일렉콤'},
    {noticeType: '모바일', title: '모바일 공지입니다.', writterID: 'admin3', writterName: 'name3', regDate: '2024-01-01', company: '동아일렉콤'},
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
                    <td>${list[i].noticeType}</td>
                    <td>${list[i].title}</td>
                    <td>${list[i].writterID}</td>
                    <td>${list[i].writterName}</td>
                    <td>${list[i].regDate}</td>
                    <td>${list[i].company}</td>
                </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();
});