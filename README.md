# FAIRware Workbench API

Build status
---

| Master  | Develop |
| ------------- | ------------- |
| ![ci-master](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=master)  | ![ci-develop](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=develop)  |

Introduction
---
This repository contains the code associated to the API layer of the FAIRware Workbench. The FAIRware Workbench is a tool under development that will evaluate the metadata for digital research objects based on the [FAIR principles](https://www.go-fair.org/fair-principles/) and will provide actionable advice on how to make the metadata FAIR. Our system’s advice will centre on the one thing directly under the control of the investigator—the quality of the metadata used to annotate the digital research objects under consideration.


How to start the FAIRware Workbench API application
---

1. Rename `config-example.yml` to `config.yml` and include your CEDAR api key into the appropriate place (`cedar.apiKey`)
2. Run `mvn clean install` to build your application
3. Start application with `java -jar target/fairware-workbench-api-0.1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter, chech the API documentation at `http://localhost:9090/swagger`

Health Check
---

To see your applications health enter url `http://localhost:9091/healthcheck`
