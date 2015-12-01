<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>
 <xsl:output method="html" encoding="ISO-8859-1" indent="no" media-type="text/html"/>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/edu365/image</xsl:variable>
 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>
 <xsl:variable name="item_image">
  <xsl:choose>
   <xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='item_image']!=''"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='item_image']/fieldentry"/></xsl:when>
   <xsl:otherwise><xsl:value-of select="$specific_images_path"/>/practica.gif</xsl:otherwise>
  </xsl:choose>
 </xsl:variable>
 <xsl:variable name="items_type_title">Pràctica</xsl:variable>

<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
  <HTML>
   <HEAD>
    <title><xsl:call-template name="html_head_title"/></title>
    <LINK TYPE="text/css" rel="stylesheet" href="skins/edu365/css/edu365.css"/>
   </HEAD>
   <BODY onload="window.focus();" >
    <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
     <TR>
      <TD style="width:7"><IMG width="7" height="6" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/caixa_mud_tl.gif</xsl:attribute></IMG></TD>
      <TD colspan="2" class="box"></TD>
      <TD></TD>
     </TR>
     <TR>
      <TD class="box"></TD>
      <TD class="box">
        <TABLE border="0" cellspacing="0" cellpadding="0">
        <TR>
         <TD valign="top"><A href="http://www.edu365.com" title="edu365.com" target="_blank"><IMG width="55" height="25" border="0" align="top"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logo_edu365.gif</xsl:attribute></IMG></A>
         <br/><A href="http://clic.xtec.net/qv" title="Quaderns Virtuals" target="_blank"><IMG width="55" height="25" border="0" align="top"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logo_qv.gif</xsl:attribute></IMG></A></TD>
         <TD class="assessment-title">
           <!-- Títol -->
           <xsl:value-of select="$header_title"/>
         </TD>
        </TR>
        </TABLE>
      </TD>
      <TD style="width:5;" class="box"></TD>
     </TR>
     <TR>
      <TD style="height:2px;" class="box" ></TD>
      <TD class="box-shadow" ></TD>
      <TD class="box" ></TD>
     </TR>
     <TR>
      <TD class="box"></TD>
      <TD height="300" style="padding:5" valign="top">
       <!-- Content -->
       <xsl:apply-templates select="assignment/sections"/>
      </TD>
      <TD class="box" ></TD>
     </TR>   
     <TR>
      <TD class="box" style="height:7;"></TD>
      <TD class="box-shadow" ></TD>
      <TD class="box" ></TD>
     </TR>   
    </TABLE>
  
   </BODY>
  </HTML>
 </xsl:template>
  
 <xsl:template name="assignment_buttons">
   <xsl:variable name="is_preview">
      <xsl:call-template name="isPreviewMode"/>
   </xsl:variable>
   <xsl:if test="$is_preview='false'">
    <TABLE width="630" border="0" cellpadding="2" cellspacing="1" bordercolor="#ffffff" bgcolor="#ffffff">
      <tbody><tr>
        <td width="59">&#160;</td>
        <td>
         <xsl:variable name="show_correct_all_button"><xsl:call-template name="show_correct_all_button"/></xsl:variable>
         <xsl:variable name="show_deliver_all_button"><xsl:call-template name="show_deliver_all_button"/></xsl:variable>
         <xsl:choose>
          <xsl:when test="$show_correct_all_button='true'">
           <a href="?wantCorrect=true" title="Corregeix tot el quadern">Corregeix tot</a>
          </xsl:when>
          <xsl:when test="$show_deliver_all_button='true'">
           <a href="?wantCorrect=true" title="Lliura tot el quadern">Lliura-ho tot</a>
          </xsl:when>
         </xsl:choose>
        </td>
       </tr></tbody>
      </TABLE>
     </xsl:if>
 </xsl:template>
 
 <xsl:template match="sections">
  <ul>
   <xsl:for-each select="section">
    <xsl:variable name="section_name">
     <xsl:choose>
      <xsl:when test="@name=''"> Enigma <xsl:value-of select="@num"/>
      </xsl:when>
      <xsl:otherwise>
       <xsl:value-of select="@name"/>
      </xsl:otherwise>
     </xsl:choose>
    </xsl:variable>
    <xsl:variable name="section_path">
      <xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;quadernXSL=<xsl:value-of select="ancestor::assignment/@quadernXSL"/>&#38;full=<xsl:value-of select="@num"/>
    </xsl:variable>
     <li>
      <a title="vés a l'enigma" class="section">
       <xsl:attribute name="href">
        <xsl:value-of select="normalize-space($section_path)"/>
       </xsl:attribute>
       <xsl:value-of select="$section_name"/> 
      </a>
     </li>
   </xsl:for-each>
  </ul>         
 </xsl:template>
 
