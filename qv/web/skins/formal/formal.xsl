<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>
 <xsl:output method="html" encoding="ISO-8859-1" indent="no" media-type="text/html"/>
 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/formal/image</xsl:variable>
 <xsl:variable name="header_image">
  <xsl:choose>
   <xsl:when test="//qtimetadata/qtimetadatafield[fieldlabel='header_image']!=''"><xsl:value-of select="$specific_images_path"/>/<xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='header_image']/fieldentry"/></xsl:when>
   <xsl:otherwise><xsl:value-of select="$specific_images_path"/>/header.gif</xsl:otherwise>
  </xsl:choose>
 </xsl:variable>
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
    <LINK TYPE="text/css" rel="stylesheet" href="skins/formal/css/formal.css"/>
   </HEAD>
   <BODY onload="window.focus();" >
    <TABLE width="630" height="100%" border="0" cellspacing="0" cellpadding="0">
     <tbody><tr><td><xsl:call-template name="index_header"/></td></tr>
     <tr><td height="100%" valign="top">
     <xsl:apply-templates select="assignment/sections"/>
     <br/><p style="height:20"/>
     <xsl:call-template name="legend"/></td></tr>
     <tr><td><xsl:call-template name="assignment_buttons"/></td></tr>
     <tr><td valign="bottom"><xsl:call-template name="separation_line"/></td></tr>
     <tr><td height="20"/></tr></tbody></TABLE>
   </BODY>
  </HTML>
 </xsl:template>
 
 <xsl:template name="legend">
    <TABLE width="630" border="0" cellpadding="2" cellspacing="1" bordercolor="#ffffff" bgcolor="#ffffff">
      <tbody>
      <tr>
        <td width="59">&#160;</td>
        <td>         
         <TABLE width="551" border="0" cellpadding="0" cellspacing="0" class="text" bgcolor="#f9f7f0">
         <tbody><tr>
          <td colspan="3" bgcolor="#ffffff" class="textmapa" align="right"><b>Llegenda estats</b></td>
         </tr>
         <tr><td colspan="3" height="1"><xsl:call-template name="separation_line"><xsl:with-param name="line_width">561</xsl:with-param></xsl:call-template></td></tr>
         <tr>
          <td class="text"><IMG width="10" height="10" border="0" title="no començat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_no_iniciat.gif</xsl:attribute></IMG> No començat</td>
          <td class="text"><IMG width="10" height="10" border="0" title="lliurat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_lliurat.gif</xsl:attribute></IMG> Lliurat</td>
          <td class="text"><IMG width="10" height="10" border="0" title="esperant resposta"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG> Esperant resposta</td>
         </tr>
         <tr>
          <td class="text"><IMG width="10" height="10" border="0" title="començat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_iniciat.gif</xsl:attribute></IMG> Començat</td>
          <td class="text"><IMG width="10" height="10" border="0" title="corregit"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_corregit.gif</xsl:attribute></IMG> Corregit</td>
          <td class="text"><IMG width="10" height="10" border="0" title="intervenció pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG> Intervenció per respondre</td>
         </tr></tbody>
         </TABLE>
        </td>
      </tr>
      </tbody>
    </TABLE>
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
    <TABLE width="630" border="0" cellpadding="2" cellspacing="1" bordercolor="#ffffff" bgcolor="#ffffff">
      <tbody>
      <tr>
        <td width="59">&#160;</td>
        <td bgcolor="#d0def9" class="textmapa"><div align="left" class="textmapang">Fulls</div></td>
        <td width="80" bgcolor="#d0def9" class="textmapa"><div align="center" class="textmapang">Estat</div></td>
        <td width="80" bgcolor="#d0def9" class="textmapa"><div align="center" class="textmapang">Lliuraments</div></td>
        <td width="80" bgcolor="#d0def9" class="textmapa"><div align="center" class="textmapang">Puntuació</div></td>
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
        <xsl:apply-templates select="section_puntuation"/>
       </xsl:variable>
     
      <tr class="textmapa">
        <td/>
        <td bgcolor="#e8f2f4" class="textmapa"><div align="left" class="textmapang">
         <a title="veure full">
          <xsl:attribute name="href">
           <xsl:value-of select="normalize-space($section_path)"/>
          </xsl:attribute>
          <xsl:value-of select="@num"/>. <xsl:value-of select="$section_name"/> 
         </a></div></td>
        <td bgcolor="#f9f7f0" class="text"><div align="center">
         <xsl:if test="section_states/no_iniciat='true'"><IMG width="10" height="10" border="0" title="full no iniciat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_no_iniciat.gif</xsl:attribute></IMG></xsl:if>          
         <xsl:if test="$view='candidate' and section_states/intervencio_professor='true'"><IMG width="10" height="10" border="0" title="full amb intervenció pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="$view='candidate' and section_states/intervencio_alumne='true'"><IMG width="10" height="10" border="0" title="full amb intervenció a l'espera de ser resposta"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="$view!='candidate' and section_states/intervencio_alumne='true'"><IMG width="10" height="10" border="0" title="full amb intervenció pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="$view!='candidate' and section_states/intervencio_professor='true'"><IMG width="10" height="10" border="0" title="full amb intervenció a l'espera de ser resposta"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="section_states/iniciat='true'"><IMG width="10" height="10" border="0" title="full iniciat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_iniciat.gif</xsl:attribute></IMG></xsl:if>          
         <xsl:if test="section_states/lliurat='true'"><IMG width="10" height="10" border="0" title="full lliurat"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_lliurat.gif</xsl:attribute></IMG></xsl:if>          
         <xsl:if test="section_states/corregit='true'"><IMG width="10" height="10" border="0" title="full corregit"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/qv_corregit.gif</xsl:attribute></IMG></xsl:if>
        </div></td>
        <td bgcolor="#f9f7f0" class="text"><div align="center"><xsl:value-of select="$section_limit"/></div></td>
        <td bgcolor="#f9f7f0" class="text"><div align="center"><xsl:value-of select="$section_puntuation"/>&#160;</div></td>
      </tr>
      </xsl:for-each>
      </tbody>
     </TABLE>
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

 <TABLE width="627" border="0" cellspacing="0" cellpadding="2">
 <tbody><tr> 
    <td colspan="2">&#160;</td>
  </tr>
  <tr> 
    <td width="75" class="titol2" align="right" valign="top" height="57"> 
      <IMG width="75" height="30" border="0"><xsl:attribute name="src"><xsl:value-of select="$item_image"/></xsl:attribute><xsl:attribute name="title"><xsl:value-of select="$items_type_title"/></xsl:attribute></IMG>
    </td>
    <td class="titol2" height="57"> 
      <!-- Assessment -->
      <p class="item-assessment">
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
      <p class="item-response">
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
      <p class="item-interaction">
        <xsl:call-template name="interactions">
         <xsl:with-param name="item_ident">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
         <xsl:with-param name="index">1</xsl:with-param>
        </xsl:call-template>       
      </p>
    </td>
  </tr>
  </tbody>
  </TABLE>

 </xsl:template> 

