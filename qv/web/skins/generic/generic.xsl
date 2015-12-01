<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="lang">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>ca</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>
 <xsl:variable name="css">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='css']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='css']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise></xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>
 <xsl:variable name="skin_name">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='skin']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='skin']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>
			 <xsl:choose>
				 <xsl:when test="$default_skin!=''"><xsl:value-of select="$default_skin"/></xsl:when>
				 <xsl:otherwise>default</xsl:otherwise>
			 </xsl:choose>
		 </xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="generic_skin">generic</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$generic_skin"/>/css/<xsl:value-of select="$skin_name"/>/image/</xsl:variable>
 <xsl:variable name="specific_images_lang_path"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="$lang"/>/</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$generic_skin"/>/css/<xsl:value-of select="$skin_name"/></xsl:variable>
 <xsl:variable name="specific_scripts_path">skins/<xsl:value-of select="$generic_skin"/>/scripts/<xsl:value-of select="$skin_name"/></xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>
 

 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>

<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
 <HTML>
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <xsl:call-template name="put_css"/>
    <xsl:call-template name="put_scripts"/>
   </HEAD>
   <BODY onload="window.focus();" >
	   <xsl:call-template name="put_header"/>
	   <xsl:apply-templates select="assignment/sections"/>
   </BODY>
  </HTML>
 </xsl:template> 
	
 <xsl:template match="sections">
  <xsl:variable name="is_preview"><xsl:call-template name="isPreviewMode"/></xsl:variable>
  <xsl:variable name="puntuation">
   <xsl:choose>
    <xsl:when test="//assignment/puntuation!=''"><xsl:value-of select="//assignment/puntuation"/></xsl:when>
    <xsl:otherwise>-</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="assignment_date">
    <xsl:value-of select="//assignment/assignment_date"/>
  </xsl:variable>	 
	 
	<div id="index">
		<div id="index-information">
			<div id="index-information-title">
				<table cellpadding="5" cellspacing="0">
				<tr>
					<td><b><xsl:call-template name="get_lang_name"><xsl:with-param name="id">puntuation</xsl:with-param></xsl:call-template>:</b></td>
					<td><xsl:value-of select="$puntuation"/></td>
				</tr>
				<tr>
					<td><b><xsl:call-template name="get_lang_name"><xsl:with-param name="id">assignment_date</xsl:with-param></xsl:call-template>:</b></td>
					<td><xsl:value-of select="$assignment_date"/></td>
				</tr>
				</table>
			</div>
			<div id="index-information-table">
				<table cellpadding="0" cellspacing="0">
				<thead>
					<th class="section-title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">sections</xsl:with-param></xsl:call-template></th>
				  <xsl:if test="$is_preview='false'">
					<th><xsl:call-template name="get_lang_name"><xsl:with-param name="id">state</xsl:with-param></xsl:call-template></th>
					<th><xsl:call-template name="get_lang_name"><xsl:with-param name="id">deliver</xsl:with-param></xsl:call-template></th>
					<th><xsl:call-template name="get_lang_name"><xsl:with-param name="id">puntuation</xsl:with-param></xsl:call-template></th>
				  </xsl:if>
				</thead>
				<tbody>
				 <xsl:for-each select="section">
				   <xsl:variable name="section_name">
					<xsl:choose>
					 <xsl:when test="@name=''"> <xsl:call-template name="get_lang_name"><xsl:with-param name="id">section</xsl:with-param></xsl:call-template> <xsl:value-of select="@num"/>
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
				<tr>
					<xsl:attribute name="class">
				       <xsl:choose>
				        <xsl:when test="@num mod 2=0">even-row</xsl:when>
						<xsl:otherwise>odd-row</xsl:otherwise>
					   </xsl:choose>
					</xsl:attribute>
					
					<td class="section-title">
						<xsl:value-of select="@num"/>. 
				        <a>
				          <xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">load_section_title</xsl:with-param></xsl:call-template></xsl:attribute>
				          <xsl:attribute name="href"><xsl:value-of select="normalize-space($section_path)"/></xsl:attribute>
				          <xsl:value-of select="$section_name"/> 
				        </a>
					</td>
				  <xsl:if test="$is_preview='false'">
					<td>x</td>
					<td><xsl:value-of select="$section_limit"/></td>
					<td><xsl:value-of select="$section_puntuation"/></td>
				  </xsl:if>
				</tr>
				</xsl:for-each>	 
				</tbody>
				<tfoot>
				<tr>
					<td colspan="4"></td>
				</tr>
				</tfoot>					
				</table>
			</div>
		</div>
		<div id="index-footer">Llegenda</div>
	</div>	   	   
 </xsl:template>	
	
 <xsl:template name="get_section_path">
	 <xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/>
 </xsl:template>	
	
 <xsl:template name="put_header">
  <xsl:variable name="section_name">
   <xsl:value-of select="//section[position()=$section_number]/@title"/>
  </xsl:variable>
	 
  <div id="header">
	<div id="header-logo"></div>
	 <div id="header-title">
		<h1> <xsl:value-of select="$header_title"/> </h1>
        <xsl:choose>
        	<xsl:when test="$section_name!=''">
        		<h3><xsl:value-of select="$section_name"/></h3>
        	</xsl:when>
        	<xsl:otherwise>
		        <h3><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='materia']/fieldentry"/></h3>
		        <h3><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='level']/fieldentry"/></h3>
        	</xsl:otherwise>
        </xsl:choose>
	 </div>
 	 <div id="header-puntuation">
	   <h1><xsl:value-of select="$section_number"/></h1>/<xsl:value-of select="$section_max_number"/><br/><br/>
	   <!-- PUNTUATION -->
	   <xsl:call-template name="get_lang_name"><xsl:with-param name="id">puntuation</xsl:with-param></xsl:call-template>: 
  	   <span id="score">
	   <xsl:choose>
		<xsl:when test="$es_correccio='true'">
		 <script>writeItemScore('<xsl:value-of select="assessment/section[position()=$section_number]/@ident"/>');</script>
		</xsl:when>
		<xsl:otherwise> - </xsl:otherwise>
	   </xsl:choose>  
	  </span>
	 </div>
  </div>
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
		<xsl:attribute name="id">feedback_<xsl:value-of select="$item_ident"/>_correct</xsl:attribute>	  
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
	   <script><![CDATA[document.write("</div>");]]></script>
   </xsl:if>
		</div>
  </xsl:if>
 </xsl:template>

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head_title">
    QV - <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="html_body_attrs">
 </xsl:template>	
	
 <xsl:template name="put_css">
    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>.css</xsl:attribute></LINK>
    <xsl:choose>
	 	<xsl:when test="$lang='es'">
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_es.css</xsl:attribute></LINK>
	 	</xsl:when>
	 	<xsl:otherwise>
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_ca.css</xsl:attribute></LINK>
	 	</xsl:otherwise>
	</xsl:choose>
	<xsl:if test="$css!=''"><LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$css"/></xsl:attribute></LINK></xsl:if>

 </xsl:template>
 
 <xsl:template name="put_specific_scripts">
    <SCRIPT type="text/javascript">
	    <xsl:attribute name="src"><xsl:value-of select="$specific_scripts_path"/>/<xsl:value-of select="$skin_name"/>.js</xsl:attribute>
    </SCRIPT>
	<script language="JavaScript" src="http://clic.xtec.net/qv/dist/launchQV.js" type="text/javascript"></script>
 </xsl:template>
 
  
 <xsl:template name="questestinterop_body">	
	<a name="top"/>	 
	<xsl:call-template name="put_header"/>
	 
	<div id="content">
		<xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
	</div>	 

	<div id="content-footer"></div>
	 
	<div id="bottom-navigation-bar">
		<div id="navigation-buttons">
			<a id="back-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">back</xsl:with-param></xsl:call-template></xsl:attribute>
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
				<img width="50" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>pixel.gif</xsl:attribute><xsl:attribute name="alt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">back</xsl:with-param></xsl:call-template></xsl:attribute></img>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">back</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
			&#160;
			<a id="home-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></xsl:attribute>
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_home"/></xsl:attribute>
				<img width="50" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>pixel.gif</xsl:attribute><xsl:attribute name="alt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></xsl:attribute></img>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
			&#160;
			<a id="next-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">next</xsl:with-param></xsl:call-template></xsl:attribute>
			    <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
				<img width="50" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>pixel.gif</xsl:attribute><xsl:attribute name="alt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">next</xsl:with-param></xsl:call-template></xsl:attribute></img>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">home</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
			&#160;
		</div>
		<div id="action-buttons">
			<a id="correct-bt" class="button">
				<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">correct</xsl:with-param></xsl:call-template></xsl:attribute>
			    <xsl:attribute name="href"><xsl:call-template name="correct_section"/></xsl:attribute>
				<img width="110" height="55" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>pixel.gif</xsl:attribute><xsl:attribute name="alt"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">correct</xsl:with-param></xsl:call-template></xsl:attribute></img>
				<xsl:if test="false"><span><xsl:call-template name="get_lang_name"><xsl:with-param name="id">correct</xsl:with-param></xsl:call-template></span></xsl:if>
			</a>
		</div>
	</div>
	 
   <xsl:call-template name="footer"/>
	 
 </xsl:template>

 <xsl:template name="footer">
 </xsl:template>
	
 
 <xsl:template match="*[local-name()='presentation_material']">
     <div id="section-additional-material"><xsl:call-template name="getSectionAdditionalMaterial"/></div>
 </xsl:template>

