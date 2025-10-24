import os

adminUser = os.environ.get('ADMIN_USERNAME')
adminPass = os.environ.get('ADMIN_PASSWORD')
domainName = os.environ.get('DOMAIN_NAME', 'hello_domain')
domainHome = os.environ.get('DOMAIN_HOME', '/u01/oracle/user_projects/domains/hello_domain')
adminPort = int(os.environ.get('ADMIN_PORT', '7001'))

# Seleccion de plantilla Basica
selectTemplate('Basic WebLogic Server Domain')
loadTemplates()

# Configuracion del usuario administrador
cd('/Security/base_domain/User/weblogic')
cmo.setName(adminUser)
cmo.setPassword(adminPass)

# Configuracion del servidor AdminServer
cd('/Servers/AdminServer')
cmo.setName('AdminServer')
cmo.setListenAddress('')
cmo.setListenPort(adminPort)

# Configuracion del dominio
cd('/')
setOption('ServerStartMode', 'dev')
setOption('OverwriteDomain', 'true')
setOption('DomainName', domainName)

writeDomain(domainHome)
closeTemplate()
exit()
