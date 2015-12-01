<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="web/xsls/qti.xsl"/>

<xsl:param name="noteBookImage" select="'web/imatges/fondoazul.gif'"/>
<xsl:param name="noteBookColor" select="'#2142de'"/>
<xsl:param name="askInteractionImage" select="'web/imatges/interrogant.gif'"/>
<xsl:param name="askInteractionImageOver" select="'web/imatges/interrogant2.gif'"/>

<xsl:param name="needResponse" select="true"/>

<xsl:output method="html"/>

<xsl:template match="questestinterop">
	<HTML>
		<HEAD>
			<LINK TYPE="text/css" href="web/estil/quadern.css" rel="stylesheet" />
			<SCRIPT SRC="web/scripts/qti_functions.js" TYPE="text/javascript"/>
 			<SCRIPT SRC="web/scripts/qti_timer.js" TYPE="text/javascript"/>
			<SCRIPT SRC="web/scripts/javaplugin.js" TYPE="text/javascript"/>
			<SCRIPT src="web/scripts/mm.js" type="text/javascript"/>
			<META HTTP-EQUIV="imagetoolbar" CONTENT="no"/>
		</HEAD>
		<BODY bgcolor="#E5E5E5" text="#000000" link="#006699" vlink="#5584AA">
			<SCRIPT>setScoring('<xsl:value-of select="$scoring"/>');</SCRIPT>
			<xsl:attribute name="onload">MM_preloadImages('web/imatges/<xsl:call-template name="getContinueButtonName"/>_on.gif')</xsl:attribute>
			<!-- <B>feedback:<xsl:value-of select="$displayfeedback"/></B> -->
			<!-- <b>sectionTime:<xsl:value-of select="$sectionTime"/></b><br/>
			<b>notebookTime:<xsl:value-of select="$notebookTime"/></b> -->
			<!-- b>date:<xsl:value-of select="$date"/></b><br/ -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			  	<td class="caixaTitol3">
					<!-- TITOL PRINCIPAL -->
					<table border="0" width="100%" cellpadding="6">
						<tr>
							<td>
								<!-- <img src="imatges/prim2003/csa_logo.gif" width="37" height="37" border="0" alt="Consell Superior d'Avaluació del Sistema Educatiu"/> -->
								<!-- img src="imatges/quaderns.ico.gif" width="37" height="37" border="0" alt="Quaderns Virtuals"/ -->
								<a>
									<xsl:attribute name="onClick">
										if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
											javascript:document.forms[0].next.value=0;
											document.forms[0].submit();
										}
									</xsl:attribute>
									<img src="web/imatges/quaderns.ico.gif" width="37" height="37" border="0" alt="Quaderns Virtuals"/>
								</a>
							</td>
							<td class="titol3" width="100%">
								<B>Quaderns Virtuals: Sistema de suport a l'eLearning</B><BR/>
								Subdirecció General de Tecnologies de la Informació<BR/>
								<i>Proves</i>
							</td>
							<td align="right" valign="bottom" class="info3">
								<xsl:value-of select="$sUserId"/><BR/>
								<xsl:value-of select="$section_number"/>/<xsl:value-of select="$section_max_number"/>
							</td>
						</tr>
				    	</table>
  				</td>
			  </tr>

			  <xsl:if test="$noModify='true'">
			  <tr>
			  	<td class="caixaTitol3">
					<!-- TITOL2 -->
					<table bgcolor="#ff0000" border="0" width="100%" cellpadding="6">
						<tr>
							<td align="center" class="info3">
								NO ES POT MODIFICAR
							</td>
						</tr>
				    	</table>
  				</td>
			  </tr>
			  </xsl:if>
				<!-- B>corregit:<xsl:value-of select="$es_correccio"/></B><BR/ -->
				<!-- B>scoring:<xsl:value-of select="$scoring"/></B -->

			  <tr><td class="sep10">&#160;</td></tr>


				<FORM method="post">
					<xsl:attribute name="onSubmit">return(doSubmit2(<xsl:value-of select="$needResponse"/>))</xsl:attribute>
					<!-- <xsl:attribute name="action">getQuadernAlumne?currentSect=<xsl:value-of select="$section_number"/></xsl:attribute> -->
					<xsl:attribute name="action"><xsl:value-of select="$serverUrl"/>?currentSect=<xsl:value-of select="$section_number"/>#int</xsl:attribute>
					<INPUT type="HIDDEN" name="es_correccio"> <!-- Aquest camp ocult indica si estem mostrant la correccio -->
						<xsl:attribute name="value"><xsl:value-of select="$es_correccio"/></xsl:attribute>
					</INPUT>

					<!-- <xsl:apply-templates select="objectbank|assessment|section[$section_number + 1 - 1]|item" /> -->
					<xsl:apply-templates select="objectbank|assessment|section|item" />
						
					<INPUT type="HIDDEN" name="param"/>
					<INPUT type="HIDDEN" name="interaction"/>
					<INPUT type="HIDDEN" name="time"/>
					<INPUT type="HIDDEN" name="next" value="-1" /> <!-- full que vol passar a visualitzar -->
					<INPUT type="HIDDEN" name="wantCorrect"/> <!-- vol veure la correccio -->
					<!-- interaction indica la interaccio que ha omplert usuari i si en demana omplir cap-->
					<INPUT type="HIDDEN" name="QTIUrl"> <!-- Aquest camp ocult indica el param de la URL on es troba l'XML original -->
						<xsl:attribute name="value"><xsl:value-of select="$QTIUrl"/></xsl:attribute>
					</INPUT>

					<!-- BOTONS -->
					<xsl:call-template name="putNorthButtons"/>
					<tr>
					  	<td>
							<table width="100%" border="0">
								<tr>
									<td align="left">
										<!-- BOTO TORNAR -->
										<!--
										<A href="getAssignacionsUsuari">
											<img title="Tornar" alt="Tornar" src="imatges/tornar.gif" border="0" width="55" height="21"/>
										</A>	
										-->
										<a onmouseout="MM_swapImgRestore()">
											<!-- <xsl:attribute name="href">javascript:document.forms[0].next.value='<xsl:value-of select="$section_number - 1"/>';doSubmit2(<xsl:value-of
select="$needResponse"/>);javascript:document.forms[0].submit();</xsl:attribute> -->
											<xsl:attribute name="onClick">
												if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
													javascript:document.forms[0].next.value='<xsl:value-of select="$section_number - 1"/>';
													document.forms[0].submit();
												}
											</xsl:attribute>
											<xsl:attribute name="onMouseOver">MM_swapImage('anterior_<xsl:value-of select="$section_number - 1"/>','','web/imatges/anterior_on.gif',1)</xsl:attribute>
											<img title="Anterior" height="20" alt="Anterior" src="web/imatges/anterior_off.gif" width="20" align="left" border="0">
												<xsl:attribute name="name">anterior_<xsl:value-of select="$section_number - 1"/></xsl:attribute>
											</img>
										</a>
									</td>
									<td align="center" width="100%">
										<!-- BOTO SUBMIT -->
										<xsl:if test="$canCorrect='true'">
											<xsl:call-template name="putContinueButton"/>
										</xsl:if>
									</td>
									<td align="right">
										<!-- ESPAI RESERVAT PER A UN TERCER BOTO -->	
										<a onmouseout="MM_swapImgRestore()">
											<!-- <xsl:attribute name="href">javascript:document.forms[0].next.value='<xsl:value-of select="$section_number + 1"/>';doSubmit2(<xsl:value-of
select="$needResponse"/>);javascript:document.forms[0].submit();</xsl:attribute> -->
											<xsl:attribute name="onClick">
												if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
													javascript:document.forms[0].next.value='<xsl:value-of select="$section_number + 1"/>';
													document.forms[0].submit();
												}
											</xsl:attribute>
											<xsl:attribute name="onMouseOver">MM_swapImage('seguent_<xsl:value-of select="$section_number + 1"/>','','web/imatges/seguent_on.gif',1)</xsl:attribute>
											<img title="Següent" height="20" alt="Següent" src="web/imatges/seguent_off.gif" width="20" align="left" border="0">
												<xsl:attribute name="name">seguent_<xsl:value-of select="$section_number + 1"/></xsl:attribute>
											</img>
										</a>
									</td>
								</tr>
							</table>
						</td>
					</tr>    
				<!-- FI BOTONS -->


				</FORM>
			</table>		
			<SCRIPT>
				select_responses(document.forms[0],
					"<xsl:value-of select="$initialselection"/>");
			</SCRIPT>
			<xsl:if test="$noModify='true'">
				<!-- <b>es_correccio</b> -->
				<SCRIPT>
					disableAll();
				</SCRIPT>
			</xsl:if>
		</BODY>
	</HTML>
