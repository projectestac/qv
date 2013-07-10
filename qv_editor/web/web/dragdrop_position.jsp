<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();


String sType = IQVListControlBean.DRAGDROP_POSITION_TYPE;
String sLayer = "dragdropPositionLayer";
String sForm = qvb.getParameter("p_form");
String sListName = qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sSourceAction =qvb.getParameter("p_dragdrop_position_action");

String sBgImg = "", nRotate="", nRatio="", sItemId="";
int iWidth=-1, iHeight=-1, iFitTarget=0;
boolean bEnabledRotating=false;
boolean bEnabledScaling=false;
java.util.Vector vSources = new java.util.Vector(), vTargets = new java.util.Vector();
edu.xtec.qv.editor.beans.QVPreguntaDragDropBean qvbdrag = null;
if (qvsb instanceof edu.xtec.qv.editor.beans.QVPreguntaDragDropBean){
	qvbdrag = (edu.xtec.qv.editor.beans.QVPreguntaDragDropBean)qvsb;
	sBgImg = qvbdrag.getBackgroundImage();
	edu.xtec.qv.qti.Matimage oMat = qvbdrag.getBackgroundMatimage();
	if (oMat!=null){
		iWidth = oMat.getWidth();
		iHeight = oMat.getHeight();
	}
	bEnabledRotating = qvbdrag.isEnabledRotating();
	nRotate = qvbdrag.getNRotate();
	bEnabledScaling = qvbdrag.isEnabledScaling();
	nRatio=qvbdrag.getNRatio();
	iFitTarget=qvbdrag.getIntFitTarget();
	vSources = qvbdrag.getSources();
	vTargets= qvbdrag.getTargets(); 
	sItemId=qvbdrag.getIdentPregunta();
}


String sIdent = qvb.getParameter(sListName);
edu.xtec.qv.qti.QTIObject o = (edu.xtec.qv.qti.QTIObject)qvsb.getObject(sType, sIdent);
if (o==null) sSourceAction=IQVListControlBean.A_SET_LIST_OBJECT;
String sText = "";
String sURI = "";
String sWidth = "0";
String sHeight = "0";
String sX0 = "";
String sY0 = "";
if (o!=null && sSourceAction!=null && sSourceAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	if (o instanceof edu.xtec.qv.qti.QTISuperRender){
		edu.xtec.qv.qti.QTISuperRender oRender = (edu.xtec.qv.qti.QTISuperRender)o;
	}

/*	sText = o.getMatText();
	sURI = o.getURI();
	sWidth = o.getWidth();
	sHeight = o.getHeight();
	sX0 = o.getX0();
	sY0 = o.getY0();*/
}

String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";

String sNullImage = "imatges/pixel.gif";

%>

<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');
// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>
<SCRIPT src="scripts/javaplugin.js"></SCRIPT>
<SCRIPT src="scripts/qti_functions.js"></SCRIPT>

<INPUT type="hidden" name="p_continue">
<INPUT type="hidden" name="p_positions">
<DIV id='<%=sLayer%>' style="position:absolute; top:100; left:150; z-index:1000; width:550; padding:5px;"  class='layer-box' >
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='400px' height='100%' style='height:100%'>
<TR>
	<TD>
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50px;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('<%=sLayer%>');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<TD align="right" class="layer-title">
				<%=qvb.getMsg("item.response_label.source.title")%>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
  <TD valign="top" height='100%' style='height:100%'>
  	<TABLE border='0' cellpadding='5' cellspacing='0' >
	<TR>
		<TD class="layer-text" valign="top" width="80">
<script type="text/javascript">
<!--
var params=''+ 'NAME=<%=sItemId%>$$'+
          'url_base=<%=qvb.getQuadernResourcesURL()%>/$$'+
          'serverUrl=/qv/getQuadernAlumne$$'+ 
		  'image_src=<%=sBgImg%>$$'+ 
		  'orientation=$$'+ 
		  'size=$$'+ 
		  'font=$$'+ 
		  'shuffle=No$$'+ 
		  'inside=No$$'+ 
		  'style=$$'+ 
		  'showTargets=<%=!("0".equals(sIdent))%>$$'+ 
		  'disabled=false$$'+ 
		  'align=Auto$$'+ 
  		  'Snratio=<%=nRatio%>$$'+ 
  		  'Snrotate=<%=nRotate%>$$'+ 
  		  'enable_rotating=<%=bEnabledRotating && !"0".equals(sIdent)?"Yes":"No"%>$$'+ 
  		  'enable_scaling=<%=bEnabledScaling && !"0".equals(sIdent)?"Yes":"No"%>$$'+ 
  		  'INITPARAM=$$'+ 
