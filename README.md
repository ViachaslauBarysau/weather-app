Starting application:
1) execute mvn clean install for weather-service and weather-repository
2) docker-compose build(docker-compose.yml file is located in parent folder)
3) docker-compose up(docker-compose.yml file is located in parent folder)

Using application:
1) POST: localhost:8082/weather with city name in the body
2) GET: localhost:8082/weather?pageNumber=1&pageSize=10&searchedValue=Snow 
  - to get weather records(pageNumber, pageSize, searchedValue are optional parameters. Searched value is used for searching by city and weather)
