<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="skin_name">infantil</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$skin_name"/>/image</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$skin_name"/>/css</xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>
	
  <xsl:variable name="style">
	  <xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='style']/fieldentry"/>
  </xsl:variable>	
	
 <xsl:variable name="idioma">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>ca</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>
		

 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/>


<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
  <HTML>
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <xsl:call-template name="put_css"/>
    <xsl:call-template name="put_scripts"/>
   </HEAD>
   <BODY onload="window.focus();" >
    <xsl:call-template name="html_body_attrs"><xsl:with-param name="background_image_param">fons.gif</xsl:with-param></xsl:call-template>
    <TABLE width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<!--  Espiral esquerra -->
		<td style="width:38"></td>
		<!--  Contingut -->
		<td height="100%">
		    <TABLE width="710" height="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr>
		    	<td valign="top"><xsl:call-template name="header"/></td>
		    </tr>
		    <tr>
		    	<td height="100%">
				    <TABLE width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
				     <tr><td height="30"/></tr>
				     <tr>
				       <td height="100%" valign="top">
				      	<xsl:apply-templates select="assignment/sections"/><br/>
				       </td>
				     </tr>
				     <tr>
				     	<td valign="bottom">
				     		<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
				     		<tr>
					     		<td valign="bottom"><img alt="Ninot" width="174" height="108"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/ninot.gif</xsl:attribute></img></td>
					     		<td valign="bottom" align="right"><img alt="Clip i taques de tinta" width="250" height="70"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/clip.gif</xsl:attribute></img></td>
					     	</tr>
					     	</TABLE>
					     </td>
				     </tr>
				     </TABLE>
				</td>
			</tr>
			</TABLE>
		</td>
	</tr>
    </TABLE>
   </BODY>
  </HTML>
 </xsl:template>

 <xsl:template match="sections">
   <xsl:variable name="is_preview">
     <xsl:call-template name="isPreviewMode"/>
   </xsl:variable>

  <xsl:variable name="puntuation">
   <xsl:choose>
    <xsl:when test="//assignment/puntuation!=''"><xsl:value-of select="//assignment/puntuation"/></xsl:when>
    <xsl:otherwise>-</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <CENTER>
   <TABLE class="header" border="0" cellpadding="0" cellspacing="1" width="75%">
   <tbody><tr>
    <td>
     <!-- INICI info quadern -->
     <TABLE border="0" cellpadding="0" cellspacing="0" height="60" width="100%">
     <tbody><tr>
      <td class="header" height="8"/>
     </tr>
     <tr>
      <td>
     <!-- INICI llistat fulls -->
       <TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
       <tbody><tr class="section-list-title">
        <td width="10">&#160;</td>
        <td valign="top"><span title="Fulls del quadern">Fulls</span></td>
        <td align="center" valign="top"><xsl:if test="$is_preview='false'"><span style="width:100" title="Estat actual del full">Estat</span></xsl:if></td>
        <td align="center" valign="top"><xsl:if test="$is_preview='false'"><span style="width:100" title="Nombre de lliuraments fets / Nombre màxim de lliuraments">Lliuraments</span></xsl:if></td>
        <td align="center" valign="top"><xsl:if test="$is_preview='false'"><span style="width:100" title="Puntuació">Puntuació</span></xsl:if></td>       
       </tr>
      <xsl:for-each select="section">
       <xsl:variable name="section_name">
        <xsl:choose>
         <xsl:when test="@name=''"> Full <xsl:value-of select="@num"/>
         </xsl:when>
         <xsl:otherwise>
          <xsl:value-of select="@name"/>
         </xsl:otherwise>
        </xsl:choose>
       </xsl:variable>
       <xsl:variable name="section_path">
         <xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/>
       </xsl:variable>
       <xsl:variable name="section_limit">
        <xsl:apply-templates select="section_delivery"/>
        <xsl:if test="ancestor::assignment/max_delivery!='-1'">/<xsl:apply-templates select="ancestor::assignment/max_delivery"/></xsl:if>
       </xsl:variable>
       <xsl:variable name="section_puntuation">
        <xsl:choose>
         <xsl:when test="section_puntuation"><xsl:apply-templates select="section_puntuation"/></xsl:when>
         <xsl:otherwise>-</xsl:otherwise>
        </xsl:choose>
       </xsl:variable>
       
     
      <tr class="sections-list-text">
       <td>
         <xsl:if test="($view='candidate' and section_states/intervencio_professor='true') or ($view!='candidate' and section_states/intervencio_alumne='true')"><IMG width="10" height="10" border="0" title="intervenció pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="($view='candidate' and section_states/intervencio_alumne='true') or ($view!='candidate' and section_states/intervencio_professor='true')"><IMG width="10" height="10" border="0" title="esperant resposta d'intervenció"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG></xsl:if>
       </td>
        <td>
         <TABLE border="0" style="width:100%" cellpadding="0" cellspacing="0">
         <tbody><tr class="sections-list-text">
          <td style="vertical-align:top;width:25px;" ><xsl:value-of select="@num"/>. </td>
          <td>
	         <a title="visualitza el full">
	          <xsl:attribute name="href"><xsl:value-of select="normalize-space($section_path)"/></xsl:attribute>
	          <xsl:value-of select="$section_name"/> 
	         </a>
	      </td>
	     </tr></tbody>
	     </TABLE>
        </td>
        <td align="center">
        <xsl:if test="$is_preview='false'">
         <!--  Estats -->
         <TABLE border="0" cellpadding="3" cellspacing="0" class="text" style="font-weight:bold">
         <tbody><tr>
          <td width="50" align="center">
			<div align="center">
	         <xsl:choose>
	         	<xsl:when test="section_states/corregit='true'"><IMG width="15" height="15" border="0" title="full corregit"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/estat_corregit.gif</xsl:attribute></IMG></xsl:when>
	         	<xsl:when test="section_states/lliurat='true'"><IMG width="15" height="15" border="0" title="full lliurat"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/estat_lliurat.gif</xsl:attribute></IMG></xsl:when>
	         	<xsl:when test="section_states/iniciat='true'"><IMG width="15" height="15" border="0" title="full iniciat"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/estat_iniciat.gif</xsl:attribute></IMG></xsl:when>
	         	<xsl:otherwise><IMG width="15" height="15" border="0" title="full no iniciat"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/estat_no_iniciat.gif</xsl:attribute></IMG></xsl:otherwise>
	         </xsl:choose>
	        </div>          
          </td>
         </tr></tbody></TABLE>
         </xsl:if>
        </td>
        <td class="text" align="center"><xsl:if test="$is_preview='false'"><xsl:value-of select="$section_limit"/></xsl:if></td>
        <td class="text" align="center"><xsl:if test="$is_preview='false'"><xsl:value-of select="$section_puntuation"/>&#160;</xsl:if></td>
      </tr>
      </xsl:for-each>       
      <tr class="header-background">
       <td colspan="5" style="height:5px"></td>
      </tr>
      <tr class="sections-list-text">
       <td colspan="4"></td>
        <td align="center" class="sections-list-puntuation"><xsl:if test="$is_preview='false'"><xsl:value-of select="$puntuation"/>&#160;</xsl:if></td>
      </tr>

       </tbody>
       </TABLE>
      </td>
     </tr></tbody>
     </TABLE>
     <!-- FI llistat fulls -->
    </td>
   </tr>
