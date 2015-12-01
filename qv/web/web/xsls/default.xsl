<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:import href="web/xsls/qti.xsl"/>

 <!-- Seleccionar la FULLA D'ESTIL -->
 <xsl:variable name="fulla_estil">
  <xsl:choose>
   <xsl:when test="$color='rosa'">web/estil/qv_rosa.css</xsl:when>
   <xsl:when test="$color='verd'">web/estil/qv_verd.css</xsl:when>
   <xsl:otherwise>web/estil/qv_blau.css</xsl:otherwise>
  </xsl:choose>
 </xsl:variable>
 <!-- Seleccionar localitzacio imatges -->
 <xsl:variable name="path_imatges">
  <xsl:choose>
   <xsl:when test="$color='rosa'">web/imatges/rosa/</xsl:when>
   <xsl:when test="$color='verd'">web/imatges/verd/</xsl:when>
   <xsl:otherwise>web/imatges/blau/</xsl:otherwise>
  </xsl:choose>
 </xsl:variable>
 <xsl:variable name="default_images_path">web/imatges/</xsl:variable>
 <xsl:variable name="images_path"><xsl:value-of select="$default_images_path"/>default</xsl:variable>

 <!--xsl:output method="html" encoding="iso-8859-1" cdata-section-elements="mattext"/-->
 <xsl:output method="html" encoding="ISO-8859-1" indent="yes" media-type="text/html"/>

