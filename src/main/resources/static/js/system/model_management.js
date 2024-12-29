$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록";
    var modelId;

    $('#resetBtn').on('click', function() {
        window.location.replace('/system/model/list');
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
        const selectedCompanyId = urlParams.get('companyIdSearch') || '';
        const selectedManfCd = urlParams.get('manfCdSearch') || '';
        const selectedChgSpeedCd = urlParams.get('chgSpeedCdSearch') || '';

        window.location.href = "/system/model/list?page=0&size=" + selectedSize +
                               "&companyIdSearch=" + (selectedCompanyId) +
                               "&manfCdSearch=" + (selectedManfCd) +
                               "&chgSpeedCdSearch=" + (selectedChgSpeedCd);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#collapseOne').collapse('hide');
        // form 초기화
        $('#manufCd').prop('selectedIndex', 0);
        $('#powerUnit').val('');
        $('#modelName').val('');
        $('#modelCode').val('');
        $('#cpType').prop('selectedIndex', 0);
        $('#installationType').prop('selectedIndex', 0);
        $('#connectorTable tbody').empty();
        addRow();
        $('#inputVoltage').val('');
        $('#inputFrequency').val('');
        $('#inputType').val('');
        $('#inputCurr').val('');
        $('#inputPower').val('');
        $('#powerFactor').val('');
        $('#outputVoltage').val('');
        $('#maxOutputCurr').val('');
        $('#ratedPower').val('');
        $('#peakEfficiency').val('');
        $('#thdi').val('');
        $('#grdType').val('');
        $('#opAltitude').val('');
        $('#opTemperature').val('');
        $('#temperatureDerating').val('');
        $('#storageTemperatureRange').val('');
        $('#humidity').val('');
        $('#dimensions').val('');
        $('#ipNIk').val('');
        $('#weight').val('');
        $('#material').val('');
        $('#cableLength').val('');
        $('#screen').val('');
        $('#rfid').val('');
        $('#emergencyBtn').val('');
        $('#communicationInterface').val('');
        $('#lang').val('');
        $('#coolingMethod').val('');
        $('#emc').val('');
        $('#protection').val('');
        $('#opFunc').val('');
        $('#standard').val('');
        $('#powerModule').val('');
        $('#charger').val('');
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#collapseOne').collapse('hide');
        modelId = selectRow.find('td').eq(0).attr('id');
        $.ajax({
            type: 'GET',
            url: `/system/model/get/${modelId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#manufCd').val(data.manufCd || '');
                $('#powerUnit').val(data.powerUnit || '');
                $('#modelName').val(data.modelName || '');
                $('#modelCode').val(data.modelCode || '');
                $('#cpType').val(data.cpType || '');
                $('#installationType').val(data.installationType || '');

                const tableBody = document.querySelector("#connectorTable tbody");
                tableBody.innerHTML = "";

                data.connector.forEach(con => {
                    const row = document.createElement("tr");
                    row.classList.add("connector-row");
                    const selectElement = document.createElement('select');
                    selectElement.classList.add('form-control', 'connectorType');
                    connTypeData.forEach(function(data) {
                        const option = document.createElement('option');
                        option.value = data.commonCode;
                        option.textContent = data.commonCodeName;
                        if (con.connectorType === data.commonCode) {
                            option.selected = true;
                        }
                        selectElement.appendChild(option);
                    });
                    row.innerHTML = `
                    <td><input type="checkbox" class="single-checkbox"/></td>
                    <td><input type="text" class="input-add-row connectorId" value="${con.connectorId ? con.connectorId : ''}"/></td>
                    `;
                    const tdSelect = document.createElement('td');
                    tdSelect.appendChild(selectElement);
                    row.appendChild(tdSelect);

                    tableBody.appendChild(row);
                });

                $('#inputVoltage').val(data.inputVoltage || '');
                $('#inputFrequency').val(data.inputFrequency || '');
                $('#inputType').val(data.inputType || '');
                $('#inputCurr').val(data.inputCurr || '');
                $('#inputPower').val(data.inputPower || '');
                $('#powerFactor').val(data.powerFactor || '');
                $('#outputVoltage').val(data.outputVoltage || '');
                $('#maxOutputCurr').val(data.maxOutputCurr || '');
                $('#ratedPower').val(data.ratedPower || '');
                $('#peakEfficiency').val(data.peakEfficiency || '');
                $('#thdi').val(data.thdi || '');
                $('#grdType').val(data.grdType || '');
                $('#opAltitude').val(data.opAltitude || '');
                $('#opTemperature').val(data.opTemperature || '');
                $('#temperatureDerating').val(data.temperatureDerating || '');
                $('#storageTemperatureRange').val(data.storageTemperatureRange || '');
                $('#humidity').val(data.humidity || '');
                $('#dimensions').val(data.dimensions || '');
                $('#ipNIk').val(data.ipNIk || '');
                $('#weight').val(data.weight || '');
                $('#material').val(data.material || '');
                $('#cableLength').val(data.cableLength || '');
                $('#screen').val(data.screen || '');
                $('#rfid').val(data.rfid || '');
                $('#emergencyBtn').val(data.emergencyBtn || '');
                $('#communicationInterface').val(data.communicationInterface || '');
                $('#lang').val(data.lang || '');
                $('#coolingMethod').val(data.coolingMethod || '');
                $('#emc').val(data.emc || '');
                $('#protection').val(data.protection || '');
                $('#opFunc').val(data.opFunc || '');
                $('#standard').val(data.standard || '');
                $('#powerModule').val(data.powerModule || '');
                $('#charger').val(data.charger || '');
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            modelId = selectRow.find('td').eq(0).attr('id');

            $.ajax({
                type: 'DELETE',
                url: `/system/model/delete/${modelId}`,
                contentType: "application/json",
                success: function(response) {
                    console.log(response);
                    window.location.replace('/system/model/list');
                },
                error: function(error) {
                    console.log(error);
                }
            });
        }
    });

    $('#modalCalcelBtn').on('click', function() {
        // window.location.reload();
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        const connectorInfoList = getConnectorList();

        // connectorInfoList가 null이면 유효성 검사 실패
        if (!connectorInfoList) {
            alert("모든 Connector ID는 필수 항목입니다. 값을 입력해주세요.");
            return;  // Ajax 요청을 중단
        }

        if (confirmSubmit(btnMsg)) {
            const DATA = {
                manufCd: $('#manufCd').val(),
                powerUnit: $('#powerUnit').val(),
                modelName: $('#modelName').val(),
                modelCode: $('#modelCode').val(),
                cpType: $('#cpType').val(),
                installationType: $('#installationType').val(),
                connector: getConnectorList(),
                inputVoltage: $('#inputVoltage').val(),
                inputFrequency: $('#inputFrequency').val(),
                inputType: $('#inputType').val(),
                inputCurr: $('#inputCurr').val(),
                inputPower: $('#inputPower').val(),
                powerFactor: $('#powerFactor').val(),
                outputVoltage: $('#outputVoltage').val(),
                maxOutputCurr: $('#maxOutputCurr').val(),
                ratedPower: $('#ratedPower').val(),
                peakEfficiency: $('#peakEfficiency').val(),
                thdi: $('#thdi').val(),
                grdType: $('#grdType').val(),
                opAltitude: $('#opAltitude').val(),
                opTemperature: $('#opTemperature').val(),
                temperatureDerating: $('#temperatureDerating').val(),
                storageTemperatureRange: $('#storageTemperatureRange').val(),
                humidity: $('#humidity').val(),
                dimensions: $('#dimensions').val(),
                ipNIk: $('#ipNIk').val(),
                weight: $('#weight').val(),
                material: $('#material').val(),
                cableLength: $('#cableLength').val(),
                screen: $('#screen').val(),
                rfid: $('#rfid').val(),
                emergencyBtn: $('#emergencyBtn').val(),
                communicationInterface: $('#communicationInterface').val(),
                lang: $('#lang').val(),
                coolingMethod: $('#coolingMethod').val(),
                emc: $('#emc').val(),
                protection: $('#protection').val(),
                opFunc: $('#opFunc').val(),
                standard: $('#standard').val(),
                powerModule: $('#powerModule').val(),
                charger: $('#charger').val(),
            };
    
            if (modalCon) modelId = selectRow.find('td').eq(0).attr('id');
            const URL = modalCon ? `/system/model/update/${modelId}` : '/system/model/new';
            const TYPE = modalCon ? 'PATCH' : 'POST';
    
            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function(response) {
                    window.location.reload();
                },
                error: function(error) {
                    alert(error);
                }
            });
        }
    });

    function getConnectorList() {
        const connectorRows = document.querySelectorAll("#connectorTable .connector-row");
        const connectorInfoList = [];
        var isValid = true;  // 유효성 플래그

        connectorRows.forEach(row => {
            const connectorId = row.querySelector(".connectorId").value;
            const connectorType = row.querySelector(".connectorType").value;

            if (!connectorId || connectorId.trim() === "") {
                isValid = false;  // 유효하지 않음
                return;  // 더 이상 진행하지 않고 루프를 중단함
            }

            // 각 행의 데이터를 객체로 추가
            connectorInfoList.push({
                connectorId: connectorId,
                connectorType: connectorType
            });
        });

        if (!isValid) {
            return null;  // 유효하지 않으면 null 반환
        }

        console.log("커넥터 정보:", connectorInfoList);
        return connectorInfoList;
    }
});