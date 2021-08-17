# FAIRware Workbench API

Build status
---

| Master  | Develop |
| ------------- | ------------- |
| ![ci-master](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=master)  | ![ci-develop](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=develop)  |

Introduction
---
This repository contains the code associated with the API layer of the FAIRware Workbench. 

The FAIRware Workbench is a tool to evaluate the metadata for digital research objects based on the [FAIR principles](https://www.go-fair.org/fair-principles/). This tool will provide researchers with actionable advice on how to improve the quality of the metadata used to annotate the digital research objects under consideration.

For more information about the FAIRware initiative, take a look at this [press release from the Research on Research Institute (RoRI)](https://researchonresearch.org/tpost/9f5ems1x81-rori-selects-the-stanford-center-for-bio).

The following diagram shows the architecture of the FAIRware Workbench. The code in the current repository corresponds to the Open APIs and Backend services layers, highlighted in red.

![Screen Shot 2021-08-17 at 3 58 05 PM](https://user-images.githubusercontent.com/7634440/129739276-388240df-c742-43ff-bd5c-a2627f956fe9.png)


How to start the FAIRware Workbench API application
---

1. Rename `config-example.yml` to `config.yml` and include your CEDAR api key into the appropriate place (`cedar.apiKey`)
2. Run `mvn clean install` to build your application
3. Start application with `java -jar target/fairware-workbench-api-0.1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter, chech the API documentation at `http://localhost:9090/swagger`

Health Check
---

To see your applications health enter url `http://localhost:9091/healthcheck`