<!-- #################### REDEFINITIONS ################# -->

 <xsl:template name="html_head_title">
    QV - <xsl:value-of select="$header_title"/>
 </xsl:template>

 <xsl:template name="put_css">
    <LINK TYPE="text/css" rel="stylesheet" href="skins/formal/css/formal.css"/>
 </xsl:template>
 

 <xsl:template name="questestinterop_body">
     <xsl:variable name="section_name">
      <xsl:value-of select="$section_number"/>. <xsl:value-of select="//section[position()=$section_number]/@title"/>
     </xsl:variable>
     
     <xsl:call-template name="section_header"/>
     <TABLE width="627" border="0" cellspacing="0" cellpadding="2">
       <tbody><tr>
         <td colspan="2" >&#160;</td>
       </tr>
       <tr>
         <td colspan="2" >&#160;</td>
       </tr>
       <tr> 
         <td width="75"> 
           <div align="right">&#160;</div>
         </td>
         <td width="550" class="titol1">
           <div align="left"><xsl:value-of select="$section_name"/></div>
         </td>
       </tr>
       <tr>
         <td colspan="2" >&#160;</td>
       </tr>
       </tbody>
     </TABLE>

     <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>


     <TABLE width="627" border="0" cellspacing="0" cellpadding="2">
       <tbody>
     <tr> 
       <td colspan="3">&#160;</td>
     </tr>
       <tr> 
       <td width="75"><div>&#160;</div></td>
       <td width="276" valign="bottom">
        <xsl:variable name="is_preview">
          <xsl:call-template name="isPreviewMode"/>
        </xsl:variable>
        <xsl:choose>
         <xsl:when test="$is_preview='true'">
            <a title="Mostra la correció del full" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="correct_section"/></xsl:attribute>
             Corregeix
            </a>
         </xsl:when>
         <xsl:otherwise>
          <xsl:choose>
           <xsl:when test="$view='candidate' and $estat_lliurament!='corregit'">
            <a title="Guarda el quadern" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="save_section"/></xsl:attribute>
             Guarda
            </a>
            &#160;
            <a title="Lliura aquest full per que el corregeixi el professor" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
             Lliura
            </a>
           </xsl:when>
           <xsl:when test="$view='teacher' and $estat_lliurament!='corregit' and $estat_lliurament!=''">
            <a title="Corregeix aquest full" onmouseover="window.status='';return true;">
             <xsl:attribute name="href"><xsl:call-template name="deliver_section"/></xsl:attribute>
             Corregeix
            </a>
           </xsl:when>
          </xsl:choose>
         </xsl:otherwise>
        </xsl:choose>
       </td>
       <td width="276" align="right" valign="bottom">
          <a title="Pàgina anterior" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
           <IMG width="25" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back.gif</xsl:attribute></IMG>
         </a>
          <a title="Pàgina principal" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_home"/></xsl:attribute>
           <IMG width="25" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/home.gif</xsl:attribute></IMG>
         </a>
          <a title="Pàgina següent" onmouseover="window.status='';return true;">
           <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
           <IMG width="25" height="20" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next.gif</xsl:attribute></IMG>
         </a>
       </td>
     </tr>
     <tr> 
       <td colspan="3"><xsl:call-template name="separation_line"/></td>
     </tr>
     <tr> 
       <td colspan="3">&#160;</td>
     </tr>
     <tr> 
       <td colspan="3">&#160;</td>
     </tr>
     </tbody>
     </TABLE>       
 </xsl:template>
  


 <xsl:template match="*[local-name()='material']">
  <xsl:apply-templates/>
  <!--DIV><xsl:if test="@align!=''"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if><xsl:apply-templates/></DIV-->
 </xsl:template>
 
 <xsl:template match="*[local-name()='presentation_material']">
   <TABLE width="670" border="0" cellspacing="0" cellpadding="2">
     <tbody><tr> 
       <td width="75" class="titol2"><IMG width="75" height="1" border="0"><xsl:attribute name="src"><xsl:value-of select="$common_images_path"/>/blanc.gif</xsl:attribute></IMG></td>
       <td width="550" class="titol2"><xsl:call-template name="getSectionAdditionalMaterial"/></td>
     </tr></tbody>
   </TABLE>
 </xsl:template>
 