</tbody>
   </TABLE>
    </CENTER>
 </xsl:template>
 

 

<!-- #################### SECTION ################# -->
 
<xsl:template name="getItemFeedback">
  <!-- Si existeix algun feedback a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <!--xsl:variable name="showFeedback"><xsl:value-of select="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showFeedback']/@fieldentry" /></xsl:variable-->
  <xsl:variable name="showFeedback">
   <xsl:value-of select="itemcontrol/@feedbackswitch"/>
  </xsl:variable>
  <xsl:if test="normalize-space($showFeedback)!='No'">
   <xsl:if test="$es_correccio='true'">
  <TABLE width="100%" border="0" cellpadding="0" cellspacing="0">
   <tbody><tr>
   <td height="10px"></td>
   </tr>
   <tr>
   <td valign="top" class="text">
    <script> if(isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
		document.write('<span class="item-feedback-ok">');
	]]> }else { <![CDATA[
		document.write('<span class="item-feedback-ko">');
	]]> } </script>

    <xsl:call-template name="getDisplayFeedback">
     <xsl:with-param name="ident_item">
      <xsl:value-of select="$item_ident"/>
     </xsl:with-param>
     <xsl:with-param name="feedback">
      <xsl:value-of select="$displayfeedback"/>
     </xsl:with-param>
    </xsl:call-template>
    
    <script><![CDATA[
				document.write('</span>');
	]]></script>
   	</td>
   </tr></tbody>
   </TABLE>
   </xsl:if>
  </xsl:if>
 </xsl:template>

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head_title">
    QV - <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="put_css">
    <LINK TYPE="text/css" rel="stylesheet"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>.css</xsl:attribute></LINK>
    <LINK TYPE="text/css" rel="stylesheet"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_verdana.css</xsl:attribute></LINK>
 </xsl:template>
  
 <xsl:template name="html_body_attrs">
  <xsl:param name="background_image_param"/>
  <xsl:variable name="background_image">
   <xsl:choose>
    <xsl:when test="$background_image_param=''">fons.gif</xsl:when>
    <xsl:otherwise><xsl:value-of select="$background_image_param"/></xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
   <xsl:attribute name="text">#000000</xsl:attribute>
   <xsl:attribute name="bgColor">#ffffff</xsl:attribute>
   <xsl:attribute name="leftMargin">0</xsl:attribute>
   <xsl:attribute name="background"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$background_image"/></xsl:attribute>
   <xsl:attribute name="topMargin">0</xsl:attribute>
   <xsl:attribute name="marginwidth">0</xsl:attribute>
   <xsl:attribute name="marginheight">0</xsl:attribute>
 </xsl:template>

 <xsl:template name="questestinterop_body">
    <TABLE width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<!--  Espiral esquerra -->
		<td style="width:38"></td>
		<!--  Contingut -->
		<td height="100%">
		    <TABLE width="710" height="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr>
		    	<td valign="top"><xsl:call-template name="header"/></td>
		    </tr>
		    <tr>
		    	<td style="height:40px"/>
		    </tr>
		    <tr>
		    	<td height="100%">
				    <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
				</td>
			</tr>
			<tr>
		       <td style="height:15px"></td>
			</tr>
			<tr class="header-background">
		       <td style="height:3px"></td>
			</tr>
			<tr>
		       <td style="height:5px"></td>
			</tr>
		    <tr>
		    	<td>
		    		<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
		    		<tr>
		    			<td width="40"></td>
		    			<td ><xsl:call-template name="put_action_buttons"/></td>
		    			<td align="right" width="100px">
							<xsl:if test="$style!='formal'">
								<xsl:call-template name="put_navigation_buttons"/>
							</xsl:if>
						</td>
		    		</tr>
		    		</TABLE>
				</td>
			</tr>
			<tr>
				<td height="30"></td>
			</tr>
			</TABLE>
		</td>
	</tr>
    </TABLE>
 </xsl:template>
 
 <xsl:template match="*[local-name()='presentation_material']">
   <TABLE width="100%" border="0" cellspacing="0" cellpadding="2">
     <tbody><tr> 
       <td class="titol2"><IMG width="40" height="1" border="0"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/blanc.gif</xsl:attribute></IMG></td>
       <td class="titol2"><xsl:call-template name="getSectionAdditionalMaterial"/></td>
     </tr></tbody>
   </TABLE>
 </xsl:template>

