$(document).ready(function() {
    $('#pageList').on('click', 'tr', function() {
        const currCompany = $(this).find('td').eq(1).text();
        const currAuthority = $(this).find('td').eq(2).text();
        const currAuthorityName = $(this).find('td').eq(3).text();

        $('#currAuthority').empty();
        $('#currAuthority').append(`
            <span>
                사업자명: ${currCompany} | 권한그룹ID: ${currAuthority} | 권한그룹명: ${currAuthorityName}
            </span>
        `);
    });
});