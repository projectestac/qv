<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();

String sType = qvb.getParameter("p_list_type");
if (sType==null) sType = edu.xtec.qv.editor.beans.IQVListControlBean.TARGET_TYPE;
String sForm = qvb.getParameter("p_form");

String sCurrent = qvb.getParameter("ident");

edu.xtec.qv.qti.ResponseLabel o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getObject(sType, sCurrent);

String sX0 = "";
String sY0 = "";
java.util.Vector vPunts = new java.util.Vector();
if (o!=null){
vPunts = o.getPoints();
}
%>	

<script>
function getPunt(){
	var x = <%=sForm%>.p_bounded_x.value;
	var y = <%=sForm%>.p_bounded_y.value;
	return x+","+y;
}
function addPunt(){
	var select = <%=sForm%>.bounded_list;
	//select.options[select.size] = new Option(getPunt());
	var oOption = document.createElement("OPTION");
	oOption.text = getPunt();
	oOption.value = getPunt();
	var i = select.selectedIndex;
	if (i==null || i<0) {
		if (select.option!=null) i = select.options.length;
		else{
			if (select.size<0) i=0;
		}
	}else i++;
	select.options.add(oOption, i);
	select.selectedIndex=i;
}

function setPunt(){
	var select = <%=sForm%>.bounded_list;
	select.options[select.selectedIndex].text=getPunt();
}

function delPunt(){
	var select = <%=sForm%>.bounded_list;
	select.remove(select.selectedIndex);
}

function upPunt(){
	movePunt('up');
}

function downPunt(){
	movePunt('down');
}

function movePunt(move){
	var select = <%=sForm%>.bounded_list;
	var index = select.selectedIndex;
	if (index!=null && index>=0){
		var nextIndex = index+1;
		if (move=='up') nextIndex = index-1;
		if (nextIndex>=0 && nextIndex<select.options.length){
			var tmp = select.options[index];		
			select.options[index]=select.options[nextIndex];
			select.options.add(tmp, nextIndex);
		}
	}
}

function refreshPunt(select){
	var punts = select.options[select.selectedIndex].text;
	if (punts!=null){
		var coords = punts.split(",");
		var form = select.form;
		form.p_bounded_x.value=coords[0];
		form.p_bounded_y.value=coords[1];		
	}
}
</script>
<INPUT type="hidden" name="p_rarea_bounded_list"/>
<TABLE width="100%" cellpadding="2" cellspacing="0" border="0">
<TR>
	<TD style="width:90" valign="top" class="edit-text" title="<%=qvb.getMsg("item.response_label.punts.tip")%>"><%=qvb.getMsg("item.response_label.punts")%></TD>
	<TD title="<%=qvb.getMsg("item.response_label.punts.tip")%>" >
		<SELECT name="bounded_list" size="4" width="150" style="width:150px;" class="edit-form" onchange="refreshPunt(this);">
<%
int index = 0;
java.util.Enumeration enumList=vPunts.elements();
while(enumList.hasMoreElements()){
	java.awt.Point punt = (java.awt.Point)enumList.nextElement();
	String sIdent="punt_"+index;
	String sValue=punt.x+","+punt.y;
%>	
			<OPTION value="<%=sIdent%>" <%=sIdent.equalsIgnoreCase(sCurrent)?"selected":""%> ><%=sValue%></OPTION>
<%}%>
		</SELECT>
	</TD>
</TR>
<TR>
	<TD/>
	<TD align="center">
		<TABLE width="100%" cellpadding="0" cellspacing="5" border="0" class="coord-box" >
	  	<TR>
	  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.punts.x")%></TD>
	  		<TD><INPUT type="text" size="3" name="p_bounded_x" value="<%=sX0%>" class="layer-form" /></TD>
	  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.punts.y")%></TD>
	  		<TD><INPUT type="text" size="3" name="p_bounded_y" value="<%=sY0%>" class="layer-form" /></TD>
	  	</TR>
		<TR>
			<TD class="layer-text" colspan="4" align="center">
				<A href="#" onclick="addPunt()" title="<%=qvb.getMsg("list.action.add.tip")%>" ><IMG src='imatges/add_off.gif' border='0' width='10' height='10' ></A> |
				<A href="#" onclick="setPunt()" title="<%=qvb.getMsg("list.action.set.tip")%>" ><IMG src='imatges/set_off.gif' border='0' width='10' height='10' ></A> |
				<A href="#" onclick="delPunt()" title="<%=qvb.getMsg("list.action.del.tip")%>" ><IMG src='imatges/del_off.gif' border='0' width='10' height='10' ></A> |
				<A href="#" onclick="upPunt()" title="<%=qvb.getMsg("list.action.up.tip")%>" ><IMG src='imatges/up_off.gif' border='0' width='10' height='10' ></A> |
				<A href="#" onclick="downPunt()" title="<%=qvb.getMsg("list.action.down.tip")%>" ><IMG src='imatges/down_off.gif' border='0' width='10' height='10' ></A>
			</TD>
		</TR>
	  	</TABLE>
	 </TD>
</TR>
</TABLE>



