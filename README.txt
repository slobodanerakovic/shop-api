
***NOTE: Docker deployment process has been tested on Ubuntu 16.04 from local docker repository scratch.

This manual contains instructions for environment configuration, application deployment and usage. 
If you do not have installed docker, go to section - NO DOCKER README, within this file
I am using docker for environment setup, so all you need is installed docker on your machine.
init.sh - which is the entry point for:
    1. database docker container/image build and run
    2. application docker container/image build and run

If you have docker installed, just go into folder where you unpacked download app, run this bash script file (./init.sh) and everything will be configured (keep in mind that it will takes several minutes to configure environment and build application - depending on you machine and Internet connection).

One of the main issue which could be is database running Ip. Since docker has various strategy for assigning ip address to its container, and the spring boot, which this application is, requires in the file "shop-api/src/main/resources/application.yml" contains valid url for application database driver location:        
    url: jdbc:postgresql://localhost/shop

However, the "init.sh" script, accompanied by other present scripts as well, does this ip detection and change on its own, but, I am just stressing if any issue about it raise.

If you are running this on windows (I guess not) then application database driver url and active url in the file shop-api/src/main/resources/application.yml needs to be updated manually, and docker build/run needs to be executed manually from the init.sh file.

Once environment is setup (by docker hopefully or by you manual for any reasons) schema creation, junit tests will successfully run.
If you are deploying app manually (by executing mvn package for compilation, and issuing java -jar target/shop-api.jar from shop-api folder) makes sure that after first stop, initialization-mode: always (from file: "shop-api/src/main/resources/application.yml") is change from 'always' to 'embedded' (to prevent repeating of schema creation). But these are all if you do manual deployment, and not using docker.

Once the application is started, you can access frontend part of the app (test and play around) by going to: http://localhost:9876/shop-api/ link.


*** NO DOCKER README ***
If you have no docker then:
- Make sure you have installed postgres, running on 5432 port
- Create database from sql: app_unpacked_folder/shop-api/src/main/resources/database_creation.sql
- Go to app_unpacked_folder/shop-api folder
- Build app by running: mvn package
- Start app from same location by: java -jar target/shop-api.jar
- Wait application to start (tomcat) and navigate your browser to http://localhost:9876/shop-api


Documented endpoints using swagger at: shop-api/src/main/resources/swagger.json
*** Once started, application will create some mock data for application production testing.
