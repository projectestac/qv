<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/esta_eso/image</xsl:variable>
 <xsl:variable name="specific_css_path">skins/esta_eso/css</xsl:variable>

 <xsl:variable name="header_title">
  <xsl:choose>
    <xsl:when test="//assignment!=''"><xsl:value-of select="//assignment/@name"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
   </xsl:choose>
 </xsl:variable>

 <!--xsl:output method="html" encoding="iso-8859-1" cdata-section-elements="mattext"/-->
 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/>

<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
  <HTML>
   <HEAD>
    <title>Quaderns Virtuals</title>
    <xsl:call-template name="put_css"/>
    <xsl:call-template name="put_scripts"/>
   </HEAD>
   <BODY topmargin="15" leftmargin="10" onload="window.focus();" >
    <xsl:call-template name="header"/>
    <BR/>
    <TABLE width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
     <tbody>
     <tr><td height="30"/></tr>
     <tr><td height="100%" valign="top">
      <xsl:apply-templates select="assignment/sections"/>
      <br/><p style="height:20"/>
     </td></tr>
     <tr><td height="20"/></tr>
     </tbody>
     </TABLE>
   </BODY>
  </HTML>
 </xsl:template>

 <xsl:template match="sections">
  <xsl:variable name="puntuation">
   <xsl:choose>
    <xsl:when test="//assignment/puntuation!=''"><xsl:value-of select="//assignment/puntuation"/></xsl:when>
    <xsl:otherwise>-</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="assignment_date">
    <xsl:value-of select="//assignment/assignment_date"/>
  </xsl:variable>
  <CENTER>
   <TABLE class="header" border="0" cellpadding="0" cellspacing="1" width="75%">
   <tbody><tr>
    <td>
     <!-- INICI info quadern -->
     <TABLE class="header-background" border="0" cellpadding="0" cellspacing="0" height="60" width="100%">
     <tbody><tr>
      <td class="header" height="8"/>
     </tr>
     <tr>
      <td class="text">
       <TABLE border="0" cellpadding="2" cellspacing="2" width="100%" class="text">
       <tbody>
       <tr><td heigth="100" colspan="2"/></tr>
       <tr>
        <td width="150" class="title">Puntuaci�:&#160;</td>
        <td><xsl:value-of select="$puntuation"/></td>
       </tr>
       <tr>
        <td class="title">Data assignaci�:&#160;</td>
        <td><xsl:value-of select="$assignment_date"/></td>
       </tr>
       <tr><td heigth="100" colspan="2"/></tr>
       </tbody>
       </TABLE>
     <!-- FI info quadern -->
      </td>
     </tr>
     <tr>
      <td>
     <!-- INICI llistat fulls -->
       <TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
       <tbody><tr class="header">
        <td width="10"/>
        <td width="300" valign="top">Fulls</td>
        <td width="175" align="center" valign="top">Estat<br/>
         <TABLE border="0" cellpadding="0" cellspacing="0" class="text" style="color:#FFFFFF">
         <tbody><tr>
          <td width="50" align="center">Iniciat</td>
          <td width="50" align="center">Lliurat</td>
          <td width="50" align="center">Corregit</td>
         </tr></tbody></TABLE>
        </td>
        <td width="100" align="center" valign="top">Lliuraments</td>
        <td width="100" align="center" valign="top">Puntuaci�</td>       
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
     
      <tr class="title">
       <xsl:choose>
        <xsl:when test="@num mod 2=0">
         <xsl:attribute name="class">title</xsl:attribute>
        </xsl:when>
        <xsl:otherwise><xsl:attribute name="style">background-color:#FFFFFF</xsl:attribute></xsl:otherwise>
       </xsl:choose>
       <td>
         <xsl:if test="($view='candidate' and section_states/intervencio_professor='true') or ($view!='candidate' and section_states/intervencio_alumne='true')"><IMG width="10" height="10" border="0" title="intervenci� pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="($view='candidate' and section_states/intervencio_alumne='true') or ($view!='candidate' and section_states/intervencio_professor='true')"><IMG width="10" height="10" border="0" title="esperant resposta d'intervenci�"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG></xsl:if>
       </td>
        <td>
         <xsl:value-of select="@num"/>. 
         <a title="veure full">
          <xsl:attribute name="href"><xsl:value-of select="normalize-space($section_path)"/></xsl:attribute>
          <xsl:value-of select="$section_name"/> 
         </a>
        </td>
        <td align="center">
         <TABLE border="0" cellpadding="3" cellspacing="0" class="text" style="font-weight:bold">
         <tbody><tr>
          <td width="50" align="center"><xsl:if test="section_states/iniciat='true'">x</xsl:if></td>
          <td width="50" align="center"><xsl:if test="section_states/lliurat='true' or section_states/corregit='true'">x</xsl:if></td>
          <td width="50" align="center"><xsl:if test="section_states/corregit='true'">x</xsl:if></td>
         </tr></tbody></TABLE>
        </td>
        <td class="text" align="center"><xsl:value-of select="$section_limit"/></td>
        <td class="text" align="center"><xsl:value-of select="$section_puntuation"/>&#160;</td>
      </tr>
      </xsl:for-each>       
       </tbody>
       </TABLE>
      </td>
     </tr></tbody>
     </TABLE>
     <!-- FI llistat fulls -->
    </td>
   </tr>
   <tr>
    <td >
     <!-- INICI botons -->
     <xsl:variable name="show_correct_all_button"><xsl:call-template name="show_correct_all_button"/></xsl:variable>
     <xsl:variable name="show_deliver_all_button"><xsl:call-template name="show_deliver_all_button"/></xsl:variable>
     <xsl:variable name="show_start_button"><xsl:call-template name="show_start_button"/></xsl:variable>

     <CENTER>
        <TABLE width="100%" height="50" border="0" cellspacing="0" cellpadding="0" class="background">
        <tbody><tr>
         <td width="30%"/>
          <td width="10"/>
          <td align="center">
            <xsl:if test="$show_correct_all_button='true'">
             <xsl:call-template name="put_image_button">
              <xsl:with-param name="href">?wantCorrect=true</xsl:with-param>
              <xsl:with-param name="button_name">btCorregirTot</xsl:with-param>
              <xsl:with-param name="button_tip">Corregir tot el quadern</xsl:with-param>
              <xsl:with-param name="image_off_src">corregirtot_off.gif</xsl:with-param>
              <xsl:with-param name="image_on_src">corregirtot_on.gif</xsl:with-param>                          
             </xsl:call-template>
            </xsl:if>
            <xsl:if test="$show_deliver_all_button='true'">
             <xsl:call-template name="put_image_button">
              <xsl:with-param name="href">?wantCorrect=true</xsl:with-param>
              <xsl:with-param name="button_name">btLliurarTot</xsl:with-param>
              <xsl:with-param name="button_tip">Lliurar tot el quadern</xsl:with-param>
              <xsl:with-param name="image_off_src">lliurartot_off.gif</xsl:with-param>
              <xsl:with-param name="image_on_src">lliurartot_on.gif</xsl:with-param>                          
             </xsl:call-template>
            </xsl:if>
          </td>
         <td width="30%"/>
        </tr></tbody>
        </TABLE>
     </CENTER>
     <!-- FI botons -->
    </td>
   </tr></tbody>
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
    <script> if(isItemCorrect("<xsl:value-of select="$item_ident"/>")) { <![CDATA[
				document.write('<TD width="20" align="center" valign="top"><img src="web/imatges/correcte.gif" alt="correcte" height="20"/></TD>');
				document.write('<TD class="feedbackOK">');
			]]> }else { <![CDATA[
				document.write('<TD width="20" align="center" valign="top"><img src="web/imatges/incorrecte.gif" alt="incorrecte" height="20"/></TD>');
				document.write('<TD class="feedbackKO">');
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
				document.write('</TD>');
			]]></script>
   </xsl:if>
  </xsl:if>
 </xsl:template>

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="html_head_title">
    Quaderns Virtuals - <xsl:value-of select="$header_title"/>
 </xsl:template>
 
 <xsl:template name="put_css">
    <LINK TYPE="text/css" rel="stylesheet" href="skins/esta_eso/css/esta_eso.css"/>
    <LINK TYPE="text/css" rel="stylesheet" href="skins/esta_eso/css/matform.css"/>
 </xsl:template>
  
 <xsl:template name="html_body_attrs">
   <xsl:attribute name="text">#000000</xsl:attribute>
   <xsl:attribute name="bgColor">#ffffff</xsl:attribute>
   <xsl:attribute name="leftMargin">0</xsl:attribute>
   <xsl:attribute name="background"><xsl:value-of select="$specific_images_path"/>/fons.gif</xsl:attribute>
   <xsl:attribute name="topMargin">0</xsl:attribute>
   <xsl:attribute name="marginwidth">0</xsl:attribute>
   <xsl:attribute name="marginheight">0</xsl:attribute>
 </xsl:template>

 <xsl:template name="questestinterop_body">
    <xsl:call-template name="header"/>
    <table cellspacing="0" cellpadding="2" width="628" border="0">
      <tbody>
        <tr>
          <td width="263">
            <div align="left"></div></td>
          <td width="5">&#160;</td>
          <td width="41">&#160;</td>
       <xsl:for-each select="//section">
        <xsl:choose>
         <xsl:when test="position()=$section_number">
          <td width="40">
           <img width="40" height="30" alt="P�gina 1" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/activ<xsl:value-of select="position()"/>n.gif</xsl:attribute></img>
          </td>
         </xsl:when>
         <xsl:otherwise>         
          <td width="40">
             <a onmouseover="window.status='';return true;">
              <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template></xsl:attribute>
              <xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute>
              <img width="38" height="21" alt="P�gina 1" border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/actip<xsl:value-of select="position()"/>n.gif</xsl:attribute></img>
             </a>           
          </td>
         </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
        </tr>
      </tbody>
    </table>
    <br/>
    <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
    <xsl:call-template name="put_navigation_buttons"/>
    <xsl:call-template name="put_action_buttons"/>     
 </xsl:template>
 
  <xsl:template name="item-attrs">
   <xsl:attribute name="width">610</xsl:attribute>
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
  <table width="98%" border="0" cellspacing="0" cellpadding="0" class="item-add-interaction">
   <tr>
    <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
     <td width="30" align="left" valign="top">
      <xsl:call-template name="put_add_interaction_button"/>
     </td>
     <td>
      <table width="100%" height="35" border="0" cellspacing="0" cellpadding="5" bgcolor="#FFFFFF">
       <xsl:call-template name="interaction">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$item_ident"/>
        </xsl:with-param>
        <xsl:with-param name="index">1</xsl:with-param>
       </xsl:call-template>
      </table>
     </td>
    </xsl:if>
   </tr>
  </table>
 </xsl:template>
 
 <xsl:template name="put_add_interaction_button">
  <xsl:variable name="show_interaction_button"><xsl:call-template name="show_interaction_button"/></xsl:variable>  
   <xsl:call-template name="put_image_button">
    <xsl:with-param name="href">#int_<xsl:value-of select="@ident"/></xsl:with-param>
    <xsl:with-param name="onclick"><xsl:if test="$show_interaction_button='true'"><xsl:call-template name="add_interaction"/></xsl:if></xsl:with-param>
    <xsl:with-param name="button_name">btAddInt_<xsl:value-of select="@ident"/></xsl:with-param>
    <xsl:with-param name="button_tip">Afegeix una nova intervenci&#243; a la zona de di&#224;leg</xsl:with-param>
    <xsl:with-param name="image_off_src">questio_off.gif</xsl:with-param>
    <xsl:with-param name="image_on_src">questio_on.gif</xsl:with-param>                          
   </xsl:call-template>
 </xsl:template>

 <xsl:template name="put_send_interaction_button">
   <xsl:call-template name="put_image_button">
    <xsl:with-param name="href">#int_<xsl:value-of select="@ident"/></xsl:with-param>
    <xsl:with-param name="onclick"><xsl:call-template name="send_interaction"/></xsl:with-param>
    <xsl:with-param name="button_name">btSendInt_<xsl:value-of select="@ident"/></xsl:with-param>
    <xsl:with-param name="button_tip">Envia la nova intervenci�</xsl:with-param>
    <xsl:with-param name="image_off_src">enviar_off.gif</xsl:with-param>
    <xsl:with-param name="image_on_src">enviar_on.gif</xsl:with-param>
   </xsl:call-template>
 </xsl:template>
 
  
<!-- #################### COMMONS ################# -->
<xsl:template name="header">
  <xsl:variable name="section_name">
    <xsl:value-of select="//section[position()=$section_number]/@title"/>
  </xsl:variable>  
 <table cellspacing="0" cellpadding="1" width="629" border="0" height="68">
   <tr> 
     <td bgcolor="#A7A7CE" width="41" valign="top">
      <a name="inici">
       <xsl:if test="assessment/section">
        <xsl:attribute name="onClick"><xsl:call-template name="go_home"/></xsl:attribute>
        <xsl:attribute name="href">#</xsl:attribute>
        <xsl:attribute name="title">Tornar a la p�gina principal</xsl:attribute>
       </xsl:if>
       <img alt="Quaderns Virtuals" width="80" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/logoQV.gif</xsl:attribute></img>
      </a> 
     </td> 
     <td bgcolor="#A7A7CE" valign="bottom" width="589"> 
       <div align="right" class="titol2"> <span class="titol1">Estad�stica amb ordinador</span>&#160;&#160;<br/>
         <span class="aut"><br/></span>
         <span class="nomcurs"><xsl:value-of select="$section_name"/></span>&#160;&#160;</div>
     </td>
   </tr>
 </table>
 <table cellspacing="0" cellpadding="0" width="626" border="0" height="21">
   <tbody> 
   <tr bgcolor="#cccc00">
     <td height="2" bgcolor="#cccc00" colspan="2">
       <div align="center">
        <img width="630" height="3"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/liniablava.gif</xsl:attribute></img>
       </div>
     </td>
   </tr>
   <tr>
     <td height="2" colspan="2">&#160;</td>
   </tr>
   </tbody>
 </table>
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
  <TABLE width="610" border="0" cellspacing="0" cellpadding="0">
   <tr>
    <td width="500">
        <xsl:if test="normalize-space($show_correct_button)='true'">
         <A title="Corregeix el full">
          <xsl:attribute name="href"><xsl:call-template name="correct_section"/></xsl:attribute>
          <img border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/corregir.gif</xsl:attribute></img>
         </A>
        </xsl:if>
        <xsl:if test="normalize-space($show_save_button)='true'">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href"><xsl:call-template name="save_section"/></xsl:with-param>
          <xsl:with-param name="button_name">btSave</xsl:with-param>
          <xsl:with-param name="button_tip">Guarda les respostes del full</xsl:with-param>
          <xsl:with-param name="image_off_src">guardar_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">guardar_on.gif</xsl:with-param>                          
         </xsl:call-template>
        </xsl:if>
        <xsl:if test="normalize-space($show_deliver_button)='true'">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href"><xsl:call-template name="deliver_section"/></xsl:with-param>
          <xsl:with-param name="button_name">btDeliver</xsl:with-param>
          <xsl:with-param name="button_tip">Lliura aquest full per poder-lo corregir</xsl:with-param>
          <xsl:with-param name="image_off_src">lliurar_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">lliurar_on.gif</xsl:with-param>                          
         </xsl:call-template>
        </xsl:if>
    </td>
    <td align="left" valign="top">
      <A title="P�gina anterior">
       <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:attribute>
       <img border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/back.gif</xsl:attribute></img>
      </A>
    </td>
    <td align="center" valign="top">
      <A href="#inici" title="Pujar">
       <img border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/pujar.gif</xsl:attribute></img>
      </A>
    </td>
    <td align="right" valign="top">
      <A title="P�gina seg�ent">
       <xsl:attribute name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:attribute>
       <img border="0"><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/next.gif</xsl:attribute></img>
      </A>
    </td>
   </tr>
   <tr>
    <td colspan="4"><img><xsl:attribute name="src"><xsl:value-of select="$specific_images_path"/>/linia.gif</xsl:attribute></img></td>
   </tr>
  </TABLE> 
 </xsl:template>
 
 <xsl:template name="put_action_buttons">
 </xsl:template>

</xsl:stylesheet>