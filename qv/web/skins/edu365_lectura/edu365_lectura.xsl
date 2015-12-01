<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="skin_name">edu365_lectura</xsl:variable>
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
 
 <xsl:variable name="idioma_lectura">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>ca</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="base_lectures">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='base']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='base']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>
		 	<xsl:choose>
 				<xsl:when test="$idioma_lectura='es'">http://www.edu365.com/eso/muds/castella/lectures/</xsl:when>
 				<xsl:otherwise>http://www.edu365.com/eso/muds/catala/lectures/</xsl:otherwise>
		 	</xsl:choose>
 		</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="base_lectura"><xsl:value-of select="$base_lectures"/><xsl:value-of select="normalize-space(//qtimetadata/qtimetadatafield[fieldlabel='nom_lectura']/fieldentry)"/></xsl:variable>

 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/>


<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
   <xsl:variable name="section_path">
     <xsl:value-of select="//assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=1
   </xsl:variable>

  <HTML>
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <xsl:call-template name="put_css"/>
    <xsl:call-template name="put_scripts"/>
   </HEAD>
   <BODY onload="window.focus();" >
   	<script>
   		document.location='<xsl:value-of select="normalize-space($section_path)"/>';
   	</script>
   </BODY>
  </HTML>
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
   <xsl:if test="$es_correccio='true' and contains($initialselection,$item_ident)">
  <TABLE border="0" cellpadding="0" cellspacing="0">
   <tbody><tr>
   <td width="80" valign="top" class="text">
    <script> if(isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
document.write('<span style="color: green;font-size:9">]]><xsl:call-template name="get_lang_name"><xsl:with-param name="id">CORRECTE</xsl:with-param></xsl:call-template><![CDATA[. </span>');
]]> }else { <![CDATA[
document.write('<span style="color: red;font-size:9">]]><xsl:call-template name="get_lang_name"><xsl:with-param name="id">INCORRECTE</xsl:with-param></xsl:call-template><![CDATA[. </span>');
]]> } </script>
</td>
<td valign="top" class="text" >
    <xsl:call-template name="getDisplayFeedback">
     <xsl:with-param name="ident_item">
      <xsl:value-of select="$item_ident"/>
     </xsl:with-param>
     <xsl:with-param name="feedback">
      <xsl:value-of select="$displayfeedback"/>
     </xsl:with-param>
    </xsl:call-template>
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
    <xsl:choose>
	 	<xsl:when test="$idioma_lectura='es'">
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_castella.css</xsl:attribute></LINK>
	 	</xsl:when>
	 	<xsl:otherwise>
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_catala.css</xsl:attribute></LINK>
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
	<!--  Contingut -->
	<div id="contingut">
	  <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
	  <div align="center">
	  	  <input type="button" name="corregeix">
	  	  	<xsl:attribute name="value"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">comprova</xsl:with-param></xsl:call-template></xsl:attribute>
	  	  	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">comprova</xsl:with-param></xsl:call-template></xsl:attribute>
	  	    <xsl:attribute name="onClick"><xsl:call-template name="deliver_section"/></xsl:attribute>
	      </input>
	  </div>
	  <br/><br/>
  	</div>
  	
  	  	  
	<!--  Capçalera  -->    <xsl:call-template name="header"/>
	
 </xsl:template>

 <xsl:template match="*[local-name()='assessment']">
   <xsl:apply-templates select="section|*[local-name()='item']"/>
 </xsl:template>  

 <xsl:template match="*[local-name()='section']">
 	<ul>
    <xsl:apply-templates select="*[local-name()='presentation_material']"/>
    <h3><xsl:value-of select="@title"/></h3>
    <xsl:apply-templates select="*[local-name()='section']|*[local-name()='item']"/>
    <xsl:if test="$noModify='false'">
      <SCRIPT> startTimerISO("<xsl:value-of
          select="ancestor::assessment/@duration"/>","<xsl:value-of select="$notebookTime"/>",
          "<xsl:value-of select="@duration"/>","<xsl:value-of select="$sectionTime"/>"); </SCRIPT>
    </xsl:if>
    </ul>
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

   <!-- Assessment -->
   <li>
     <strong>
       <xsl:number level="any"/>.
       <xsl:call-template name="getAssessment">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
     </strong>
 	 <br/>
      <!-- Additional material -->
      <ol>
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>

      <!-- Responses -->
       <xsl:call-template name="getResponses">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
       <br/>
      <!-- Feedback -->
       <xsl:call-template name="getItemFeedback">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
       <br/>
      </ol>
    </li>
   <br/>
 </xsl:template> 
   
  <xsl:template match="*[local-name()='flow_mat']">
    <xsl:apply-templates/>
    <BR/>
  </xsl:template>
  