</xsl:template>

<xsl:template name="putNorthButtons">
</xsl:template>

<xsl:template name="putContinueButton">
	<xsl:variable name="buttonName"><xsl:call-template name="getContinueButtonName"/></xsl:variable>
	<!-- <input type="image" name="btSubmit" width="145" height="28" onMouseOut="MM_swapImgRestore()" border="0">		
		<xsl:attribute name="onmouseover">MM_swapImage('btSubmit','','imatges/<xsl:value-of select="$buttonName"/>_on.gif',1)</xsl:attribute>
		<xsl:attribute name="alt"><xsl:call-template name="getContinueButtonAltText"/></xsl:attribute>
		<xsl:attribute name="src">imatges/<xsl:value-of select="$buttonName"/>_off.gif</xsl:attribute>
		<xsl:attribute name="href">javascript:document.forms[0].wantCorrect.value='true';doSubmit2(false);javascript:document.forms[0].submit();</xsl:attribute>
	</input> -->
	<a onmouseout="MM_swapImgRestore()">
		<!--<xsl:attribute name="href">javascript:document.forms[0].wantCorrect.value='true';doSubmit2(<xsl:value-of select="$needResponse"/>);javascript:document.forms[0].submit();</xsl:attribute>-->
		<xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {javascript:document.forms[0].wantCorrect.value='true';document.forms[0].submit();}</xsl:attribute>
		<xsl:attribute name="onmouseover">MM_swapImage('btSubmit','','web/imatges/<xsl:value-of select="$buttonName"/>_on.gif',1)</xsl:attribute>
		<img name="btSubmit" width="145" height="28" border="0">
			<xsl:attribute name="alt"><xsl:call-template name="getContinueButtonAltText"/></xsl:attribute>
			<xsl:attribute name="src">web/imatges/<xsl:value-of select="$buttonName"/>_off.gif</xsl:attribute>
		</img>
	</a>
	<!-- <a href="javascript:doSubmit2()" onmouseout="MM_swapImgRestore()">
		<xsl:attribute name="onmouseover">MM_swapImage('btSubmit','','imatges/<xsl:value-of select="$buttonName"/>_on.gif',1)</xsl:attribute>
		<img name="btSubmit" width="145" height="28" border="0">
			<xsl:attribute name="alt"><xsl:call-template name="getContinueButtonAltText"/></xsl:attribute>
			<xsl:attribute name="src">imatges/<xsl:value-of select="$buttonName"/>_off.gif</xsl:attribute>
		</img>
	</a> -->
