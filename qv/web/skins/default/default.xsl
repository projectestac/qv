<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="skins/common/qti.xsl"/>

 <xsl:variable name="common_images_path">skins/common/image</xsl:variable>
 <xsl:variable name="specific_images_path">skins/default/image</xsl:variable>
 <xsl:variable name="specific_css_path">skins/default/css</xsl:variable>

 <xsl:variable name="path_imatges">
  <xsl:choose>
   <xsl:when test="$color='rosa'">skins/default/image/rosa</xsl:when>
   <xsl:when test="$color='verd'">skins/default/image/verd</xsl:when>
   <xsl:otherwise>skins/default/image/blau</xsl:otherwise>
  </xsl:choose>
 </xsl:variable>

<!-- ELIMINAR VARIABLES -->
 <xsl:variable name="default_images_path">web/imatges/</xsl:variable>
 <xsl:variable name="images_path"><xsl:value-of select="$default_images_path"/>default</xsl:variable>



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
   <xsl:variable name="is_preview">
     <xsl:call-template name="isPreviewMode"/>
   </xsl:variable>

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
       <xsl:choose>
       <xsl:when test="$is_preview='false'">
       <tr>
        <td width="150" class="title">Puntuaci�:&#160;</td>
        <td><xsl:value-of select="$puntuation"/></td>
       </tr>
       <tr>
        <td class="title">Data assignaci�:&#160;</td>
        <td><xsl:value-of select="$assignment_date"/></td>
       </tr>
       </xsl:when>
       <xsl:otherwise><br/><br/><br/></xsl:otherwise>
       </xsl:choose>
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
        <td width="300" valign="top" class="section-text">Fulls</td>
        <xsl:if test="$is_preview='false'">
        <td width="175" align="center" valign="top" class="section-text">
         Estat<br/>
         <TABLE border="0" cellpadding="0" cellspacing="0" class="text" style="color:#FFFFFF">
         <tbody><tr>
          <td width="50" align="center" class="section-text">Iniciat</td>
          <td width="50" align="center" class="section-text">Lliurat</td>
          <td width="50" align="center" class="section-text">Corregit</td>
         </tr></tbody></TABLE>
        </td>
        <td width="100" align="center" valign="top" class="section-text">Lliuraments</td>
        <td width="100" align="center" valign="top" class="section-text">Puntuaci�</td>
        </xsl:if>
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
		<xsl:if test="$is_preview='false'">
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
        </xsl:if>
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
  
   <xsl:variable name="show">
    <xsl:call-template name="show_feedback"/> 
   </xsl:variable>
   <xsl:if test="$show='true'">
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
 </xsl:template>

 <!--xsl:template match="*[local-name()='material']">
  <xsl:variable name="align">
   <xsl:value-of select="@align"/>
  </xsl:variable>
  <xsl:choose>
   <xsl:when test="$align='right'">
    <SPAN align="right">
     <xsl:apply-templates/>
    </SPAN>
   </xsl:when>
   <xsl:when test="$align='center'">
    <DIV align="center">
     <xsl:apply-templates/>
    </DIV>
   </xsl:when>
   <xsl:otherwise>
    <xsl:apply-templates/>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>
 <xsl:template match="*[local-name()='mataudio']">
  <xsl:variable name="align">
   <xsl:choose>
    <xsl:when test="normalize-space(@align)!=''">
     <xsl:value-of select="normalize-space(@align)"/>
    </xsl:when>
    <xsl:otherwise>left</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <SPAN>
   <xsl:attribute name="align">
    <xsl:value-of select="normalize-space($align)"/>
   </xsl:attribute>
   <object CLASSID="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" HEIGHT="18" WIDTH="200">
    <xsl:attribute name="id">
     <xsl:value-of select="generate-id()"/>
    </xsl:attribute>
    <param name="SRC">
     <xsl:attribute name="value">
      <xsl:call-template name="getResourceSource">
       <xsl:with-param name="uri">
        <xsl:value-of select="@uri"/>
       </xsl:with-param>
      </xsl:call-template>
     </xsl:attribute>
    </param>
    <param name="AUTOSTART" value="false"/>
    <EMBED NOJAVA="true" CONTROLS="ControlPanel" CONSOLE="one" HEIGHT="18" WIDTH="200"
     autostart="false" autoplay="false">
     <xsl:call-template name="putResourceSource">
      <xsl:with-param name="uri">
       <xsl:value-of select="@uri"/>
      </xsl:with-param>
     </xsl:call-template>
    </EMBED>
    <noembed>
     <a>
      <xsl:attribute name="href">
       <xsl:call-template name="getResourceSource">
        <xsl:with-param name="uri">
         <xsl:value-of select="@uri"/>
        </xsl:with-param>
       </xsl:call-template>
      </xsl:attribute>
     </a>
    </noembed>
   </object>
   <IMG src="web/imatges/blanc.gif" width="5"/>
  </SPAN>
 </xsl:template>
 <xsl:template match="*[local-name()='matvideo']">
  <xsl:variable name="videotype">
   <xsl:choose>
    <xsl:when test="normalize-space(@videotype)!=''">
     <xsl:value-of select="normalize-space(@videotype)"/>
    </xsl:when>
    <xsl:otherwise> audio/x-pn-realaudio-plugin </xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="width">
   <xsl:choose>
    <xsl:when test="normalize-space(@width)!=''">
     <xsl:value-of select="normalize-space(@width)"/>
    </xsl:when>
    <xsl:otherwise>250</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="height">
   <xsl:choose>
    <xsl:when test="normalize-space(@height)!=''">
     <xsl:value-of select="normalize-space(@height)"/>
    </xsl:when>
    <xsl:otherwise>210</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <xsl:variable name="align">
   <xsl:choose>
    <xsl:when test="normalize-space(@align)!=''">
     <xsl:value-of select="normalize-space(@align)"/>
    </xsl:when>
    <xsl:otherwise>center</xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <table border="0">
   <xsl:attribute name="align">
    <xsl:value-of select="normalize-space($align)"/>
   </xsl:attribute>
   <tr>
    <td>
     <object ID="RVOCX" classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">
      <xsl:attribute name="width">
       <xsl:value-of select="normalize-space($width)"/>
      </xsl:attribute>
      <xsl:attribute name="height">
       <xsl:value-of select="normalize-space($height)"/>
      </xsl:attribute>
      <param name="SRC">
       <xsl:attribute name="value">
        <xsl:call-template name="getResourceSource">
         <xsl:with-param name="uri">
          <xsl:value-of select="@uri"/>
         </xsl:with-param>
        </xsl:call-template>
       </xsl:attribute>
      </param>
      <param name="CONTROLS" value="ImageWindow"/>
      <param name="CONSOLE" value="one"/>
      <param name="AUTOSTART" value="false"/>
      <embed nojava="true" controls="ImageWindow" console="one" autostart="false" pluginspage="http://get.real.com/RCH1/195.141.101.151/20a4e60630d83dd90d05/windows/mrkt/RN30TD/RealPlayer10GOLD_bb.exe">
       <xsl:call-template name="putResourceSource">
        <xsl:with-param name="uri">
         <xsl:value-of select="@uri"/>
        </xsl:with-param>
       </xsl:call-template>
       <xsl:attribute name="width">
        <xsl:value-of select="normalize-space($width)"/>
       </xsl:attribute>
       <xsl:attribute name="height">
        <xsl:value-of select="normalize-space($height)"/>
       </xsl:attribute>
       <xsl:attribute name="type">
        <xsl:value-of select="normalize-space($videotype)"/>
       </xsl:attribute>
      </embed>
      <noembed>
       <a>
        <xsl:attribute name="href">
         <xsl:call-template name="getResourceSource">
          <xsl:with-param name="uri">
           <xsl:value-of select="@uri"/>
          </xsl:with-param>
         </xsl:call-template>
        </xsl:attribute>
       </a>
      </noembed>
     </object>
    </td>
   </tr>
   <tr>
    <td>
     <object ID="RVOCX" CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT="40">
      <xsl:attribute name="width">
       <xsl:value-of select="$width"/>
      </xsl:attribute>
      <PARAM NAME="CONSOLE" VALUE="one"/>
      <PARAM NAME="CONTROLS" VALUE="ControlPanel"/>
      <EMBED NOJAVA="true" CONTROLS="ControlPanel" CONSOLE="one" HEIGHT="40">
       <xsl:attribute name="width">
        <xsl:value-of select="normalize-space($width)"/>
       </xsl:attribute>
       <xsl:attribute name="type">
        <xsl:value-of select="normalize-space($videotype)"/>
       </xsl:attribute>
      </EMBED>
     </object>
    </td>
   </tr>
  </table>
  <BR/>
 </xsl:template>
 <xsl:template name="get_hotspot_response_ident">
  <xsl:value-of select="ancestor-or-self::item/@ident"/>
  <xsl:text>- -&gt;</xsl:text>
  <xsl:value-of select="ancestor::response_lid/@ident"/>
 </xsl:template-->

