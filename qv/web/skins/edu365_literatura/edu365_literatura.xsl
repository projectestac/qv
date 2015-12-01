<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="idioma_lite">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='idioma']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>ca</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="skin_name">edu365_literatura</xsl:variable>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/<xsl:value-of select="$skin_name"/>/image/<xsl:value-of select="$idioma_lite"/>/</xsl:variable>
 <xsl:variable name="specific_css_path">skins/<xsl:value-of select="$skin_name"/>/css</xsl:variable>
 <xsl:variable name="specific_scripts_path">skins/<xsl:value-of select="$skin_name"/>/scripts</xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>
 
 <xsl:variable name="base_lite">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='base']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='base']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>
		 	<xsl:choose>
 				<xsl:when test="$idioma_lite='es'">http://www.edu365.com/eso/muds/castella/literatura/</xsl:when>
 				<xsl:otherwise>http://www.edu365.com/eso/muds/catala/literatura/</xsl:otherwise>
		 	</xsl:choose>
 		</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="nom_lite">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='nom_lectura']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='nom_lectura']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise></xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

 <xsl:variable name="tipus_lite">
 	<xsl:choose>
 		<xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry!=''">
 			<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='tipus']/fieldentry"/>
 		</xsl:when>
 		<xsl:otherwise>prosa</xsl:otherwise>
 	</xsl:choose>
 </xsl:variable>

	
 <xsl:variable name="base_lectura"><xsl:value-of select="$base_lite"/><xsl:value-of select="normalize-space(//qtimetadata/qtimetadatafield[fieldlabel='nom_lectura']/fieldentry)"/></xsl:variable>

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

 <xsl:template name="html_body_attrs">
	 <xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
	 <xsl:attribute name="leftmargin">0</xsl:attribute>
	 <xsl:attribute name="topmargin">0</xsl:attribute>
	 <xsl:attribute name="marginwidth">0</xsl:attribute>
	 <xsl:attribute name="marginheight">0</xsl:attribute>
 </xsl:template>	
	
 <xsl:template name="put_css">
    <xsl:choose>
	 	<xsl:when test="$idioma_lite='es'">
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_es.css</xsl:attribute></LINK>
	 	</xsl:when>
	 	<xsl:otherwise>
		    <LINK rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="$specific_css_path"/>/<xsl:value-of select="$skin_name"/>_ca.css</xsl:attribute></LINK>
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
	 <xsl:variable name="diana"><xsl:value-of select="$base_lite"/><xsl:value-of select="$tipus_lite"/>/<xsl:value-of select="$nom_lite"/></xsl:variable>
	<table  width="610" height="300" border="0" cellpadding="0" cellspacing="0">
	  <tr>
		<td valign="top">
			<img width="610" height="110" usemap="#Map" border="0">
				<xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/titol.gif</xsl:attribute>
			</img>
			<table width="610" border="0" cellpadding="0" cellspacing="0">
			<tr> 
			  <td width="30"></td>
			  <td valign="top">
				<table width="550" border="0" cellspacing="0" cellpadding="0">
				  <tr> 
					<td width="270" height="51" align="left" valign="center">
						<xsl:attribute name="background"><xsl:value-of select="$specific_images_path"/>/titoletg.gif</xsl:attribute>						
					  <div style="margin-left:20; margin-right:15; margin-top:5" class="titol"> 
						<xsl:value-of select="$header_title"/></div>
					</td>
					<td>&#160;</td>
					<td width="39" align="right" valign="middle">
						<img width="39" height="39" alt="Activitats" title="Activitats">
							<xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/exercici.gif</xsl:attribute>
						</img>						
					</td>
				  </tr>
				</table>
				
			  </td>
			  <td width="30">&#160;</td>
			</tr>
			<tr> 
			  <td >&#160;</td>
			  <td valign="top" class="lectura" width="550">
				<xsl:attribute name="background"><xsl:value-of select="$specific_images_path"/>/colorfonsdins.gif</xsl:attribute>						
				  <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>	  				  
				  <div style="background-color:#dac38d;width:500px;margin:0px 25px 0px 35px;">
				  <center>
					<input type="button" name="corregeix">
						<xsl:attribute name="value"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">corregeix</xsl:with-param></xsl:call-template></xsl:attribute>
						<xsl:attribute name="title"><xsl:call-template name="get_lang_name"><xsl:with-param name="id">corregeix</xsl:with-param></xsl:call-template></xsl:attribute>
						<xsl:attribute name="onClick"><xsl:call-template name="deliver_section"/></xsl:attribute>
					</input>
				  </center>
				  </div>
				  <br/>
			  </td>
			  <td width="30">&#160;</td>
			</tr>
			<tr width="20">
			  <td>&#160;</td>
			  <td ><table width="550" border="0" cellspacing="0" cellpadding="0">
				  <tr>
					<td width="37" align="left">
						<a title="Portada">
							<xsl:attribute name="href">javascript:crea_finestra2('<xsl:value-of select="$diana"/>',width=412,height=450)</xsl:attribute>
							<img alt="Portada" title="Portada" width="39" height="39" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/diana.gif</xsl:attribute></img></a></td>
					<td>&#160;</td>
					<td width="37" align="right" colspan="2">
						<a title="Endarrera">
							<xsl:attribute name="href"><xsl:value-of select="$diana"/>/<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='endarrera']/fieldentry"/></xsl:attribute>
							<img width="39" height="39" border="0" alt="Endarrera" title="Endarrera"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back.gif</xsl:attribute></img>
						</a>
					</td>
					
				  </tr>
				</table></td>
			  <td>&#160;</td>
			</tr>				
			</table>
		</td>
	 </tr>
	</table>  	
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
<span class="poema">
   <!-- Assessment -->
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
</span>	
	  <br/>
	  <HR color="#990000" noShade="no" style="margin:0px 20px 0px 0px;"/>			  
	  <br/>
 </xsl:template> 
   
  <xsl:template match="*[local-name()='flow_mat']">
    <xsl:apply-templates/>
    <BR/>
  </xsl:template>
  
<!-- #################### COMMONS ################# -->

<xsl:template name="get_lang_name">
	<xsl:param name="id"/>
  <xsl:choose>
  	<xsl:when test="$id='corregeix'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lite='es'">corrige</xsl:when>
  			<xsl:otherwise>corregeix</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='CORRECTE'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lite='es'">CORRECTO</xsl:when>
  			<xsl:otherwise>CORRECTE</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:when test="$id='INCORRECTE'">
  		<xsl:choose>
  			<xsl:when test="$idioma_lite='es'">INCORRECTO</xsl:when>
  			<xsl:otherwise>INCORRECTE</xsl:otherwise>
  		</xsl:choose>
  	</xsl:when>
  	<xsl:otherwise>
		<xsl:value-of select="$id"/>
  	</xsl:otherwise>
  </xsl:choose>	
</xsl:template>

</xsl:stylesheet>
