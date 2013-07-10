head	1.5;
access;
symbols
	qv_editor_20040719:1.4
	inicial:1.1.1.1
	xtec:1.1.1;
locks; strict;
comment	@# @;


1.5
date	2004.08.10.06.12.21;	author sarjona;	state dead;
branches;
next	1.4;

1.4
date	2004.06.29.07.26.18;	author sarjona;	state Exp;
branches;
next	1.3;

1.3
date	2004.06.23.12.41.32;	author sarjona;	state Exp;
branches;
next	1.2;

1.2
date	2004.06.18.16.28.20;	author sarjona;	state Exp;
branches;
next	1.1;

1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches
	1.1.1.1;
next	;

1.1.1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches;
next	;


desc
@@


1.5
log
@Eliminades jsp i classes que no es fan servir
@
text
@<%@@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	edu.xtec.qv.editor.beans.QVRespostaOrdenacioBean qvsb=(edu.xtec.qv.editor.beans.QVRespostaOrdenacioBean)qvb.getSpecificBean();
	String sForm = qvb.getParameter("form");
	String sIdResp = qvb.getParameter(qvsb.IDENT_RESPONSE_LID_PARAM);
	int iNumRespostes = qvsb.getNumResponseLabels(sIdResp);
	String sOrientacio = qvsb.getOrientacio(sIdResp);
	boolean bShuffle = qvsb.isShuffle(sIdResp);
	String sResponseMatType = qvb.getParameter("response_material_type");
	if (sResponseMatType==null){
		sResponseMatType = qvsb.getResponseMaterialType(sIdResp);
	}
	
	String[] sRecursos = null;
	if (sResponseMatType.equals("image")) sRecursos = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
	else if (sResponseMatType.equals("audio")) sRecursos = qvb.getAssessmentResources(qvb.AUDIO_RESOURCE);
%>
<!-- INICI dades pel conjunt de respostes d'ordenacio amb identificador <%=sIdResp%> -->
<INPUT type="hidden" name="<%=qvsb.IDENT_RESPONSE_LID_PARAM%>" value="<%=sIdResp%>" />
<TABLE class='quadreDades' width="100%" cellpadding="5" cellspacing="0" border="0">
<TR>
	<TD>
		<TABLE class='quadreDades' width="100%" cellpadding="5" cellspacing="0" border="0">
		<TR>
			<TD width='15%' class='textNomDades' title="<%=qvb.getMsg("item.orientation.tip")%>" ><%=qvb.getMsg("item.orientation")%></TD>	
			<TD >
				<SELECT name='<%="orientacio_"+sIdResp%>' class='textValorDades'>
					<OPTION value='<%=qvsb.ROW_ORIENTATION%>' <%=(sOrientacio!=null||sOrientacio.equalsIgnoreCase(qvsb.ROW_ORIENTATION))?"selected":""%>><%=qvb.getMsg("item.orientation.row")%></OPTION>
					<OPTION value='<%=qvsb.COLUMN_ORIENTATION%>' <%=(sOrientacio!=null&&sOrientacio.equalsIgnoreCase(qvsb.COLUMN_ORIENTATION))?"selected":""%> ><%=qvb.getMsg("item.orientation.column")%></OPTION>
				</SELECT>
			</TD>	
		</TR>
		<TR>
			<TD class='textNomDades' title="<%=qvb.getMsg("item.materialResponse.tip")%>" ><%=qvb.getMsg("item.materialResponse")%></TD>	
			<TD >
				<SELECT name='response_material_type' class='textValorDades' onChange='enviar("set_response_material_type", <%=sForm%>)'>
					<OPTION value='text' <%=sResponseMatType.equals("text")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.text")%></OPTION>
					<OPTION value='image' <%=sResponseMatType.equals("image")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.image")%></OPTION>
				</SELECT>
			</TD>	
		</TR>
		<TR>
			<TD colspan="2" class='textNomDades' title="<%=qvb.getMsg("item.shuffle.tip")%>" >
				<INPUT type="checkbox" name="shuffle" class="textNomDades" <%=bShuffle?"checked":""%>/><%=qvb.getMsg("item.shuffle")%></TD>	
			</TD>	
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
	<TD>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
