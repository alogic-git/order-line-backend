if ./gradlew clean build; then
#  docker rmi new-pters/alogic:latest
  ./gradlew docker -x test
else
  echo "Gradle task failed"
fi