<xsl:template name="getItemFeedback">
  <!-- Si existeix algun feedback a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <!--xsl:variable name="showFeedback"><xsl:value-of select="itemmetadata/qtimetadata/qtimetadatafield[@fieldlabel='showFeedback']/@fieldentry" /></xsl:variable-->
  <xsl:variable name="showFeedback">
   <xsl:value-of select="itemcontrol/@feedbackswitch"/>
  </xsl:variable>
  <xsl:if test="normalize-space($showFeedback)!='No'">
   <xsl:if test="$es_correccio='true'">
  <TABLE border="0" cellpadding="0" cellspacing="0">
   <tbody><tr>
   <td valign="top">
      <IMG width="50" height="20" border="0" title="Correcció"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/feedback.gif</xsl:attribute></IMG>
   </td>
   <td width="75" valign="top" class="text">
    <script> if(isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
document.write('<span style="color: green;font-size:9">CORRECTE. </span>');
]]> }else { <![CDATA[
document.write('<span style="color: red;font-size:9">INCORRECTE. </span>');
]]> } </script>
</td>
<td valign="top" class="textmapa" >
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

 <xsl:template name="interactions">
  <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <xsl:param name="index"/>
  <xsl:variable name="showInteraction">
   <xsl:call-template name="show_interaction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$item_ident"/></xsl:with-param>
   </xsl:call-template>
  </xsl:variable>
  
  <table width="98%" border="0" cellspacing="1" cellpadding="0" class="text" align="left">
   <tr>
    <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
     <td width="30" class="text" align="left" valign="top">
       <IMG width="50" height="20" border="0" title="Intervencions"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/interaction.gif</xsl:attribute></IMG>
     </td>
     <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#FFFFFF">
       <xsl:call-template name="interaction">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$item_ident"/>
        </xsl:with-param>
        <xsl:with-param name="index">1</xsl:with-param>
       </xsl:call-template>
      </table>
      <xsl:variable name="new_interaction_index">
       <xsl:value-of select="substring-after(substring(substring-before($displayinteraction,'_view=|'), (string-length(substring-before($displayinteraction,'_view=|'))-3)),'_')"/>
      </xsl:variable>
      <xsl:if test="not (contains($displayinteraction, concat($item_ident,concat('_',concat($new_interaction_index,'_view=|')))))">
       <A title="Afegir una nova intervenci&#243; a la zona de di&#224;leg">
          <xsl:attribute name="href">#int_<xsl:value-of select="$item_ident"/></xsl:attribute>
          <xsl:attribute name="onClick"><xsl:call-template name="add_interaction"/></xsl:attribute>
          afegeix intervenció
       </A>
      </xsl:if>
     </td>
    </xsl:if>
   </tr>
  </table>
 </xsl:template>
 
 