</xsl:template>

<xsl:template name="getContinueButtonName">
	<!-- El nom del bot. es es defineix amb el param. type de la section -->
	<xsl:variable name="type"><xsl:value-of select="/questestinterop/assessment/section[$section_number + 0]/@type"/></xsl:variable>
	<xsl:choose>
		<xsl:when test="$es_correccio='true'">continuar</xsl:when>
		<xsl:when test="$type='send'">enviar</xsl:when>
		<xsl:when test="$type='continue'">continuar</xsl:when>
		<xsl:when test="$type='start'">inici</xsl:when>
		<xsl:when test="$type='end'">tancar</xsl:when>
		<xsl:when test="$type='back'">tornar</xsl:when>
		<xsl:otherwise>enviar</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="getContinueButtonAltText">enviar la resposta</xsl:template>
	<!-- El nom del bot. es es defineix amb el param. type de la section -->
	<!-- <xsl:variable name="type"><xsl:value-of select="//section[$section_number + 1 - 1]/@type"/></xsl:variable>
	<xsl:choose>
		<xsl:when test="$type='end'">tancar la finestra</xsl:when>
		<xsl:otherwise>enviar la resposta</xsl:otherwise>
	</xsl:choose> -->


<xsl:template match="assessment">
	<!-- <B>
		<xsl:call-template name="itemfeedback">
			<xsl:with-param name="item_ident">
				<xsl:value-of select="@ident"/>
			</xsl:with-param>
		</xsl:call-template>
	</B> -->
	<!-- <xsl:apply-templates select="section[$section_number + 1 - 1]|item"/> --> <!-- +1-1 ... sino no veu que es un numero... -->
	<xsl:choose>
		<xsl:when test="$section_number=-1">
			<xsl:apply-templates select="section|item"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="section[$section_number + 0]|item"/>
		</xsl:otherwise>
	</xsl:choose>
	
</xsl:template>

