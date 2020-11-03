FROM openjdk:11
WORKDIR /app/
COPY src ./src
RUN javac -sourcepath src ./src/OpgHandler.java -Xlint:unchecked -d ./