<!-- #################### SECTION ################# -->
 
<xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>

  <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
   <TR>
    <TD style="width:7"><IMG width="7" height="6" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/caixa_mud_tl.gif</xsl:attribute></IMG></TD>
    <TD colspan="2" class="box"></TD>
    <TD></TD>
   </TR>
   <TR>
    <TD class="box"></TD>
    <TD class="box">
      <TABLE border="0" cellspacing="0" cellpadding="0">
      <TR>
       <TD valign="top"><A href="http://www.edu365.com" title="edu365.com" target="_blank"><IMG width="55" height="25" border="0" align="top"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logo_edu365.gif</xsl:attribute></IMG></A>
       <br/><A href="http://clic.xtec.net/qv" title="Quaderns Virtuals" target="_blank"><IMG width="55" height="25" border="0" align="top"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logo_qv.gif</xsl:attribute></IMG></A></TD>
       <TD>
        <!-- Assessment -->
        <div class="item-assessment">
         <xsl:call-template name="getAssessment">
          <xsl:with-param name="ident_item">
           <xsl:value-of select="$it_ident"/>
          </xsl:with-param>
         </xsl:call-template>
        </div>
       </TD>
      </TR>
      </TABLE>
    </TD>
    <TD style="width:5;" class="box"></TD>
    <TD rowspan="4" valign="bottom">
     <!-- Buttons -->
     <TABLE border="0" cellpadding="0" cellspacing="0" class="box">
     <TR>
       <TD align="right"><IMG width="44" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_top.gif</xsl:attribute></IMG></TD>
     </TR>
     <TR>
       <TD valign="bottom">
        <a title="Corregeix la pregunta" onmouseover="window.status='';return true;" class="boton">
         <xsl:attribute name="href"><xsl:call-template name="correct_section"/></xsl:attribute>
         <xsl:call-template name="getItemFeedback">
          <xsl:with-param name="item_ident">
           <xsl:value-of select="$it_ident"/>
          </xsl:with-param>
         </xsl:call-template>
        </a>  
       </TD>
     </TR>
     <TR>
       <TD valign="bottom" align="left">
        &#160;<xsl:call-template name="put_navigation_buttons"/>
       </TD>
     </TR>
     <TR>
       <TD class="box" align="right"><IMG width="44" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/icon_bottom.gif</xsl:attribute></IMG></TD>
     </TR>
      </TABLE>   
    </TD>
   </TR>
   <TR>
    <TD style="height:2px;" class="box" ></TD>
    <TD class="box-shadow" ></TD>
    <TD class="box" ></TD>
    <TD></TD>
   </TR>
   <TR>
    <TD class="box"></TD>
    <TD height="100%" style="padding:5">
     <!-- Additional material -->
     <div class="item-additional-material">
       <xsl:call-template name="getItemAdditionalMaterial">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>       
     </div>
     <!-- Responses -->
     <div class="item-response">
      <xsl:call-template name="getResponses">
       <xsl:with-param name="ident_item">
        <xsl:value-of select="$it_ident"/>
       </xsl:with-param>
      </xsl:call-template>
     </div>
     
     <!-- Feedback -->
     <!-- span class="item-feedback" >
      <xsl:if test="$displayfeedback!=''">
       Les respostes correctes són:<b>
       <xsl:call-template name="getDisplayFeedback">
        <xsl:with-param name="ident_item">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
        <xsl:with-param name="feedback">
         <xsl:value-of select="$displayfeedback"/>
        </xsl:with-param>
       </xsl:call-template>
      </b>
      </xsl:if>
     </span-->
    </TD>
    <TD class="box" ></TD>
   </TR>   
   <TR>
    <TD class="box" style="height:7;"></TD>
    <TD class="box-shadow" ></TD>
    <TD class="box" ></TD>
   </TR>   
  </TABLE>
  
 </xsl:template> 