<!-- #################### REDEFINITIONS ################# -->
 <xsl:template name="put_css">
    <xsl:variable name="fulla_estil">
     <xsl:choose>
      <xsl:when test="$color='rosa'"><xsl:value-of select="$specific_css_path"/>/qv_rosa.css</xsl:when>
      <xsl:when test="$color='verd'"><xsl:value-of select="$specific_css_path"/>/qv_verd.css</xsl:when>
      <xsl:otherwise><xsl:value-of select="$specific_css_path"/>/qv_blau.css</xsl:otherwise>
     </xsl:choose>
    </xsl:variable>
    <LINK TYPE="text/css" rel="stylesheet" href="skins/default/css/default.css"/>
    <LINK TYPE="text/css" rel="stylesheet">
     <xsl:attribute name="href">
      <xsl:value-of select="$fulla_estil"/>
     </xsl:attribute>
    </LINK>
    <xsl:if test="normalize-space($css)!=''">
     <LINK TYPE="text/css" rel="stylesheet">
      <xsl:attribute name="href">
       <xsl:call-template name="putResourceSource2">
        <xsl:with-param name="uri">
         <xsl:value-of select="$css"/>
        </xsl:with-param>
       </xsl:call-template>
      </xsl:attribute>
     </LINK>
    </xsl:if>  
 </xsl:template>
  
 <xsl:template name="questestinterop_body">
    <xsl:call-template name="header"/>
    <br/>
    <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
    <xsl:call-template name="put_navigation_buttons"/>
    <xsl:call-template name="put_action_buttons"/>     
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

  <div style="border: 1px none rgb(255, 255, 255); position: absolute; left: 15px; top: 5px; width: 110px; height: 84px; z-index: 2;" id="textsuperior">
   <a>
    <xsl:if test="assessment/section">
     <xsl:attribute name="onClick"><xsl:call-template name="go_home"/></xsl:attribute>
     <xsl:attribute name="href">#</xsl:attribute>
     <xsl:attribute name="title">Tornar a la p�gina principal</xsl:attribute>
    </xsl:if>
    <img alt="Quaderns Virtuals" width="109" height="83" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>/logo.gif</xsl:attribute></img>
   </a>
  </div>
  <table class="header" border="0" cellpadding="0" cellspacing="1" height="60" width="100%">
  <tbody><tr>
   <td>
    <table class="header-background" border="0" cellpadding="0" cellspacing="0" height="60" width="100%">
    <tbody><tr>
     <td class="header" colspan="2" height="8"/>
    </tr>
    <tr>
     <td width="120"/>
     <td>
      <TABLE class="header-text" border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
      <tbody><tr>
       <td>
        <span style="font-weight:bold; font-size:15">
         <xsl:choose>
          <xsl:when test="assignment!=''"><xsl:value-of select="assignment/@name"/></xsl:when>
          <xsl:otherwise><xsl:value-of select="//assessment/@title"/></xsl:otherwise>
         </xsl:choose>
         </span>
        <xsl:choose>
        	<xsl:when test="$section_name!=''">
        		<br/><xsl:value-of select="$section_name"/>
        		<br/>
        	</xsl:when>
        	<xsl:otherwise>
		        <br/><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='materia']/fieldentry"/>
		        <br/><xsl:value-of select="//qtimetadata/qtimetadatafield[fieldlabel='level']/fieldentry"/>
        	</xsl:otherwise>
        </xsl:choose>
       </td>
       <td valign="top" align="right" height="100%">
        <xsl:if test="assessment/section">
         <span style="font-weight:bold; font-size:18"><xsl:value-of select="$section_number"/></span>/<xsl:value-of select="$section_max_number"/>
         <br/><br/><span style="valign:bottom">
          puntuaci�:&#160;
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
  </td>
 </tr>
 </tbody></table>
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
     <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
       <td align="left" valign="top" width="33%">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number -1"/></xsl:with-param></xsl:call-template></xsl:with-param>
          <xsl:with-param name="button_name">btBack</xsl:with-param>
          <xsl:with-param name="button_tip">P�gina anterior</xsl:with-param>
          <xsl:with-param name="image_off_src">anterior_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">anterior_on.gif</xsl:with-param>                          
         </xsl:call-template>
       </td>
       <td align="center" valign="top" width="33%">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href">javascript:<xsl:call-template name="go_home"/></xsl:with-param>
          <xsl:with-param name="button_name">btHome</xsl:with-param>
          <xsl:with-param name="button_tip">P�gina principal</xsl:with-param>
          <xsl:with-param name="image_off_src">inici_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">inici_on.gif</xsl:with-param>                          
         </xsl:call-template>
       </td>
       <td align="right" valign="top" width="33%">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href">javascript:<xsl:call-template name="go_section"><xsl:with-param name="section_number_to_go"><xsl:value-of select="$section_number +1"/></xsl:with-param></xsl:call-template></xsl:with-param>
          <xsl:with-param name="button_name">btNext</xsl:with-param>
          <xsl:with-param name="button_tip">P�gina seg�ent</xsl:with-param>
          <xsl:with-param name="image_off_src">seguent_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">seguent_on.gif</xsl:with-param>                          
         </xsl:call-template>
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
     <TABLE width="100%" border="0" cellspacing="0" cellpadding="10">
      <tr>
       <td width="30%"/>
       <td align="center">
        <xsl:if test="normalize-space($show_correct_button)='true'">
         <xsl:call-template name="put_image_button">
          <xsl:with-param name="href"><xsl:call-template name="correct_section"/></xsl:with-param>
          <xsl:with-param name="button_name">btCorrect</xsl:with-param>
          <xsl:with-param name="button_tip">Corregir aquest full</xsl:with-param>
          <xsl:with-param name="image_off_src">corregir_off.gif</xsl:with-param>
          <xsl:with-param name="image_on_src">corregir_on.gif</xsl:with-param>                          
         </xsl:call-template>
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
       <td width="30%"/>
      </tr>
     </TABLE>
 </xsl:template>

</xsl:stylesheet>
