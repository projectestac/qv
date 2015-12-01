<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="skin_name">edu365_mud</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$skin_name"/>/image</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$skin_name"/>/css</xsl:variable>
 <xsl:variable name="specific_scripts_path">skins/<xsl:value-of select="$skin_name"/>/scripts</xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
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
    <!--  Números dels fulls -->
	<div id="muds">
		<div id="titol">
		  <h1>
		  	<xsl:choose>
		  		<xsl:when test="//section[position()=$section_number]/@title!=''"><xsl:value-of select="//section[position()=$section_number]/@title"/></xsl:when>
		  		<xsl:otherwise><xsl:value-of select="$header_title"/></xsl:otherwise>
		  	</xsl:choose>
		  </h1>
		  <div id="numeros">
			  <div align="right">
			  </div>
		</div>
	  </div>
	  
	  <!--  AVALUAT o PRACTICA -->
	  <xsl:variable name="tipus">
	  <xsl:choose>
	  	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry='practica'">practica</xsl:when>
	  	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry='avaluat'">avaluat</xsl:when>
	  </xsl:choose>
	  </xsl:variable>
	  <div><xsl:attribute name="id"><xsl:value-of select="$tipus"/></xsl:attribute></div>
	  
	  <!--  Contingut -->
	  <div id="contingut">
      	<xsl:apply-templates select="assignment/sections"/><br/>
  	  </div>
	</div> 
	  
	<!--  Icones  -->
	<div id="icones">
		<img align="top" height="37" width="48"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_top.gif</xsl:attribute></img>
		<xsl:call-template name="put_icones_buttons"/>
		<img align="top" height="37" width="48"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_bottom.gif</xsl:attribute></img>
	</div>

	<div id="barra" title="barra edu365.com"></div>
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
   <TABLE class="header" border="0" cellpadding="0" cellspacing="1" width="100%">
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
        <td valign="top"></td>
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
	<div>
		<xsl:attribute name="id">feedback_<xsl:value-of select="$item_ident"/></xsl:attribute>	  	  
   <xsl:if test="$es_correccio='true' and contains($initialselection,$item_ident)">
    <script> if (isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
			document.write("<div class='item-feedback-ok'>");
		]]> }else { <![CDATA[
			document.write("<div class='item-feedback-ko'>");
		]]> } 
	</script>	   
    <xsl:call-template name="getDisplayFeedback">
     <xsl:with-param name="ident_item">
      <xsl:value-of select="$item_ident"/>
     </xsl:with-param>
     <xsl:with-param name="feedback">
      <xsl:value-of select="$displayfeedback"/>
     </xsl:with-param>
    </xsl:call-template>
   </xsl:if>
	</div>
  </xsl:if>
 </xsl:template>

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head_title">
    QV - <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="put_css">
    <xsl:choose>
	 	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='nivell']/fieldentry='primaria'">
		    <LINK rel="stylesheet" media="screen" type="text/css" title="default"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_primaria.css</xsl:attribute></LINK>
			<LINK rel="stylesheet" media="print" type="text/css" title="imprimeix"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_primaria_print.css</xsl:attribute></LINK>
	 	</xsl:when>
	 	<xsl:otherwise>
		    <LINK rel="stylesheet" media="screen" type="text/css" title="default"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>.css</xsl:attribute></LINK>
			<LINK rel="stylesheet" media="print" type="text/css" title="imprimeix"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_print.css</xsl:attribute></LINK>
	 	</xsl:otherwise>
	</xsl:choose>


 </xsl:template>
 
 <xsl:template name="put_specific_scripts">
    <SCRIPT type="text/javascript">
	    <xsl:attribute name="src"><xsl:value-of select="$specific_scripts_path"/>/<xsl:value-of select="$skin_name"/>.js</xsl:attribute>
    </SCRIPT>
	<script language="JavaScript" src="http://clic.xtec.net/qv/dist/launchQV.js" type="text/javascript"></script>
 </xsl:template>
 
  
 <xsl:template name="questestinterop_body">
    <!--  Números dels fulls -->
	<div id="muds">
		<div id="titol">
		  <h1>
		  	<xsl:choose>
		  		<xsl:when test="//section[position()=$section_number]/@title!=''"><xsl:value-of select="//section[position()=$section_number]/@title"/></xsl:when>
		  		<xsl:otherwise><xsl:value-of select="$header_title"/></xsl:otherwise>
		  	</xsl:choose>
		  </h1>
		  <div id="numeros">
			  <div align="right">
			  	<xsl:if test="count(//section)>1">
			      <xsl:for-each select="//section">
			       <xsl:choose>
			        <xsl:when test="position()=$section_number">
					  <xsl:call-template name="put_cercle_button">
					  	<xsl:with-param name="number"><xsl:value-of select="position()"/></xsl:with-param>
					  </xsl:call-template>
			         </xsl:when>
			         <xsl:otherwise>
					  <xsl:call-template name="put_cercle_button">
					  	<xsl:with-param name="number"><xsl:value-of select="position()"/></xsl:with-param>
						<xsl:with-param name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template></xsl:with-param>
					  </xsl:call-template>
			         </xsl:otherwise>
			        </xsl:choose>
			      </xsl:for-each>
			     </xsl:if>
			  </div>
		</div>
	  </div>
	  
	  <!--  AVALUAT o PRACTICA -->
	  <xsl:variable name="tipus">
	  <xsl:choose>
	  	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry='practica'">practica</xsl:when>
	  	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry='avaluat'">avaluat</xsl:when>
	  </xsl:choose>
	  </xsl:variable>
	  <div><xsl:attribute name="id"><xsl:value-of select="$tipus"/></xsl:attribute></div>
	  
	  <!--  Contingut -->
	  <div id="contingut">
		<a name="top"/>	 
	    <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
	    <center>
	      <xsl:if test="( (not(//qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry) or (//qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry!='fals') and (//qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry!='totes')) and (not(//section[position()=$section_number]/qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry) or //section[position()=$section_number]/qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry!='fals') )">
			<xsl:call-template name="put_image_button">
			  	<xsl:with-param name="name">corregeix</xsl:with-param>
				<xsl:with-param name="href"><xsl:call-template name="correct_section"/></xsl:with-param>
			  	<xsl:with-param name="width">85</xsl:with-param>
			  	<xsl:with-param name="height">20</xsl:with-param>
			</xsl:call-template>
		  </xsl:if>
			<br/><br/>
	    </center>
  	  </div>
	</div> 
	  
	<!--  Icones  -->
	<div id="icones">
		<img align="top" height="37" width="48"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_top.gif</xsl:attribute></img>
		<xsl:call-template name="put_icones_buttons"/>
		<img align="top" height="37" width="48"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_bottom.gif</xsl:attribute></img>
	</div>

	<div id="barra" title="barra edu365.com"></div>
	
	<div id="credits" class="credits">
		Per fer algunes activitats cal tenir instal·lat el <A href="http://clic.xtec.net/ca/jclic/instjava.htm" target="_blank">motor Java</A>
	</div>
	 
	<xsl:call-template name="footer"/>
	
 </xsl:template>

 <xsl:template name="footer">
 </xsl:template>
 
 <xsl:template name="put_icones_buttons">
  <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='practica']/fieldentry!=''">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">practica</xsl:with-param>
		<xsl:with-param name="href">javascript:MM_openBrWindow('<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='practica']/fieldentry"/>','practica','width=650,height=525')</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
  <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='avaluat']/fieldentry!=''">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">avaluat</xsl:with-param>
		<xsl:with-param name="href">javascript:MM_openBrWindow('<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='avaluat']/fieldentry"/>','avaluat','width=650,height=525')</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
  <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='eines']/fieldentry!=''">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">eines</xsl:with-param>
		<xsl:with-param name="href">javascript:MM_openBrWindow('<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='eines']/fieldentry"/>','eines','width=650,height=525')</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
  <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='glossari']/fieldentry!=''">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">glossari</xsl:with-param>
		<xsl:with-param name="href">javascript:MM_openBrWindow('<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='glossari']/fieldentry"/>','glossari','width=650,height=525')</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
  <xsl:if test="not(//qtimetadata/qtimetadatafield[fieldlabel='pregunta']/fieldentry) or //qtimetadata/qtimetadatafield[fieldlabel='pregunta']/fieldentry!='fals'">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">pregunta</xsl:with-param>
		<xsl:with-param name="href">http://edu365.com/pls/edu365/edu_sup_ori_creacio_consulta.cre_form?p_flux_id=1</xsl:with-param>
	  	<xsl:with-param name="target">_blank</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
  <xsl:if test="not(//qtimetadata/qtimetadatafield[fieldlabel='imprimeix']/fieldentry) or //qtimetadata/qtimetadatafield[fieldlabel='imprimeix']/fieldentry!='fals'">
	<xsl:call-template name="put_image_button">
	  	<xsl:with-param name="name">imprimeix</xsl:with-param>
		<xsl:with-param name="href">javascript:window.print();</xsl:with-param>
	</xsl:call-template>
  </xsl:if>
 </xsl:template>
 
 <xsl:template match="*[local-name()='presentation_material']">
     <div><xsl:call-template name="getSectionAdditionalMaterial"/></div>
 </xsl:template>

