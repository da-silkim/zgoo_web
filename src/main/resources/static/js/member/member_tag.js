$(document).ready(function() {

    $('#resetBtn').on('click', function() {
        window.location.replace('/member/tag/list');
    });

    $('#searchBtn').on('click', function() {
        const selectedSize = document.getElementById('size').value;
        const form = document.getElementById('searchForm');

        const hiddenSizeInput = document.createElement('input');
        hiddenSizeInput.type = 'hidden';
        hiddenSizeInput.name = 'size';
        hiddenSizeInput.value = selectedSize;
        hiddenSizeInput.id = 'hiddenSizeInput';

        form.appendChild(hiddenSizeInput);
        form.submit();
    });

    $('#size').on('change', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedIdTag = urlParams.get('idTagSearch') || '';
        const selectedName = urlParams.get('nameSearch') || '';

        window.location.href = "/member/tag/list?page=0&size=" + selectedSize +
                               "&idTagSearch=" + (selectedIdTag) +
                               "&nameSearch=" + (selectedName);
    });
});