<!-- #################### INDEX ################# -->
 <xsl:template match="quadern_assignments">
  <HTML>
   <HEAD>
    <title>Quaderns Virtuals</title>
    <xsl:call-template name="putStylesheets"/>
    <xsl:call-template name="putScripts"/>
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
     <!--xsl:call-template name="legend"/></td></tr>
     <tr><td><xsl:call-template name="assignment_buttons"/></td></tr>
     <tr><td valign="bottom"><xsl:call-template name="separation_line"/--></td></tr>
     <tr><td height="20"/></tr></tbody></TABLE>
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
        <td width="150" class="title">Puntuació:&#160;</td>
        <td><xsl:value-of select="$puntuation"/></td>
       </tr>
       <tr>
        <td class="title">Data assignació:&#160;</td>
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
        <td width="100" align="center" valign="top">Puntuació</td>       
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
         <xsl:if test="($view='candidate' and section_states/intervencio_professor='true') or ($view!='candidate' and section_states/intervencio_alumne='true')"><IMG width="10" height="10" border="0" title="intervenció pendent de respondre"><xsl:attribute name="src"><xsl:value-of select="$images_path"/>/qv_intervencio_per_respondre.gif</xsl:attribute></IMG></xsl:if>
         <xsl:if test="($view='candidate' and section_states/intervencio_alumne='true') or ($view!='candidate' and section_states/intervencio_professor='true')"><IMG width="10" height="10" border="0" title="esperant resposta d'intervenció"><xsl:attribute name="src"><xsl:value-of select="$images_path"/>/qv_intervencio_pendent.gif</xsl:attribute></IMG></xsl:if>
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
          <td align="center">
           <xsl:if test="$show_start_button='true'">
            <xsl:choose>
             <xsl:when test="$view='teacher'">
              <xsl:call-template name="put_image_button">
               <xsl:with-param name="href"><xsl:call-template name="go_section_from_assignment"><xsl:with-param name="section_number_to_go">1</xsl:with-param></xsl:call-template></xsl:with-param>
               <xsl:with-param name="button_name">btRevisar</xsl:with-param>
               <xsl:with-param name="button_tip">Començar a revisar el quadern</xsl:with-param>
               <xsl:with-param name="image_off_src">revisar_off.gif</xsl:with-param>
               <xsl:with-param name="image_on_src">revisar_on.gif</xsl:with-param>                          
              </xsl:call-template>
             </xsl:when>
             <xsl:when test="$view='candidate'">
              <xsl:call-template name="put_image_button">
               <xsl:with-param name="href"><xsl:call-template name="go_section_from_assignment"><xsl:with-param name="section_number_to_go">1</xsl:with-param></xsl:call-template></xsl:with-param>
               <xsl:with-param name="button_name">btRealitzar</xsl:with-param>
               <xsl:with-param name="button_tip">Començar a fer el quadern</xsl:with-param>
               <xsl:with-param name="image_off_src">realitzar_off.gif</xsl:with-param>
               <xsl:with-param name="image_on_src">realitzar_on.gif</xsl:with-param>                          
              </xsl:call-template>
             </xsl:when>
            </xsl:choose>
           </xsl:if>
          </td>
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
 <xsl:template match="*[local-name()='questestinterop']">
  <HTML>
   <HEAD>
    <xsl:call-template name="putStylesheets"/>
    <xsl:call-template name="putScripts"/>
    <META HTTP-EQUIV="imagetoolbar" CONTENT="no"/>
    <TITLE>Quaderns Virtuals</TITLE>
   </HEAD>
   <BODY onload="window.focus();" text="#000000" link="#006699" vlink="#5584AA" leftmargin="10" topmargin="15">
    <SCRIPT>setScoring('<xsl:value-of select="$scoring"/>');</SCRIPT>
    <xsl:call-template name="header"/>
    <br/>

    <FORM method="post">
     <xsl:attribute name="onSubmit">return(doSubmit2(<xsl:value-of select="$needResponse"/>))</xsl:attribute>
     <xsl:attribute name="action"><xsl:call-template name="get_action_url"/></xsl:attribute>
     <!--xsl:attribute name="action">
      <xsl:value-of select="$serverUrl"/>?assignacioId=<xsl:value-of
       select="$assignacioId"/>&amp;currentSect=<xsl:value-of select="$section_number"/>#int</xsl:attribute-->
     <!--xsl:value-of select="$serverUrl"/>?currentSect=<xsl:value-of select="$section_number"/>#int</xsl:attribute-->
     <INPUT type="HIDDEN" name="es_correccio">
      <!-- Aquest camp ocult indica si estem mostrant la correccio -->
      <xsl:attribute name="value">
       <xsl:value-of select="$es_correccio"/>
      </xsl:attribute>
     </INPUT>
     <INPUT type="HIDDEN" name="param"/>
     <INPUT type="HIDDEN" name="interaction"/>
     <INPUT type="HIDDEN" name="time"/>
     <INPUT type="HIDDEN" name="next" value="-1"/>
     <!-- full que vol passar a visualitzar -->
     <INPUT type="HIDDEN" name="wantCorrect"/>
     <!-- vol veure la correccio -->
     <INPUT type="HIDDEN" name="QTIUrl">
      <!-- Aquest camp ocult indica el param de la URL on es troba l'XML original -->
      <xsl:attribute name="value">
       <xsl:value-of select="$QTIUrl"/>
      </xsl:attribute>
     </INPUT>
     <xsl:apply-templates select="*[local-name()='objectbank']|*[local-name()='assessment']|*[local-name()='section']|*[local-name()='item']"/>
     <!-- inici BOTONS NAVEGACIO -->
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
       <td width="30" height="31" align="left" valign="top">
        <a href="#" onmouseout="MM_swapImgRestore()">
         <xsl:attribute name="onClick"> if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
           javascript:document.forms[0].next.value='<xsl:value-of select="$section_number - 1"/>';
          document.forms[0].submit(); } </xsl:attribute>
         <xsl:attribute name="onMouseOver">MM_swapImage('anterior_<xsl:value-of
           select="$section_number - 1"/>','','<xsl:value-of select="$path_imatges"/>anterior_on.gif',1)</xsl:attribute>
         <img title="Anterior" alt="Anterior" align="left" border="0">
          <xsl:attribute name="src">
           <xsl:value-of select="$path_imatges"/>anterior_off.gif</xsl:attribute>
          <xsl:attribute name="name">anterior_<xsl:value-of select="$section_number - 1"/>
          </xsl:attribute>
         </img>
        </a>
       </td>
       <td width="50%">
        <img src="web/imatges/blanc.gif" border="0"/>
       </td>
       <td height="31" align="center" valign="top">
        <xsl:call-template name="botoInici"/>
       </td>
       <td width="50%">
        <img src="web/imatges/blanc.gif" border="0"/>
       </td>
       <td width="30" height="31" align="right" valign="top">
        <a href="#" onmouseout="MM_swapImgRestore()">
         <xsl:attribute name="onClick"> if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
           javascript:document.forms[0].next.value='<xsl:value-of select="$section_number + 1"/>';
          document.forms[0].submit(); } </xsl:attribute>
         <xsl:attribute name="onMouseOver">MM_swapImage('seguent_<xsl:value-of
           select="$section_number + 1"/>','','<xsl:value-of select="$path_imatges"/>seguent_on.gif',1)</xsl:attribute>
         <img title="Següent" alt="Següent" align="left" border="0">
          <xsl:attribute name="src">
           <xsl:value-of select="$path_imatges"/>seguent_off.gif</xsl:attribute>
          <xsl:attribute name="name">seguent_<xsl:value-of select="$section_number + 1"/>
          </xsl:attribute>
         </img>
        </a>
       </td>
      </tr>
     </table>
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
       <td width="50%">
        <img src="web/imatges/blanc.gif" border="0"/>
       </td>
       <td align="center">
        <xsl:if test="$view='candidate' and $estat_lliurament!='corregit'">
         <xsl:call-template name="botoGuardar"/>
        </xsl:if>
       </td>
       <td align="center">
        <xsl:choose>
         <xsl:when test="$view='candidate' and $estat_lliurament!='corregit'">
          <xsl:call-template name="botoLliurar"/>
         </xsl:when>
         <xsl:when test="$view='teacher' and $estat_lliurament!='corregit' and $estat_lliurament!=''">
          <xsl:call-template name="botoCorregir"/>
         </xsl:when>
        </xsl:choose>
       </td>
       <td width="50%">
        <img src="web/imatges/blanc.gif" border="0"/>
       </td>
      </tr>
     </table>
     <!-- fi BOTONS NAVEGACIO -->
    </FORM>
    <SCRIPT> select_responses(document.forms[0],"<xsl:value-of select="$initialselection"/>"); </SCRIPT>
    <xsl:if test="$noModify='true' or $estat_lliurament='corregit'">
     <SCRIPT> disableAll(); </SCRIPT>
    </xsl:if>
   </BODY>
  </HTML>
 </xsl:template>
 <xsl:template name="section_header">
  <!-- inici CAPÇALERA -->
  <div id="textsuperior" style="position:absolute; left:20px; top:5px; width:110px; height:84px; z-index:2; overflow: none; background-color: none; layer-background-color: none; border: 1px none #ffffff; visibility: show;">
   <a>
    <xsl:attribute name="onClick"> if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {
     javascript:document.forms[0].next.value=0; document.forms[0].submit(); } </xsl:attribute>
    <img alt="Quaderns Virtuals" width="109" height="83" border="0" align="left">
     <xsl:attribute name="src">
      <xsl:value-of select="$path_imatges"/>logo.gif</xsl:attribute>
    </img>
   </a>
  </div>
  <table width="100%" height="60" border="0" cellspacing="1" cellpadding="0" class="presentacio">
   <tr>
    <td>
     <table width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="fonsRespostes">
      <tr>
       <td height="8" colspan="2" class="presentacio">
        <img src="web/imatges/blanc.gif" alt="" width="100" height="8" border="0"/>
       </td>
       <td width="198" height="8" class="presentacio">
        <img src="web/imatges/blanc.gif" alt="" width="100" height="8" border="0"/>
       </td>
      </tr>
      <tr>
       <td width="120">
        <img src="web/imatges/blanc.gif" alt="" width="120" height="10" border="0"/>
       </td>
       <td width="340" align="left">
        <span class="headerText">
         <b>Quaderns Virtuals: Sistema de suport a l'eLearning</b>
         <br/>Subdirecció General de Tecnologies de la Informació <br/>
         <xsl:value-of select="//assessment/@title"/>
        </span>
       </td>
       <td align="left" valign="top">
        <table width="100%" height="50" border="0" align="right" cellpadding="0" cellspacing="0" class="headerText">
         <tr>
          <td colspan="2" align="right">
           <xsl:value-of select="$section_number"/>/<xsl:value-of select="$section_max_number"/>
          </td>
          <td width="5">
           <img src="web/imatges/blanc.gif" width="5" height="5"/>
          </td>
         </tr>
         <tr>
          <td colspan="2">&#160;</td>
          <td>
           <img src="web/imatges/blanc.gif" width="5" height="5"/>
          </td>
         </tr>
         <tr>
          <xsl:choose>
           <xsl:when test="$estat_lliurament='lliurat' or $estat_lliurament='corregit'">
            <td align="right"> estat:&#160; </td>
            <td>
             <strong>
              <xsl:value-of select="$estat_lliurament"/>
             </strong>
            </td>
           </xsl:when>
           <xsl:otherwise>
            <td colspan="2">&#160;</td>
           </xsl:otherwise>
          </xsl:choose>
          <td>
           <img src="web/imatges/blanc.gif" width="5" height="5"/>
          </td>
         </tr>
         <tr>
          <td width="100%" align="right"> puntuació:&#160; </td>
          <td>
           <strong>
            <xsl:choose>
             <xsl:when test="$es_correccio='true'">
              <script>writeItemScore('<xsl:value-of select="assessment/section[position()=$section_number]/@ident"/>');</script>
             </xsl:when>
             <xsl:otherwise> - </xsl:otherwise>
            </xsl:choose>
           </strong>
          </td>
          <td>
           <img src="web/imatges/blanc.gif" width="5" height="5"/>
          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
    </td>
   </tr>
  </table>
  <br/>
  <!-- fi CAPÇALERA -->
 </xsl:template>
 <xsl:template name="botoGuardar">
  <a href="#" onmouseout="MM_swapImgRestore()">
   <xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {javascript:document.forms[0].wantCorrect.value='false';document.forms[0].submit();}</xsl:attribute>
   <xsl:attribute name="onMouseOver">MM_swapImage('btGuardar','','<xsl:value-of select="$path_imatges"/>guardar_on.gif',1)</xsl:attribute>
   <img name="btGuardar" align="left" border="0" alt="guardar">
    <xsl:attribute name="src">
     <xsl:value-of select="$path_imatges"/>guardar_off.gif</xsl:attribute>
   </img>
  </a>
 </xsl:template>
 <xsl:template name="botoInici">
   <xsl:call-template name="put_image_button">
    <xsl:with-param name="href">#</xsl:with-param>
    <xsl:with-param name="onclick"><xsl:call-template name="go_home"/></xsl:with-param>
    <xsl:with-param name="button_name">btInici</xsl:with-param>
    <xsl:with-param name="button_tip">Tornar a la pàgina principal</xsl:with-param>
    <xsl:with-param name="image_off_src">inici_off.gif</xsl:with-param>
    <xsl:with-param name="image_on_src">inici_on.gif</xsl:with-param>                          
   </xsl:call-template>  
 </xsl:template>
 <!--xsl:template name="botoInici">
  <a href="#" onmouseout="MM_swapImgRestore()">
   <xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {javascript:document.forms[0].next.value=0;document.forms[0].submit();}</xsl:attribute>
   <xsl:attribute name="onMouseOver">MM_swapImage('btInici','','<xsl:value-of select="$path_imatges"/>inici_on.gif',1)</xsl:attribute>
   <img name="btInici" border="0" align="left" alt="inici">
    <xsl:attribute name="src">
     <xsl:value-of select="$path_imatges"/>inici_off.gif</xsl:attribute>
   </img>
  </a>
 </xsl:template-->
 <xsl:template name="botoLliurar">
  <a href="#" onmouseout="MM_swapImgRestore()">
   <xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {javascript:document.forms[0].wantCorrect.value='true';document.forms[0].submit();}</xsl:attribute>
   <xsl:attribute name="onMouseOver">MM_swapImage('btLliurar','','<xsl:value-of select="$path_imatges"/>lliurar_on.gif',1)</xsl:attribute>
   <img name="btLliurar" border="0" align="left" alt="lliurar">
    <xsl:attribute name="src">
     <xsl:value-of select="$path_imatges"/>lliurar_off.gif</xsl:attribute>
   </img>
  </a>
 </xsl:template>
 <xsl:template name="botoCorregir">
  <a href="#" onmouseout="MM_swapImgRestore()">
   <xsl:attribute name="onClick">if (doSubmit2(<xsl:value-of select="$needResponse"/>)) {javascript:document.forms[0].wantCorrect.value='true';document.forms[0].submit();}</xsl:attribute>
   <xsl:attribute name="onMouseOver">MM_swapImage('btCorregir','','<xsl:value-of select="$path_imatges"/>corregir_on.gif',1)</xsl:attribute>
   <img name="btCorregir" align="left" border="0" alt="corregir">
    <xsl:attribute name="src">
     <xsl:value-of select="$path_imatges"/>corregir_off.gif</xsl:attribute>
   </img>
  </a>
 </xsl:template>
 <xsl:template match="*[local-name()='presentation_material']">
  <TABLE width="100%" border="0" cellspacing="10" cellpadding="0" class="additional-material-section">
   <TR>
    <TD>
     <xsl:call-template name="getSectionAdditionalMaterial"/>
    </TD>
   </TR>
  </TABLE>
  <BR/>
 </xsl:template>
 <xsl:template match="*[local-name()='item']">
  <xsl:variable name="it_ident">
   <xsl:value-of select="@ident"/>
  </xsl:variable>
  <A>
   <xsl:attribute name="name">item_<xsl:value-of select="$it_ident"/>
   </xsl:attribute>
  </A>
  <TABLE width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="item">
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="0">
      <TR>
       <TD class="assessment-text">
        <xsl:call-template name="getAssessment">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="0" class="additional-material">
      <TR>
       <TD>
        <xsl:call-template name="getItemAdditionalMaterial">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="0" class="response">
      <TR>
       <TD>
        <xsl:call-template name="getResponses">
         <xsl:with-param name="ident_item">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="10" class="response">
      <TR>
       <xsl:call-template name="getItemFeedback">
        <xsl:with-param name="item_ident">
         <xsl:value-of select="$it_ident"/>
        </xsl:with-param>
       </xsl:call-template>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <TR>
    <TD>
     <TABLE width="100%" border="0" cellpadding="10" cellspacing="10" class="response">
      <TR>
       <TD>
        <xsl:call-template name="interactions">
         <xsl:with-param name="item_ident">
          <xsl:value-of select="$it_ident"/>
         </xsl:with-param>
         <xsl:with-param name="index">1</xsl:with-param>
        </xsl:call-template>
       </TD>
      </TR>
     </TABLE>
    </TD>
   </TR>
   <!--tr>
       <td>
        <img src="web/imatges/blanc.gif" alt="" width="20" height="10" border="0"/>
       </td>
       <td class="preguntes">
        <img src="web/imatges/blanc.gif" alt="" width="20" height="10" border="0"/>
       </td>
      </tr>
      <xsl:call-template name="interactions">
       <xsl:with-param name="item_ident">
        <xsl:value-of select="$it_ident"/>
       </xsl:with-param>
       <xsl:with-param name="index">1</xsl:with-param>
      </xsl:call-template>
      <tr>
       <td>
        <img src="web/imatges/blanc.gif" alt="" width="20" height="10" border="0"/>
       </td>
       <td class="preguntes">
        <img src="web/imatges/blanc.gif" alt="" width="20" height="10" border="0"/>
       </td>
      </tr-->
  </TABLE>
  <br/>
 </xsl:template>
 <xsl:template name="interactions">
  <!-- Si existeix alguna interaccio a mostrar per l'item amb ident=item_ident es mostra-->
  <xsl:param name="item_ident"/>
  <xsl:param name="index"/>
  <xsl:variable name="showInteraction">
   <xsl:call-template name="showInteraction">
    <xsl:with-param name="ident_item"><xsl:value-of select="$item_ident"/></xsl:with-param>
   </xsl:call-template>
  </xsl:variable>
  
  <table width="98%" border="0" cellspacing="1" cellpadding="0" class="interaction" align="left">
   <tr>
    <xsl:if test="$writingEnabled='true' and normalize-space($showInteraction)!='false'">
     <td width="30" class="forum" align="left" valign="top">
      <a href="#" onmouseout="MM_swapImgRestore()">
       <xsl:attribute name="onClick"> if (doSubmit2(false)) {
         document.forms[0].interaction.value='<xsl:value-of select="@ident"/>';
        document.forms[0].submit(); } </xsl:attribute>
       <xsl:attribute name="onMouseOver">MM_swapImage('im_int_<xsl:value-of
         select="@ident"/>','','<xsl:value-of select="$path_imatges"/>questio_on.gif',1)</xsl:attribute>
       <img title="Afegir una nova intervenci&#243; a la zona de di&#224;leg"
        alt="Afegir una nova intervenci&#243; a la zona de di&#224;leg" align="left" border="0">
        <xsl:attribute name="src">
         <xsl:value-of select="$path_imatges"/>questio_off.gif</xsl:attribute>
        <xsl:attribute name="name">im_int_<xsl:value-of select="@ident"/>
        </xsl:attribute>
       </img>
      </a>
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
 <!--xsl:template match="*[local-name()='render_choice']">
  <xsl:choose>
   <xsl:when test="child::material/matimage">
    <TABLE border="0" cellpadding="5" cellspacing="0">
     <TR>
      <TD class="preguntes">
       <xsl:apply-templates select="material|flow_mat"/>
      </TD>
      <TD class="preguntes">
       <xsl:call-template name="response_label"/>
      </TD>
     </TR>
    </TABLE>
   </xsl:when>
   <xsl:otherwise>
    <xsl:if test="child::material|child::flow_mat">
     <BR/>
     <B>
      <xsl:apply-templates select="material|flow_mat"/>
     </B>
     <BR/>
     <BR/>
    </xsl:if>
    <xsl:call-template name="response_label"/>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template-->
 <xsl:template match="*[local-name()='material']">
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
    <!-- PARAM NAME="CONSOLE" VALUE="one"/ -->
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
  <xsl:text>--&gt;</xsl:text>
  <xsl:value-of select="ancestor::response_lid/@ident"/>
  <!--xsl:for-each select="ancestor::presentation">
   <xsl:for-each select=".//render_hotspot">
    <xsl:value-of select="ancestor::response_lid/@ident"/>
   </xsl:for-each>
  </xsl:for-each-->
 </xsl:template>
 <!--xsl:template match="text()">
	<xsl:value-of select="normalize-space()"/>
</xsl:template-->

<!-- #################### COMMONS ################# -->
<xsl:template name="header">
  <div style="border: 1px none rgb(255, 255, 255); position: absolute; left: 15px; top: 5px; width: 110px; height: 84px; z-index: 2;" id="textsuperior">
   <a>
    <xsl:if test="assessment/section">
     <xsl:attribute name="onClick"><xsl:call-template name="go_home"/></xsl:attribute>
     <xsl:attribute name="href">#</xsl:attribute>
     <xsl:attribute name="title">Tornar a la pàgina principal</xsl:attribute>
    </xsl:if>
    <img alt="Quaderns Virtuals" width="109" height="83" border="0" align="left"><xsl:attribute name="src"><xsl:value-of select="$path_imatges"/>logo.gif</xsl:attribute></img>
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
  </td>
 </tr>
 </tbody></table>
</xsl:template>

 <xsl:template name="putStylesheets">
    <LINK TYPE="text/css" rel="stylesheet" href="web/estil/default.css"/>
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

 <xsl:template name="put_image_button">
  <xsl:param name="href"/>
  <xsl:param name="onclick"/>
  <xsl:param name="button_name"/>
  <xsl:param name="button_tip"/>
  <xsl:param name="image_off_src"/>
  <xsl:param name="image_on_src"/>
   <a onmouseout="MM_swapImgRestore()">
    <xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute>
    <xsl:attribute name="onclick"><xsl:value-of select="$onclick"/></xsl:attribute>
    <xsl:attribute name="onMouseOver">MM_swapImage('<xsl:value-of select="$button_name"/>','','<xsl:value-of select="$path_imatges"/><xsl:value-of select="$image_on_src"/>',1)</xsl:attribute>
    <img border="0" align="top">
     <xsl:attribute name="name"><xsl:value-of select="$button_name"/></xsl:attribute>
     <xsl:attribute name="alt"><xsl:value-of select="$button_tip"/></xsl:attribute>
     <xsl:attribute name="src"><xsl:value-of select="$path_imatges"/><xsl:value-of select="$image_off_src"/></xsl:attribute>
    </img>
   </a>
 </xsl:template>

</xsl:stylesheet>