<xsl:template match="section">
	<B>
		<xsl:if test="$es_correccio='true'">
			<tr><td>
			<script>writeItemScore('<xsl:value-of select="@ident"/>');</script>
			<br/>
			</td></tr>
			<xsl:call-template name="itemfeedback">
				<xsl:with-param name="item_ident">
					<xsl:value-of select="@ident"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</B>

	<xsl:choose>
		<xsl:when test="@type='start'">
			<tr>
				<td class="intro1">
					<xsl:apply-templates select="presentation_material/material[1]"/>
				</td>
			</tr>
			<xsl:apply-templates select="section|item"/> 
			<tr>
				<td class="intro1">
					<xsl:apply-templates select="presentation_material/material[position()!=1]"/>
				</td>
			</tr>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="presentation_material"/>
			<xsl:apply-templates select="section|item"/> 
		</xsl:otherwise>
	</xsl:choose>
	<!-- <xsl:if test="@duration!='' and $es_correccio='false'"> -->
	<xsl:if test="$noModify='false'">
		<SCRIPT>
			startTimerISO("<xsl:value-of select="ancestor::assessment/@duration"/>","<xsl:value-of select="$notebookTime"/>",
				"<xsl:value-of select="@duration"/>","<xsl:value-of select="$sectionTime"/>");
		</SCRIPT>
	</xsl:if>
</xsl:template>

<xsl:template match="sectionfeedback">
	<tr><td class="sectionfeedback"><xsl:apply-templates/></td></tr>
</xsl:template>

<xsl:template match="presentation_material">
	<tr>
		<td class="intro1">
			<xsl:apply-templates/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="item">
	<xsl:variable name="it_ident"><xsl:value-of select="@ident"/></xsl:variable>
	<xsl:if test="$es_correccio='true'">
		<script>writeItemCorrectCell('<xsl:value-of select="$it_ident"/>');</script>
		<!-- tr><td>correct:<script>writeItemCorrect('<xsl:value-of select="$it_ident"/>');</script></td></tr>
		<tr><td>score:<script>writeItemScore('<xsl:value-of select="$it_ident"/>');</script></td></tr -->
	</xsl:if>
	<tr> 
		<td class="caixaItem1" bgcolor="#FFFFFF"> 
			<table border="0" cellpadding="10">
				<xsl:apply-templates select="presentation"/>


			</table>
			<!-- BOTO D'INTERVENCIO -->

			<xsl:if test="$writingEnabled='true'">
				<a onmouseout="MM_swapImgRestore()">
					<xsl:attribute name="href">javascript:document.forms[0].interaction.value='<xsl:value-of select="@ident"/>';doSubmit2(false);javascript:document.forms[0].submit();</xsl:attribute>
					<xsl:attribute name="onMouseOver">MM_swapImage('im_int_<xsl:value-of select="@ident"/>','','web/imatges/question2.gif',1)</xsl:attribute>
					<img title="Preguntar" height="20" alt="Preguntar" src="web/imatges/question.gif" width="20" align="left" border="0">
						<xsl:attribute name="name">im_int_<xsl:value-of select="@ident"/></xsl:attribute>
					</img>
				</a>
			</xsl:if>
			<xsl:call-template name="itemfeedback">
				<xsl:with-param name="item_ident">
					<xsl:value-of select="$it_ident"/>
				</xsl:with-param>
			</xsl:call-template>

			<xsl:if test="$writingEnabled='true'">
				<P>
					<TABLE BORDER="1" WIDTH="80%">
						<xsl:call-template name="interaction">
							<xsl:with-param name="item_ident">
								<xsl:value-of select="$it_ident"/>
							</xsl:with-param>
							<xsl:with-param name="index">1</xsl:with-param>
						</xsl:call-template>
					</TABLE>
				</P>
			</xsl:if>

		</td>
	</tr>
	<tr><td class="sep10">&#160;</td></tr>
</xsl:template>

<xsl:template match="flow_mat">
	<xsl:apply-templates/><BR/>
</xsl:template>

<xsl:template name="flow_nostyle">
	<tr>
		<td class="enunciat1" colspan="2">
			<!-- <p> -->
				<xsl:choose>
					<xsl:when test="material[position()=2]">
					<!-- Si es mostra mes de un material, el primer es separa una línia extra del segon -->
						<P><xsl:apply-templates select="material[1]"/></P>
						<p><xsl:apply-templates select="material[position()!=1]"/></p>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="material"/>
					</xsl:otherwise>
				</xsl:choose>
			<!-- </p> -->
	
		</td>
	</tr>
	<tr>
		<td class="resposta1" colspan="2">
			<xsl:call-template name="template_responses"/>
		</td>	  	  
	</tr>