<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>
	
  <xsl:variable name="showInteraction">
   <xsl:call-template name="show_interaction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$it_ident"/></xsl:with-param>
  </xsl:call-template>
  </xsl:variable>
	

 <TABLE width="100%" border="0" cellspacing="0" cellpadding="2">
 <tbody><tr> 
    <td colspan="2">&#160;</td>
  </tr>
  <tr> 
    <td width="40" class="titol2" align="right" valign="top" height="57"></td>
    <td class="titol2" height="57"> 
      <!-- Assessment -->
      <p class="item-assessment">
       <xsl:value-of select="position()"/>.
       <xsl:call-template name="getAssessment">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
      </p>
      <!-- Additional material -->
      <p class="item-additional-material">
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>       
      </p>
      <!-- Responses -->
      <p class="item-response" style="margin-left:30px">
       <xsl:call-template name="getResponses">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
      </p>
      <!-- Feedback -->
	 <xsl:if test="$es_correccio='true'">
      <p class="item-feedback" >
       <xsl:call-template name="getItemFeedback">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>       
      </p>
	 </xsl:if>
	  
	  <xsl:if test="$showInteraction='true'">
      <p class="item-interaction">
        <xsl:call-template name="interactions">
         <xsl:with-param name="item_ident">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
         <xsl:with-param name="index">1</xsl:with-param>
        </xsl:call-template>       
      </p>
      <br/>
	  </xsl:if>
    </td>
  </tr>
  </tbody>
  </TABLE>

 </xsl:template> 
 
  
 <xsl:template name="interactions">
  <xsl:param name="item_ident"/>
  <xsl:param name="index"/>
  <xsl:variable name="showInteraction">
   <xsl:call-template name="show_interaction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$item_ident"/></xsl:with-param>
   </xsl:call-template>
  </xsl:variable>
  
  <A>
   <xsl:attribute name="name">int_<xsl:value-of select="$item_ident"/></xsl:attribute>
  </A>
  <table width="98%" border="0" cellspacing="0" cellpadding="0">
   <tr>
    <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
     <td width="40" align="left" valign="top">
      <xsl:call-template name="put_add_interaction_button"/>
     </td>
     <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td style="background-color:#000099;height:1px;"></td>
      </tr>
      <tr>
      	<td>
	      <table width="100%" height="35" border="0" cellspacing="0" cellpadding="3" class="item-add-interaction">
	       <xsl:call-template name="interaction">
	        <xsl:with-param name="item_ident">
	         <xsl:value-of select="$item_ident"/>
	        </xsl:with-param>
	        <xsl:with-param name="index">1</xsl:with-param>
	       </xsl:call-template>
	      </table>
	    </td>
	  </tr>
      <tr>
      	<td style="background-color:#000099;height:1px;"></td>
      </tr>
      </table>
     </td>
    </xsl:if>
   </tr>
  </table>
 </xsl:template>
 
 <xsl:template name="put_add_interaction_button">
  <xsl:variable name="show_interaction_button"><xsl:call-template name="show_interaction_button"/></xsl:variable>  
  <a title="Afegir una nova intervenció" onmouseover="window.status='';return true;">
   <xsl:attribute name="href">#int_<xsl:value-of select="@ident"/></xsl:attribute>
   <xsl:attribute name="onclick"><xsl:if test="$show_interaction_button='true'"><xsl:call-template name="add_interaction"/></xsl:if></xsl:attribute>
   <IMG width="46" height="38" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/add_interaction.gif</xsl:attribute></IMG>
 </a>
 </xsl:template>

 <xsl:template name="put_send_interaction_button">
  <a title="Envia la nova intervenció" onmouseover="window.status='';return true;">
   <xsl:attribute name="href">#int_<xsl:value-of select="@ident"/></xsl:attribute>
   <xsl:attribute name="onclick"><xsl:call-template name="send_interaction"/></xsl:attribute>
   <IMG width="30" height="30" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/send_interaction.gif</xsl:attribute></IMG>
 </a>
 </xsl:template>

  