<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>

 <TABLE width="100%" border="0" cellspacing="0" cellpadding="2">
 <tbody>
  <tr> 
    <td class="text" height="57"> 
      <!-- Assessment -->
      <p class="item-assessment">
	   <xsl:if test="not(//qtimetadata/qtimetadatafield[fieldlabel='num_pregunta']/fieldentry) or //qtimetadata/qtimetadatafield[fieldlabel='num_pregunta']/fieldentry!='fals'">
	       <xsl:value-of select="position()"/>.
	   </xsl:if>
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
      <!--p class="item-response" style="margin-left:100px"-->
      <p style="margin-left:30px">
       <xsl:call-template name="getResponses">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
      </p>
      <!-- Feedback -->
      <p class="item-feedback" >
       <xsl:call-template name="getItemFeedback">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>       
      </p>
      <!-- Correct button -->
      <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='corregeix']/fieldentry='totes'">
      <p style="text-align:right">
      		<br/>
			<xsl:call-template name="put_image_button">
			  	<xsl:with-param name="name">corregeix</xsl:with-param>
				<xsl:with-param name="href"><xsl:call-template name="deliver_section"/></xsl:with-param>
			  	<xsl:with-param name="width">85</xsl:with-param>
			  	<xsl:with-param name="height">20</xsl:with-param>
			</xsl:call-template>
      </p>
      </xsl:if>
      <br/>
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
  <TABLE class="header" border="0" cellpadding="0" cellspacing="0" width="710">
  <tbody>
  <tr>
  	<td valign="top">
	   <a>
	    <xsl:if test="assessment/section">
	     <xsl:attribute name="onClick"><xsl:call-template name="go_home"/></xsl:attribute>
	     <xsl:attribute name="href">#</xsl:attribute>
	     <xsl:attribute name="title">Tornar a la pàgina principal</xsl:attribute>
	    </xsl:if>
	    <img alt="Quaderns Virtuals" width="280" height="119" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logo.gif</xsl:attribute></img>
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
    <td valign="top" align="right"><img alt="Quaderns Virtuals" width="38" height="38" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/sol.gif</xsl:attribute></img></td>
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

 <xsl:template name="put_cercle_button">
 	<xsl:param name="number"/>
    <xsl:param name="href"/>
    <xsl:variable name="img_name">
 		<xsl:choose>
 			<xsl:when test="$href!=''">cercle</xsl:when>
 			<xsl:otherwise>cercle_inactiu</xsl:otherwise>
 		</xsl:choose>
    </xsl:variable>
 	<xsl:call-template name="put_button">
	  	<xsl:with-param name="img"><xsl:value-of select="$img_name"/><xsl:value-of select="$number"/>.gif</xsl:with-param>
	  	<xsl:with-param name="imgb">cercle<xsl:value-of select="$number"/>b.gif</xsl:with-param>
		<xsl:with-param name="href"><xsl:value-of select="$href"/></xsl:with-param>
	  	<xsl:with-param name="title"><xsl:value-of select="$number"/></xsl:with-param>
	  	<xsl:with-param name="name">cercle<xsl:value-of select="$number"/></xsl:with-param>
	  	<xsl:with-param name="width">30</xsl:with-param>
	  	<xsl:with-param name="height">29</xsl:with-param> 		
 	</xsl:call-template>
 </xsl:template>

 <xsl:template name="put_image_button">
 	<xsl:param name="name"/>
    <xsl:param name="href"/>
    <xsl:param name="width"/>
    <xsl:param name="height"/>
    <xsl:param name="target"/>
	<xsl:call-template name="put_button">
	  	<xsl:with-param name="img"><xsl:value-of select="$name"/>.gif</xsl:with-param>
	  	<xsl:with-param name="imgb"><xsl:value-of select="$name"/>1.gif</xsl:with-param>
		<xsl:with-param name="href"><xsl:value-of select="$href"/></xsl:with-param>
	  	<xsl:with-param name="title"><xsl:value-of select="$name"/></xsl:with-param>
	  	<xsl:with-param name="name"><xsl:value-of select="$name"/></xsl:with-param>
	  	<xsl:with-param name="width"><xsl:choose><xsl:when test="$width!=''"><xsl:value-of select="$width"/></xsl:when><xsl:otherwise>48</xsl:otherwise></xsl:choose></xsl:with-param>
	  	<xsl:with-param name="height"><xsl:choose><xsl:when test="$height!=''"><xsl:value-of select="$height"/></xsl:when><xsl:otherwise>54</xsl:otherwise></xsl:choose></xsl:with-param>
	  	<xsl:with-param name="target"><xsl:value-of select="$target"/></xsl:with-param>
	</xsl:call-template>
 </xsl:template>

 <xsl:template name="put_button">
  <xsl:param name="img"/>
  <xsl:param name="imgb"/>
  <xsl:param name="title"/>
  <xsl:param name="name"/>
  <xsl:param name="width"/>
  <xsl:param name="height"/>
  <xsl:param name="href"/>
  <xsl:param name="target"/>
  <xsl:choose>
  <xsl:when test="$href!=''">
	  	<A onmouseout="MM_swapImgRestore()">
	  	 <xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute>
	  	 <!--xsl:attribute name="href"><xsl:choose><xsl:when test="contains($href, 'javascript')">#item_<xsl:value-of select="@ident"/></xsl:when><xsl:otherwise><xsl:value-of select="$href"/></xsl:otherwise></xsl:choose></xsl:attribute-->
	  	 <xsl:attribute name="onmouseover">MM_swapImage('<xsl:value-of select="$name"/>','','<xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$imgb"/>',1)</xsl:attribute>
	  	 <!--xsl:attribute name="onclick"><xsl:value-of select="$href"/></xsl:attribute-->
		 <xsl:attribute name="target"><xsl:choose><xsl:when test="$target!=''"><xsl:value-of select="$target"/></xsl:when><xsl:otherwise></xsl:otherwise></xsl:choose></xsl:attribute>
	  	 <IMG border="0" align="top" >
		  	<xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$img"/></xsl:attribute>
		  	<xsl:attribute name="width"><xsl:value-of select="$width"/></xsl:attribute>
		  	<xsl:attribute name="height"><xsl:value-of select="$height"/></xsl:attribute>
		  	<xsl:attribute name="name"><xsl:value-of select="$name"/></xsl:attribute>
		  	<xsl:attribute name="title"><xsl:value-of select="$title"/></xsl:attribute>
		  	<xsl:attribute name="alt"><xsl:value-of select="$title"/></xsl:attribute>
		  </IMG>
		 </A>
	</xsl:when>
	<xsl:otherwise>
	  <IMG border="0">
	  	<xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$img"/></xsl:attribute>
	  	<xsl:attribute name="width"><xsl:value-of select="$width"/></xsl:attribute>
	  	<xsl:attribute name="height"><xsl:value-of select="$height"/></xsl:attribute>
	  	<xsl:attribute name="name"><xsl:value-of select="$name"/></xsl:attribute>
	  	<xsl:attribute name="title"><xsl:value-of select="$title"/></xsl:attribute>
	  	<xsl:attribute name="alt"><xsl:value-of select="$title"/></xsl:attribute>
	  </IMG>
	</xsl:otherwise>
  </xsl:choose>
 </xsl:template>

</xsl:stylesheet>
