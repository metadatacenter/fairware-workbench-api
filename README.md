# FAIRware Workbench API

How to start the FAIRware Workbench API application
---

1. Rename `config-example.yml` to `config.yml` and include your CEDAR api key into the appropriate place (`cedar.apiKey`)
2. Run `mvn clean install` to build your application
3. Start application with `java -jar target/fairware-workbench-api-0.1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter, chech the API documentation at `http://localhost:9090/swagger`

Health Check
---

To see your applications health enter url `http://localhost:9091/healthcheck`
