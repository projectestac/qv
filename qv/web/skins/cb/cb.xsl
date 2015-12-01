<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="skin_name">cb</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$skin_name"/>/image</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$skin_name"/>/css</xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>

 <!-- xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/-->
 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

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
	<div id="header">
		<div id="header-header"></div>
		<div id="header-title">CURS 2005-2006</div>
		<div id="header-content" ></div>
		<div id="header-footer" ></div>
	</div>
	
	<div id="content">
		<div id="content-header" >
			<span class="header-title1"><xsl:value-of select="$header_title"/></span><br/>
			<span class="header-title2"><xsl:call-template name="header-subtitle"/></span>
		</div>
		<div id="content-title" ></div>
		<div id="content-content" >
			<br/><br/>
			<xsl:apply-templates select="assignment/sections"/><br/>
		</div>
	</div>
    <xsl:call-template name="quadern_assignments_specific"/>
   </BODY>
  </HTML>
 </xsl:template>
 
 <xsl:template name="header-subtitle">
 </xsl:template>

 <xsl:template name="quadern_assignments_specific">
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
  
  <ul>
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
	   	 <xsl:call-template name="get_section_path"/>
	     <!--xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/-->
	   </xsl:variable>
	   <li>
         <a title="visualitza el full">
          <xsl:attribute name="href"><xsl:value-of select="normalize-space($section_path)"/></xsl:attribute>
          <xsl:value-of select="$section_name"/> 
         </a>
	   </li>
	  </xsl:for-each>
  </ul>
 </xsl:template>
 
 <xsl:template name="get_section_path">
	<xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/> 
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
 </xsl:template>
  
 <xsl:template name="html_body_attrs">
 </xsl:template>

 <xsl:template name="questestinterop_body">
	<div id="nav-buttons">
    	<xsl:call-template name="put_navigation_buttons"/>
	</div>
	

	<div id="header">
		<div id="header-header" ></div>
		<div id="header-title" >CURS 2005-2006</div>
	</div>
	<div id="content">
		<div id="content-header" >
			<span class="header-title1"><xsl:value-of select="$header_title"/></span><br/>
			<span class="header-title2"><xsl:call-template name="header-subtitle"/></span>
		</div>
		<div id="content-title">
			<div class="title"><xsl:value-of select="//section[position()=$section_number]/@title"/></div>
		</div>
		<div id="content-content" style="background: #FFFFFF; none; margin-left:-200px;width:760px;">
			<div id="item-content">
				<xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
			</div>
			<div id="nav">
		    	<xsl:call-template name="put_action_buttons"/><br/><br/>
			</div>
		</div>
	</div> 
	<xsl:call-template name="questestinterop_specific_body"/>
 </xsl:template>
 
 <xsl:template name="questestinterop_specific_body">
 </xsl:template> 
 
 <xsl:template match="*[local-name()='presentation_material']">
 	<div class="presentation-material">
 		<xsl:call-template name="getSectionAdditionalMaterial"/>
 	</div>
 </xsl:template>

<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>

  <div id="item-assessment">
   <div id="item-number" class="title"><xsl:value-of select="position()"/>.</div>
   <div class="item-assessment" >
   <xsl:call-template name="getAssessment">
    <xsl:with-param name="ident_item">
     <xsl:value-of select="$it_ident"/>
    </xsl:with-param>
   </xsl:call-template>
   </div>
  </div>
  
  <div class="item-additional-material">
    <xsl:call-template name="getItemAdditionalMaterial">
     <xsl:with-param name="ident_item">
      <xsl:value-of select="$it_ident"/>
     </xsl:with-param>
    </xsl:call-template>       
  </div>

  <div class="item-response" style="margin-left:30px">
   <xsl:call-template name="getResponses">
    <xsl:with-param name="ident_item">
     <xsl:value-of select="$it_ident"/>
    </xsl:with-param>
   </xsl:call-template>
  </div>

  <div class="item-feedback" >
   <xsl:call-template name="getItemFeedback">
    <xsl:with-param name="item_ident">
     <xsl:value-of select="$it_ident"/>
    </xsl:with-param>
   </xsl:call-template>       
  </div>

  <div class="item-interaction">
    <xsl:call-template name="interactions">
     <xsl:with-param name="item_ident">
      <xsl:value-of select="$it_ident"/>
     </xsl:with-param>
     <xsl:with-param name="index">1</xsl:with-param>
    </xsl:call-template>       
  </div>

 </xsl:template> 
 
 <xsl:template name="interactions">
 </xsl:template>
 
 <xsl:template name="put_add_interaction_button">
 </xsl:template>

 <xsl:template name="put_send_interaction_button">
 </xsl:template>

  
<!-- #################### COMMONS ################# -->

 <xsl:template name="put_navigation_buttons">
      <a id="back-button" title="Pàgina anterior" onmouseover="window.status='';return true;">
       <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
       <span class="alt">Pàgina anterior</span>
       <!--img width="20" height="15" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back-off.gif</xsl:attribute></img-->
     </a>
      <a id="home-button" title="Pàgina principal" onmouseover="window.status='';return true;">
       <xsl:attribute name="href">javascript:<xsl:call-template name="go_home"/></xsl:attribute>
       <span class="alt">Pàgina principal</span>
       <!--img width="20" height="15" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/home-off.gif</xsl:attribute></img-->
     </a>
      <a id="next-button" title="Pàgina següent" onmouseover="window.status='';return true;">
       <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
       <span class="alt">Pàgina següent</span>
       <!--img width="20" height="15" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next-off.gif</xsl:attribute></img-->
     </a>
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
  <br/>
	<xsl:if test="normalize-space($show_correct_button)='true'">
	    <span class="button">
		    <a title="Corregeix aquest full" onmouseover="window.status='';return true;">
		     <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
		     Corregeix
		    </a>
	    </span>
	    
	</xsl:if>
	&#160;
    <xsl:if test="normalize-space($show_save_button)='true'">
        <a title="Guarda el quadern" onmouseover="window.status='';return true;">
         <xsl:attribute name="href"><xsl:call-template name="save_section"/></xsl:attribute>Desa</a>
    </xsl:if>
    &#160;
    <xsl:if test="normalize-space($show_deliver_button)='true'">
        <a title="Lliura aquest full per que el corregeixi el professor" onmouseover="window.status='';return true;">
         <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
         Lliura
        </a>
    </xsl:if>
</xsl:template>

</xsl:stylesheet>