<%
	for (int i=0;i<iNumRespostes;i++){
		String sIdentRespLabel = qvsb.getIdentResponseLabel(sIdResp, i);
		String sTextRespLabel = qvsb.getTextResponseLabel(sIdResp, sIdentRespLabel, i);
		String sResourceResp = qvsb.getResourceResponseLabel(sIdResp, sIdentRespLabel);
		int iOrderRespLabel = qvsb.getOrderResponseLabel(sIdResp, sIdentRespLabel);
		if (iNumRespostes==1) iOrderRespLabel=0;
%>	
		<TR>
			<TD>
				<TABLE class='fonsQuadre' id="<%="resposta_"+sIdResp+"_"+i%>" width="100%" cellpadding="0" cellspacing="0" border="0" onclick="seleccionar_item('<%="resposta_"+sIdResp+"_"%>', this.id, <%=iNumRespostes%>, <%=sForm+".ident_resposta"%>, '<%=sIdentRespLabel%>');" <%=sIdentRespLabel.equals(qvsb.getCurrentIdentResponseLabel())?"style='background-color:#FDB671'":""%> >
				<TR>
					<TD width='5%'>
						<INPUT type="text" size="1" name="<%="ordre_"+sIdResp%>" value="<%=iOrderRespLabel<0?String.valueOf(i+1):String.valueOf(iOrderRespLabel+1)%>"  class="textValorDades" />
					</TD>
					<TD width="10"/>
					<TD align='left'>
						<INPUT type='hidden' name='<%=qvsb.IDENT_RESPOSTES_PARAM+"_"+sIdResp%>' value='<%=sIdentRespLabel%>'/>
					</TD>
					<TD align='left' class="textValorDades">
<%if (sResponseMatType.equals("text")){%>
						<INPUT type='text' size='70' name="<%=qvsb.TEXT_RESPOSTES_PARAM+"_"+sIdResp%>" value="<%=sTextRespLabel%>" class="textValorDades"/>
<%}else if (sResponseMatType.equals("image")){
	if(sRecursos!=null && sRecursos.length>0){%>		
						<SELECT name="<%=qvsb.RECURS_RESPOSTES_PARAM+"_"+sIdResp%>" class="textValorDades">
							<OPTION value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION>
<%		for(int k=0;k<sRecursos.length;k++){%>
							<OPTION value="<%=sRecursos[k]%>" <%=sRecursos[k].equalsIgnoreCase(sResourceResp)?"selected":""%>><%=sRecursos[k]%></OPTION>
<%		}%>
						</SELECT>
<%	}else{%>
	No hi ha recursos
<%}
}%>								
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
<%
	}
%>				
		</TABLE>
	</TD>
</TR>
<TR>
	<TD>
		<jsp:include page="control.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="name_add_action_param" value="add_num_respostes" />
			<jsp:param name="name_del_action_param" value="del_resposta" />
			<jsp:param name="name_up_action_param" value="up_resposta" />
			<jsp:param name="name_down_action_param" value="down_resposta" />
			<jsp:param name="name_num_items" value="num_respostes" />
			<jsp:param name="value_num_items" value="<%=String.valueOf(iNumRespostes)%>" />
			<jsp:param name="name_param" value="ident_resp_lid" />
			<jsp:param name="value_param" value="<%=sIdResp%>" />
		</jsp:include>
	</TD>
</TR>
</TABLE>
<!-- FI dades pel conjunt de respostes d'ordenacio amb identificador <%=sIdResp%> -->
@


1.4
log
@Afegits parametres de configuracio inicial en la creació de preguntes
Revisats bugs:
- Afegir/Pujar/Baixar preguntes de seleccio
- Preguntes d'ordenacio: amb text, imatges (i num d'ordre correcte)
- Afegir materials: previsualitzacio de les imatges
@
text
@@


1.3
log
@Revisada numeracio en les respostes d'ordenacio
@
text
@d10 4
a13 1
	String sResponseMatType = qvb.getParameter("response_material_type", "text");
d60 1
a60 1
		if (iNumRespostes==1) iOrderRespLabel=1;
d67 1
a67 1
						<INPUT type="text" size="1" name="<%="ordre_"+sIdResp%>" value="<%=iOrderRespLabel<0?String.valueOf(i+1):String.valueOf(iOrderRespLabel)%>"  class="textValorDades" />
@


1.2
log
@Revisats detalls per fer l'editor mes intuitiu
@
text
@d64 1
a64 1
						<INPUT type="text" size="1" name="<%="ordre_"+sIdResp%>" value="<%=iOrderRespLabel<0?"":String.valueOf(iOrderRespLabel)%>"  class="textValorDades" />
@


1.1
log
@Initial revision
@
text
@d9 6
d23 20
a42 2
			<TD class='textNomDades'>
			<INPUT type='checkbox' name='<%="orientacio_"+sIdResp%>' <%=(sOrientacio!=null&&sOrientacio.equalsIgnoreCase(qvsb.ROW_ORIENTATION))?"checked":""%> onClick='enviar("set_orientacio", <%=sForm%>)' /> Mostrar les respostes a la mateixa linia
d55 1
d57 1
d69 3
d73 12
@


1.1.1.1
log
@Editor de QV.
S'ha separat del projecte qv
@
text
@@
