<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="web/xsls/qti_prof_ext.xsl"/>

<xsl:output method="html"/>

<xsl:template name="putNorthButtons">
<!--
	<tr>
		<td class="caixaItem1" bgcolor="#FFFFFF">
			<center>
			<table>
				<tr>
					<xsl:call-template name="loop_section_link">
						<xsl:with-param name="i">1</xsl:with-param>
						<xsl:with-param name="max"><xsl:value-of select="$section_max_number"/></xsl:with-param>
					</xsl:call-template>
					<td>
						<a href="getQualification?assignacioId=null">R</a>
					</td>
				</tr>
			</table>
			</center>
		</td>
	</tr>
-->
</xsl:template>

<xsl:template name="loop_section_link">
	<xsl:param name="i"/>
	<xsl:param name="max"/>
	<xsl:if test="$i &lt;= $max">
	<!--    body of the loop goes here    -->
		<td>
			<xsl:choose>
				<xsl:when test="$i=$section_number">
					<xsl:value-of select="$i"/>
				</xsl:when>
				<xsl:otherwise>		
					<a>
						<xsl:attribute name="href">getQuadernAlumne?full=<xsl:value-of select="$i"/></xsl:attribute>
						<xsl:value-of select="$i"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:if>
	<xsl:if test="$i &lt;= $max">
		<xsl:call-template name="loop_section_link">
			<xsl:with-param name="i">
				<xsl:value-of select="$i + 1"/>
			</xsl:with-param>
			<xsl:with-param name="max">
				<xsl:value-of select="$max"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template name="flow_style2">
	<!-- Directe: cada fila te un enunciat i una resposta -->
	<xsl:for-each select="material|response_lid|response_xy|response_str|response_num|response_grp|flow">
		<xsl:choose>
			<xsl:when test="name(.)='material'">
				<xsl:if test="position()!=1"><BR/></xsl:if>
				<xsl:apply-templates select="."/>
				<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text> <!-- necessari per posar espais -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>	