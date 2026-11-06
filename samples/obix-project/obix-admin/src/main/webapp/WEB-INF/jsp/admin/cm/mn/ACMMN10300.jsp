<%--
/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 메인
 * 설명 : 즐겨찾기 관리
 *
 * 파일명 : ACMMN10300.jsp
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 14.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021. 6. 14.         정영탁            최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<style>
    .jstree .jstree-anchor.fvrt-config.jstree-hovered, .jstree .jstree-anchor.fvrt-config.jstree-clicked {
        background: #eaeaea;
        border-radius: 2px;
        box-shadow: none;
        text-decoration: underline;
        font-weight: bold;
    }
</style>
<script type="text/javascript">
//<![CDATA[

$(function() {

    var userFvrtList = [];      // 즐겨찾기 메뉴 배열

    var treeId = "fvrt-config-jstree";
    var tree;
    //--------------------------화면 초기화--------------------------

    httpAjax("/admin/cm/mn/ACMMN10301.json", null, function(data) {
        var userFvrtList = [];
        if(data.result.fvrt && data.result.fvrt.fvrtCtnt) {
            // 즐겨찾기 존재 시
            userFvrtList = JSON.parse(data.result.fvrt.fvrtCtnt);
            $.each(userFvrtList, function(i, fvrt) {
                // 즐겨찾기 객체 유효성 검증
                fvrt = validateFvrt(fvrt);
                // order 설정(즐겨찾기 순서 저장하고 위로, 아래로 이동 위해)
                fvrt.data.order = i; // i와 order 일치                
                if(fvrt.children && fvrt.children.length > 0) {
                    // children에도 같은 처리
                    $.each(fvrt.children, function(i, child) {
                        fvrt = validateFvrt(fvrt);
                        child.data.order = i;
                    });
                }
            });

            // 즐겨찾기가 유효하면 그대로 리턴, 유효하지못하면 type: "error" 객체 리턴
            function validateFvrt(fvrt) {
                /*
                fvrt의 필수 항목
                (1) id : 화면거래아이디(폴더는 임의생성 아이디)
                (2) type : page, folder
                (3) data : 
                    페이지 - 서비스아이디(화면거래아이디), 서비스타입(view, indv..), 주소
                    폴더 - data 객체만 필수
                */     
                if(!fvrt || !fvrt.id || !fvrt.type || !fvrt.data
                || (fvrt.type == "page" && (!fvrt.data.serviceId || !fvrt.data.serviceType || !fvrt.data.urlAddr))) {
                    // 유효한 즐겨찾기 데이터 아닐 시 type:'error' 리턴
                    if(!fvrt) fvrt = {};
                    if(fvrt.text) {
                        fvrt.text = fvrt.text + "(데이터 오류, 저장 시 자동 삭제)";
                    } else {
                        fvrt.text = "(데이터 오류, 저장 시 자동 삭제)";
                    }
                    fvrt.type = "error";
                    fvrt.data = {};
                }
                return fvrt;
            }
        } else {
            // 즐겨찾기 없을 시
            $('#fvrt-config-jstree').next().children("span").show().text("등록된 즐겨찾기가 없습니다.");
        }
        
        // 즐겨찾기 유무 상관없이 공통
        $('#fvrt-config-jstree').jstree({
            core: { 
                data: userFvrtList 
                , check_callback: true
                , themes: {
					icons: false
				}
            }            
            , types: {
                default: {
					a_attr: {
						class: "jstree-menu fvrt-config"
					}
				}
                , folder: {
                    a_attr: {
						class: "jstree-menu menu-category fvrt-config"
					}
                    // , icon: "far fa-folder"
                }
                , page: {
                    a_attr: {
						class: "jstree-menu menu-page fvrt-config"
					}
                    // , icon: "fas fa-circle new-fs3"
                }       
                , error: {
                    a_attr: {
						class: "jstree-menu fvrt-config text-danger"
					}
                    // , icon: "fas fa-exclamation-triangle"
                }               
            }
            , sort: function(a, b) {
                // order 비교 정렬
                return (Number(this.get_node(a).data.order) > Number(this.get_node(b).data.order)) ? 1 : -1;
            }
            , plugins: [
                "types"
                , "changed"
                // , "wholerow"
                , "sort"
            ]
        })
        .bind('loaded.jstree', function(e, data) {
            //font-awesome 폴더 아이콘 제어
            $('#fvrt-config-jstree').on("open_node.jstree", function(e, data) {
                data.instance.set_icon(data.node.id, "far fa-folder-open");
            });
            $('#fvrt-config-jstree').on("close_node.jstree", function(e, data) {  
                data.instance.set_icon(data.node.id, "far fa-folder");
            });

            tree = $(this).jstree(true);
            tree.redraw();
        });

    });

    //--------------------------이벤트--------------------------

    // 즐겨찾기 폴더 추가
    $("#btnFvrtAddFldr").click(function() {
        $('#fvrt-config-jstree').next().children("span").hide();
        // 폴더 id는 F + 난수 6자리 + _FVRT (페이지 id는 화면거래ID + _FVRT)
        var id = "F"+Math.floor(Math.random()*1000000) + "_FVRT";
        var newFldr = {
            id: id
            , parent: "#"
            , text: "새 폴더"
            , type: "folder"
            , data: {
                order: tree.get_json().length // order는 0부터 시작
            }
        };

        tree.create_node("#", newFldr, newFldr.data.order, function(node) {            
            tree.edit(node);
        });
    });

    // 즐겨찾기 이름 바꾸기 : 복수 비허용
    $("#btnFvrtChngNm").click(function() {
        if(tree.get_selected().length < 1) {
            biz.showAlert("수정할 즐겨찾기를 선택해주세요.");
            return;
        }
        if(tree.get_node(tree.get_selected()[0]).type == 'error') {
            return;
        }

        // 마지막으로 선택한 노드
        tree.edit(tree.get_selected()[tree.get_selected().length-1]);
    });

    // 즐겨찾기 1칸 위로 : 복수 비허용
    $("#btnFvrtUp").click(function() {
        if(tree.get_selected().length < 1) return;

        // 마지막으로 선택한 노드
        var node = tree.get_node(tree.get_selected()[tree.get_selected().length-1]);
        var order = node.data.order;
        // 이미 최상위면 리턴
        if(order == 0) return;

        var parent = tree.get_parent(node);
        var siblings = tree.get_node(parent).children;
        $.each(siblings, function(i, e) {
            var sib = tree.get_node(e);
            if(sib.data.order == order - 1) {
                sib.data.order++;
                node.data.order--;
                return false;
            }
        });

        tree.sort(parent, true);
        tree.redraw_node(parent, true);

    });

    // 즐겨찾기 1칸 아래로 : 복수 비허용
    $("#btnFvrtDn").click(function() {
        if(tree.get_selected().length < 1) return;

        // 마지막으로 선택한 노드
        var node = tree.get_node(tree.get_selected()[tree.get_selected().length-1]);
        var order = node.data.order;

        var parent = tree.get_parent(node);
        var siblings = tree.get_node(parent).children;
        
        // 이미 최하위면 리턴
        if(order == siblings.length - 1) return;

        $.each(siblings, function(i, e) {
            var sib = tree.get_node(e);
            if(sib.data.order == order + 1) {
                sib.data.order--;
                node.data.order++;
                return false;
            }
        });

        tree.sort(parent, true);
        tree.redraw_node(parent, true);
    });

    // 즐겨찾기 삭제 : 복수 허용
    $("#btnFvrtDel").click(function() {
        if(tree.get_selected().length < 1) {
            biz.showAlert("삭제할 즐겨찾기를 선택해주세요.");
            return;
        }
        
        // 선택한 노드 모두
        var selected = tree.get_selected();
        var parent = tree.get_node(tree.get_parent(selected[0]));
        var siblings = tree.get_node(parent).children;

        $.each(selected, function(i, e) {
            tree.delete_node(e);
        });
        
        // 삭제된 요소의 형제 요소들 order 갱신해주기
        if(siblings.length > 0) {    
            $.each(siblings, function(i, e) {
                var sib = tree.get_node(e);
                sib.data.order = i;
            });
        }
    });

    // 확인 버튼
    $("#btnConfFvrtConf").click(function() {
        biz.showConfirm("저장하시겠습니까?", null, function() {
            $.each(tree._model.data, function(i, e) {
                // 에러 데이터 삭제
                if(e.type == "error") {
                    tree.delete_node(e.id);
                } else {
                    // tree 속성 없애기(icon, class)
                    e.icon = "";
                    if(e.a_attr) e.a_attr.class = ""; 
                }
            });
            // tree의 state 없애기(opend, selected)
            tree.close_all();
            tree.deselect_all();            
            
            // jstree data 추출
            var treeData = tree.get_json();
            var param = {
                fvrtCtnt:JSON.stringify(treeData)
            };
            httpAjax("/admin/cm/mn/ACMMN10302.json", param, function(data) {
                $("#btnCloseFvrtConf").click();
            }, null, {fragmentJSONRequest: true});
        });
    });

    // 닫기 버튼
    $("#btnCloseFvrtConf").one("click", function() {
        if($('#fvrt-config-jstree').jstree()) $('#fvrt-config-jstree').jstree(true).destroy();
        $("#btnFvrtAddFldr, #btnFvrtChngNm, #btnFvrtDn, #btnFvrtDel, #btnConfFvrtConf").unbind("click");
        popup.closeLayerPopup();
    }); 

});

