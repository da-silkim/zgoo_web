document.addEventListener("DOMContentLoaded", function() {
    // 사용자 정의 장소 데이터
    var places = [
        { name: '동아일렉콤(서울연구소)', lat: 37.480025, lng: 126.878101 },
        { name: '동아일렉콤(본사)', lat: 37.232039, lng: 127.284647 },
        { name: '동아일렉콤(PRC)', lat: 37.226655, lng: 127.299036 }
    ];

    places = csList;

    // map
    var container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
    var options = { // 지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(37.480025, 126.878101), // 지도의 중심좌표
        level: 3 // 지도의 레벨(확대, 축소 정도)
    };

    var map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
    var markers = [];
    
    // 가산어반워크 마커
    var imgSrc = '../../img/marker.png',
        imgSize = new kakao.maps.Size(36, 50),
        imgOption = {offset: new kakao.maps.Point(27, 69)};
    
    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성
    var markerImg = new kakao.maps.MarkerImage(imgSrc, imgSize, imgOption),
        markerPos = new kakao.maps.LatLng(37.480025, 126.878101);
    
    // 마커 생성
    var marker = new kakao.maps.Marker({
        position: markerPos,
        image: markerImg
    });

    marker.setMap(map);

    var ps = new kakao.maps.services.Places(); // 장소 검색 객체 생성

    // 검색 버튼 클릭 시 장소 검색 수행
    document.getElementById('searchBtn').addEventListener('click', function() {
        searchPlaces();
    });


    // Enter 키 입력으로도 검색 가능
    document.getElementById('searchBox').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            var firstSuggestion = document.querySelector('#autocompleteList div');
            if (firstSuggestion) {
                firstSuggestion.click(); // 첫 번째 항목 선택
            }
        }
    });

    function searchPlaces() {
        var keyword = document.getElementById('searchBox').value;
        if (keyword.trim() !== '') {
            removeMarkers();    // 기존 마커 제거
            ps.keywordSearch(keyword, placesSearchCB);
        }
    }
    
    // 마커 삭제 함수
    function removeMarkers() {
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);    // 지도에서 마커 제거
        }
        markers = [];   // 초기화
    }

    // 장소 검색 완료 시 호출되는 콜백 함수
    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            // map.setMap(null);
            var bounds = new kakao.maps.LatLngBounds();

            for (var i=0; i<data.length; i++) {
                var place = data[i];
                var coords = new kakao.maps.LatLng(place.y, place.x);

                // 마커를 지도에 표시
                var placeMaker = new kakao.maps.Marker({
                    map: map,
                    position: coords
                });

                markers.push(placeMaker);  // 배열에 마커 정보 저장
                bounds.extend(coords); // 영역 확장
            }
            // 검색된 장소를 지도의 중앙으로 설정
            map.setBounds(bounds);
        }
    }

    // 사용자 현재 위치 정보
    var userImgSrc = '../../img/user_marker.png',
        userimgSize = new kakao.maps.Size(36, 50),
        userimgOption = {offset: new kakao.maps.Point(27, 69)};
    var userMarkerImg = new kakao.maps.MarkerImage(userImgSrc, userimgSize, userimgOption);

    document.getElementById('currentLocationBtn').addEventListener('click', function() {
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude;
                var lng = position.coords.longitude;
                var locPos = new kakao.maps.LatLng(lat, lng);

                // 현재 위치에 마커 표시
                var userMarker = new kakao.maps.Marker({
                    map: map,
                    position: locPos,
                    image: userMarkerImg
                });

                map.setCenter(locPos);  // 지도의 중심을 현재 위치로 이동
                map.setLevel(3);        // zoom level init
            });
        }else {
            alert("현재 위치를 가져올 수 없습니다.");
        }
    });

    // 자동 완성 함수
    function autocomplete(val) {
        let suggestions = places.filter(place => place.stationName.toLowerCase().includes(val.toLowerCase()));
        displaySuggestions(suggestions);
    }

    // 자동 완성 결과 표시
    function displaySuggestions(suggestions) {
        const autocompleteList = document.getElementById('autocompleteList');
        autocompleteList.innerHTML = ''; // 기존 목록 초기화

        suggestions.forEach(suggestion => {
            const item = document.createElement('div');
            item.textContent = suggestion.stationName;
            item.addEventListener('click', function() {
                selectPlace(suggestion); // 장소 선택 시 처리
            });
            autocompleteList.appendChild(item);
        });
    }

    // 장소 선택 시 처리
    function selectPlace(place) {
        if (marker) {
            marker.setMap(null); // 기존 마커 제거
        }

        // 새로운 마커 생성
        var markerPosition = new kakao.maps.LatLng(place.latitude, place.longitude);
        marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImg
        });
        
        marker.setMap(map); // 지도에 마커 표시
        map.setCenter(markerPosition); // 지도 중심 이동
        map.setLevel(3); // 지도 레벨 재설정

        // 자동완성 목록 숨김
        document.getElementById('autocompleteList').innerHTML = '';
        document.getElementById('searchBox').value = place.stationName; // 입력란에 선택한 장소 표시
    }

    // 입력 시 자동 완성 기능 동작
    document.getElementById('searchBox').addEventListener('input', function() {
        var val = this.value;
        if (val.length > 0) {
            autocomplete(val); // 자동 완성 검색
        } else {
            document.getElementById('autocompleteList').innerHTML = ''; // 입력이 없으면 목록 비우기
        }
    });
});