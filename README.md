# FAIRware Workbench API

How to start the FAIRware Workbench API application
---

1. Rename `config-example.yml` to `config.yml`. Include your CEDAR api key into the appropriate place (cedar.apiKey)
2. Run `mvn clean install` to build your application
3. Start application with `java -jar target/fairware-workbench-api-0.1.0.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
