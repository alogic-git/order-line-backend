FROM openjdk:8

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-cp","app:app/lib/*","com.ptglue.PtGlueApplication"]