<%
	java.util.Enumeration enumSources = vSources.elements();
  	int i = 1;
  	while (enumSources.hasMoreElements()){
  		edu.xtec.qv.qti.ResponseLabel oSource = (edu.xtec.qv.qti.ResponseLabel)enumSources.nextElement();
  		edu.xtec.qv.qti.ResponseLabel oTarget = qvbdrag.getTarget(oSource.getIdent(), sIdent);
  		//System.out.println("source="+oSource.getIdent()+" ident="+sIdent+" target="+oTarget);
  		if (oTarget!=null){
  			// Create target; x0,y0 source from target
  			int iX0 = Integer.parseInt(oTarget.getX0())+iFitTarget;
  			int iY0 = Integer.parseInt(oTarget.getY0())+iFitTarget;
%>
  	  		'S<%=i%>_ident=<%=oSource.getIdent()%>$$'+  'S<%=i%>_match_group=<%=oSource.getMatchgroup()%>$$'+  'S<%=i%>_match_max=<%=oSource.getMatchmax()%>$$'+  'S<%=i%>_imagtype=image/jpeg$$'+  'S<%=i%>_uri=<%=oSource.getURI()!=null?oSource.getURI():""%>$$'+  'S<%=i%>_width=<%=oSource.getWidth()%>$$'+  'S<%=i%>_height=<%=oSource.getHeight()%>$$'+  'S<%=i%>_x0=<%=iX0%>$$'+  'S<%=i%>_y0=<%=iY0%>$$'+  'S<%=i%>_text=<%=oSource.getMatText()%>$$'+  'S<%=i%>_rotate=<%=oTarget.getRotate()%>$$'+
<%			i++;%>
	  		'T<%=i%>_ident=<%=oTarget.getIdent()%>$$'+ 'T<%=i%>_rarea=<%=oTarget.getRarea()%>$$'+  'T<%=i%>_text=<%=oTarget.getText()%>$$'+ <%=oTarget.getRotate()!=null?"'T"+i+"_rotate="+oTarget.getRotate()+"$$'+":""%>
<%  	
  		}else{
  			int iX0 = Integer.parseInt(oSource.getX0())+iFitTarget;
  			int iY0 = Integer.parseInt(oSource.getY0())+iFitTarget;
%>  			
  	  		'S<%=i%>_ident=<%=oSource.getIdent()%>$$'+  'S<%=i%>_match_group=<%=oSource.getMatchgroup()%>$$'+  'S<%=i%>_match_max=<%=oSource.getMatchmax()%>$$'+  'S<%=i%>_imagtype=image/jpeg$$'+  'S<%=i%>_uri=<%=oSource.getURI()!=null?oSource.getURI():""%>$$'+  'S<%=i%>_width=<%=oSource.getWidth()%>$$'+  'S<%=i%>_height=<%=oSource.getHeight()%>$$'+  'S<%=i%>_x0=<%=iX0%>$$'+  'S<%=i%>_y0=<%=iY0%>$$'+  'S<%=i%>_text=<%=oSource.getMatText()%>$$'+
<% 
  		}
		i++;
  	}  	
%>
	'';
		  //'T1_ident=rectangle$$'+'T1_rarea=Rectangle$$'+'T1_text=43,48,110,60$$'+
  		  //'S2_ident=1141925285312508_figura1.gif$$'+  'S2_match_group=rectangle$$'+  'S2_match_max=1$$'+  'S2_imagtype=image/jpeg$$'+  'S2_uri=figura1.gif$$'+  'S2_width=100$$'+  'S2_height=50$$'+  'S2_x0=50$$'+  'S2_y0=200$$'+  'S2_text=$$'+  'S3_ident=1141925285312505_figura2.gif$$'+  'S3_match_group=rectangle$$'+  'S3_match_max=1$$'+  'S3_imagtype=image/jpeg$$'+  'S3_uri=figura2.gif$$'+  'S3_width=100$$'+  'S3_height=50$$'+  'S3_x0=200$$'+  'S3_y0=200$$'+  'S3_text=$$'+  '';
			   
writeDragDropApplet(params, <%=iWidth%>, <%=iHeight%>, '<%=sItemId%>');
-->
</script>
		
		</TD>
	</TR>
	</TABLE>
  </TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:<%=sForm%>.p_positions.value=getAppletValue('<%=sItemId%>');<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sSourceAction%>', <%=sForm%>);" title="<%=qvb.getMsg("item.response_label.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_source_action.value='';hide_dragdrop_position_layer();" title="<%=qvb.getMsg("item.response_label.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>

<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_dragdrop_position_action") && sSourceAction!=null && 
	( sSourceAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sSourceAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	show_dragdrop_position_layer(<%=sForm%>);
<%}else{%>
	hide_dragdrop_position_layer();
<%}%>
</SCRIPT>

