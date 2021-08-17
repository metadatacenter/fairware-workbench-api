# FAIRware Workbench API

Build status
---

| Master  | Develop |
| ------------- | ------------- |
| ![ci-master](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=master)  | ![ci-develop](https://github.com/metadatacenter/fairware-workbench-api/actions/workflows/ci.yml/badge.svg?branch=develop)  |

Introduction
---
This repository contains the code corresponding to the API layer of the FAIRware Workbench. 

The FAIRware Workbench is a tool to evaluate metadata for digital research objects based on the [FAIR principles](https://www.go-fair.org/fair-principles/). This tool will provide researchers with actionable advice on how to improve the quality of the metadata used to annotate the digital research objects under consideration.

For more information about the FAIRware initiative and the FAIRware Workbench, take a look at this [press release from the Research on Research Institute (RoRI)](https://researchonresearch.org/tpost/9f5ems1x81-rori-selects-the-stanford-center-for-bio).

The following diagram shows the architecture of the FAIRware Workbench. The code in the current repository corresponds to the Open APIs and Backend services layers, highlighted in red.

![Screen Shot 2021-08-17 at 3 58 05 PM](https://user-images.githubusercontent.com/7634440/129739276-388240df-c742-43ff-bd5c-a2627f956fe9.png)


How to start the FAIRware Workbench API application
---

1. The FAIRware Workbench takes advantage of some of the metadata services provided by [the CEDAR system](https://metadatacenter.org/). If you don't have a CEDAR account yet, create one at [https://cedar.metadatacenter.org/](https://cedar.metadatacenter.org/) and copy your CEDAR API key from the [CEDAR profile page](https://cedar.metadatacenter.org/profile).
5. Rename `config-example.yml` to `config.yml` and enter your CEDAR api key as the value of `cedar.apiKey`. Example:
    ```
    ...
    
    cedar:
      apiKey: 'ae7a5d2bf6254707b3d9c51822e5ef49c528428791c3e2f84b9e75532326e74b'
    
    ...  
    
    ```
7. Run `mvn clean install` to build your application
8. Start application with `java -jar target/fairware-workbench-api-0.1.0-SNAPSHOT.jar server config.yml`
9. To check that your application is running enter, chech the API documentation at `http://localhost:9090/swagger`

Health Check
---

To see your applications health enter url `http://localhost:9091/healthcheck`
