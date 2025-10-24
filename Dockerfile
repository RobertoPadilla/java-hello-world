# https://container-registry.oracle.com/
FROM container-registry.oracle.com/middleware/weblogic:14.1.2.0-generic-jdk17-ol8-250424

ENV ORACLE_HOME=/u01/oracle \
    MW_HOME=/u01/oracle \
    WLST=/u01/oracle/oracle_common/common/bin/wlst.sh \
    DOMAIN_NAME=hello_domain \
    DOMAIN_HOME=/u01/oracle/user_projects/domains/hello_domain \
    ADMIN_PORT=7001

# WAR y script
COPY target/helloworld.war /u01/oracle/apps/helloworld.war
COPY create-domain.py /u01/oracle/create-domain.py

USER root
RUN mkdir -p /u01/oracle/apps && chown -R oracle:oracle /u01/oracle
USER oracle

# Crea el dominio y coloca el WAR
RUN ${WLST} /u01/oracle/create-domain.py \
 && mkdir -p ${DOMAIN_HOME}/autodeploy \
 && cp /u01/oracle/apps/helloworld.war ${DOMAIN_HOME}/autodeploy/

EXPOSE 7001
WORKDIR ${DOMAIN_HOME}
CMD ["bash","-lc","${DOMAIN_HOME}/startWebLogic.sh"]