<!-- #################### COMMONS ################# -->
<xsl:template name="header">
  <xsl:variable name="logo_image">
	<xsl:choose>
	  <xsl:when test="$style='formal'">logo-formal.gif</xsl:when>
	  <xsl:otherwise>logo.gif</xsl:otherwise>
	</xsl:choose>
  </xsl:variable>	
	
  <TABLE class="header" border="0" cellpadding="0" cellspacing="0" width="710">
  <tbody>
  <tr>
  	<td valign="top">
	   <a>
	    <xsl:if test="assessment/section">
	     <xsl:attribute name="href">http://clic.xtec.net/qv_web/ca</xsl:attribute>
	     <xsl:attribute name="target">_blank</xsl:attribute>
	     <xsl:attribute name="title">Tornar a la pàgina principal</xsl:attribute>
	    </xsl:if>
	    <img alt="Quaderns Virtuals" width="280" height="119" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$logo_image"/></xsl:attribute></img>
	   </a>
    </td>
    <td width="20"><IMG width="20" height="1" border="0"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/blanc.gif</xsl:attribute></IMG></td>
    <td style="width:100%">
	    <table class="header-background" border="0" cellpadding="5" cellspacing="0" height="80" width="100%">
	    <tbody>
	    <tr>
      	 <td class="header-title">
          <xsl:choose>
           <xsl:when test="assignment!=''"><xsl:value-of select="assignment/@name"/></xsl:when>
           <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
          </xsl:choose>
     	 </td>
	    </tr>
	    <tr>
	    	<td class="header-text">
	        <xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='materia']/fieldentry"/>
	        <br/><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='level']/fieldentry"/>
	    	</td>
	    </tr>
	    </tbody>
	    </table>
    </td>
    <td valign="top" align="right">
		<xsl:if test="$style!='formal'">
			<img alt="Quaderns Virtuals" width="38" height="38" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/sol.gif</xsl:attribute></img>
		</xsl:if>
	</td>
  </tr>
	    <!-- tr>
	     <td width="120"/>
	     <td>
	      <TABLE class="header-text" border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	      <tbody><tr>
	       <td>
	        <span style="font-weight:bold;">
	         <xsl:choose>
	          <xsl:when test="assignment!=''"><xsl:value-of select="assignment/@name"/></xsl:when>
	          <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
	         </xsl:choose>
	         </span>
	        <br/><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='materia']/fieldentry"/>
	        <br/><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='level']/fieldentry"/>
	       </td>
	       <td valign="top" align="right" height="100%">
	        <xsl:if test="assessment/section">
	         <span style="font-weight:bold; font-size:18"><xsl:value-of select="$section_number"/></span>/<xsl:value-of select="$section_max_number"/>
	         <br/><br/><span style="valign:bottom">
	          puntuació:&#160;
	          <strong>
	           <xsl:choose>
	            <xsl:when test="$es_correccio='true'">
	             <script>writeItemScore('<xsl:value-of select="assessment/section[position()=$section_number]/@ident"/>');</script>
	            </xsl:when>
	            <xsl:otherwise> - </xsl:otherwise>
	           </xsl:choose>
	          </strong>
	         </span>
	        </xsl:if>
	       </td>
	       <td width="10"/>
	      </tr></tbody>
	      </TABLE>
	     </td>
	    </tr>
	   </tbody></table>
	  </td></tr-->
  </tbody>
  </TABLE>
