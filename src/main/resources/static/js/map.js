document.addEventListener("DOMContentLoaded", function() {
    // var places = placeList(stationList);
    var places;

    // 지도에 출력할 장소 정보 리턴
    function placeList(stationList) {
        var list = stationList.map(function(station) {
            return {
                content: `<div class="infowindow"><div>${station.stationName}</div><div>${station.address}</div></div>`,
                stationId: station.stationId,
                stationName: station.stationName,
                latitude: station.latitude,
                longitude: station.longitude,
                latlng: new kakao.maps.LatLng(station.latitude, station.longitude)
            }
        });
        return list;
    }

    // map
    var container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
    var options = {                                 // 지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(37.480025, 126.878101), // 지도의 중심좌표
        level: 5                                    // 지도의 레벨(확대, 축소 정도)
    };

    var map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
    var markers = [];                                 // 지도에 표시된 마커의 정보를 저장
    
    // 마커 클러스터 생성
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 10,
        disableClickZoom: true
    });

    // custom 마커이미지 정보
    var imgSrc = '../../img/marker.png',
        imgSize = new kakao.maps.Size(36, 50),
        imgOption = {offset: new kakao.maps.Point(27, 69)};
    
    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성
    var markerImg = new kakao.maps.MarkerImage(imgSrc, imgSize, imgOption),
        markerPos = new kakao.maps.LatLng(37.480025, 126.878101);

    // 사용자 접속 위치를 초기 중심좌표로 세팅
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var currLat = position.coords.latitude;
            var currLng = position.coords.longitude;
            var currPos = new kakao.maps.LatLng(currLat, currLng);

            // 사용자 위치 마커 표시
            // var userMarker = new kakao.maps.Marker({
            //     map: map,
            //     position: currPos,
            //     image: userMarkerImg
            // });
            // markers.push(userMarker);

            var center = map.getCenter();
            removeMarkers();
            clusterer.clear();

            $.ajax({
                url: `/map/nearby`,
                type: 'GET',
                data: {
                    latitude: center.getLat(),
                    longitude: center.getLng()
                },
                success: function(response) {
                    console.log('Nearby places:', response.stationsList);

                    places = placeList(response.stationsList);
                    nearbyStationList(response.stationsList);
                    mapMarkers();
                },
                error: function(error) {
                    console.log('Error:', error);
                }
            });

            map.setCenter(currPos);
            map.setLevel(5);
        });
    } else {
        alert("현재 위치를 가져올 수 없습니다.");
    }

    // 마커 생성
    // var marker = new kakao.maps.Marker({
    //     position: markerPos,
    //     image: markerImg
    // });
    // marker.setMap(map);

    // var ps = new kakao.maps.services.Places(); // 장소 검색 객체 생성
    // mapMarkers();

    // 지도에 마커 표시 함수
    function mapMarkers() {
        for (var i = 0, len = places.length; i < len; i++) {
            (function(markerPosition) {
                // 마커를 생성하고 지도 위에 표시
                var marker = new kakao.maps.Marker({
                    map: map,
                    position: markerPosition.latlng,
                    image: markerImg
                });
        
                // 마커에 표시할 인포윈도우 생성
                var infowindow = new kakao.maps.InfoWindow({
                    content: markerPosition.content,
                    removable: true
                });
        
                marker.stationId = markerPosition.stationId;
                markers.push(marker);

                // 클러스터리에 마커 추가
                clusterer.addMarkers(markers);
    
                kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
                kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
                kakao.maps.event.addListener(marker, 'click', function() {
                    moveMapCenter(marker);
                    infowindow.close();
                    searchStationDetail(marker.stationId);
                    document.getElementById('stationInfoDetail').style.display = 'block';
                });
            })(places[i]);
        }
    }

    // 클러스터 클릭 시 현재 지도 레벨에서 1레벨 확대
    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
        var level = map.getLevel() - 1;
        map.setLevel(level, {anchor: cluster.getCenter()});
    });

    // 인포윈도우 지도에 표시
    function makeOverListener(map, marker, infowindow) {
        return function() {
            infowindow.open(map, marker);
        };
    }

    // 인포윈도우 지도에서 제거
    function makeOutListener(infowindow) {
        return function() {
            infowindow.close();
        };
    }

    // 지도 중심좌표 변경 함수
    function moveMapCenter(marker) {
        var pos = marker.getPosition();
        // map.setCenter(pos);
        map.panTo(pos);
    }

    // 검색 버튼 클릭 시 장소 검색 수행
    document.getElementById('searchBtn').addEventListener('click', function() {
        // searchPlaces();
        searchStationKeyword();
    });

    // Enter 키 입력으로도 검색 가능
    document.getElementById('searchBox').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            searchStationKeyword();
        }
    });

    // 사용자 주변 충전소
    function nearbyStationList(list) {
        var searchStationList = $('#searchStationList');
        searchStationList.empty().append(`<div class="nearby-station">주변충전소</div>`);

        list.forEach(function(csList) {
            searchStationList.append(`<div class="search-station" id="${csList.stationId}">
                                        <div>${csList.stationName}</div>
                                        <div>${csList.address}</div>
                                    </div>`);
        });
        stationClickEvent();
    }

    // 충전소 리스트 출력 함수
    function showStationList(list) {
        var searchStationList = $('#searchStationList');
        searchStationList.empty();

        list.forEach(function(csList) {
            searchStationList.append(`<div class="search-station" id="${csList.stationId}">
                                        <div>${csList.stationName}</div>
                                        <div>${csList.address}</div>
                                    </div>`);
        });
        stationClickEvent();
    }

    function stationClickEvent() {
        var stations = document.querySelectorAll('.search-station');
        stations.forEach(function(station) {
            station.addEventListener('click', function() {
                var stationId = station.id;
                searchStationDetail(stationId);
                document.getElementById('stationInfoDetail').style.display = 'block';
            });
        });
    }

    // 충전소명 검색 함수
    function searchStationKeyword() {
        const keyword = $('#searchBox').val();

        $.ajax({
            url: `/map/search/station/${keyword}`,
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: function(response) {
                // removeMarkers();
                // clusterer.clear();

                if (response.csList) {
                    document.getElementById('stationInfoDetail').style.display = 'none';
                    showStationList(response.csList);
                }
            },
            error: function(error) {
                console.log(error);
            }
        });
    }

    // 충전소 정보 상세 조회
    function searchStationDetail(stationId) {
        $.ajax({
            url: `/map/search/detail/${stationId}`,
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: function(response) {
                map.setLevel(1);
                if (response.csInfo) {
                    document.getElementById('stationNameTitle').innerText = response.csInfo.stationName || '';
                    document.getElementById('stationName').innerText = response.csInfo.stationName || '-';
                    document.getElementById('companyName').innerText = response.csInfo.companyName || '-';
                    document.getElementById('stationId').innerText = response.csInfo.stationId || '-';
                    document.getElementById('address').innerText = response.csInfo.address || '-';
                    document.getElementById('opStatusName').innerText = response.csInfo.opStatusName || '-';
                    document.getElementById('parkingFeeYn').innerText = response.csInfo.parkingFeeString || '-';
                    document.getElementById('asNum').innerText = response.csInfo.asNum || '-';

                    // 해당 충전소로 좌표이동
                    var currPos = new kakao.maps.LatLng(response.csInfo.latitude, response.csInfo.longitude);
                    // map.setCenter(currPos);
                    map.panTo(currPos);
                    updateMapInfo();
                }

                if (response.cpList) {
                    var chargerList = $('#chargerList');
                    chargerList.empty();

                    response.cpList.forEach(function(cpList) {
                        chargerList.append(`<div class="row row-cols-2 charger-info">
                                                <div class="col-4">충전기ID</div>
                                                <div class="col-8">${cpList.chargerId}</div>
                                                <div class="col-4">모델명</div>
                                                <div class="col-8">${cpList.modelName}</div>
                                                <div class="col-4">제조사</div>
                                                <div class="col-8">${cpList.manufCdName}</div>
                                                <div class="col-4">상태</div>
                                                <div class="col-8">-</div>
                                                <div class="col-4">펌웨어버전</div>
                                                <div class="col-8">${cpList.fwVersion}</div>
                                                <div class="col-4">유형</div>
                                                <div class="col-8">${cpList.cpTypeName}</div>
                                                <div class="col-4">위치</div>
                                                <div class="col-8">${cpList.location}</div>
                                                <div class="col-4">최근<br>충전일시</div>
                                                <div class="col-8 charging-time">-</div>
                                            </div>`);
                    });
                } else {
                    var chargerList = $('#chargerList');
                    chargerList.empty();
                    chargerList.append(`<div class="pt-2">${response.message}</div>`);
                }
            },
            error: function(error) {
                console.log(error);
            }
        });
    }

    // 마커 삭제 함수
    function removeMarkers() {
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);    // 지도에서 마커 제거
        }
        markers = [];   // 초기화
    }

    // 사용자 현재 위치 정보
    var userImgSrc = '../../img/user_marker.png',
        userimgSize = new kakao.maps.Size(36, 50),
        userimgOption = {offset: new kakao.maps.Point(27, 69)};
    var userMarkerImg = new kakao.maps.MarkerImage(userImgSrc, userimgSize, userimgOption);

    document.getElementById('currentLocationBtn').addEventListener('click', function() {
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var currLat = position.coords.latitude;
                var currLng = position.coords.longitude;
                var currPos = new kakao.maps.LatLng(currLat, currLng);

                // 현재 위치에 마커 표시
                var userMarker = new kakao.maps.Marker({
                    map: map,
                    position: currPos,
                    image: userMarkerImg
                });

                markers.push(userMarker);
                // map.setCenter(currPos);
                map.panTo(currPos);
                map.setLevel(3);
            });
        }else {
            alert("현재 위치를 가져올 수 없습니다.");
        }
    });

    // 충전소 상세 내용 닫기
    document.getElementById('infoCloseBtn').addEventListener('click', function() {
        document.getElementById('stationInfoDetail').style.display = 'none';
    });

    // 지도가 이동한 후 발생하는 이벤트 등록
    kakao.maps.event.addListener(map, 'dragend', function() {
        updateMapInfo();
    });

    // 지도 정보 업데이트
    function updateMapInfo() {
        var center = map.getCenter();
        removeMarkers();
        clusterer.clear();

        $.ajax({
            url: `/map/nearby`,
            type: 'GET',
            data: {
                latitude: center.getLat(),
                longitude: center.getLng()
            },
            success: function(response) {
                places = placeList(response.stationsList);
                mapMarkers();
            },
            error: function(error) {
                console.log('Error:', error);
            }
        });
    }
});