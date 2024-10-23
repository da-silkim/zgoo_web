// 회원 리스트 데이터
var list = [
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'홍길동', userID:'test', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'}
];
list = list.reverse();

document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();

    const checkedBox = document.querySelectorAll('.delete-checkbox:checked');

});

// 페이징 함수
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

// 데이터 렌더링 함수
function setList(page){

    // 페이지 당 표시 될 튜플 수
    let pageCount = 10;
    page = page == null ? 1 : page;

    // 표시될 첫 게시글
    let startPage = (page - 1) * pageCount + 1;
    // 표시될 마지막 게시글
    let endPage = startPage + pageCount - 1;
    // if(마지막 게시글 > 총 게시글) 총 게시글을 마지막 게시글로
    endPage = endPage > totalCount ? totalCount : endPage;

    showList(startPage, endPage);

    let html = `${page}/${totalPage} 쪽 [총 <strong>${totalCount}</strong>건]`;
    document.getElementById("page-info").innerHTML = html;

    // 변경된 페이지 표시
    document.querySelectorAll("#paging li").forEach( (item) => {
        let str = item.querySelector("#paging li a").innerText;
        if(str.includes(page)) {
            item.classList.add("active");
        }else{
            item.classList.remove("active");
        }
    });
}

/**
 * 페이지 클릭 이벤트
 * @param page
 * @returns
 */
function changePage(page){
    log("page ==> " + page);

    // 현재 페이지
    // let nowPage = parseInt(document.querySelector("#paging .active a").innerText);
    let nowPage = parseInt(document.querySelector("#paging .page-item.active .page-link").innerText);
    log("nowPage --> " + nowPage);

    if(page === "first"){
        page = "1";
    }else if(page === "prev"){
        page = (nowPage - 1) < 1 ? nowPage : (nowPage - 1);
    }else if(page === "next"){
        page = (nowPage + 1) > totalPage ? totalPage : (nowPage + 1);
    }else if(page === "last"){
        page = totalPage;
    }

    if(nowPage != page)
        setList(page);
}


/**
 * 해당 페이지 데이터 세팅
 * @param startPage
 * @param endPage
 */
function showList(startPage, endPage){
    let html = "";
    for(let i = (startPage - 1) ; i < endPage; i++) {
        html += `<tr id="charge-user-${i}">
                    <th scope="row"><input type="checkbox"></th>
                    <td>${list[i].company}</td>
                    <td>${list[i].membershipNum}</td>
                    <td>${list[i].userName}</td>
                    <td>${list[i].userID}</td>
                    <td>${list[i].membershipCard}</td>
                    <td>${list[i].phoneNum}</td>
                    <td>${list[i].email}</td>
                    <td>${list[i].type}</td>
                    <td>${list[i].carNum}</td>
                    <td>${list[i].regDate}</td>
                  </tr>`;
    }
    document.getElementById("pageList").innerHTML = html;
}