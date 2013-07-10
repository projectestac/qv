head	1.2;
access;
symbols
	qv_editor_20040719:1.1.1.1
	inicial:1.1.1.1
	xtec:1.1.1;
locks; strict;
comment	@# @;


1.2
date	2004.08.10.06.12.21;	author sarjona;	state dead;
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


1.2
log
@Eliminades jsp i classes que no es fan servir
@
text
@<%@@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	edu.xtec.qv.editor.beans.QVMaterialBean qvsb=(edu.xtec.qv.editor.beans.QVMaterialBean)qvb.getSpecificBean();
	String sForm = qvb.getParameter("form", "fullForm");
	int iNumMaterials = qvb.getIntParameter("num_materials",0);
	int iMaterialIndex = qvb.getIntParameter(qvsb.MATERIAL_INDEX_PARAM, 0);
	int iCurrentMaterial = qvb.getIntParameter("current_material", -1);
	String sMaterialType = qvsb.getMaterialType(iMaterialIndex);
	String[] sImages = null;
	String sImage = null;
	if (sMaterialType.equalsIgnoreCase(qvsb.IMAGE_MATERIAL_TYPE)){
		sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
		sImage = qvsb.getMaterialImage(iMaterialIndex);
		if ((sImage==null || sImage.trim().length()==0) && sImages!=null && sImages.length>0){
			sImage = sImages[0];
		}
	}
%>

<!-- INICI material -->
<INPUT type='hidden' name='<%=qvsb.MATERIAL_INDEX_PARAM%>' value='<%=iMaterialIndex%>' />
<TABLE class='quadreDades' width="100%" cellpadding="5" cellspacing="0" border="0" id='material_<%=iMaterialIndex%>' onclick='seleccionar_item("material_", this.id, <%=iNumMaterials%>, <%=sForm+".current_material"%>, <%=iMaterialIndex%>)' <%=(iCurrentMaterial==iMaterialIndex)?"style='background-color:#FDB671'":""%> >
<TR>
	<TD>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
			<TR>
				<TD class='textNomDades' width='75'>Tipus</TD>
				<TD>	
					<SELECT onChange='enviar("set_material_type", <%=sForm%>)' name='<%=qvsb.MATERIAL_TYPE_PARAM+"_"+iMaterialIndex%>' class='textValorDades'>
						<OPTION value="<%=qvsb.TEXT_MATERIAL_TYPE%>" <%=qvsb.TEXT_MATERIAL_TYPE.equals(sMaterialType)?"selected":""%> >Text</OPTION>
						<OPTION value="<%=qvsb.IMAGE_MATERIAL_TYPE%>" <%=qvsb.IMAGE_MATERIAL_TYPE.equals(sMaterialType)?"selected":""%>>Imatge</OPTION>
						<OPTION value="<%=qvsb.AUDIO_MATERIAL_TYPE%>" <%=qvsb.AUDIO_MATERIAL_TYPE.equals(sMaterialType)?"selected":""%> >So</OPTION>
						<OPTION value="<%=qvsb.VIDEO_MATERIAL_TYPE%>" <%=qvsb.VIDEO_MATERIAL_TYPE.equals(sMaterialType)?"selected":""%> >Video</OPTION>
					</SELECT>
				</TD>
			</TR>
		</TABLE>
	</TD>
