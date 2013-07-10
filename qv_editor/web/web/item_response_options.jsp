<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}

String sForm = qvb.getParameter("form");
String sCardinality = qvb.getParameter("cardinalitat", "Single");
boolean bIsVertical = qvb.getBooleanParameter("is_vertical", false);
String sResponseMatType = qvb.getParameter("response_material_type", "text");
String sResponseOptionType = qvb.getParameter("response_option_type", "close");

%>
<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" style="height:100%">
<TR>
	<TD colspan="2" valign="top">
		<DIV id='inicialValuesLayer' style="display: none">
		<TABLE border="0" cellpadding="5" cellspacing="0">
		<TR>
			<TD valign="top" class='edit-text' >
				<B>2.</B>
			</TD>
			<TD valign="top" class='edit-text' ><%=qvb.getMsg("item.add.step2")%></TD>
		</TR>
		</TABLE>
		</DIV>
	</TD>
</TR>
<TR>
	<TD height="1" width="15"></TD>
	<TD valign="top">
		<TABLE class="edit-box" width="100%" cellpadding="5" cellspacing="0" border="0">
		<TR>
			<TD valign="top">
				<DIV id='numberResponsesLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD width='150' class='edit-text' title="<%=qvb.getMsg("item.numberResponses.tip")%>" ><%=qvb.getMsg("item.numberResponses")%></TD>	
					<TD >
						<INPUT type='text' name='num_respostes' class='edit-form' value="2"/>
					</TD>	
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>	
		<TR>
			<TD valign="top">
				<DIV id='cardinalityLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD width='150' class='edit-text' title="<%=qvb.getMsg("item.cardinality.tip")%>" ><%=qvb.getMsg("item.cardinality")%></TD>	
					<TD >
						<SELECT name='cardinalitat' class='edit-form'>
							<OPTION value='Single' <%=sCardinality.equals("Single")?"selected":""%> ><%=qvb.getMsg("item.cardinality.single")%></OPTION>
							<OPTION value='Multiple' <%=sCardinality.equals("Multiple")?"selected":""%>><%=qvb.getMsg("item.cardinality.multiple")%></OPTION>
						</SELECT>
					</TD>	
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>	
		<TR>
			<TD valign="top">
				<DIV id='orientationLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD width='150' class='edit-text' title="<%=qvb.getMsg("item.orientation.tip")%>" ><%=qvb.getMsg("item.orientation")%></TD>	
					<TD >
						<SELECT name='orientation' class='edit-form'>
							<OPTION value='vertical' <%=bIsVertical?"selected":""%>><%=qvb.getMsg("item.orientation.vertical")%></OPTION>
							<OPTION value='horizontal' <%=!bIsVertical?"selected":""%> ><%=qvb.getMsg("item.orientation.horizontal")%></OPTION>
						</SELECT>
					</TD>	
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>	
		<TR>
			<TD valign="top">
				<DIV id='materialResponseLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD width='150' class='edit-text' title="<%=qvb.getMsg("item.materialResponse.tip")%>" ><%=qvb.getMsg("item.materialResponse")%></TD>	
					<TD >
						<SELECT name='response_material_type' class='edit-form'>
							<OPTION value='text' <%=sResponseMatType.equals("text")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.text")%></OPTION>
							<OPTION value='image' <%=sResponseMatType.equals("image")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.image")%></OPTION>
							<OPTION value='audio' <%=sResponseMatType.equals("audio")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.audio")%></OPTION>
						</SELECT>
					</TD>	
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>	
		<TR>
			<TD valign="top">
				<DIV id='responseOptionLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD width='150' class='edit-text' title="<%=qvb.getMsg("item.response_option_type.tip")%>" ><%=qvb.getMsg("item.response_option_type")%></TD>
					<TD>
						<SELECT name='response_option_type' class='edit-form'>
							<OPTION value='close' <%=sResponseOptionType.equals("close")?"selected":""%> ><%=qvb.getMsg("item.response_option_type.close")%></OPTION>
							<OPTION value='open' <%=sResponseOptionType.equals("open")?"selected":""%>><%=qvb.getMsg("item.response_option_type.open")%></OPTION>
							<OPTION value='mix' <%=sResponseOptionType.equals("mix")?"selected":""%>><%=qvb.getMsg("item.response_option_type.mix")%></OPTION>
						</SELECT>
					</TD>	
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>
		<TR>
			<TD valign="top">
				<DIV id='hotspotNoEditableLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<!--TD valign="top" class='edit-text' title="<%=qvb.getMsg("msg.item.hotspot.add.not_visual_edition.tip")%>" ><%=qvb.getMsg("msg.item.hotspot.add.not_visual_edition")%></TD-->
					<TD valign="top" class='edit-text' title="<%=qvb.getMsg("msg.item.no_params.tip")%>" ><%=qvb.getMsg("msg.item.no_params")%></TD>
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>
		<TR>
			<TD valign="top">
				<DIV id='dragdropNoEditableLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<!--TD valign="top" class='edit-text' title="<%=qvb.getMsg("msg.item.dragdrop.add.not_visual_edition.tip")%>" ><%=qvb.getMsg("msg.item.dragdrop.add.not_visual_edition")%></TD-->
					<TD valign="top" class='edit-text' title="<%=qvb.getMsg("msg.item.no_params.tip")%>" ><%=qvb.getMsg("msg.item.no_params")%></TD>
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>
		<TR>
			<TD valign="top">
				<DIV id='drawNoEditableLayer' style="display: none">
				<TABLE cellpadding="0" cellspacing="0" border="0">
				<TR>
					<TD valign="top" class='edit-text' title="<%=qvb.getMsg("msg.item.no_params.tip")%>" >
						<%=qvb.getMsg("item.type.draw.description")%>					
						<br/><br/><br/>
						<%=qvb.getMsg("msg.item.no_params")%>
					</TD>
				</TR>
				</TABLE>
				</DIV>
			</TD>
		</TR>
		</TABLE>	
	</TD>
</TR>
</TABLE>