<xsl:template name="interaction">
  <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <xsl:param name="index"/>
  <!-- A item_ident_num deixo el nom de l'ident + el numero d'interaccio que busco -->
  <xsl:variable name="item_ident_num">
   <xsl:value-of select="$item_ident"/>_<xsl:value-of select="$index"/>_view</xsl:variable>
  <xsl:variable name="interaction_to_show">
   <!-- forma: (item_ident)_(index)_view(autor)=(valor) -->
   <xsl:value-of select="substring-before(substring-after($displayinteraction,$item_ident_num),'#')"/>
  </xsl:variable>
  <xsl:variable name="req_view">
   <xsl:value-of select="substring-before($interaction_to_show,'=')"/>
  </xsl:variable>
  <!-- Qui ho ha escrit -->
  <xsl:variable name="interaction_clean">
   <xsl:value-of select="substring-after($interaction_to_show,'=')"/>
  </xsl:variable>
  <!-- text que ha escrit -->
  <xsl:choose>
   <xsl:when test="normalize-space($interaction_to_show)!=''">
    <!-- Si existeix alguna interaccio per aquesta interaccio es mostra i tambe es mira si hi ha mes interaccions. -->
    <tr>
     <xsl:if test="$req_view='teacher'">
      <xsl:attribute name="style">background-color:#e8f2f4</xsl:attribute>
     </xsl:if>
     <xsl:choose>
      <xsl:when test="$interaction_clean='|'">
       <!-- Ha demanat posar aquesta interaccio -->
       <TD colspan="2" class="text" style="padding:0">
        <xsl:if test="$view='teacher'">
         <xsl:attribute name="style">background-color:#e8f2f4</xsl:attribute>
        </xsl:if>
        <table border="0" cellspacing="0" cellpadding="0">
         <TR>
          <TD class="text" width="100%">
           <input type="text" style="width: 98%;">
            <xsl:attribute name="name">interaccio_<xsl:value-of select="$item_ident"/>_<xsl:value-of select="$index"/>_view<xsl:value-of select="$view"/></xsl:attribute>
           </input>
          </TD>
          <TD>
           <a title="Envia la nova intervenció">
            <xsl:attribute name="href">#int_<xsl:value-of select="$item_ident"/></xsl:attribute>
            <xsl:attribute name="onClick"><xsl:call-template name="send_interaction"/></xsl:attribute>
            envia
           </a>
          </TD>
         </TR>
        </table>
       </TD>
      </xsl:when>
      <xsl:otherwise>
       <!-- La interaccio ja estava posada -->
       <TD width="30" class="textmapa">
        <xsl:choose>
         <xsl:when test="$req_view='candidate'"><b>Alumne. </b></xsl:when>
         <xsl:when test="$req_view='teacher'"><b>Professor. </b></xsl:when>
        </xsl:choose>
       </TD>
       <TD class="textmapa">
        <xsl:value-of select="$interaction_clean"/>
        <BR/>
       </TD>
      </xsl:otherwise>
     </xsl:choose>
    </tr>
    <xsl:call-template name="interaction">
     <xsl:with-param name="item_ident">
      <xsl:value-of select="$item_ident"/>
     </xsl:with-param>
     <xsl:with-param name="index">
      <xsl:value-of select="$index + 1"/>
     </xsl:with-param>
    </xsl:call-template>
   </xsl:when>
  </xsl:choose>
 </xsl:template> 
  
