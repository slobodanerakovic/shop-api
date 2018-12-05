function cleanup_handler {
	echo "******** CLEANUP **********"
	sleep 1
	docker kill shop_api
	sleep 1
	docker rm --force shop_api
	sleep 1

	docker kill pg_database
	sleep 1
	docker rm --force pg_database
	sleep 1

	echo "******** CLEANUP FINISHED **********"
	exit
}
echo "******** DOCKER DEPLOYMENT STARTED **********"
trap cleanup_handler INT

./postgresinit.sh

./prepareApp.sh

sleep 5
echo "******** START BUILDING SHOP_API CONTAINER **********"
docker build -t shop-api shop-api/
echo "******** FINISHED BUILDING SHOP_API CONTAINER **********"
sleep 3
echo "******** RUN SHOP_API CONTAINER **********"
docker run -P --name shop_api -p 9876:9876 -t shop-api

