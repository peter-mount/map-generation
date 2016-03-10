FROM area51/java:serverjre-8

# Default file locations
ENV CONFIGURATION_DIR   /opt/fileserver/etc
ENV FILESYSTEM_BASE     /opt/fileserver/data

ADD map*-assembly.tar /opt

CMD ["/opt/mapgenerator/mapgenerator"]