<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A><xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/></xsl:attribute></A>
  <div id="item">
    <!-- Assessment -->
	<div id="assessment">
	   <xsl:value-of select="position()"/>.
       <xsl:call-template name="getAssessment">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
	</div>
    <!-- Additional material -->
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>

	<!-- Responses -->
	<div id="responses">
       <xsl:call-template name="getResponses">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
	</div>
	<!-- Feedback -->
	<div class="item-feedback">
		<xsl:call-template name="getItemFeedback">
			<xsl:with-param name="item_ident">
			 <xsl:value-of select="$it_ident"/>
			</xsl:with-param>
		</xsl:call-template>
	</div>
  </div>	
 </xsl:template> 
   
  <xsl:template match="*[local-name()='flow_mat']">
    <xsl:apply-templates/>
    <BR/>
  </xsl:template>
  
<!-- #################### COMMONS ################# -->

<xsl:template name="get_lang_name">
  <xsl:param name="id"/>
  <xsl:choose>
  	<xsl:when test="$id='assignment_date'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Fecha de asignación</xsl:when>
  			<xsl:otherwise>Data assignació</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='back'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página anterior</xsl:when>
  			<xsl:otherwise>Pàgina anterior</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='correct'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Corregir esta hoja</xsl:when>
  			<xsl:otherwise>Corregeix aquest full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='deliver'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Entregas</xsl:when>
  			<xsl:otherwise>Lliuraments</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='home'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página principal</xsl:when>
  			<xsl:otherwise>Pàgina principal</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>	  
  	<xsl:when test="$id='load_section_title'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Ver hoja</xsl:when>
  			<xsl:otherwise>Veure full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='next'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Página siguiente</xsl:when>
  			<xsl:otherwise>Pàgina següent</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='puntuation'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Puntuación</xsl:when>
  			<xsl:otherwise>Puntuació</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='section'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Hoja</xsl:when>
  			<xsl:otherwise>Full</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='sections'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Hojas</xsl:when>
  			<xsl:otherwise>Fulls</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='state'">
  		<xsl:choose>
  			<xsl:when test="$lang='es'">Estado</xsl:when>
  			<xsl:otherwise>Estat</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:otherwise>
		<xsl:value-of select="$id"/>
  	</xsl:otherwise>
  </xsl:choose>	
</xsl:template>

</xsl:stylesheet>
