$(function() {
    //depAndRoles();

    $(document).on('click', '.collapse-button-x, .collapse-button-y', function() {
        $(this).toggleClass('active');
        $($(this).data('target')).toggleClass('active');

        if ($(this).data('func')) {
            window[$(this).data('func')]();
        }
    });
});

var Dec = opener ? opener.parent.parent.Dec : parent.parent.Dec;
var BI = opener ? opener.parent.parent.BI : parent.parent.BI;

var userId = Dec ? Dec.personal.username : '';
var fineServletURL = Dec ? Dec.fineServletURL : '/webroot/decision';

function toast(msg, option) {
    if (BI) {
        BI.Msg.toast(msg, option);

    } else {
        alert(msg);
    }
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

/*
    부서/직무/역할 조회
*/
function depAndRoles() {
    var data = {};

    try {
        if (parent.Dec.userInfo.uplus.channels) {
            return;
        }

        data.page = page;

    } catch (e) {}

    $.ajax({
        url         : fineServletURL + '/uplus/v2/depAndRoles',
        type        : 'GET',
        contentType : 'application/json',
        async       : false,
        data        : data,
        success     : function(res) {
            $.extend(Dec.userInfo, {uplus: res});
        },
        error       : function(request,status,error) {
            toast('권한 조회 실패');
        }
    });
}

/*
    array to tree (for treeselect)

    @param {Object} [options]
           {Array}  [options.data]   - 변환하고자 하는 배열
           {String} [options.value]  - value key
           {String} [options.name]   - name key
           {String} [options.parent] - parent key
*/
function arrayToTree(options) {
    var tree = [];
    var maps = {};

    $(options.data).each(function() {
        maps[this[options.value]] = {
            name: this[options.name],
            value: this[options.value],
            parent: this[options.parent],
            children: [],
            htmlAttr: {'data-value': this[options.value]}
        };
    });

    for (var id in maps) {
        var elem = maps[id];
        if (elem['parent']) {
            maps[elem['parent']]['children'].push(elem);
        } else {
            tree.push(elem);
        }
    }

    sort(tree);

    return tree;
}

/*
    org array to tree (for treeselect) -- 조직정보 tree 구조로 변환

    @param {Array} [data] - 조직정보
*/
function orgToTree(data) {
    var tree = [];
    var maps = {};

    $(data).each(function() {
        var name = this.org_name,
            value = this.depth == 1 ? this.org_name : this.org_cd,
            depth = this.depth,
            parent = this.depth == 2 ? this.chn_nm : this.hirk_org_cd,
            channel = this.chn_nm;

        maps[value] = {
            name: name,
            value: value,
            parent: parent,
            channel: channel,
            children: [],
            htmlAttr: {'data-value': value, 'data-depth': depth}
        };
    });

    for (var id in maps) {
        var elem = maps[id];
        if (elem['parent']) {
            maps[elem['parent']]['children'].push(elem);
        } else {
            tree.push(elem);
        }
    }

    sort(tree);

    return tree;
}

/*
    이름순 정렬 (for treeselect)
*/
function sort(t) {
    $(t).each((i, e) => {
        if (e.children.length > 0) {
            e.children.sort((a, b) => {
                return a.name < b.name ? -1 : a.name > b.name ? 1 : 0;
            });

            if (e.children.find(o => o.children.length > 0)) {
                sort(e.children);
            }
        }
    });
}

function orgToMap(org, users) {
    var orgMap = {};

    org.forEach(e => {
        var org_cd = e.depth == 1 ? e.chn_nm : e.org_cd;

        orgMap[org_cd] = {
            org_cd: org_cd,
            org_cd_name: e.depth <= 2 ? e.org_name : (e.cen_nm + ' > ' + e.org_name),
            depth: e.depth,
            chn_cd: e.chn_cd,
            cen_cd: e.cen_cd,
            grp_cd: e.grp_cd,
            tem_cd: e.tem_cd,
            chn_cd_name: e.chn_nm,
            cen_cd_name: e.cen_nm || undefined,
            grp_cd_name: e.grp_nm ? e.cen_nm + ' > ' + e.grp_nm : undefined,
            tem_cd_name: e.tem_nm ? e.cen_nm + ' > ' + e.tem_nm : undefined,
            fl_org_cd: e.fl_org_cd,
            fl_org_name: e.fl_org_name
        };

        if (users && users.length > 0) {
            orgMap[org_cd].agids = users.filter(u => u.org_cd == e.org_cd && u.agid).map(u => u.agid);
        }
    });

    return orgMap;
}

/*
    treeselect listSlotHtmlComponent - 전체선택 체크박스
*/
function getAllSelectSlot(var_ts) {
    var slot = document.createElement('a');
        slot.classList.add('treeselect-list__item');
        slot.classList.add('treeselect-allselect');
        slot.setAttribute('href', 'javascript:void(0);');
        slot.setAttribute('onclick', 'clickAllSelectSlot("' + var_ts + '");');

    var html =  '<div class="treeselect-list__item-checkbox-container ml-1">';
        html += '    <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>';
        html += '</div>';
        html += '<label class="treeselect-list__item-label">전체선택</label>';

    slot.innerHTML = html;

    return slot;
}

/*
    treeselect listSlotHtmlComponent - 전체선택 체크박스
    click 이벤트
*/
function clickAllSelectSlot(var_ts) {
    var ts = window[var_ts];
    if (!ts) {
        return;
    }

    var $slot = $(ts.listSlotHtmlComponent);
    $slot.toggleClass('treeselect-list__item--checked');

    var result = null;
    var isChecked = $slot.hasClass('treeselect-list__item--checked'); // 전체선택 체크여부

    if (ts.searchText) {
        // 검색 중인 경우 검색 결과에 대한 데이터만 처리
        var item = $(ts.srcElement).find('.treeselect-list__item:visible').not('.treeselect-allselect');
        var itemValue = Array.from(item.map((i, item) => String($(item).data('value'))));

        if (isChecked) {
            result = [...new Set(ts.allValue.concat(itemValue))];

        } else {
            result = ts.allValue.filter(e => itemValue.indexOf(e) < 0);
        }

    } else {
        if (isChecked) {
            result = ts.options.map(e => e.value);
        }
    }

    ts.updateValue(result);
}

/*
    treeselect listSlotHtmlComponent - 전체선택 체크박스
    선택값에 따른 전체선택 체크박스 제어
*/
function treeselectAll(ts) {
    if (!ts) {
        return;
    }

    var $item = $(ts.srcElement).find('.treeselect-list__item:visible').not('.treeselect-allselect');
    var $itemChecked = $(ts.srcElement).find('.treeselect-list__item--checked:visible').not('.treeselect-allselect');

    if ($item.length > 0 && $item.length == $itemChecked.length) {
        $(ts.listSlotHtmlComponent).addClass('treeselect-list__item--checked');
        ts.allChecked = true;

    } else {
        $(ts.listSlotHtmlComponent).removeClass('treeselect-list__item--checked');
        ts.allChecked = false;
    }
}

/*
    muuri grid 각 box 영역 height 지정
    grid layout 라이브러리로 인해 빈 공간이 채워지는 것을 재조정하기 위함 (가시성)
*/
function setGridLayout(init) {
    if ($('.box-wrapper').length < 1) {
        return;
    }

    // 화면에 보여지는 컬럼 수 체크
    var top = $('.box-wrapper').eq(0).offset().top;
    var size = $('.box-wrapper').filter((i, e) => $(e).offset().top == top).length;

    // 해당하는 row에서 최대 높이를 가진 box 영역과 동일하게 변경
    if (col_size != size || init) {
        // row별로 진행
        var roop = $('.box-wrapper').length;
        for (var i=0; i<roop; i+=size) {
            var h = 'auto';
            var subItems = $('.box-wrapper').slice(i, i+size);

            if (subItems.find('.item').length > 0) {
                subItems.sort((a, b) => $(b).find('.box').height() - $(a).find('.box').height());
                h = $(subItems[0]).find('.box').height();
            }

            $(subItems).height(h);
        }

        col_size = size;
        grid.refreshItems().layout();
    }
}

class MultiFilter {
    constructor(page, type, colNm, defaultYn, mergeYn) {
        this.userId    = userId || null;
        this.page      = page || null;
        this.type      = type || null;
        this.colNm     = colNm || null;
        this.col       = null;
        this.col2      = null;
        this.col3      = null;
        this.col4      = null;
        this.col5      = null;
        this.defaultYn = defaultYn || 'N';
        this.mergeYn   = mergeYn || 'N';
        this.cols  = [];
        this.values  = [];
    }

    addData(type, data, options) {
        if (!type) {
            return;
        }

        var arr = data == undefined ? [] : (Array.isArray(data) ? data : new Array(data));
        if (arr.length < 1) {
            return;
        }

        var mf = new MultiFilter(this.page, type, this.colNm, this.defaultYn, this.mergeYn);
        mf.colNm = (options && options.colNm) || this.colNm;
        mf.col = (options && options.col) || null;
        mf.col2 = (options && options.col2) || null;
        mf.col3 = (options && options.col3) || null;
        mf.col4 = (options && options.col4) || null;
        mf.col5 = (options && options.col5) || null;
        mf.values = arr.map((e, i) => {
            if (typeof e == 'object' && Object.keys(e).length > 0) {
                return {
                    userId : mf.userId,
                    page   : mf.page,
                    type   : mf.type,
                    value  : e.value,
                    remark : e.remark,
                    sort   : e.sort ? e.sort : (i+1)
                }

            } else {
                return {
                    userId : mf.userId,
                    page   : mf.page,
                    type   : mf.type,
                    value  : e,
                    remark : e,
                    sort   : (i+1)
                }
            }
        });

        this.cols.push(mf);
    }
}

/*
    필터 저장
*/
function updateMultiFilter(params, noMsg) {
    var res;

    $.ajax({
        url         : fineServletURL + '/uplus/v2/multi/filter',
        type        : 'POST',
        async       : false,
        contentType : 'application/json',
        data        : JSON.stringify(params),
        success     : function(data) {
            if (!noMsg) {
                if (data.result) {
                    toast('필터 저장 성공');

                } else {
                    if (data.message) {
                        toast(data.message, {level: 'error'});

                    } else {
                        toast('필터 저장 실패', {level: 'error'});
                    }
                }
            }

            res = data;
        },
        error       : function(request,status,error) {
            toast('필터 저장 실패');
        }
    });

    return res;
}

/*
    초 -> 시:분:초
*/
function getHis(time) {
    time = toNumber(time);

    var hour = Math.floor(time/3600);
    var min = Math.floor(time%3600/60);
    var sec = Math.floor(time%60);
    if (hour < 10) {
        hour = "0" + hour;
    }
    if (min < 10) {
        min = "0" + min;
    }
    if (sec < 10) {
        sec = "0" + sec;
    }

    return hour + ':' + min + ':' + sec;
}

/*
    시:분:초 -> 초
*/
function getSS(time) {
    const arr = time.split(':');
    return (Number(arr[0]) * 3600) + (Number(arr[1]) * 60) + Number(arr[2]);
}

/*
    email 내 id 반환
*/
function getLoginId(str) {
    var str = $.trim(str);

    if (!str || str.indexOf('@') < 0) {
        return str;
    }

    return str.substring(0, str.indexOf('@'));
}

/*
    string to number
    NaN 또는 Infinity인 경우 0 반환
*/
function toNumber(value) {
    return isNaN(value) || !isFinite(value) ? 0 : Number(value);
}

/*
    winker websocket 연결 여부. OPEN(1) 상태인 경우에만 true
    > WebSocket.CONNECTING : 0
    > WebSocket.OPEN       : 1
    > WebSocket.CLOSING    : 2
    > WebSocket.CLOSED     : 3
*/
function isOpen(winker) {
    return winker && winker.wWebSocket && winker.wWebSocket.readyState == WebSocket.OPEN;
}

/*
    winker connection properties
*/
function getWProperties() {
    var result;

    $.ajax({
        url         : fineServletURL + '/uplus/v2/wprop',
        type        : 'GET',
        contentType : 'application/json',
        async       : false,
        success     : function(res) {
            result = res;
        }
    });

    if (result.serverType == 'local') {
        result.wssFlag = true;

    } else {
        result.wssFlag = location.protocol.indexOf('https') > -1;
    }

    result.consoleFlag = false;

    return result;
}

/*
    winker 통계 요청 (메시지당 최대 100kb로 나눠서 요청)
*/
function openStat(winker, dbIDArr, statID) {
    var refid = winker.wGetRefID() ;
    var msg = '';
    var s = 0, e = 0;

    var req_dbID = [];

    dbIDArr.forEach((id, i) => {
        if (!msg) {
            msg = WMessageType.RequestOpenStat + MSG_DIM + refid;
        }
        msg += MSG_DIM + id + STA_DIM + statID;

        var byte = getByte(msg);
        if (byte > 128000) {
            var arr = dbIDArr.slice(s, e);
            winker.wOpenStatExt(arr, statID);
            req_dbID.push(arr);
            msg = '';
            s = e;

        } else {
            if (i >= dbIDArr.length -1) {
                var arr = dbIDArr.slice(s, dbIDArr.length);
                if (arr.length > 0) {
                    winker.wOpenStatExt(arr, statID);
                    req_dbID.push(arr);

                    return;
                }
            }
        }

        e++;
    });

    return req_dbID;
}

/*
    winker 통계 요청 취소
*/
function wsClose(winker, dbIDArr, statID) {
    dbIDArr.forEach(e => winker.wCloseStatExt(e, statID));
}

/*
    websocket error event
*/
function wsError(isAlert) {
    onLoading(false);

    var active = !document.hidden && $('.u-container').is(':visible');
    if (!active) {
        return;
    }

    if (isAlert) {
        alert('통신 실패');

    } else {
        toast('통신 실패', {level: 'error'});
    }
}

/*
    websocket 페이지 오픈 여부에 따른 통계 요청 처리
    페이지가 오픈되어 있지 않은 경우 현재 통계 요청 취소 (ex. 탭 비활성화, 브라우저 최소화)
    다시 오픈되면 재요청
*/
function wsCheckState(winker, active) {
    if (isOpen(winker) && !active && !winker.inactiveCloseFlag) {
        winker.inactiveCloseFlag = true;
        winker.wDisconnect();
    }

    if (winker && active && winker.inactiveCloseFlag) {
        wsConnect();
    }
}

/*
    byte 계산
*/
function getByte(str) {
    var byte = 0;

    for (var i=0; i<str.length; i++) {
        if (str.charCodeAt(i) > 128) {
            byte += 3;

        } else {
            byte++;
        }
    }

    return byte;
}

/*
    로딩 시 중복 클릭 방지 및 로딩 이미지 표시
*/
function onLoading(loading) {
    if (loading) {
        if (!$('.u-container').hasClass('no-pointer-events')) {
            $('.u-container').addClass('no-pointer-events');
        }
        if ($('.u-container').find('.onloading').length < 1) {
            $('.u-container .load-data').before('<span class="onloading"></span>');
        }

    } else {
        if ($('.u-container').hasClass('no-pointer-events')) {
            $('.u-container').removeClass('no-pointer-events');
        }
        if ($('.u-container').find('.onloading').length > 0) {
            $('.u-container .onloading').remove();
        }
    }
}

/*
    현재 팝업 개수 조회
*/
function getPopupCount(key) {
    return toNumber($.cookie(key));
}

/*
    현재 팝업 개수를 cookie에 저장한다
    > 팝업 개수 제한을 위함
*/
function setPopupCount(key, v) {
    var cnt = getPopupCount(key);

    if (v == 'OPEN') {
        $.cookie(key, (cnt+1), {path: '/'});
    } else if (v == 'CLOSE') {
        $.cookie(key, (cnt-1), {path: '/'});
    } else {
        $.cookie(key, v, {path: '/'});
    }
}

function filter_form_close() {
    $('#dialog-filter').dialog('close');
}

function threshold_close() {
    $('#dialog-threshold').dialog('close');
}

function exportReasonForm() {
    window.open(fineServletURL + '/uplus/v2/export/reason', 'exportReason', 'width=400,height=180');
}