<!-- #################### COMMONS ################# -->
<xsl:template name="header">
  <img width="615" height="25" border="0" usemap="#Map"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/cap_document.png</xsl:attribute></img>
  <map name="Map">
    <area shape="poly" coords="1,2,88,2,93,9,89,18,4,19" href="http://www.edu365.com/" title="edu365.com" alt="edu365.com"/>
  </map>

  <div id="proposta"> 
    <h4 align="right" class="clar"><a href="#"><xsl:attribute name="onClick">MM_openBrWindow('<xsl:value-of select="$base_lectures"/>','lectures','scrollbars=yes,width=400,height=375')</xsl:attribute><xsl:call-template name="get_lang_name"><xsl:with-param name="id">propostes</xsl:with-param></xsl:call-template></a>&#160; 
    </h4>
  </div>
  
  <div id="titol">
    <h1><xsl:value-of select="$header_title"/></h1>
    <h2><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='autor']/fieldentry"/></h2>
  </div>

  <xsl:variable name="tipus">
  <xsl:choose>
  	<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry!=''"><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry"/></xsl:when>
  	<xsl:otherwise>despres</xsl:otherwise>
  </xsl:choose>
  </xsl:variable>

  <div id="navegacio1">
    <h4>
     <a>
     	<xsl:attribute name="href"><xsl:value-of select="$base_lectura"/>/index.htm</xsl:attribute>
     	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">inici</xsl:with-param></xsl:call-template></xsl:attribute>
     	<xsl:call-template name="get_lang_name"><xsl:with-param name="id">inici</xsl:with-param></xsl:call-template>
     </a>
     &#160;|&#160;
     <a>
     	<xsl:attribute name="href"><xsl:value-of select="$base_lectura"/>/<xsl:call-template name="get_lang_name"><xsl:with-param name="id">escrit.htm</xsl:with-param></xsl:call-template></xsl:attribute>
     	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">escrit</xsl:with-param></xsl:call-template></xsl:attribute>
     	<xsl:call-template name="get_lang_name"><xsl:with-param name="id">escrit</xsl:with-param></xsl:call-template>
     </a>
     <xsl:if test="$tipus!='abans'">
	     &#160;|&#160;
	     <a>
	     	<xsl:attribute name="href"><xsl:value-of select="$base_lectura"/>/<xsl:call-template name="get_lang_name"><xsl:with-param name="id">abans.htm</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">abans</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:call-template name="get_lang_name"><xsl:with-param name="id">abans</xsl:with-param></xsl:call-template>
	     </a>
	 </xsl:if>
     <xsl:if test="$tipus!='mentre'">
	     &#160;|&#160;
	     <a>
	     	<xsl:attribute name="href"><xsl:value-of select="$base_lectura"/>/<xsl:call-template name="get_lang_name"><xsl:with-param name="id">mentre.htm</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">mentre</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:call-template name="get_lang_name"><xsl:with-param name="id">mentre</xsl:with-param></xsl:call-template>
	     </a>
	 </xsl:if>
     <xsl:if test="$tipus!='despres'">
	     &#160;|&#160;
	     <a>
	     	<xsl:attribute name="href"><xsl:value-of select="$base_lectura"/>/<xsl:call-template name="get_lang_name"><xsl:with-param name="id">despres.htm</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">despres</xsl:with-param></xsl:call-template></xsl:attribute>
	     	<xsl:call-template name="get_lang_name"><xsl:with-param name="id">despres</xsl:with-param></xsl:call-template>
	     </a>
     </xsl:if>
    </h4>
    <h2><xsl:call-template name="get_lang_name"><xsl:with-param name="id"><xsl:value-of select="$tipus"/></xsl:with-param></xsl:call-template></h2>
  </div>
</xsl:template>

<xsl:template name="get_lang_name">
	<xsl:param name="id"/>
  <xsl:choose>
  	<xsl:when test="$id='propostes'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">Propuestas de lectura</xsl:when>
  			<xsl:otherwise>Propostes de lectura</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='inici'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">Inicio</xsl:when>
  			<xsl:otherwise>Inici</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='escrit'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">¿Quién lo ha escrito?</xsl:when>
  			<xsl:otherwise>Qui l'ha escrit?</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='escrit.htm'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">escrito.htm</xsl:when>
  			<xsl:otherwise>escrit.htm</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='abans'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">Antes de leer</xsl:when>
  			<xsl:otherwise>Abans de llegir</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='abans.htm'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">antes.htm</xsl:when>
  			<xsl:otherwise>abans.htm</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='mentre'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">Mientras lees</xsl:when>
  			<xsl:otherwise>Mentre llegeixes</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='mentre.htm'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">mientras.htm</xsl:when>
  			<xsl:otherwise>mentre.htm</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='comprova'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">comprueba</xsl:when>
  			<xsl:otherwise>comprova</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='CORRECTE'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">CORRECTO</xsl:when>
  			<xsl:otherwise>CORRECTE</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='INCORRECTE'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">INCORRECTO</xsl:when>
  			<xsl:otherwise>INCORRECTE</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='despres.htm'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">despues.htm</xsl:when>
  			<xsl:otherwise>despres.htm</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:otherwise>
  		<xsl:choose>
  			<xsl:when test="$idioma_lectura='es'">Después de leer</xsl:when>
  			<xsl:otherwise>Després de llegir</xsl:otherwise>
  		</xsl:choose>
  	</xsl:otherwise>
  </xsl:choose>	
</xsl:template>

</xsl:stylesheet>
