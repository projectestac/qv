keytool -genkey -keyalg RSA -dname "cn=Quaderns Virtuals, ou=Projectes TIC,o=Departament Educacio, L=Barcelona, S=Spain, C=es" -alias qv -keypass virtuals -validity 7305 -keystore qv.keystore -storepass virtuals

keytool -certreq -alias qv -keystore qv.keystore -keypass virtuals -file qv.csr

keytool -export -alias qv -keystore qv.keystore -rfc -file qv.cer

keytool -import -alias qvcert -file qv.cer -keystore truststore