<!-- #################### REDEFINITIONS ################# -->

 <xsl:template name="html_head_title">
    <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="put_css">
    <LINK TYPE="text/css" rel="stylesheet" href="skins/edu365/css/edu365.css"/>
 </xsl:template>
 

 <xsl:template name="questestinterop_body">
     <xsl:variable name="section_name">
      <xsl:value-of select="//section[position()=$section_number]/@title"/>
     </xsl:variable>

     <div style="text-align:right;margin-right:44" class="section-title">
      <xsl:value-of select="$section_name"/>
     </div>
     <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>  
 </xsl:template>
  
 <xsl:template match="*[local-name()='presentation_material']">
 </xsl:template>
 
<xsl:template name="getItemFeedback">
  <!-- Si existeix algun feedback a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <!--xsl:variable name="showFeedback"><xsl:value-of select="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showFeedback']/@fieldentry" /></xsl:variable-->
  <xsl:variable name="showFeedback">
   <xsl:value-of select="itemcontrol/@feedbackswitch"/>
  </xsl:variable>
  <xsl:choose>
   <xsl:when test="normalize-space($showFeedback)!='No' and $es_correccio='true'">
    <script> if(isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
				document.write('<img src="skins/edu365/image/avaluat_ok.gif" alt="resposta correcta" border="0"/>');
]]> }else { <![CDATA[
				document.write('<img src="skins/edu365/image/avaluat_ko.gif" alt="Resposta incorrecta" border="0"/>');
]]> } </script>
   </xsl:when>
   <xsl:otherwise>
      <IMG border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/avaluat.gif</xsl:attribute></IMG>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>


  <xsl:template name="getDisplayFeedback">
    <xsl:param name="ident_item"/>
    <xsl:param name="feedback"/>
    <xsl:if test="contains($feedback, $ident_item)">
      <xsl:variable name="ident_feed">
        <xsl:value-of select="substring-before(substring-after($feedback,$ident_item),'#')"/>
      </xsl:variable>
      <xsl:if test="itemfeedback[@ident=$ident_feed]!=''">
        <xsl:variable name="filtered_html">
          <xsl:call-template name="filter-html">
            <xsl:with-param name="text">
              <xsl:value-of select="itemfeedback[@ident=$ident_feed]" disable-output-escaping="yes"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$filtered_html" disable-output-escaping="yes"/>
      </xsl:if>
      <xsl:call-template name="getDisplayFeedback">
        <xsl:with-param name="ident_item">
          <xsl:value-of select="$ident_item"/>
        </xsl:with-param>
        <xsl:with-param name="feedback">
          <xsl:value-of select="substring-after($feedback,$ident_item)"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

<!-- #################### COMMONS ################# --> 
<xsl:template name="put_navigation_buttons">
  <a title="Joc anterior" onmouseover="window.status='';return true;">
   <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
   <IMG width="17" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back.gif</xsl:attribute></IMG>
  </a>
  <xsl:choose>
   <xsl:when test="$section_number>1">
    <a title="Joc següent" onmouseover="window.status='';return true;">
     <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
     <IMG width="17" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next.gif</xsl:attribute></IMG>
    </a>
   </xsl:when>
   <xsl:otherwise>
    <IMG width="17" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next_off.gif</xsl:attribute></IMG>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>

<!-- #################### TEST ################# -->
 <!--xsl:template match="text()">
 <xsl:value-of select="normalize-space()"/>
 </xsl:template-->

</xsl:stylesheet>
