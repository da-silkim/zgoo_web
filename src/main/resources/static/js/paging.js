// 회원 리스트 데이터
var list = [
    {company: '동아일렉콤', membershipNum:'DA00000001', userName:'name1', userID:'test1', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000002', userName:'name2', userID:'test2', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000003', userName:'name3', userID:'test3', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000004', userName:'name4', userID:'test4', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000005', userName:'name5', userID:'test5', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000006', userName:'name6', userID:'test6', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000007', userName:'name7', userID:'test7', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000008', userName:'name8', userID:'test8', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'},
    {company: '동아일렉콤', membershipNum:'DA00000009', userName:'name9', userID:'test9', membershipCard:1111222233334444, phoneNum:'01011112222', email:'test@gmail.com', type:'법인', carNum:'123가 4567', regDate:'2024-07-10'}
];
list = list.reverse();

const totalCount = list.length;
const pageSize = 10;    // 페이지 당 표시될 데이터 수
const totalPage = Math.ceil(totalCount / pageSize);  // 총 페이지 수
document.addEventListener('DOMContentLoaded', () => {
    setPageHtml();
    setList();

    // document.getElementById('deleteBtn').addEventListener('click', () => {
    //     const checkedBox = document.querySelector('input[type="checkbox"]:checked'); // 단일 선택을 가정
    
    //     if (checkedBox) {
    //         // 사용자에게 삭제 여부 확인
    //         const isConfirmed = confirm('정말로 이 항목을 삭제하시겠습니까?');
            
    //         if (isConfirmed) {
    //             const idx = parseInt(checkedBox.getAttribute('data-index')); // 체크된 항목의 data-index 값 추출
    //             list.splice(idx, 1); // 데이터 배열에서 해당 인덱스 삭제
                
    //             showList(1, list.length); // 변경된 리스트 렌더링
                
    //             // 버튼 비활성화
    //             document.getElementById('deleteBtn').disabled = true;
    //             document.getElementById('editBtn').disabled = true;
    //         }
    //     } else {
    //         alert('삭제할 항목을 선택하세요.');
    //     }
    // });
    

});

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
 * 해당 페이지 데이터 세팅
 * @param startPage
 * @param endPage
 */
function showList(startPage, endPage){
    let html = "";
    for(let i = (startPage - 1) ; i < endPage; i++) {
        // let company = list[i].company;
        // let membershipNum = list[i].membershipNum;
        // let userName = list[i].userName;
        // let userID = list[i].userID;
        // let membershipCard = list[i].membershipCard;
        // let phoneNum = list[i].phoneNum;
        // let email = list[i].email;
        // let type = list[i].type;
        // let carNum = list[i].carNum;
        // let regDate = list[i].regDate;
        html += `<tr id="charge-user-${i}">
                    <th scope="row"><input type="checkbox" data-index="${i}"></th>
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