<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%--@ include file="/WEB-INF/include/include-header.jspf" --%>
</head>
<body>
    <h2>게시판 목록</h2>
    <table class="board_list">
        <colgroup>
            <col width="10%"/>
            <col width="*"/>
            <col width="15%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
            <tr>
                <th scope="col">글번호</th>
                <th scope="col">제목</th>
                <th scope="col">조회수</th>
                <th scope="col">작성일</th>
            </tr>
        </thead>
        <tbody>
             
        </tbody>
    </table>
     
    <div id="PAGE_NAVI"></div>
    <input type="hidden" id="PAGE_INDEX" name="PAGE_INDEX"/>
     
    <br/>
    <a href="#this" class="btn" id="write">글쓰기</a>
     
    <%--@ include file="/WEB-INF/include/include-body.jspf" --%>
    <script type="text/javascript">
        $(document).ready(function(){
            fn_selectBoardList(1);
             
            $("#write").on("click", function(e){ //글쓰기 버튼
                e.preventDefault();
                fn_openBoardWrite();
            }); 
             
            $("a[name='title']").on("click", function(e){ //제목 
                e.preventDefault();
                fn_openBoardDetail($(this));
            });
        });
         
         
        function fn_openBoardWrite(){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/sample/openBoardWrite.do' />");
            comSubmit.submit();
        }
         
        function fn_openBoardDetail(obj){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/sample/openBoardDetail.do' />");
            comSubmit.addParam("IDX", obj.parent().find("#IDX").val());
            comSubmit.submit();
        }
         
        function fn_selectBoardList(pageNo){
            var comAjax = new ComAjax();
            comAjax.setUrl("<c:url value='/sample/selectBoardList.do' />");
            comAjax.setCallback("fn_selectBoardListCallback");
            comAjax.addParam("PAGE_INDEX",pageNo);
            comAjax.addParam("PAGE_ROW", 15);
            comAjax.ajax();
        }
         
        function fn_selectBoardListCallback(data){
            var total = data.TOTAL;
            var body = $("table>tbody");
            body.empty();
            if(total == 0){
                var str = "<tr>" + 
                                "<td colspan='4'>조회된 결과가 없습니다.</td>" + 
                            "</tr>";
                body.append(str);
            }
            else{
                var params = {
                    divId : "PAGE_NAVI",
                    pageIndex : "PAGE_INDEX",
                    totalCount : total,
                    eventName : "fn_selectBoardList"
                };
                gfn_renderPaging(params);
                 
                var str = "";
                $.each(data.list, function(key, value){
                    str += "<tr>" + 
                                "<td>" + value.IDX + "</td>" + 
                                "<td class='title'>" +
                                    "<a href='#this' name='title'>" + value.TITLE + "</a>" +
                                    "<input type='hidden' name='title' value=" + value.IDX + ">" + 
                                "</td>" +
                                "<td>" + value.HIT_CNT + "</td>" + 
                                "<td>" + value.CREA_DTM + "</td>" + 
                            "</tr>";
                });
                body.append(str);
                 
                $("a[name='title']").on("click", function(e){ //제목 
                    e.preventDefault();
                    fn_openBoardDetail($(this));
                });
            }
        }
        
        
        
        /*
        divId : 페이징 태그가 그려질 div
        pageIndx : 현재 페이지 위치가 저장될 input 태그 id
        recordCount : 페이지당 레코드 수
        totalCount : 전체 조회 건수 
        eventName : 페이징 하단의 숫자 등의 버튼이 클릭되었을 때 호출될 함수 이름
        */
        var gfv_pageIndex = null;
        var gfv_eventName = null;
        function gfn_renderPaging(params){
            var divId = params.divId; //페이징이 그려질 div id
            gfv_pageIndex = params.pageIndex; //현재 위치가 저장될 input 태그
            var totalCount = params.totalCount; //전체 조회 건수
            var currentIndex = $("#"+params.pageIndex).val(); //현재 위치
            if($("#"+params.pageIndex).length == 0 || gfn_isNull(currentIndex) == true){
                currentIndex = 1;
            }
             
            var recordCount = params.recordCount; //페이지당 레코드 수
            if(gfn_isNull(recordCount) == true){
                recordCount = 20;
            }
            var totalIndexCount = Math.ceil(totalCount / recordCount); // 전체 인덱스 수
            gfv_eventName = params.eventName;
             
            $("#"+divId).empty();
            var preStr = "";
            var postStr = "";
            var str = "";
             
            var first = (parseInt((currentIndex-1) / 10) * 10) + 1;
            var last = (parseInt(totalIndexCount/10) == parseInt(currentIndex/10)) ? totalIndexCount%10 : 10;
            var prev = (parseInt((currentIndex-1)/10)*10) - 9 > 0 ? (parseInt((currentIndex-1)/10)*10) - 9 : 1; 
            var next = (parseInt((currentIndex-1)/10)+1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex-1)/10)+1) * 10 + 1 : totalIndexCount;
             
            if(totalIndexCount > 10){ //전체 인덱스가 10이 넘을 경우, 맨앞, 앞 태그 작성
                preStr += "<a href='#this' class='pad_5' onclick='_movePage(1)'>[<<]</a>" +
                        "<a href='#this' class='pad_5' onclick='_movePage("+prev+")'>[<]</a>";
            }
            else if(totalIndexCount <=10 && totalIndexCount > 1){ //전체 인덱스가 10보다 작을경우, 맨앞 태그 작성
                preStr += "<a href='#this' class='pad_5' onclick='_movePage(1)'>[<<]</a>";
            }
             
            if(totalIndexCount > 10){ //전체 인덱스가 10이 넘을 경우, 맨뒤, 뒤 태그 작성
                postStr += "<a href='#this' class='pad_5' onclick='_movePage("+next+")'>[>]</a>" +
                            "<a href='#this' class='pad_5' onclick='_movePage("+totalIndexCount+")'>[>>]</a>";
            }
            else if(totalIndexCount <=10 && totalIndexCount > 1){ //전체 인덱스가 10보다 작을경우, 맨뒤 태그 작성
                postStr += "<a href='#this' class='pad_5' onclick='_movePage("+totalIndexCount+")'>[>>]</a>";
            }
             
            for(var i=first; i<(first+last); i++){
                if(i != currentIndex){
                    str += "<a href='#this' class='pad_5' onclick='_movePage("+i+")'>"+i+"</a>";
                }
                else{
                    str += "<b><a href='#this' class='pad_5' onclick='_movePage("+i+")'>"+i+"</a></b>";
                }
            }
            $("#"+divId).append(preStr + str + postStr);
        }
         
        function _movePage(value){
            $("#"+gfv_pageIndex).val(value);
            if(typeof(gfv_eventName) == "function"){
                gfv_eventName(value);
            }
            else {
                eval(gfv_eventName + "(value);");
            }
        }

        
        
        var gfv_ajaxCallback = "";
        function ComAjax(opt_formId){
            this.url = "";      
            this.formId = gfn_isNull(opt_formId) == true ? "commonForm" : opt_formId;
            this.param = "";
             
            if(this.formId == "commonForm"){
                var frm = $("#commonForm");
                if(frm.length > 0){
                    frm.remove();
                }
                var str = "<form id='commonForm' name='commonForm'></form>";
                $('body').append(str);
            }
             
            this.setUrl = function setUrl(url){
                this.url = url;
            };
             
            this.setCallback = function setCallback(callBack){
                fv_ajaxCallback = callBack;
            };
         
            this.addParam = function addParam(key,value){ 
                this.param = this.param + "&" + key + "=" + value; 
            };
             
            this.ajax = function ajax(){
                if(this.formId != "commonForm"){
                    this.param += "&" + $("#" + this.formId).serialize();
                }
                $.ajax({
                    url : this.url,    
                    type : "POST",   
                    data : this.param,
                    async : false, 
                    success : function(data, status) {
                        if(typeof(fv_ajaxCallback) == "function"){
                            fv_ajaxCallback(data);
                        }
                        else {
                            eval(fv_ajaxCallback + "(data);");
                        }
                    }
                });
            };
        }

    </script> 
</body>
</html>
