# BUILDER (Stage)
# - image where we build the application (in this have JAR of Spring Boot app)
# - for that we will use Gradle with JDK 17
FROM gradle:jdk17 as builder

# Let's work in /builder directory
WORKDIR /builder
# Copy everything to that directory
# (it will not copy files mentioned in .gitignore and .dockerignore)
ADD . /builder
# Run Gradle to build
# - build runs more checks than assemble (but we don't want to run tests)
# - daemon would slow it down for a single run
RUN gradle build -x test --stacktrace --no-daemon

# MAIN (Stage)
# - producing image that will be actually used for deployment
# - we should try to keep it as small as possible
FROM amazoncorretto:17-alpine

# Let's work in /app directory
WORKDIR /app
# App will need to expose port 8080 from the image
EXPOSE 8080
# Copy JAR and config files from BUILDER
COPY --from=builder /builder/build/libs/githubreports.jar /app/githubreports.jar
COPY --from=builder /builder/build/resources/main/application.properties /app/application.properties
# Run the app using the JAR file (and config)
# - Mount point /app/application.yml for overrides
ENTRYPOINT java -jar /app/githubreports.jar --spring.config.location=classpath:/application.properties,file:/app/application.properties
