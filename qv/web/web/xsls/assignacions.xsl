<?xml version='1.0' encoding="ISO-8859-1"?>
<!-- !ENTITY nbsp "&#160;" -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:param name="bgimage" select="'http://www.xtec.es/imatges/edu365.gif'"/>
 <xsl:param name="bgcolor" select="'#e0e0e0'"/>
 <xsl:param name="finishPage" select="''"/>
 <xsl:param name="pwdPage" select="''"/>
 <xsl:param name="returnPage" select="'/qv/getAssignacionsUsuari'"/>
 <xsl:param name="view"/>
 <!-- INICI Parametres pel disseny: colors, imatges, etc... -->
 <xsl:param name="color"/>
 <!-- Seleccionar la FULLA D'ESTIL -->
 <xsl:variable name="css">
  <xsl:value-of select="//assignment/qtimetadata/qtimetadatafield[fieldlabel[text()='css']]/fieldentry[text()]"/>
 </xsl:variable>
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
 <!-- FI Parametres pel disseny: colors, imatges, etc... -->
 <xsl:output method="html"/>
 <xsl:template match="quadern_assignments">
  <HTML>
   <head>
    <title>Quaderns Virtuals</title>
    <LINK TYPE="text/css" rel="stylesheet" href="web/estil/qv.css"/>
    <LINK TYPE="text/css" rel="stylesheet">
     <xsl:attribute name="href">
      <xsl:value-of select="$fulla_estil"/>
     </xsl:attribute>
    </LINK>
    <xsl:if test="normalize-space($css)!=''">
     <LINK TYPE="text/css" rel="stylesheet">
      <xsl:attribute name="href">http://clic.xtec.es/qv/test/mates/<xsl:value-of select="$css"/></xsl:attribute>
     </LINK>
    </xsl:if>
    <SCRIPT src="web/scripts/mm.js" type="text/javascript"/>
   </head>
   <body onload="window.focus();" leftmargin="10" topmargin="15">
    <xsl:call-template name="header"/>
    <xsl:apply-templates select="assignment"/>
   </body>
  </HTML>
 </xsl:template>
 <xsl:template name="header">
  <!-- inici CAPÇALERA -->
  <div id="textsuperior" style="position:absolute; left:20px; top:5px; width:110px; height:84px; z-index:2; overflow: none; background-color: none; layer-background-color: none; border: 1px none #ffffff; visibility: show;">
   <img alt="Quaderns Virtuals" width="109" height="83" border="0">
    <xsl:attribute name="src">
     <xsl:value-of select="$path_imatges"/>logo.gif</xsl:attribute>
   </img>
  </div>
  <table width="100%" height="60" border="0" cellspacing="1" cellpadding="0" class="presentacio">
   <tr>
    <td>
     <table width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="fonsRespostes">
      <tr><td colspan="2" class="presentacio" style="font-size:0"><img src="web/imatges/blanc.gif" alt="" width="100" height="8" border="0"/></td></tr>
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
      </tr>
     </table>
    </td>
   </tr>
  </table>
  <br/>
  <!-- fi CAPÇALERA -->
 </xsl:template>
 <xsl:template match="assignment">
  <table width="75%" height="60" border="0" align="center" cellpadding="0" cellspacing="1" class="fonsPresentacio">
   <tr>
    <td>
     <table width="100%" height="60" border="0" cellspacing="0" cellpadding="0" class="fonsRespostes">
      <tr>
       <td class="fonsPresentacio" width="116" height="8">
        <img src="web/imatges/blanc.gif" alt="" width="100" height="8" border="0"/>
       </td>
       <td width="288" height="8" class="fonsPresentacio">
        <img src="web/imatges/blanc.gif" alt="" width="100" height="8" border="0"/>
       </td>
      </tr>
      <tr>
       <td height="5">
        <b>
         <img src="web/imatges/blanc.gif" width="10" height="5" border="0"/>
        </b>
       </td>
       <td height="5" class="fonsRespostes">
        <img src="web/imatges/blanc.gif" width="10" height="5" border="0"/>
       </td>
      </tr>
      <!-- TR>
				<TD align="center" colspan="2" BGCOLOR="#0000ff">
					<SPAN class="nom_quadern">
						<xsl:value-of select="@name"/>
					</SPAN>
				</TD>
			</TR -->
      <!-- xsl:apply-templates select="description"/ -->
      <xsl:apply-templates select="state"/>
      <xsl:apply-templates select="puntuation"/>
      <xsl:apply-templates select="teacher"/>
      <xsl:apply-templates select="assignment_date"/>
      <xsl:apply-templates select="finish_date"/>
      <!-- <xsl:apply-templates select="limit"/> -->
      <xsl:apply-templates select="sections"/>
      <tr>
       <td colspan="2" align="CENTER">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
         <tr>
          <td width="35%">
           <img src="web/imatges/blanc.gif" border="0"/>
          </td>
          <td align="center">
           <xsl:choose>
            <xsl:when test="$view='teacher'">
             <a onmouseout="MM_swapImgRestore()">
              <xsl:attribute name="href">
               <xsl:value-of select="@servlet"/>?assignacioId=<xsl:value-of select="@id"/>&#38;full=1&#38;finishPage=<xsl:value-of select="$finishPage"/>
              </xsl:attribute>
              <xsl:attribute name="onMouseOver">MM_swapImage('btRevisar','','<xsl:value-of select="$path_imatges"/>revisar_on.gif',1)</xsl:attribute>
              <img name="btRevisar" alt="revisar" border="0" align="left">
               <xsl:attribute name="src">
                <xsl:value-of select="$path_imatges"/>revisar_off.gif</xsl:attribute>
              </img>
             </a>
            </xsl:when>
            <xsl:when test="$view='candidate'">
             <a onmouseout="MM_swapImgRestore()">
              <xsl:attribute name="href">
               <xsl:value-of select="@servlet"/>?assignacioId=<xsl:value-of select="@id"/>&#38;full=1&#38;finishPage=<xsl:value-of select="$finishPage"/>
              </xsl:attribute>
              <xsl:attribute name="onMouseOver">MM_swapImage('btRealitzar','','<xsl:value-of select="$path_imatges"/>realitzar_on.gif',1)</xsl:attribute>
              <img name="btRealitzar" alt="realitzar" border="0" align="left">
               <xsl:attribute name="src">
                <xsl:value-of select="$path_imatges"/>realitzar_off.gif</xsl:attribute>
              </img>
             </a>
            </xsl:when>
           </xsl:choose>
          </td>
          <td align="center">
           <xsl:choose>
            <xsl:when test="$view='teacher'">
             <a onmouseout="MM_swapImgRestore()">
              <xsl:attribute name="HREF">?wantCorrect=true</xsl:attribute>
              <xsl:attribute name="onMouseOver">MM_swapImage('btCorregirTot','','<xsl:value-of select="$path_imatges"/>corregirtot_on.gif',1)</xsl:attribute>
              <img name="btCorregirTot" alt="corregir tot" border="0" align="left">
               <xsl:attribute name="src">
                <xsl:value-of select="$path_imatges"/>corregirtot_off.gif</xsl:attribute>
              </img>
             </a>
            </xsl:when>
            <xsl:when test="$view='candidate'">
             <a onmouseout="MM_swapImgRestore()">
              <xsl:attribute name="HREF">?wantCorrect=true</xsl:attribute>
              <xsl:attribute name="onMouseOver">MM_swapImage('btLliurarTot','','<xsl:value-of select="$path_imatges"/>lliurartot_on.gif',1)</xsl:attribute>
              <img name="btLliurarTot" alt="lliurar tot" border="0" align="left">
               <xsl:attribute name="src">
                <xsl:value-of select="$path_imatges"/>lliurartot_off.gif</xsl:attribute>
              </img>
             </a>
            </xsl:when>
           </xsl:choose>
          </td>
          <td width="35%">
           <img src="web/imatges/blanc.gif" border="0"/>
          </td>
         </tr>
        </table>
       </td>
      </tr>
      <tr>
       <td colspan="2" align="center">&#160;</td>
      </tr>
     </table>
    </td>
   </tr>
  </table>
 </xsl:template>
 <xsl:template match="description">
  <TR>
   <TD colspan="2" BGCOLOR="#8080ff" class="assignacioText">
    <xsl:apply-templates/>
   </TD>
  </TR>
 </xsl:template>
 <xsl:template match="state">
  <tr class="fonsRespostes">
   <td width="116" class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Estat:</b>
   </td>
   <td align="left" class="assignacioText">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="puntuation">
  <tr class="fonsRespostes">
   <td class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Puntuació:</b>
   </td>
   <td align="left" class="assignacioText">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="teacher">
  <tr class="fonsRespostes">
   <td class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Professor:</b>
   </td>
   <td align="left">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="assignment_date">
  <tr class="fonsRespostes">
   <td class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Data assignació:</b>
   </td>
   <td align="left" class="assignacioText">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="finish_date">
  <tr class="fonsRespostes">
   <td class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Data lliurament:</b>
   </td>
   <td align="left" class="assignacioText">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="limit">
  <tr class="fonsRespostes">
   <td class="assignacioText">
    <b>
     <img src="web/imatges/blanc.gif" width="7" height="5" border="0"/>Max. lliuraments:</b>
   </td>
   <td align="left" class="assignacioText">
    <xsl:apply-templates/>
   </td>
  </tr>
 </xsl:template>
 <xsl:template match="sections">
  <tr class="fonsRespostes">
   <td colspan="2">
    <br/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr class="fonsPresentacio">
      <td class="assignacioTitol">&#160;</td>
      <td class="assignacioTitol">Fulls </td>
      <td class="assignacioTitol">Estat</td>
      <td class="assignacioTitol" align="center">Límit</td>
      <td class="assignacioTitol" align="center">Punts</td>
      <td class="assignacioTitol" align="center">Correcció</td>
     </tr>
     <xsl:apply-templates select="section"/>
     <tr class="fonsPresentacio" align="left" style="font-size:0">
      <td colspan="6">
       <img src="web/imatges/blanc.gif" alt="" width="1" height="1" border="0"/>
      </td>
     </tr>
    </table>
    <br/>
   </td>
  </tr>
  <!-- TR>
		<TD colspan="2">
			<TABLE WIDTH="100%">
				<TR BGCOLOR="#c0c0ff">
					<TD WIDTH="50%">Nom</TD>
					<TD WIDTH="15%">Punts</TD>
					<TD WIDTH="25%">Estat</TD>
					<TD WIDTH="10%">Limit</TD>
				</TR>
				<xsl:apply-templates select="section"/>
			</TABLE>
		</TD>
	</TR -->
 </xsl:template>
 <xsl:template name="section2">
  <xsl:variable name="section_name">
   <xsl:choose>
    <xsl:when test="@name=''"> Full <xsl:value-of select="@num"/>
    </xsl:when>
    <xsl:otherwise>
     <xsl:value-of select="@name"/>
    </xsl:otherwise>
   </xsl:choose>
  </xsl:variable>
  <td>&#160;</td>
  <td>
   <a>
    <xsl:attribute name="href">
     <xsl:value-of select="ancestor::assignment/@servlet"/>?<xsl:choose><xsl:when test="ancestor::assignment/@id!='' and ancestor::assignment/@id!='null'">assignacioId=<xsl:value-of select="ancestor::assignment/@id"/></xsl:when><xsl:otherwise>quadernURL=<xsl:value-of select="ancestor::assignment/@quadernURL"/></xsl:otherwise></xsl:choose>&#38;full=<xsl:value-of select="@num"/>&#38;finishPage=<xsl:value-of select="$finishPage"/>
    </xsl:attribute>
    <xsl:value-of select="$section_name"/>
   </a>
  </td>
  <td>
   <xsl:apply-templates select="section_state"/>
  </td>
  <td>
   <xsl:apply-templates select="section_limit"/>
  </td>
  <td align="center">
   <xsl:apply-templates select="section_puntuation"/>
  </td>
  <td>&#160;</td>
 </xsl:template>
 <xsl:template match="section">
  <xsl:variable name="num_section">
   <xsl:number/>
  </xsl:variable>
  <xsl:choose>
   <xsl:when test="$num_section mod 2=1">
    <tr bgcolor="#FFFFFF" align="left" class="fullText">
     <xsl:call-template name="section2"/>
    </tr>
   </xsl:when>
   <xsl:otherwise>
    <tr align="left" class="fullText" bgcolor="#E7EFF9">
     <xsl:call-template name="section2"/>
    </tr>
   </xsl:otherwise>
  </xsl:choose>
  <!-- TR>
		<TD WIDTH="50%">
			<A>
				<xsl:attribute name="href"><xsl:value-of select="ancestor::assignment/@urlQuadern"/>&#38;full=<xsl:value-of select="@num"/>&#38;finishPage=<xsl:value-of
select="$finishPage"/></xsl:attribute><xsl:value-of select="@name"/>
			</A>
		</TD>
		<TD ALIGN="CENTER"><xsl:apply-templates select="section_puntuation"/></TD>
		<TD><xsl:apply-templates select="section_state"/></TD>
		<TD ALIGN="RIGHT"><xsl:apply-templates select="section_limit"/></TD>
	</TR -->
 </xsl:template>
</xsl:stylesheet>
