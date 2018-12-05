echo "******** CHECK DOCKER IP AND UPDATE APPLICATION.YML **********"
postgresIP=$(docker inspect -f '{{ .NetworkSettings.IPAddress }}' pg_database)
echo "postgres IP: $postgresIP"

sed -i -e 's/localhost/'${postgresIP}'/g' shop-api/src/main/resources/application.yml
