<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!--简历基本信息表单-->
<div class="easyui-layout" data-options="fit:true">
  <div class="TableTitle" data-options="region:'north',border:false">
    <span class="label label-primary"> 
    <c:if test="${pageset.operateType==1}">新增${JSP_Messager_Title}记录</c:if>
    <c:if test="${pageset.operateType==2}">修改${JSP_Messager_Title}记录</c:if>
    <c:if test="${pageset.operateType==0}">浏览${JSP_Messager_Title}记录</c:if>
    </span>
  </div>
  <div data-options="region:'center'">
    <form id="dom_formResumebase">
      <input type="hidden" id="entity_id" name="id" value="${entity.id}">
      <table class="editTable">
      <tr>
        <td class="title">
        户口所在地:
        </td>
        <td colspan="3">
          <select name="registered" id="entity_registered"  val="${entity.registered}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        参加工作年份（年）:
        </td>
        <td colspan="3">
          <input id="entity_dateyear" name="dateyear" value="${entity.dateyear}" type="text" class="easyui-datebox" ></input>
        </td>
      </tr>
      <tr>
        <td class="title">
        参加工作年份（月）:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[4]']"
          id="entity_datemonth" name="datemonth" value="${entity.datemonth}">
        </td>
      </tr>
      <tr>
        <td class="title">
        户口所在地（省）:
        </td>
        <td colspan="3">
          <select name="registeredarea" id="entity_registeredarea"  val="${entity.registeredarea}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        出生日期:
        </td>
        <td colspan="3">
          <input id="entity_birthday" name="birthday" value="${entity.birthday}" type="text" class="easyui-datebox" ></input>
        </td>
      </tr>
      <tr>
        <td class="title">
        性别:
        </td>
        <td colspan="3">
          <select name="sex" id="entity_sex"  val="${entity.sex}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        PCONTENT:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[64]']"
          id="entity_pcontent" name="pcontent" value="${entity.pcontent}">
        </td>
      </tr>
      <tr>
        <td class="title">
        USERID:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[16]']"
          id="entity_userid" name="userid" value="${entity.userid}">
        </td>
      </tr>
      <tr>
        <td class="title">
        姓名:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[32]']"
          id="entity_name" name="name" value="${entity.name}">
        </td>
      </tr>
      <tr>
        <td class="title">
        PSTATE:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[1]']"
          id="entity_pstate" name="pstate" value="${entity.pstate}">
        </td>
      </tr>
      <tr>
        <td class="title">
        EUSER:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[16]']"
          id="entity_euser" name="euser" value="${entity.euser}">
        </td>
      </tr>
      <tr>
        <td class="title">
        EUSERNAME:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[32]']"
          id="entity_eusername" name="eusername" value="${entity.eusername}">
        </td>
      </tr>
      <tr>
        <td class="title">
        ETIME:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[8]']"
          id="entity_etime" name="etime" value="${entity.etime}">
        </td>
      </tr>
      <tr>
        <td class="title">
        CUSERNAME:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[32]']"
          id="entity_cusername" name="cusername" value="${entity.cusername}">
        </td>
      </tr>
      <tr>
        <td class="title">
        CUSER:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[16]']"
          id="entity_cuser" name="cuser" value="${entity.cuser}">
        </td>
      </tr>
      <tr>
        <td class="title">
        CTIME:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="required:true,validType:[,'maxLength[8]']"
          id="entity_ctime" name="ctime" value="${entity.ctime}">
        </td>
      </tr>
      <tr>
        <td class="title">
        居住所在地（省）:
        </td>
        <td colspan="3">
          <select name="livestrarea" id="entity_livestrarea"  val="${entity.livestrarea}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        居住所在地:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[64]']"
          id="entity_livestr" name="livestr" value="${entity.livestr}">
        </td>
      </tr>
      <tr>
        <td class="title">
        qq:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[8]']"
          id="entity_qqcode" name="qqcode" value="${entity.qqcode}">
        </td>
      </tr>
      <tr>
        <td class="title">
        微信:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[8]']"
          id="entity_wxcode" name="wxcode" value="${entity.wxcode}">
        </td>
      </tr>
      <tr>
        <td class="title">
        手机:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[8]']"
          id="entity_mobilecode" name="mobilecode" value="${entity.mobilecode}">
        </td>
      </tr>
      <tr>
        <td class="title">
        电子邮箱:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[16]']"
          id="entity_emailcode" name="emailcode" value="${entity.emailcode}">
        </td>
      </tr>
      <tr>
        <td class="title">
        婚姻状况:
        </td>
        <td colspan="3">
          <select name="marriagesta" id="entity_marriagesta"  val="${entity.marriagesta}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        国籍:
        </td>
        <td colspan="3">
          <select name="nationality" id="entity_nationality"  val="${entity.nationality}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        其他证件类型:
        </td>
        <td colspan="3">
          <select name="othertype" id="entity_othertype"  val="${entity.othertype}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        其他证件编号:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[16]']"
          id="entity_otherid" name="otherid" value="${entity.otherid}">
        </td>
      </tr>
      <tr>
        <td class="title">
        身份证号:
        </td>
        <td colspan="3">
          <input type="text" style="width: 360px;" class="easyui-validatebox" data-options="validType:[,'maxLength[16]']"
          id="entity_idcode" name="idcode" value="${entity.idcode}">
        </td>
      </tr>
      <tr>
        <td class="title">
        海外工作/学习经历:
        </td>
        <td colspan="3">
          <select name="studyabroad" id="entity_studyabroad"  val="${entity.studyabroad}"><option value="0">NONE</option></select>
        </td>
      </tr>
      <tr>
        <td class="title">
        政治面貌:
        </td>
        <td colspan="3">
          <select name="zzmm" id="entity_zzmm"  val="${entity.zzmm}"><option value="0">NONE</option></select>
        </td>
      </tr>
    </table>
    </form>
  </div>
  <div data-options="region:'south',border:false">
    <div class="div_button" style="text-align: center; padding: 4px;">
      <c:if test="${pageset.operateType==1}">
      <a id="dom_add_entityResumebase" href="javascript:void(0)"  iconCls="icon-save" class="easyui-linkbutton">增加</a>
      </c:if>
      <c:if test="${pageset.operateType==2}">
      <a id="dom_edit_entityResumebase" href="javascript:void(0)" iconCls="icon-save" class="easyui-linkbutton">修改</a>
      </c:if>
      <a id="dom_cancle_formResumebase" href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton"   style="color: #000000;">取消</a>
    </div>
  </div>
</div>
<script type="text/javascript">
  var submitAddActionResumebase = 'resumebase/add.do';
  var submitEditActionResumebase = 'resumebase/edit.do';
  var currentPageTypeResumebase = '${pageset.operateType}';
  var submitFormResumebase;
  $(function() {
    //表单组件对象
    submitFormResumebase = $('#dom_formResumebase').SubmitForm( {
      pageType : currentPageTypeResumebase,
      grid : gridResumebase,
      currentWindowId : 'winResumebase'
    });
    //关闭窗口
    $('#dom_cancle_formResumebase').bind('click', function() {
      $('#winResumebase').window('close');
    });
    //提交新增数据
    $('#dom_add_entityResumebase').bind('click', function() {
      submitFormResumebase.postSubmit(submitAddActionResumebase);
    });
    //提交修改数据
    $('#dom_edit_entityResumebase').bind('click', function() {
      submitFormResumebase.postSubmit(submitEditActionResumebase);
    });
  });
  //-->
</script>