</xsl:template>

<xsl:template name="flow_style1">
	<TR>
		<TD CLASS="enunciat1" COLSPAN="2">
			<P><xsl:apply-templates select="material[1]"/></P>
		</TD>
	</TR>
	<TR>
		<TD class="enunciat1">
			<P><xsl:apply-templates select="material[position()!=1]"/></P>
		</TD>
		<TD class="resposta1" WIDTH="100%">
			<xsl:call-template name="template_responses"/>
		</TD>
	</TR>
</xsl:template>

<!-- <xsl:template name="flow_style2"> Continuat
	<xsl:apply-templates select="material|response_lid|response_xy|response_str|response_num|response_grp|flow"/>
</xsl:template> -->

<xsl:template name="flow_style2">
	<TR>
		<TD class="enunciat1" width="155">
			<xsl:apply-templates select="material"/>
		</TD>
		<TD class="resposta1">
			<xsl:call-template name="template_responses"/>
		</TD>
	</TR>
</xsl:template>

<xsl:template match="ims_render_object">

			<xsl:variable name="ident"><xsl:call-template name="get_dragdrop_response_ident"/></xsl:variable>
			<INPUT TYPE="HIDDEN"><xsl:attribute name="name"><xsl:value-of select="$ident"/></xsl:attribute></INPUT>


			<xsl:variable name="cardinality">
				<xsl:value-of select="translate(../../@rcardinality,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/> <!-- manera de passar a minuscules a xsl... -->
			</xsl:variable>

			<xsl:variable name="WIDTH"><xsl:call-template name="getHotspotWidth"/></xsl:variable>

			<xsl:variable name="HEIGHT"><xsl:call-template name="getHotspotHeight"/></xsl:variable>

			<xsl:variable name="params">'+ 
				'NAME=<xsl:value-of select="$ident"/>;'+
				'url_base=<xsl:value-of select="$QTIUrl"/>/../;'+
				'image_src=<xsl:value-of select="$QTIUrl"/>/../<xsl:value-of select="ancestor::presentation//matimage/@uri"/>;'+
				'orientation=<xsl:value-of select="@orientation"/>;'+
				'disabled=<xsl:value-of select="$noModify"/>;'+
				'INITPARAM=<xsl:value-of select="substring-after(substring-before(substring-after($initialselection,$ident),'#'),'=')"/>;'+
				<xsl:choose>
					<xsl:when test="$cardinality='ordered'"> <!-- ordenacio -->
						<xsl:for-each select="ancestor::render_extension">
							<xsl:call-template name="putDragDropSources"/>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- drag drop -->
						<xsl:for-each select="ancestor::presentation">
							<xsl:for-each select=".//response_label[@rarea!='']">
								<xsl:variable name="num_param"><xsl:number/></xsl:variable>
								<xsl:for-each select="attribute::*">
									'T<xsl:value-of select="$num_param"/>_<xsl:value-of select="name(.)"/>=<xsl:value-of select="."/>;'+
								</xsl:for-each>
								'T<xsl:value-of select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>;'+
							</xsl:for-each>
							<xsl:call-template name="putDragDropSources"/>
						</xsl:for-each>					
					</xsl:otherwise>
				</xsl:choose>
			'</xsl:variable>
			<!-- <B>Params:<xsl:value-of select="$params"/></B> -->
			<SCRIPT>
				<xsl:choose>
					<xsl:when test="$cardinality='ordered'">
						writeJavaApplet('QVOrdering.class', './appl', 'QVOrdering.jar', '<xsl:value-of select="$params"/>', '<xsl:call-template name="getOrderingWidth"/>', '<xsl:call-template
name="getOrderingHeight"/>' , null, null,'qvApplet','<xsl:value-of select="$ident"/>',true);
					</xsl:when>
					<xsl:otherwise>
						writeJavaApplet('QVDragDrop.class', './appl', 'QVDragDrop.jar', '<xsl:value-of select="$params"/>', '<xsl:call-template name="getHotspotWidth"/>', '<xsl:call-template
name="getHotspotHeight"/>' , null, null,'qvApplet','<xsl:value-of select="$ident"/>',true);
					</xsl:otherwise>
				</xsl:choose>
			</SCRIPT>
</xsl:template>

<xsl:template name="getHotspotWidth">
	<xsl:variable name="temp2"><xsl:value-of select="ancestor::presentation//matimage/@width"/></xsl:variable>
	<xsl:choose><xsl:when test="normalize-space($temp2)=''">500</xsl:when><xsl:otherwise><xsl:value-of select="$temp2"/></xsl:otherwise></xsl:choose>
</xsl:template>

<xsl:template name="getHotspotHeight">
	<xsl:variable name="temp3"><xsl:value-of select="ancestor::presentation//matimage/@height"/></xsl:variable>
	<xsl:choose><xsl:when test="normalize-space($temp3)=''">350</xsl:when><xsl:otherwise><xsl:value-of select="$temp3"/></xsl:otherwise></xsl:choose>
</xsl:template>

<xsl:template name="getOrderingWidth">
	<xsl:variable name="temp2"><xsl:value-of select="translate(@orientation,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/></xsl:variable>
	<xsl:choose>
		<xsl:when test="@width!=''"><xsl:value-of select="@width"/></xsl:when>
		<!-- <xsl:when test="$temp2='row'">600</xsl:when> -->
		<xsl:otherwise>600</xsl:otherwise> <!-- es troba indicada -->
	</xsl:choose>
</xsl:template>

<xsl:template name="getOrderingHeight">
	<xsl:variable name="temp2"><xsl:value-of select="translate(@orientation,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/></xsl:variable>
	<xsl:choose>
		<xsl:when test="@height!=''"><xsl:value-of select="@height"/></xsl:when>
		<xsl:when test="$temp2='row'">40</xsl:when>
		<xsl:otherwise>350</xsl:otherwise> <!-- es troba indicada -->
	</xsl:choose>
</xsl:template>

<xsl:template name="itemfeedback">
	<!-- Si existeix algun feedback a mostrar per l'item amb ident=item_ident es mostra-->
	<xsl:param name="item_ident"/>
	<xsl:variable name="feedback_to_show_ident">
		<xsl:value-of select="substring-before(substring-after($displayfeedback,$item_ident),'#')"/>
	</xsl:variable>

	<xsl:apply-templates select="itemfeedback[@ident=$feedback_to_show_ident]"/>
	<xsl:apply-templates select="sectionfeedback[@ident=$feedback_to_show_ident]"/><!-- <BR/> -->

	<xsl:if test="$feedback_to_show_ident!=''">
		<xsl:call-template name="feed2">
			<xsl:with-param name="item_ident"><xsl:value-of select="$item_ident"/></xsl:with-param>
			<xsl:with-param name="feed"><xsl:value-of select="substring-after($displayfeedback,$feedback_to_show_ident)"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template name="feed2">
	<xsl:param name="item_ident"/>
	<xsl:param name="feed"/>

	<xsl:variable name="feedback_to_show_ident">
		<xsl:value-of select="substring-before(substring-after($feed,$item_ident),'#')"/>
	</xsl:variable>

<!-- TD>
	<xsl:apply-templates select="itemfeedback[@ident=$feedback_to_show_ident]"/>
</TD -->
	<xsl:apply-templates select="sectionfeedback[@ident=$feedback_to_show_ident]"/><!-- <BR/> -->

	<xsl:if test="$feedback_to_show_ident!=''">
		<xsl:call-template name="feed2">
			<xsl:with-param name="item_ident"><xsl:value-of select="$item_ident"/></xsl:with-param>
			<xsl:with-param name="feed"><xsl:value-of select="substring-after($feed,$feedback_to_show_ident)"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>

</xsl:template>

<xsl:template name="get_dragdrop_response_ident">
	<xsl:value-of select="ancestor-or-self::item/@ident"/><xsl:text>--></xsl:text>
	<xsl:value-of select="ancestor::response_grp/@ident"/> <!-- POTSER ES UN ALTRE TIPUS DE RESPONSE... -->
	<xsl:value-of select="ancestor::response_lid/@ident"/> <!-- POTSER ES UN ALTRE TIPUS DE RESPONSE... -->
</xsl:template>

<xsl:template name="putDragDropSources">
	<xsl:for-each select=".//response_label[not(@rarea)]">
		<xsl:variable name="num_param"><xsl:number/></xsl:variable>
		<xsl:for-each select=".//attribute::*">
			'S<xsl:value-of select="$num_param"/>_<xsl:value-of select="name(.)"/>=<xsl:value-of select="."/>;'+
		</xsl:for-each>
		'S<xsl:value-of select="$num_param"/>_text=<xsl:value-of select="normalize-space(.)"/>;'+
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>	