</xsl:template>

 <xsl:template name="put_image_button">
  <xsl:param name="href"/>
  <xsl:param name="onclick"/>
  <xsl:param name="button_name"/>
  <xsl:param name="button_tip"/>
  <xsl:param name="image_off_src"/>
  <xsl:param name="image_on_src"/>
   <a onmouseout="MM_swapImgRestore()" class="buttons">
    <xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute>
    <xsl:attribute name="onclick"><xsl:value-of select="$onclick"/></xsl:attribute>
    <xsl:attribute name="onMouseOver">window.status='';MM_swapImage('<xsl:value-of select="$button_name"/>','','<xsl:value-of select="$path_imatges"/>/<xsl:value-of select="$image_on_src"/>',1);return true;</xsl:attribute>
    <img border="0" align="top">
     <xsl:attribute name="name"><xsl:value-of select="$button_name"/></xsl:attribute>
     <xsl:attribute name="alt"><xsl:value-of select="$button_tip"/></xsl:attribute>
     <xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/<xsl:value-of select="$image_off_src"/></xsl:attribute>
    </img>
   </a>
 </xsl:template>
 
 <xsl:template name="put_navigation_buttons">
     <TABLE border="0" cellspacing="0" cellpadding="0">
      <tr>
       <td align="left" valign="top" width="33%">
          <a title="Pàgina anterior" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
           <IMG width="24" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back.gif</xsl:attribute></IMG>
         </a>
          <a title="Pàgina principal" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_home"/></xsl:attribute>
           <IMG width="42" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/home.gif</xsl:attribute></IMG>
         </a>
          <a title="Pàgina següent" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
           <IMG width="24" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next.gif</xsl:attribute></IMG>
         </a>
       </td>
      </tr>
     </TABLE> 
 </xsl:template>
 
 <xsl:template name="put_action_buttons">
   <xsl:variable name="is_preview">
     <xsl:call-template name="isPreviewMode"/>
   </xsl:variable>
   <xsl:variable name="show_correct_button">
    <xsl:call-template name="show_correct_button">
     <xsl:with-param name="is_preview" select="$is_preview"/>
    </xsl:call-template>
  </xsl:variable>
   <xsl:variable name="show_save_button">
    <xsl:call-template name="show_save_button">
     <xsl:with-param name="is_preview" select="$is_preview"/>
    </xsl:call-template>
  </xsl:variable>
   <xsl:variable name="show_deliver_button">
    <xsl:call-template name="show_deliver_button">
     <xsl:with-param name="is_preview" select="$is_preview"/>
    </xsl:call-template>
  </xsl:variable>
     <TABLE border="0" cellspacing="0" cellpadding="0">
      <tr>
       <td>
        <xsl:if test="normalize-space($show_correct_button)='true'">
            <a title="Corregeix aquest full" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
				<xsl:choose>
					<xsl:when test="$style='formal'"><br/><span style="color:#FFF;background-color:#000099;padding:10px;text-decoration:none;font-weight:bold;"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">correct</xsl:with-param></xsl:call-template></span></xsl:when>
					<xsl:otherwise><IMG width="55" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/corregir.gif</xsl:attribute></IMG></xsl:otherwise>
				</xsl:choose>
            </a>
        </xsl:if>
        &#160;
        <xsl:if test="normalize-space($show_save_button)='true'">
	        <a title="Guarda el quadern" onmouseover="window.status='';return true;">
	         <xsl:attribute name="href"><xsl:call-template name="save_section"/></xsl:attribute>
	       	 <IMG width="41" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/guardar.gif</xsl:attribute></IMG>
	        </a>
        </xsl:if>
        &#160;
        <xsl:if test="normalize-space($show_deliver_button)='true'">
            <a title="Lliura aquest full per que el corregeixi el professor" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
           	 <IMG width="54" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/lliurar.gif</xsl:attribute></IMG>
            </a>
        </xsl:if>
       </td>
       </tr>
     </TABLE>
 </xsl:template>
	
	
<!-- #################### COMMONS ################# -->

<xsl:template name="get_lang_name">
	<xsl:param name="id"/>
  <xsl:choose>
  	<xsl:when test="$id='correct'">
  		<xsl:choose>
  			<xsl:when test="$idioma='es'">Comprueba las respuestas</xsl:when>
  			<xsl:otherwise>Comprova les respostes</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:otherwise>
		<xsl:value-of select="$id"/>
  	</xsl:otherwise>
  </xsl:choose>	
</xsl:template>
	

</xsl:stylesheet>