<%
if (sMaterialType.equalsIgnoreCase(qvsb.IMAGE_MATERIAL_TYPE) && sImages!=null && sImages.length>0){
	String sImageURL = qvb.getQuadernResourcesURL()+"/"+sImage;
	boolean bNoPreviewImage = qvb.getParameter("image_preview_"+iMaterialIndex)!=null;
%>
	<TD rowspan="2" width="100%">
		<TABLE cellpadding="0" cellspacing="0" border="0">
			<TR>
				<TD width="10"><IMG src="images/pixel.gif" width="10" height="1"/></TD>
				<TD>
					<TABLE cellpadding="0" cellspacing="0" border="0">
					<TR>
						<TD class="textNomDades" width=30">
							<INPUT type="checkbox" onClick="setVisibility(document.<%="image_"+iMaterialIndex%>);" name="<%="image_preview_"+iMaterialIndex%>" <%=bNoPreviewImage?"checked":""%> />
						</TD>
						<TD colspan="2" class="textNomDades">No previsualitzar imatge</TD>
					</TR>
<%if (false){%>
					<TR>
						<TD class="textNomDades">
							<INPUT type="checkbox" name="<%="image_proportion_"+iMaterialIndex%>" checked/>
						</TD>
						<TD colspan="2" class="textNomDades">Mantenir proporcionalitat</TD>
					</TR>
<%}%>
					<TR>
						<TD/>
						<TD colspan="2" class="textNomDades">
							<A href="javascript:setSize(<%=sForm%>, <%=iMaterialIndex%>);" class="link">Obtenir tamany real de la imatge</A>
						</TD>
					</TR>
					<TR>
						<TD/>
						<TD class="textNomDades">Amplada</TD>
						<TD class='textNomDades'><INPUT type="text" class='textValorDades' name="<%="image_width_"+iMaterialIndex%>" size="5" value="<%=qvsb.getMaterialImageWidth(iMaterialIndex)%>"/> píxels</TD>
					</TR>
					<TR>
						<TD/>
						<TD class="textNomDades">Alçada</TD>
						<TD class='textNomDades'><INPUT type="text" class='textValorDades' name="<%="image_height_"+iMaterialIndex%>" size="5" value="<%=qvsb.getMaterialImageHeight(iMaterialIndex)%>"/> píxels</TD>
					</TR>
					</TABLE>
				</TD>
				<TD><IMG src="imatges/pixel.gif" width="20 height="1"/></TD>
				<TD class='textNomDades' valign="top">				
					<IMG name="<%="image_"+iMaterialIndex%>" src="<%=sImageURL%>" width="80" height="80" style="visibility:<%=bNoPreviewImage?"hidden":"visible"%>"/>
				</TD>
			</TR>
		</TABLE>
	</TD>
<%}%>
</TR>
<TR>
	<TD valign="top">
<%if (sMaterialType.equalsIgnoreCase(qvsb.TEXT_MATERIAL_TYPE)){%>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
			<TR>
				<TD valign='top' class='textNomDades' width='75'>Text</TD>
				<TD><TEXTAREA rows='5' cols='60' name='<%=qvsb.MATERIAL_TEXT_PARAM+"_"+iMaterialIndex%>' class='textValorDades'><%=qvsb.getMaterialText(iMaterialIndex)%></TEXTAREA></TD>
			</TR>
		</TABLE>
<%} else if (sMaterialType.equalsIgnoreCase(qvsb.IMAGE_MATERIAL_TYPE)){%>
		<TABLE cellpadding="0" cellspacing="0" border="0">
			<TR>
				<TD><IMG src="imatges/pixel.gif" width="75" height="1"/></TD>
				<TD/>
			<TR>
				<TD valign='top' class='textNomDades' width='75'>Imatge</TD>
				<TD class='textValorDades'>
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT onChange='document.<%="image_"+iMaterialIndex%>.src="<%=qvb.getQuadernResourcesURL()+"/"%>"+this.value;' name='<%=qvsb.MATERIAL_IMAGE_PARAM+"_"+iMaterialIndex%>' class='textValorDades'>
<%	for(int i=0;i<sImages.length;i++){%>
						<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(qvsb.getMaterialImage(iMaterialIndex))?"selected":""%> ><%=sImages[i]%></OPTION>
<%}%>
					</SELECT>
<%} else {%>
					No hi ha cap imatge disponible. Per pujar arxius s'ha de bla bla
<%}%>
				</TD>
			</TR>
		</TABLE>
<%} else if (sMaterialType.equalsIgnoreCase(qvsb.AUDIO_MATERIAL_TYPE)){%>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
			<TR>
				<TD valign='top' class='textNomDades' width='75'>So</TD>
				<TD class='textValorDades'>
<%
String[] sSons = qvb.getAssessmentResources(qvb.AUDIO_RESOURCE);
if (sSons!=null && sSons.length>0){%>
					<SELECT name='<%=qvsb.MATERIAL_AUDIO_PARAM+"_"+iMaterialIndex%>' class='textValorDades'>
<%	for(int i=0;i<sSons.length;i++){%>
						<OPTION value="<%=sSons[i]%>" <%=sSons[i].equals(qvsb.getMaterialAudio(iMaterialIndex))?"selected":""%> ><%=sSons[i]%></OPTION>
<%}%>
					</SELECT>
<%} else {%>
					No hi ha cap so disponible. Per pujar arxius s'ha de bla bla
<%}%>
				</TD>
			</TR>
		</TABLE>
<%}%>
	</TD>
</TR>
</TABLE>
<!-- FI material -->
@


1.1
log
@Initial revision
@
text
@@


1.1.1.1
log
@Editor de QV.
S'ha separat del projecte qv
@
text
@@
