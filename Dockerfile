FROM openjdk:12-oraclelinux7

COPY . /usr/app/
WORKDIR /usr/app/

RUN yum -y install libXi
RUN yum -y install libXrender
RUN yum -y install libXtst

ENTRYPOINT ["java", "-jar", "/usr/app/cloudGUI.jar"]