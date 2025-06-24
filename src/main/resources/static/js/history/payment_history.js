document.addEventListener("DOMContentLoaded", function () {


    $('#size').on('change', function () {
        updatePageSize(this, "/history/payment", ["companyIdSearch", "opSearch", "contentSearch", "stateCode", "transactionStart", "transactionEnd"]);
    });
});