<!-- #################### COMMONS ################# -->
 <xsl:template name="image_header">
   <!-- Header image -->
    <TABLE width="625" border="0" cellspacing="0" cellpadding="0" class="nomcurs">
      <tbody>
        <tr>
          <td height="3">
            <div align="right">
             <a href="#">
              <xsl:if test="//questestinterop"><xsl:attribute name="onclick"><xsl:call-template name="go_home"/></xsl:attribute></xsl:if>
              <xsl:attribute name="title"><xsl:value-of select="$header_title"/></xsl:attribute>
              <IMG width="630" height="63" border="0"><xsl:attribute name="src"><xsl:value-of select="$header_image"/></xsl:attribute></IMG>
             </a>
            </div>
          </td>
        </tr>
      </tbody>
    </TABLE>  
    <xsl:if test="not(//qtimetadata/qtimetadatafield[fieldlabel='header_image'])">
     <DIV style="position: absolute; left: 86px; top: 31px; z-index: 2;" class="header">
       <A href="#" class="header">
         <xsl:attribute name="onclick"><xsl:call-template name="go_home"/></xsl:attribute>
         <xsl:attribute name="title"><xsl:value-of select="$header_title"/></xsl:attribute>
         <DIV class="header"><xsl:value-of select="$header_title"/></DIV>
       </A>
     </DIV>
    </xsl:if>
 </xsl:template>
 
<xsl:template name="index_header">
  <xsl:call-template name="image_header"/>
  <TABLE width="630" border="0" cellspacing="0" cellpadding="0">
  <tbody>
  <tr>
    <td height="19" align="right" class="subheader">
     <xsl:if test="//qtimetadata/qtimetadatafield[fieldlabel='header_image']"><xsl:value-of select="$header_title"/>&#160;</xsl:if>
    </td>
   </tr>
   <tr>
    <td><xsl:call-template name="separation_line"/></td>
   </tr>
   <tr>
    <td height="20"/>
   </tr>
   </tbody>
   </TABLE>
 </xsl:template>
 
 <xsl:template name="section_header">
  <xsl:call-template name="image_header"/>

  <!-- Menu -->
<table width="630" border="0" cellspacing="2" cellpadding="0">
  <tbody>
    <tr align="right" valign="middle">
      <td width="90%" class="menusup">
        <div align="right">
        </div>
        <div align="right">
         <IMG width="50" height="15" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/full.gif</xsl:attribute></IMG>
        </div>
      </td>
      <xsl:for-each select="//section">
       <xsl:choose>
        <xsl:when test="position()=$section_number">
         <td width="16" bgcolor="#C07878" class="textmapa">
           <div align="center" style="color:#ffffff">
             <xsl:value-of select="position()"/>
           </div>
         </td>
         </xsl:when>
         <xsl:otherwise>
          <td width="16" bgcolor="#E9E9D7" class="textmapa">
            <div align="center">
             <a onmouseover="window.status='';return true;">
              <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template></xsl:attribute>
              <xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute>
              <xsl:value-of select="position()"/>
             </a>
            </div>
          </td>
         </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </tr>
  </tbody>
</table>
  
  <!-- Línia -->
  <TABLE width="630" border="0" cellspacing="0" cellpadding="0">
    <tbody>
    <tr>
      <td><xsl:call-template name="separation_line"/></td>
    </tr>
    <tr>
     <td height="20"/>
    </tr>
  </tbody>
  </TABLE>

 </xsl:template>
 

 <xsl:template name="separation_line">
  <xsl:param name="line_width"/>
  <xsl:variable name="width">
   <xsl:choose>
    <xsl:when test="$line_width=''">630</xsl:when>
    <xsl:otherwise><xsl:value-of select="$line_width"/></xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <IMG height="1" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/linia.gif</xsl:attribute><xsl:attribute name="width"><xsl:value-of select="$width"/></xsl:attribute></IMG>
 </xsl:template>

<!-- #################### TEST ################# -->
 <!--xsl:template match="text()">
 <xsl:value-of select="normalize-space()"/>
 </xsl:template-->

</xsl:stylesheet>