//]]>
</script>

	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 950px;">
			<div class="modal-header">
				<h5 class="modal-title align-self-center">즐겨찾기 관리</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onclick="popup.closeLayerPopup();">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			<div class="modal-body">

<div class="border-box">
    <div id="fvrt-config-jstree">
    </div>
    <div class="d-flex">
        <span class="text-muted new-mb10" style="display: none;"></span>
    </div>
</div>
<div class="new-pb15 new-mb15 border-bottom d-flex">
    <button type="button" id="btnFvrtAddFldr" class="btn btn-outline-primary new-mr10 flex-child-auto">    <i class="new-pr5 fas fa-folder-plus"></i>새 폴더</button>
    <button type="button" id="btnFvrtChngNm" class="btn btn-outline-primary new-mr10 flex-child-auto">            <i class="new-pr5 fas fa-pen"></i>이름바꾸기</button>
    <button type="button" id="btnFvrtUp" class="btn btn-outline-primary new-mr10 flex-child-auto">             <i class="new-pr5 fas fa-caret-up"></i>위로</button>
    <button type="button" id="btnFvrtDn" class="btn btn-outline-primary new-mr10 flex-child-auto">    <i class="new-pr5 fas fa-caret-down"></i>아래로</button>
    <button type="button" id="btnFvrtDel" class="btn btn-outline-primary flex-child-auto">                     <i class="new-pr5 fas fa-trash"></i>삭제</button>
</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger new-mr10" id="btnConfFvrtConf">확인</button>
				<button type="button" class="btn btn-outline-primary" id="btnCloseFvrtConf">닫기</button>
			</div>
		</div>
	</div>
